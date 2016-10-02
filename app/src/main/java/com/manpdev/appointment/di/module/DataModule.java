package com.manpdev.appointment.di.module;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.manpdev.appointment.data.remote.AuthProvider;
import com.manpdev.appointment.data.remote.DataProvider;
import com.manpdev.appointment.data.remote.firebase.FBAuthProvider;
import com.manpdev.appointment.data.remote.firebase.FBServiceProvider;

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
    public AuthProvider provideAuthProvider(FirebaseAuth fbAuth){
        return new FBAuthProvider(fbAuth);
    }
}
