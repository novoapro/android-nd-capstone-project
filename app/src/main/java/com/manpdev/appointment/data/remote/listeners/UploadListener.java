package com.manpdev.appointment.data.remote.listeners;

import android.net.Uri;

import com.google.firebase.storage.StorageMetadata;

/**
 * novoa on 11/23/16.
 */

public interface UploadListener {
    void onUploadCompleted(Uri downloadUrl, StorageMetadata metadata);
    void onUploadFailed(Exception exception);
}
