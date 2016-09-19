package com.manpdev.appointment.di;

import com.manpdev.appointment.di.module.ContextModule;
import com.manpdev.appointment.di.module.FirebaseModule;

import dagger.Component;

/**
 * novoa on 9/18/16.
 */

@Component(modules = {
        FirebaseModule.class,
        ContextModule.class}
)
public interface ApplicationComponent {


}
