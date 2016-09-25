package com.manpdev.appointment.ui.mvp.di;

import com.manpdev.appointment.ui.activities.LoginActivity;
import com.manpdev.appointment.ui.activities.SplashScreenActivity;
import com.manpdev.appointment.ui.activities.UserRegistrationActivity;
import com.manpdev.appointment.ui.mvp.di.modules.PresentersModule;

import dagger.Subcomponent;

/**
 * novoa on 9/18/16.
 */

@MVPScope
@Subcomponent(modules = PresentersModule.class)
public interface MVPComponent {
    //presenters required
    void inject(LoginActivity view);
    void inject(UserRegistrationActivity view);
}
