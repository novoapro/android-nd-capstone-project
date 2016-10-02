package com.manpdev.appointment.data.remote;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.manpdev.appointment.data.remote.listeners.AuthStateProviderListener;

/**
 * novoa on 9/24/16.
 */

public interface AuthProvider {
    void registerListener(@NonNull AuthStateProviderListener listener);
    void unRegisterListener();
    void loginWithGoogle(@NonNull String token);
    void logoutUser();
    boolean isUserLoggedIn();
    String getUserId();
    String getUserEmail();
    String getUserFullName();
    Uri getUserAvatarUri();
}
