package com.manpdev.appointment.di.module;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.manpdev.appointment.data.model.ServiceModel;
import com.manpdev.appointment.data.remote.AuthProvider;
import com.manpdev.appointment.data.remote.firebase.FBAuthProvider;
import com.manpdev.appointment.data.remote.firebase.FBServiceProvider;

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
    public AuthProvider provideAuthProvider(FirebaseAuth fbAuth){
        return new FBAuthProvider(fbAuth);
    }

    @Provides
    @Singleton
    public FBServiceProvider provideServiceInfoProvider(FirebaseDatabase fbDatabase){
        return new FBServiceProvider(fbDatabase);
    }
}
