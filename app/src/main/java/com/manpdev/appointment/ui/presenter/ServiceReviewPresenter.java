package com.manpdev.appointment.ui.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.manpdev.appointment.data.remote.AuthProvider;
import com.manpdev.appointment.data.remote.firebase.database.FBRatingProvider;
import com.manpdev.appointment.data.remote.firebase.database.FBReviewProvider;
import com.manpdev.appointment.service.WidgetRatingUpdateService;
import com.manpdev.appointment.ui.mvp.ServiceReviewContract;
import com.manpdev.appointment.ui.mvp.base.MVPContract;

/**
 * novoa on 9/11/16.
 */

public class ServiceReviewPresenter implements ServiceReviewContract.Presenter {

    private static final String TAG = "LoginPresenter";
    private final Context mContext;

    private ServiceReviewContract.View mView;
    private final AuthProvider mAuthProvider;
    private final FBReviewProvider mReviewProvider;
    private final FBRatingProvider mRatingProvider;

    private boolean mViewAttached;

    public ServiceReviewPresenter(Context context, AuthProvider authProvider,
                                  FBReviewProvider reviewProvider, FBRatingProvider ratingProvider) {
        this.mAuthProvider = authProvider;
        this.mReviewProvider = reviewProvider;
        this.mRatingProvider = ratingProvider;
        this.mContext = context;
    }

    @Override
    public void attachView(MVPContract.View view) {
        Log.d(TAG, "attachView");
        mView = (ServiceReviewContract.View) view;
        mViewAttached = true;
    }

    @Override
    public void detachView() {
        mViewAttached = false;
    }

    @Override
    public void loadReviewList() {

        if (mViewAttached)
            mView.showList(mReviewProvider.getCollectionObservable(mAuthProvider.getUserId()));
        updateWidgetInformation();
    }

    @Override
    public void loadServiceRating() {
        if (mViewAttached)
            mView.showServiceRating(mRatingProvider.getCollectionObservable(mAuthProvider.getUserId()));
    }

    private void updateWidgetInformation() {
        Intent updateService = new Intent(mContext, WidgetRatingUpdateService.class);
        mContext.startService(updateService);
    }
}
