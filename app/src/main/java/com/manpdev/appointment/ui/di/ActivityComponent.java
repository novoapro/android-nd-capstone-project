package com.manpdev.appointment.ui.di;

import com.manpdev.appointment.ui.activities.LoginActivity;
import com.manpdev.appointment.ui.activities.UserRegistrationActivity;
import com.manpdev.appointment.ui.di.modules.PresentersModule;

import dagger.Subcomponent;

/**
 * novoa on 9/18/16.
 */

@ActivityScope
@Subcomponent(modules = PresentersModule.class)
public interface ActivityComponent {
    void inject(LoginActivity view);
    void inject(UserRegistrationActivity view);
}
