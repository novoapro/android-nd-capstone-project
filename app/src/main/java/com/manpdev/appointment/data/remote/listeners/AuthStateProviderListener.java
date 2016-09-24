package com.manpdev.appointment.data.remote.listeners;

import android.support.annotation.Nullable;

/**
 * novoa on 9/24/16.
 */

public interface AuthStateProviderListener {
    void onStateChanged(@Nullable String userId);
}
