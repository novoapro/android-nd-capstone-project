package com.manpdev.appointment.ui.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.manpdev.appointment.R;
import com.manpdev.appointment.data.model.AppointmentModel;
import com.manpdev.appointment.data.model.ServiceModel;
import com.manpdev.appointment.data.remote.AuthProvider;
import com.manpdev.appointment.data.remote.firebase.database.FBCAppointmentProvider;
import com.manpdev.appointment.data.remote.firebase.database.FBServiceProvider;
import com.manpdev.appointment.data.remote.firebase.database.FBUserProvider;
import com.manpdev.appointment.ui.mvp.ClientAppoinmentContract;
import com.manpdev.appointment.ui.mvp.base.MVPContract;

import rx.Single;
import rx.functions.Action1;

/**
 * novoa on 9/11/16.
 */

public class ClientAppointmentPresenter implements ClientAppoinmentContract.Presenter {

    private static final String TAG = "LoginPresenter";

    private ClientAppoinmentContract.View mView;
    private final AuthProvider mAuthProvider;
    private final FBCAppointmentProvider mAppointmentProvider;
    private final FBUserProvider mUserProvider;
    private final FBServiceProvider mServiceProvider;

    private boolean mViewAttached;

    public ClientAppointmentPresenter(Context context, AuthProvider authProvider,
                                      FBCAppointmentProvider appointmentProvider, FBUserProvider userProvider,
                                      FBServiceProvider serviceProvider) {
        this.mAuthProvider = authProvider;
        this.mAppointmentProvider = appointmentProvider;
        this.mUserProvider = userProvider;
        this.mServiceProvider = serviceProvider;
    }

    @Override
    public void attachView(MVPContract.View view) {
        Log.d(TAG, "attachView");
        mView = (ClientAppoinmentContract.View) view;
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
