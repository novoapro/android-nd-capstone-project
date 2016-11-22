package com.manpdev.appointment.data.remote.firebase.storage;

import com.manpdev.appointment.data.remote.AuthProvider;
import com.manpdev.appointment.data.remote.StorageProvider;
import com.manpdev.appointment.data.remote.firebase.storage.base.FBStorageProvider;

import javax.inject.Inject;

/**
 * novoa on 11/22/16.
 */

public class FBBannerStorage{

    private static final String TAG = "FBBannerStorage";
    private static final String BANNER_REFERENCE = FBStorageProvider.PUBLIC_REFERENCE + "/" + "banners";

    private FBStorageProvider mStorageProvider;
    private AuthProvider mAuthProvider;

    @Inject
    public FBBannerStorage(FBStorageProvider mStorageProvider, AuthProvider mAuthProvider) {
        this.mStorageProvider = mStorageProvider;
        this.mAuthProvider = mAuthProvider;
    }

    public void store(){

    }
}
