package com.manpdev.appointment.ui.presenter;

import android.content.Context;
import android.util.Log;

import com.manpdev.appointment.data.remote.AuthProvider;
import com.manpdev.appointment.data.remote.firebase.database.FBPAppointmentProvider;
import com.manpdev.appointment.ui.mvp.ProviderAppoinmentContract;
import com.manpdev.appointment.ui.mvp.base.MVPContract;

/**
 * novoa on 9/11/16.
 */

public class ProviderAppointmentPresenter implements ProviderAppoinmentContract.Presenter {

    private static final String TAG = "LoginPresenter";

    private ProviderAppoinmentContract.View mView;
    private final AuthProvider mAuthProvider;
    private final FBPAppointmentProvider mAppointmentProvider;

    private boolean mViewAttached;

    public ProviderAppointmentPresenter(Context context, AuthProvider authProvider, FBPAppointmentProvider appointmentProvider) {
        this.mAuthProvider = authProvider;
        this.mAppointmentProvider = appointmentProvider;
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
}
