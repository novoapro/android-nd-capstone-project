package com.manpdev.appointment.data.remote.firebase.storage.base;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.manpdev.appointment.data.remote.AuthProvider;
import com.manpdev.appointment.data.remote.StorageProvider;
import com.manpdev.appointment.data.remote.listeners.AuthStateProviderListener;

import javax.inject.Inject;

/**
 * novoa on 9/24/16.
 */

public class FBStorageProvider implements StorageProvider{

    private static final String TAG = "FBAuthProvider";

    public static final String PUBLIC_REFERENCE = "public";

    private final FirebaseStorage mFireBaseStorage;

    public FBStorageProvider(FirebaseStorage firebaseStorage) {
        this.mFireBaseStorage = firebaseStorage;
    }

    public StorageReference getPublicReference(){
        return this.mFireBaseStorage.getReference(PUBLIC_REFERENCE);
    }
}
