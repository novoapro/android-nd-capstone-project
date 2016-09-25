package com.manpdev.appointment.di;

import com.manpdev.appointment.di.module.ContextModule;
import com.manpdev.appointment.di.module.FirebaseModule;
import com.manpdev.appointment.di.module.DataModule;
import com.manpdev.appointment.ui.di.ActivityComponent;

import javax.inject.Singleton;

import dagger.Component;

/**
 * novoa on 9/18/16.
 */

@Component(modules =
        {
                ContextModule.class,
                FirebaseModule.class,
                DataModule.class
        })
@Singleton
public interface ApplicationComponent {

    ActivityComponent activity();

}
