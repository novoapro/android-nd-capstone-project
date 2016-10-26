package com.manpdev.appointment.ui.presenter;

import android.content.Context;
import android.util.Log;

import com.manpdev.appointment.data.model.AppointmentModel;
import com.manpdev.appointment.data.remote.AuthProvider;
import com.manpdev.appointment.data.remote.firebase.FBCAppointmentProvider;
import com.manpdev.appointment.ui.mvp.ClientAppoinmentContract;
import com.manpdev.appointment.ui.mvp.ServiceInfoContract;
import com.manpdev.appointment.ui.mvp.base.MVPContract;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.functions.Action1;

/**
 * novoa on 9/11/16.
 */

public class ClientAppointmentPresenter implements ClientAppoinmentContract.Presenter {

    private static final String TAG = "LoginPresenter";

    private ClientAppoinmentContract.View mView;
    private final AuthProvider mAuthProvider;
    private final FBCAppointmentProvider mAppointmentProvider;

    private boolean mViewAttached;

    public ClientAppointmentPresenter(Context context, AuthProvider authProvider, FBCAppointmentProvider appointmentProvider) {
        this.mAuthProvider = authProvider;
        this.mAppointmentProvider = appointmentProvider;
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
    public void createNewAppointment() {

    }
}
