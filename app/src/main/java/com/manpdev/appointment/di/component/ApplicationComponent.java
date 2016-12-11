package com.manpdev.appointment.di.component;

import com.manpdev.appointment.di.module.ContextModule;
import com.manpdev.appointment.di.module.FirebaseModule;
import com.manpdev.appointment.di.module.DataModule;
import com.manpdev.appointment.service.di.component.ServiceComponent;
import com.manpdev.appointment.ui.di.component.ActivityComponent;

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
    ServiceComponent service();
}
