package com.manpdev.appointment.ui.presenter;

import android.content.Context;
import android.util.Log;

import com.manpdev.appointment.data.model.ServiceModel;
import com.manpdev.appointment.data.remote.AuthProvider;
import com.manpdev.appointment.data.remote.DataProvider;
import com.manpdev.appointment.ui.mvp.ServiceInfoContract;
import com.manpdev.appointment.ui.mvp.base.MVPContract;

import rx.functions.Action1;

/**
 * novoa on 9/11/16.
 */

public class ServiceInfoPresenter implements ServiceInfoContract.Presenter {

    private static final String TAG = "LoginPresenter";

    private ServiceInfoContract.View mView;
    private final Context mContext;
    private final AuthProvider mAuthProvider;
    private final DataProvider<ServiceModel> mServiceProvider;

    private boolean mViewAttached;

    public ServiceInfoPresenter(Context context, AuthProvider authProvider, DataProvider<ServiceModel> serviceProvider) {
        this.mContext = context;
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

                        if (serviceModel != null)
                            mView.updateServiceInformation(serviceModel);
                        else
                            mView.launchAddServiceView();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (!mViewAttached)
                            return;
                        
                        mView.showError(throwable.getMessage());
                        mView.launchAddServiceView();
                    }
                });
    }

    @Override
    public void detachView() {
        mViewAttached = false;
    }
}
