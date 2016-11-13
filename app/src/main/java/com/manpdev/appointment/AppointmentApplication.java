package com.manpdev.appointment;

import android.app.Application;
import android.util.Log;

import com.manpdev.appointment.di.component.ApplicationComponent;
import com.manpdev.appointment.di.component.DaggerApplicationComponent;
import com.manpdev.appointment.di.module.ContextModule;
import com.manpdev.appointment.di.module.FirebaseModule;

/**
 * novoa on 9/11/16.
 */

public class AppointmentApplication extends Application {
    private static final String TAG = "AppointmentApplication";

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        mApplicationComponent = DaggerApplicationComponent.builder()
                .contextModule(new ContextModule(this))
                .firebaseModule(new FirebaseModule())
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }
}
