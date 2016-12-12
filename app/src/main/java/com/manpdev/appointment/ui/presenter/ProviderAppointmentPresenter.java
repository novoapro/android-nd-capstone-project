package com.manpdev.appointment.ui.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.manpdev.appointment.R;
import com.manpdev.appointment.data.local.AppointmentCalendarContract;
import com.manpdev.appointment.data.local.AppointmentCalendarProvider;
import com.manpdev.appointment.data.model.AppointmentModel;
import com.manpdev.appointment.data.remote.AuthProvider;
import com.manpdev.appointment.data.remote.firebase.database.FBPAppointmentProvider;
import com.manpdev.appointment.ui.mvp.ProviderAppointmentContract;
import com.manpdev.appointment.ui.mvp.base.MVPContract;

import rx.Single;
import rx.SingleSubscriber;
import rx.functions.Action1;

/**
 * novoa on 9/11/16.
 */

public class ProviderAppointmentPresenter implements ProviderAppointmentContract.Presenter {

    private static final String TAG = "LoginPresenter";
    private final Context mContext;

    private ProviderAppointmentContract.View mView;
    private final AuthProvider mAuthProvider;
    private final FBPAppointmentProvider mAppointmentProvider;

    private boolean mViewAttached;

    public ProviderAppointmentPresenter(Context context, AuthProvider authProvider, FBPAppointmentProvider appointmentProvider) {
        this.mAuthProvider = authProvider;
        this.mAppointmentProvider = appointmentProvider;
        this.mContext = context;
    }

    @Override
    public void attachView(MVPContract.View view) {
        Log.d(TAG, "attachView");
        mView = (ProviderAppointmentContract.View) view;
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
    public void insertCalendarEvent(@NonNull final AppointmentModel model) {
        Single.create(new Single.OnSubscribe<String>() {
            @Override
            public void call(SingleSubscriber<? super String> singleSubscriber) {
                try {
                    Uri insert = mContext.getContentResolver()
                            .insert(AppointmentCalendarContract.getBaseContentUri(AppointmentCalendarProvider.sAUTHORITY),
                                    AppointmentCalendarContract.getContentValue(mContext, mAuthProvider.getUserEmail(), model));

                    if (insert != null)
                        singleSubscriber.onSuccess(insert.toString());
                    else
                        singleSubscriber.onError(new Throwable(mContext.getString(R.string.invalid_appointment_information)));

                } catch (Exception ex) {
                    singleSubscriber.onError(new Throwable(mContext.getString(R.string.invalid_appointment_information)));
                }
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                if (mViewAttached)
                    mView.calendarEventInserted();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                if (mViewAttached)
                    mView.showError(throwable.getMessage());
            }
        });
    }


    @Override
    public void editAppointment(AppointmentModel appointment) {
        mAppointmentProvider.update(appointment)
                .subscribe(new Action1<Void>() {
                               @Override
                               public void call(Void aVoid) {
                                   if (!mViewAttached)
                                       return;

                                   mView.hideProgressDialog();
                                   loadList();

                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                if (!mViewAttached)
                                    return;

                                mView.hideProgressDialog();
                                mView.showError(R.string.invalid_appointment_information);
                            }
                        });
    }
}
