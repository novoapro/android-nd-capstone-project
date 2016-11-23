package com.manpdev.appointment.data.remote.firebase.storage;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.manpdev.appointment.data.remote.AuthProvider;
import com.manpdev.appointment.data.remote.StorageProvider;
import com.manpdev.appointment.data.remote.listeners.StorageMetadataListener;
import com.manpdev.appointment.data.remote.listeners.UploadListener;

import java.io.InputStream;

import javax.inject.Inject;

/**
 * novoa on 11/22/16.
 */

public class FBBannerStorage {

    private static final String TAG = "FBBannerStorage";
    private static final String BANNER_REFERENCE = "banners";

    private StorageProvider mStorageProvider;
    private AuthProvider mAuthProvider;
    private UploadListener mUploadListener;
    private StorageMetadataListener mStorageMetadataListener;

    @Inject
    FBBannerStorage(StorageProvider mStorageProvider, AuthProvider mAuthProvider) {
        this.mStorageProvider = mStorageProvider;
        this.mAuthProvider = mAuthProvider;
    }

    public void addUploadListener(@NonNull UploadListener listener) {
        mUploadListener = listener;
    }

    public void removeUploadListener() {
        mUploadListener = null;
    }

    public void addStorageMetadataListener(@NonNull StorageMetadataListener listener) {
        mStorageMetadataListener = listener;
    }

    public void removeStorageMetadataListener() {
        mStorageMetadataListener = null;
    }

    public void upload(InputStream stream) {
        Log.d(TAG, "upload");

        final StorageReference bannerReference = mStorageProvider.getPublicReference().child(buildBannerReference());

        UploadTask uploadTask = bannerReference.putStream(stream);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                if (mUploadListener != null)
                    mUploadListener.onUploadFailed(exception);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if (mUploadListener != null)
                    mUploadListener.onUploadCompleted(taskSnapshot.getDownloadUrl(), taskSnapshot.getMetadata());
            }
        });
    }

    public void getBannerUrl(final StorageMetadataListener listener) {
        Log.d(TAG, "getBannerUrl: ");

        StorageReference bannerReference = mStorageProvider.getPublicReference().child(buildBannerReference());

        bannerReference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        if (listener != null)
                            listener.onDownloadUriSuccess(uri);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (listener != null)
                            listener.onDownloadUriFailed();
                    }
                });
    }

    private String buildBannerReference() {
        return BANNER_REFERENCE + "/" + mAuthProvider.getUserId() + ".jpg";
    }
}
