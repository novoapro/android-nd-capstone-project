package com.manpdev.appointment.ui.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.manpdev.appointment.R;
import com.manpdev.appointment.data.model.UserModel;
import com.manpdev.appointment.data.remote.AuthProvider;
import com.manpdev.appointment.data.remote.listeners.AuthStateProviderListener;
import com.manpdev.appointment.data.remote.DataProvider;
import com.manpdev.appointment.ui.mvp.LoginContract;
import com.manpdev.appointment.ui.mvp.base.MVPContract;

import rx.functions.Action1;

/**
 * novoa on 9/11/16.
 */

public class LoginPresenter implements LoginContract.Presenter, AuthStateProviderListener,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LoginPresenter";

    private LoginContract.View mView;
    private final Context mContext;
    private final AuthProvider mAuthProvider;
    private final DataProvider<UserModel> mUserProvider;

    private boolean mViewAttached;

    public LoginPresenter(Context context, AuthProvider authProvider,
                          DataProvider<UserModel> mUserProvider) {
        this.mContext = context;
        this.mAuthProvider = authProvider;
        this.mUserProvider = mUserProvider;
    }

    @Override
    public void attachView(MVPContract.View view) {
        mView = (LoginContract.View) view;
        mViewAttached = true;
        mAuthProvider.registerListener(this);
    }

    @Override
    public void detachView() {
        mViewAttached = false;
        mAuthProvider.unRegisterListener();
    }

    @Override
    public void userSignedWithGoogle(String idToken) {
        mAuthProvider.loginWithGoogle(idToken);
    }

    @Override
    public void userSignFailed() {
        if (mViewAttached)
            mView.showError(R.string.signing_failed);
    }

    @Override
    public void signUserWithGoogle(AppCompatActivity hostActivity) {
        if (!mViewAttached)
            return;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mContext.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(mContext)
                .enableAutoManage(hostActivity, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        mView.launchGoogleAuthentication(signInIntent);
    }

    @Override
    public void onStateChanged(@Nullable String userId) {
        if (!mViewAttached)
            return;

        if (TextUtils.isEmpty(userId))
            mView.enableSigninControllers(true);
        else
            checkAppUserData(userId);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.w(TAG, "onConnectionFailed: " + connectionResult.getErrorMessage());
    }

    private void checkAppUserData(String userId) {
        mUserProvider.getSingleValueObservable(userId)
                .subscribe(
                        new Action1<UserModel>() {
                            @Override
                            public void call(UserModel userModel) {
                                if (mViewAttached) {
                                    if (userModel == null)
                                        mView.launchRegistrationActivity();
                                    else
                                        mView.launchFirstActivity();
                                }
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                if (mViewAttached)
                                    mView.launchRegistrationActivity();
                            }
                        });
    }
}
