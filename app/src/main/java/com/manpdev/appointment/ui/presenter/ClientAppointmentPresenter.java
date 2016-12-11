package com.manpdev.appointment.ui.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.manpdev.appointment.R;
import com.manpdev.appointment.data.local.CalendarProvider;
import com.manpdev.appointment.data.model.AppointmentModel;
import com.manpdev.appointment.data.model.RatingModel;
import com.manpdev.appointment.data.model.ReviewModel;
import com.manpdev.appointment.data.model.ServiceModel;
import com.manpdev.appointment.data.remote.AuthProvider;
import com.manpdev.appointment.data.remote.firebase.database.FBCAppointmentProvider;
import com.manpdev.appointment.data.remote.firebase.database.FBRatingProvider;
import com.manpdev.appointment.data.remote.firebase.database.FBReviewProvider;
import com.manpdev.appointment.data.remote.firebase.database.FBServiceProvider;
import com.manpdev.appointment.data.remote.firebase.database.FBUserProvider;
import com.manpdev.appointment.ui.mvp.ClientAppointmentContract;
import com.manpdev.appointment.ui.mvp.base.MVPContract;

import rx.functions.Action1;

/**
 * novoa on 9/11/16.
 */

public class ClientAppointmentPresenter implements ClientAppointmentContract.Presenter {

    private static final String TAG = "LoginPresenter";

    private ClientAppointmentContract.View mView;

    private final AuthProvider mAuthProvider;
    private final FBCAppointmentProvider mAppointmentProvider;
    private final FBUserProvider mUserProvider;
    private final FBServiceProvider mServiceProvider;
    private final CalendarProvider mCalendarProvider;
    private final FBReviewProvider mReviewProvider;
    private final FBRatingProvider mRatingProvider;

    private boolean mViewAttached;

    public ClientAppointmentPresenter(Context context, AuthProvider authProvider,
                                      FBCAppointmentProvider appointmentProvider, FBUserProvider userProvider,
                                      FBServiceProvider serviceProvider, CalendarProvider calendarProvider,
                                      FBReviewProvider reviewProvider, FBRatingProvider ratingProvider) {
        this.mAuthProvider = authProvider;
        this.mAppointmentProvider = appointmentProvider;
        this.mUserProvider = userProvider;
        this.mServiceProvider = serviceProvider;
        this.mCalendarProvider = calendarProvider;
        this.mReviewProvider = reviewProvider;
        this.mRatingProvider = ratingProvider;
    }

    @Override
    public void attachView(MVPContract.View view) {
        Log.d(TAG, "attachView");
        mView = (ClientAppointmentContract.View) view;
        mViewAttached = true;
    }

    @Override
    public void detachView() {
        mViewAttached = false;
    }

    @Override
    public void loadList() {
        mView.showList(mAppointmentProvider.getCollectionObservable(mAuthProvider.getUserId()));
    }

    @Override
    public void createNewAppointment(@NonNull final AppointmentModel model) {
        model.setCid(mAuthProvider.getUserId());
        model.setClient(mAuthProvider.getUserFullName());

        mUserProvider.getSingleValueObservable(model.getProvider())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String providerId) {
                        model.setPid(providerId);

                        mServiceProvider.getSingleValueObservable(providerId).subscribe(new Action1<ServiceModel>() {
                            @Override
                            public void call(ServiceModel serviceModel) {
                                if (mViewAttached && !serviceModel.isActive()) {
                                    mView.hideProgressDialog();
                                    mView.showError(R.string.provider_service_disabled);
                                    return;
                                }
                                model.setProvider(serviceModel.getName());
                                insertAppointment(model);
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                if (mViewAttached) {
                                    mView.hideProgressDialog();
                                    mView.showError(R.string.provider_service_disabled);
                                }
                            }
                        });
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (!mViewAttached)
                            return;

                        mView.hideProgressDialog();
                        mView.showError(R.string.invalid_provider_information);
                    }
                });
    }

    @Nullable
    @Override
    public Intent getCalendarIntent(@NonNull AppointmentModel model) {
        if(model.getState() == AppointmentModel.DENIED || model.getState() == AppointmentModel.REQUESTED){
            mView.showError(R.string.appointment_no_accepted);
            return null;
        }

        return mCalendarProvider.getCalendarIntent(model);
    }

    @Override
    public void insertCalendarEvent(@NonNull AppointmentModel model) {
        mCalendarProvider.insertCalendarEvent(mAuthProvider.getUserEmail(), model)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        // TODO: 12/11/16 Insert in futures version the uri in the database. to reference  the same event.
                        if(mViewAttached)
                            mView.calendarEventInserted();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if(mViewAttached)
                            mView.showError(throwable.getMessage());
                    }
                });
    }


    @Override
    public void createNewServiceReview(@NonNull ReviewModel review) {
        mReviewProvider.insert(review);
        insertRating(review);
    }

    private void insertRating(@NonNull ReviewModel review) {
        RatingModel model = new RatingModel();
        model.setCount(1);
        model.setuId(review.getUid());
        model.increaseTotal(review.getRating());
        mRatingProvider.insert(model);
    }

    private void insertAppointment(@NonNull final AppointmentModel model) {
        mAppointmentProvider.insert(model)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mViewAttached) {
                            mView.hideProgressDialog();
                            loadList();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (mViewAttached) {
                            mView.hideProgressDialog();
                            mView.showError(R.string.invalid_appointment_information);
                        }
                    }
                });
    }
}
