package com.manpdev.appointment.ui.di;

import com.manpdev.appointment.ui.activities.SplashScreenActivity;
import com.manpdev.appointment.ui.mvp.di.MVPComponent;
import com.manpdev.appointment.ui.mvp.di.modules.PresentersModule;

import dagger.Subcomponent;

/**
 * novoa on 9/18/16.
 */

@ActivityScope
@Subcomponent()
public interface ActivityComponent {

    void inject(SplashScreenActivity splashScreenActivity);

    MVPComponent mvp(PresentersModule module);
}
