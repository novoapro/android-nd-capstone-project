package com.manpdev.appointment;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.manpdev.appointment.di.component.ApplicationComponent;
import com.manpdev.appointment.di.component.DaggerApplicationComponent;
import com.manpdev.appointment.di.module.ContextModule;
import com.manpdev.appointment.di.module.FirebaseModule;
import com.manpdev.appointment.service.WidgetRatingUpdateService;

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

        updateExistingWidgets();
    }

    private void updateExistingWidgets() {
        Intent updateService = new Intent(this, WidgetRatingUpdateService.class);
        startService(updateService);
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }
}
