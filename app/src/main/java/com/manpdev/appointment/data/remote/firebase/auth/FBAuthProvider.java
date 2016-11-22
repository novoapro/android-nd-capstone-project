package com.manpdev.appointment.data.remote.firebase.auth;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.manpdev.appointment.data.model.UserEmailModel;
import com.manpdev.appointment.data.remote.AuthProvider;
import com.manpdev.appointment.data.remote.firebase.database.FBUserProvider;
import com.manpdev.appointment.data.remote.listeners.AuthStateProviderListener;

import rx.functions.Action1;

/**
 * novoa on 9/24/16.
 */

public class FBAuthProvider implements AuthProvider, FirebaseAuth.AuthStateListener {

    private static final String TAG = "FBAuthProvider";

    private FirebaseAuth mFirebaseAuth;
    private FBUserProvider mUserProvider;
    private AuthStateProviderListener mListener;

    public FBAuthProvider(FirebaseAuth firebaseAuth, FBUserProvider userProvider) {
        this.mFirebaseAuth = firebaseAuth;
        this.mUserProvider = userProvider;
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        Log.d(TAG, "onAuthStateChanged() called with: firebaseAuth = [" + firebaseAuth + "]");
        if (mListener == null)
            return;

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            mListener.onStateChanged(currentUser.getUid());
            registerUserEmail(currentUser);
        } else
            mListener.onStateChanged(null);
    }

    @Override
    public void registerListener(@NonNull AuthStateProviderListener listener) {
        this.mFirebaseAuth.addAuthStateListener(this);
        mListener = listener;
    }

    @Override
    public void unRegisterListener() {
        this.mFirebaseAuth.removeAuthStateListener(this);
        mListener = null;
    }

    @Override
    public void loginWithGoogle(@NonNull String token) {
        AuthCredential credential = GoogleAuthProvider.getCredential(token, null);
        firebaseAuthWithCredentials(credential);
    }

    @Override
    public void logoutUser() {
        mFirebaseAuth.signOut();
    }

    @Override
    public boolean isUserLoggedIn() {
        return mFirebaseAuth.getCurrentUser() != null;
    }

    @Override
    @Nullable
    public String getUserId() {
        return mFirebaseAuth.getCurrentUser() != null ? mFirebaseAuth.getCurrentUser().getUid() : null;
    }

    @Override
    public String getUserEmail() {
        return mFirebaseAuth.getCurrentUser() != null ? mFirebaseAuth.getCurrentUser().getEmail() : null;
    }

    @Override
    public String getUserFullName() {
        return mFirebaseAuth.getCurrentUser() != null ? mFirebaseAuth.getCurrentUser().getDisplayName() : null;
    }

    @Override
    public Uri getUserAvatarUri() {
        return mFirebaseAuth.getCurrentUser() != null ? mFirebaseAuth.getCurrentUser().getPhotoUrl() : null;
    }

    private void firebaseAuthWithCredentials(AuthCredential credential) {
        mFirebaseAuth.signInWithCredential(credential);
    }

    private void registerUserEmail(@NonNull final FirebaseUser currentUser) {
        mUserProvider.getSingleValueObservable(currentUser.getEmail()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                if (!TextUtils.isEmpty(s))
                    return;

                subscribeUserEmail(currentUser);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                subscribeUserEmail(currentUser);
            }
        });
    }

    private void subscribeUserEmail(@NonNull FirebaseUser currentUser) {
        UserEmailModel model = new UserEmailModel();
        model.setuId(currentUser.getUid());
        model.setEmail(currentUser.getEmail());
        mUserProvider.insert(model);
    }
}
