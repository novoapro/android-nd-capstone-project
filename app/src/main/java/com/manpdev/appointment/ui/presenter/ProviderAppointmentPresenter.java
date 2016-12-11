package com.manpdev.appointment.ui.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.manpdev.appointment.R;
import com.manpdev.appointment.data.local.CalendarProvider;
import com.manpdev.appointment.data.model.AppointmentModel;
import com.manpdev.appointment.data.remote.AuthProvider;
import com.manpdev.appointment.data.remote.firebase.database.FBPAppointmentProvider;
import com.manpdev.appointment.ui.mvp.ProviderAppoinmentContract;
import com.manpdev.appointment.ui.mvp.base.MVPContract;

import rx.functions.Action1;

/**
 * novoa on 9/11/16.
 */

public class ProviderAppointmentPresenter implements ProviderAppoinmentContract.Presenter {

    private static final String TAG = "LoginPresenter";

    private final CalendarProvider mCalendarProvider;
    private ProviderAppoinmentContract.View mView;
    private final AuthProvider mAuthProvider;
    private final FBPAppointmentProvider mAppointmentProvider;

    private boolean mViewAttached;

    public ProviderAppointmentPresenter(Context context, AuthProvider authProvider, FBPAppointmentProvider appointmentProvider, CalendarProvider calendarProvider) {
        this.mAuthProvider = authProvider;
        this.mAppointmentProvider = appointmentProvider;
        this.mCalendarProvider = calendarProvider;
    }

    @Override
    public void attachView(MVPContract.View view) {
        Log.d(TAG, "attachView");
        mView = (ProviderAppoinmentContract.View) view;
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
    public Intent getCalendarIntent(AppointmentModel model) {
        return mCalendarProvider.getCalendarIntent(model);
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
