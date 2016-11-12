package com.manpdev.appointment.ui.di.module;

import android.content.Context;

import com.manpdev.appointment.data.remote.AuthProvider;
import com.manpdev.appointment.data.remote.firebase.FBCAppointmentProvider;
import com.manpdev.appointment.data.remote.firebase.FBPAppointmentProvider;
import com.manpdev.appointment.data.remote.firebase.FBRatingProvider;
import com.manpdev.appointment.data.remote.firebase.FBReviewProvider;
import com.manpdev.appointment.data.remote.firebase.FBServiceProvider;
import com.manpdev.appointment.ui.mvp.ClientAppoinmentContract;
import com.manpdev.appointment.ui.mvp.LoginContract;
import com.manpdev.appointment.ui.mvp.ProviderAppoinmentContract;
import com.manpdev.appointment.ui.mvp.ServiceInfoContract;
import com.manpdev.appointment.ui.mvp.ServiceReviewContract;
import com.manpdev.appointment.ui.presenter.ClientAppointmentPresenter;
import com.manpdev.appointment.ui.presenter.LoginPresenter;
import com.manpdev.appointment.ui.presenter.ProviderAppointmentPresenter;
import com.manpdev.appointment.ui.presenter.ServiceInfoPresenter;
import com.manpdev.appointment.ui.presenter.ServiceReviewPresenter;
import com.manpdev.appointment.ui.utils.AlertsHelper;

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
