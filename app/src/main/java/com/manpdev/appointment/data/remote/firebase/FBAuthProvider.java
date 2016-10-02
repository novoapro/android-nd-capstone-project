package com.manpdev.appointment.data.remote.firebase;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.manpdev.appointment.data.remote.AuthProvider;
import com.manpdev.appointment.data.remote.listeners.AuthStateProviderListener;

import javax.inject.Inject;

/**
 * novoa on 9/24/16.
 */

public class FBAuthProvider implements AuthProvider, FirebaseAuth.AuthStateListener {

    private static final String TAG = "FBAuthProvider";

    private FirebaseAuth mFirebaseAuth;
    private AuthStateProviderListener mListener;

    @Inject
    public FBAuthProvider(FirebaseAuth firebaseAuth) {
        this.mFirebaseAuth = firebaseAuth;
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        Log.d(TAG, "onAuthStateChanged() called with: firebaseAuth = [" + firebaseAuth + "]");
        if (mListener == null)
            return;

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        mListener.onStateChanged(currentUser != null ? currentUser.getUid() : null);
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
}
