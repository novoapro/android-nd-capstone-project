package com.manpdev.appointment.data.remote;

import android.support.annotation.NonNull;

import com.manpdev.appointment.data.remote.listeners.AuthStateProviderListener;

/**
 * novoa on 9/24/16.
 */

public interface AuthProvider {
    void registerListener(@NonNull AuthStateProviderListener listener);
    void unRegisterListener();
    void loginUser(@NonNull String username, @NonNull String password);
    void loginWithGoogle(@NonNull String token);
    void logoutUser();
    boolean isUserLoggedIn();
    String getUserId();
    String getUserEmail();
}
