package com.manpdev.appointment.ui.di.module;

import android.content.Context;

import com.manpdev.appointment.data.local.CalendarProvider;
import com.manpdev.appointment.data.remote.AuthProvider;
import com.manpdev.appointment.data.remote.firebase.database.FBCAppointmentProvider;
import com.manpdev.appointment.data.remote.firebase.database.FBPAppointmentProvider;
import com.manpdev.appointment.data.remote.firebase.database.FBRatingProvider;
import com.manpdev.appointment.data.remote.firebase.database.FBReviewProvider;
import com.manpdev.appointment.data.remote.firebase.database.FBServiceProvider;
import com.manpdev.appointment.data.remote.firebase.database.FBUserProvider;
import com.manpdev.appointment.data.remote.firebase.storage.FBBannerStorage;
import com.manpdev.appointment.ui.mvp.ClientAppointmentContract;
import com.manpdev.appointment.ui.mvp.LoginContract;
import com.manpdev.appointment.ui.mvp.ProviderAppointmentContract;
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
    ServiceInfoContract.Presenter provideServiceInfoPresenter(Context context, AuthProvider authProvider, FBServiceProvider serviceProvider, FBBannerStorage bannerStorage){
        return new ServiceInfoPresenter(context, authProvider, serviceProvider, bannerStorage);
    }

    @Provides
    ClientAppointmentContract.Presenter provideClientAppointmentPresenter(Context context, AuthProvider authProvider,
                                                                          FBCAppointmentProvider appointmentProvider,
                                                                          FBUserProvider userProvider, FBServiceProvider serviceProvider,
                                                                          CalendarProvider calendarProvider, FBRatingProvider ratingProvider,
                                                                          FBReviewProvider reviewProvider){
        return new ClientAppointmentPresenter(context, authProvider, appointmentProvider, userProvider, serviceProvider, calendarProvider, reviewProvider, ratingProvider);
    }

    @Provides
    ProviderAppointmentContract.Presenter provideProviderAppointmentPresenter(Context context, AuthProvider authProvider, FBPAppointmentProvider appointmentProvider, CalendarProvider calendarProvider){
        return new ProviderAppointmentPresenter(context, authProvider, appointmentProvider, calendarProvider);
    }

    @Provides
    ServiceReviewContract.Presenter provideServiceReviewPresenter(Context context, AuthProvider authProvider, FBReviewProvider reviewProvider, FBRatingProvider ratingProvider){
        return new ServiceReviewPresenter(context, authProvider, reviewProvider, ratingProvider);
    }
}
