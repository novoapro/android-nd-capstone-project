package com.manpdev.appointment.di.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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
    public Context provideContext() {
        return mContext;
    }

    @Provides
    public SharedPreferences provideSharedPreferences(Context _context) {
        return PreferenceManager.getDefaultSharedPreferences(_context);
    }

    @Provides
    public SharedPreferences.Editor provideSharedPreferencesEditor(SharedPreferences _sharedPreferences) {
        return _sharedPreferences.edit();
    }
}
