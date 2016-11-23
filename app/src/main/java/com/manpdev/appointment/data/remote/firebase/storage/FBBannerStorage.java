package com.manpdev.appointment.data.remote.firebase.storage;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.manpdev.appointment.data.remote.AuthProvider;
import com.manpdev.appointment.data.remote.StorageProvider;
import com.manpdev.appointment.data.remote.firebase.storage.base.FBStorageProvider;

import java.io.InputStream;
import java.util.Locale;

import javax.inject.Inject;

/**
 * novoa on 11/22/16.
 */

public class FBBannerStorage {

    private static final String TAG = "FBBannerStorage";
    private static final String BANNER_REFERENCE = "banners";

    private StorageProvider mStorageProvider;
    private AuthProvider mAuthProvider;

    @Inject
    public FBBannerStorage(StorageProvider mStorageProvider, AuthProvider mAuthProvider) {
        this.mStorageProvider = mStorageProvider;
        this.mAuthProvider = mAuthProvider;
    }

    public void upload(InputStream stream) {
        StorageReference bannerReference = mStorageProvider.getPublicReference().child(buildBannerReference());

        UploadTask uploadTask = bannerReference.putStream(stream);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });
    }

    private String buildBannerReference() {
        return BANNER_REFERENCE + "/" + mAuthProvider.getUserId() + ".jpg";
    }
}
