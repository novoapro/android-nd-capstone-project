package com.manpdev.appointment.ui.presenter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.storage.StorageMetadata;
import com.manpdev.appointment.data.model.ServiceModel;
import com.manpdev.appointment.data.remote.AuthProvider;
import com.manpdev.appointment.data.remote.firebase.database.FBServiceProvider;
import com.manpdev.appointment.data.remote.firebase.storage.FBBannerStorage;
import com.manpdev.appointment.data.remote.listeners.StorageMetadataListener;
import com.manpdev.appointment.data.remote.listeners.UploadListener;
import com.manpdev.appointment.ui.mvp.ServiceInfoContract;
import com.manpdev.appointment.ui.mvp.base.MVPContract;

import java.io.IOException;
import java.io.InputStream;

import rx.functions.Action1;

/**
 * novoa on 9/11/16.
 */

public class ServiceInfoPresenter implements ServiceInfoContract.Presenter, UploadListener{

    private static final String TAG = "LoginPresenter";

    private ServiceInfoContract.View mView;
    private final AuthProvider mAuthProvider;
    private final FBServiceProvider mServiceProvider;
    private final FBBannerStorage mBannerStorage;

    private boolean mViewAttached;

    public ServiceInfoPresenter(Context context, AuthProvider authProvider, FBServiceProvider serviceProvider, FBBannerStorage bannerStorage) {
        this.mAuthProvider = authProvider;
        this.mServiceProvider = serviceProvider;
        this.mBannerStorage = bannerStorage;
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

        mBannerStorage.addUploadListener(this);
    }

    @Override
    public void detachView() {
        mViewAttached = false;
        mBannerStorage.removeUploadListener();
    }


    @Override
    public void updateServiceInfo(@NonNull final ServiceModel service) {
        service.setuId(mAuthProvider.getUserId());
        mBannerStorage.getBannerUrl(new StorageMetadataListener() {
            @Override
            public void onDownloadUriSuccess(Uri downloadUri) {
                service.setBanner(downloadUri.toString());
                submitUpdatedService(service);
            }

            @Override
            public void onDownloadUriFailed() {
                submitUpdatedService(service);
            }
        });
    }

    private void submitUpdatedService(ServiceModel model){
        mServiceProvider.insert(model).subscribe(
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

    @Override
    public void uploadNewBanner(InputStream stream) {
        mBannerStorage.upload(stream);
    }

    @Override
    public void onUploadCompleted(Uri downloadUrl, StorageMetadata metadata) {
        if(mViewAttached)
            mView.bannerCallback(true);
    }

    @Override
    public void onUploadFailed(Exception exception) {
        if(mViewAttached)
            mView.bannerCallback(false);
    }
}
