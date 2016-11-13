package com.manpdev.appointment.ui.di.module;

import android.content.Context;

import com.manpdev.appointment.data.remote.AuthProvider;
import com.manpdev.appointment.data.remote.firebase.database.FBCAppointmentProvider;
import com.manpdev.appointment.data.remote.firebase.database.FBPAppointmentProvider;
import com.manpdev.appointment.data.remote.firebase.database.FBRatingProvider;
import com.manpdev.appointment.data.remote.firebase.database.FBReviewProvider;
import com.manpdev.appointment.data.remote.firebase.database.FBServiceProvider;
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

import dagger.Module;
import dagger.Provides;

/**
 * novoa on 9/24/16.
 */

@Module
public class PresentersModule {

    public PresentersModule() {
    }

    @Provides
    LoginContract.Presenter provideLoginPresenter(Context context, AuthProvider authProvider){
        return new LoginPresenter(context, authProvider);
    }

    @Provides
    ServiceInfoContract.Presenter provideServiceInfoPresenter(Context context, AuthProvider authProvider, FBServiceProvider serviceProvider){
        return new ServiceInfoPresenter(context, authProvider, serviceProvider);
    }

    @Provides
    ClientAppoinmentContract.Presenter provideClientAppointmentPresenter(Context context, AuthProvider authProvider, FBCAppointmentProvider appointmentProvider){
        return new ClientAppointmentPresenter(context, authProvider, appointmentProvider);
    }

    @Provides
    ProviderAppoinmentContract.Presenter provideProviderAppointmentPresenter(Context context, AuthProvider authProvider, FBPAppointmentProvider appointmentProvider){
        return new ProviderAppointmentPresenter(context, authProvider, appointmentProvider);
    }

    @Provides
    ServiceReviewContract.Presenter provideServiceReviewPresenter(Context context, AuthProvider authProvider, FBReviewProvider reviewProvider, FBRatingProvider ratingProvider){
        return new ServiceReviewPresenter(context, authProvider, reviewProvider, ratingProvider);
    }
}
