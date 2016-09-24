package com.manpdev.appointment.ui.presenter;

import android.support.annotation.Nullable;
import android.util.Log;

import com.manpdev.appointment.data.model.UserModel;
import com.manpdev.appointment.data.remote.AuthProvider;
import com.manpdev.appointment.data.remote.listeners.AuthStateProviderListener;
import com.manpdev.appointment.data.remote.DataProvider;
import com.manpdev.appointment.ui.mvp.UserRegistrationContract;
import com.manpdev.appointment.ui.mvp.base.MVPContract;

import rx.functions.Action1;

/**
 * novoa on 9/11/16.
 */

public class UserRegistrationPresenter implements UserRegistrationContract.Presenter, AuthStateProviderListener {

    private static final String TAG = "LoginPresenter";

    private UserRegistrationContract.View mView;
    private final AuthProvider mAuthProvider;
    private final DataProvider<UserModel> mUserProvider;

    private boolean mViewAttached;

    public UserRegistrationPresenter(AuthProvider authProvider,
                                     DataProvider<UserModel> mUserProvider) {
        this.mAuthProvider = authProvider;
        this.mUserProvider = mUserProvider;
    }

    @Override
    public void attachView(MVPContract.View view) {
        mView = (UserRegistrationContract.View) view;
        mViewAttached = true;
        mAuthProvider.registerListener(this);
    }

    @Override
    public void detachView() {
        mViewAttached = false;
        mAuthProvider.unRegisterListener();
    }

    @Override
    public void onStateChanged(@Nullable String userId) {
        if (!mViewAttached)
            return;

        if (!mAuthProvider.isUserLoggedIn())
            mView.launchLoginActivity();
    }

    @Override
    public void registerUser(String fullName, String phoneNumber) {
        Log.d(TAG, "registerUser");
        
        UserModel user = new UserModel(mAuthProvider.getUserId());
        user.setEmail(mAuthProvider.getUserEmail());
        user.setFullName(fullName);
        user.setPhone(phoneNumber);

        mUserProvider.insert(user)
                .subscribe(
                        new Action1<Void>() {
                            @Override
                            public void call(Void aVoid) {
                                mView.launchFirstActivity();
                            }
                        },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                if(mViewAttached)
                                    mView.showError(throwable.getMessage());
                            }
                        });
    }
}
