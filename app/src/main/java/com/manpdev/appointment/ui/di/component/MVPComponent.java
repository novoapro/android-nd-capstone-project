package com.manpdev.appointment.ui.di.component;

import com.manpdev.appointment.ui.activity.ClientAppointmentListActivity;
import com.manpdev.appointment.ui.activity.LoginActivity;
import com.manpdev.appointment.ui.activity.ProviderAppointmentListActivity;
import com.manpdev.appointment.ui.activity.ProviderServiceInfoActivity;
import com.manpdev.appointment.ui.activity.ProviderServiceReviewListActivity;
import com.manpdev.appointment.ui.di.MVPScope;
import com.manpdev.appointment.ui.di.module.PresentersModule;

import dagger.Subcomponent;

/**
 * novoa on 9/18/16.
 */

@MVPScope
@Subcomponent(modules = PresentersModule.class)
public interface MVPComponent {
    //presenters required
    void inject(LoginActivity view);
    void inject(ProviderServiceInfoActivity view);
    void inject(ClientAppointmentListActivity clientAppointmentListActivity);
    void inject(ProviderAppointmentListActivity providerAppointmentListActivity);
    void inject(ProviderServiceReviewListActivity providerServiceReviewListActivity);
}
