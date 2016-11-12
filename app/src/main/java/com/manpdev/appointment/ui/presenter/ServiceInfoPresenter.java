package com.manpdev.appointment.ui.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.manpdev.appointment.data.model.ServiceModel;
import com.manpdev.appointment.data.remote.AuthProvider;
import com.manpdev.appointment.data.remote.firebase.FBServiceProvider;
import com.manpdev.appointment.ui.mvp.ServiceInfoContract;
import com.manpdev.appointment.ui.mvp.base.MVPContract;

import rx.functions.Action1;

/**
 * novoa on 9/11/16.
 */

public class ServiceInfoPresenter implements ServiceInfoContract.Presenter {

    private static final String TAG = "LoginPresenter";

    private ServiceInfoContract.View mView;
    private final AuthProvider mAuthProvider;
    private final FBServiceProvider mServiceProvider;

    private boolean mViewAttached;

    public ServiceInfoPresenter(Context context, AuthProvider authProvider, FBServiceProvider serviceProvider) {
        this.mAuthProvider = authProvider;
        this.mServiceProvider = serviceProvider;
    }

    @Override
    public void attachView(MVPContract.View view) {
        Log.d(TAG, "attachView");
        mView = (ServiceInfoContract.View) view;
        mViewAttached = true;

        mServiceProvider.getSingleValueObservable(mAuthProvider.getUserId())
                .subscribe(new Action1<ServiceModel>() {
                    @Override
                    public void call(ServiceModel serviceModel) {
                        if (!mViewAttached)
                            return;

                        mView.updateServiceInformation(serviceModel);

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (!mViewAttached)
                            return;

                        mView.showError(throwable.getMessage());
                    }
                });
    }

    @Override
    public void detachView() {
        mViewAttached = false;
    }


    @Override
    public void updateServiceInfo(@NonNull ServiceModel service) {
        service.setuId(mAuthProvider.getUserId());

        mServiceProvider.insert(service).subscribe(
                new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mViewAttached)
                            mView.serviceUpdated();
                    }
                },
                new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (mViewAttached)
                            mView.showError(throwable.getMessage());
                    }
                });
    }
}
