package com.manpdev.appointment.ui.di.component;

import com.manpdev.appointment.ui.activity.SplashScreenActivity;
import com.manpdev.appointment.ui.activity.base.BaseNavigationActivity;
import com.manpdev.appointment.ui.di.ActivityScope;
import com.manpdev.appointment.ui.di.module.PresentersModule;
import com.manpdev.appointment.ui.di.module.UIUtilsModule;

import dagger.Subcomponent;

/**
 * novoa on 9/18/16.
 */

@ActivityScope
@Subcomponent(modules = UIUtilsModule.class)
public interface ActivityComponent {
    MVPComponent mvp(PresentersModule module);
    void inject(SplashScreenActivity splashScreenActivity);
    void inject(BaseNavigationActivity baseNavigationActivity);
}
