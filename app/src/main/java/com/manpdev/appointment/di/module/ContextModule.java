package com.manpdev.appointment.di.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;

import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * novoa on 9/18/16.
 */

@Module
public class ContextModule {

    private Context mContext;

    public ContextModule(Context context) {
        this.mContext = context;
    }

    @Provides
    Context provideContext() {
        return mContext;
    }

    @Provides
    SharedPreferences provideSharedPreferences(Context _context) {
        return PreferenceManager.getDefaultSharedPreferences(_context);
    }

    @Provides
    @Singleton
    Picasso providePicasso(Context context){
        return Picasso.with(context);
    }

    @Provides
    SharedPreferences.Editor provideSharedPreferencesEditor(SharedPreferences _sharedPreferences) {
        return _sharedPreferences.edit();
    }
}
