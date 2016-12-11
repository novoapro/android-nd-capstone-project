package com.manpdev.appointment.di.module;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.manpdev.appointment.data.local.CalendarProvider;
import com.manpdev.appointment.data.remote.AuthProvider;
import com.manpdev.appointment.data.remote.DatabaseProvider;
import com.manpdev.appointment.data.remote.StorageProvider;
import com.manpdev.appointment.data.remote.firebase.auth.FBAuthProvider;
import com.manpdev.appointment.data.remote.firebase.database.FBUserProvider;
import com.manpdev.appointment.data.remote.firebase.database.base.FBBaseDatabaseProvider;
import com.manpdev.appointment.data.remote.firebase.storage.base.FBStorageProvider;

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
    DatabaseProvider provideDatabaseProvider(FirebaseDatabase fbDatabase){
        return new FBBaseDatabaseProvider(fbDatabase);
    }

    @Provides
    @Singleton
    AuthProvider provideAuthProvider(FirebaseAuth fbAuth, FBUserProvider userProvider){
        return new FBAuthProvider(fbAuth, userProvider);
    }

    @Provides
    @Singleton
    StorageProvider provideStorageProvider(FirebaseStorage fbStorage){
        return new FBStorageProvider(fbStorage);
    }

    @Provides
    @Singleton
    CalendarProvider provideCalendarProvider(Context context){
        return new CalendarProvider(context);
    }
}
