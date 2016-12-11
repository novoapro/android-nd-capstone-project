package com.manpdev.appointment.service.di.component;

import com.manpdev.appointment.service.WidgetRatingUpdateService;
import com.manpdev.appointment.service.di.ServiceScope;
import com.manpdev.appointment.ui.activity.SplashScreenActivity;
import com.manpdev.appointment.ui.activity.base.BaseNavigationActivity;
import com.manpdev.appointment.ui.di.ActivityScope;
import com.manpdev.appointment.ui.di.component.MVPComponent;
import com.manpdev.appointment.ui.di.module.PresentersModule;
import com.manpdev.appointment.ui.di.module.UIUtilsModule;

import dagger.Subcomponent;

/**
 * novoa on 9/18/16.
 */

@ServiceScope
@Subcomponent
public interface ServiceComponent {
    void inject(WidgetRatingUpdateService service);
}
