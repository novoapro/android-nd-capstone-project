package com.manpdev.appointment.ui.di.module;

import com.manpdev.appointment.ui.helper.AlertsHelper;

import dagger.Module;
import dagger.Provides;

/**
 * novoa on 9/24/16.
 */

@Module
public class UIUtilsModule {

    public UIUtilsModule() {
    }

    @Provides
    AlertsHelper provideAlertsHelper(){
        return new AlertsHelper();
    }
}
