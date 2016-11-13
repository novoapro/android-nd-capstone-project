package com.manpdev.appointment.di.module;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.manpdev.appointment.data.model.ServiceModel;
import com.manpdev.appointment.data.remote.AuthProvider;
import com.manpdev.appointment.data.remote.StorageProvider;
import com.manpdev.appointment.data.remote.firebase.auth.FBAuthProvider;
import com.manpdev.appointment.data.remote.firebase.database.FBCAppointmentProvider;
import com.manpdev.appointment.data.remote.firebase.database.FBPAppointmentProvider;
import com.manpdev.appointment.data.remote.firebase.database.FBRatingProvider;
import com.manpdev.appointment.data.remote.firebase.database.FBServiceProvider;
import com.manpdev.appointment.data.remote.firebase.storage.FBStorageProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * novoa on 9/18/16.
 */

@Module
public class DataModule {

    public DataModule() {
    }

    @Provides
    @Singleton
    AuthProvider provideAuthProvider(FirebaseAuth fbAuth){
        return new FBAuthProvider(fbAuth);
    }

    @Provides
    @Singleton
    StorageProvider provideStorageProvider(FirebaseStorage fbStorage){
        return new FBStorageProvider(fbStorage);
    }
}
