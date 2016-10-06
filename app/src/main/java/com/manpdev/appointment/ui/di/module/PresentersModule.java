package com.manpdev.appointment.ui.di.module;

import android.content.Context;

import com.manpdev.appointment.data.model.ServiceModel;
import com.manpdev.appointment.data.remote.AuthProvider;
import com.manpdev.appointment.data.remote.DataProvider;
import com.manpdev.appointment.ui.mvp.LoginContract;
import com.manpdev.appointment.ui.mvp.ServiceInfoContract;
import com.manpdev.appointment.ui.presenter.LoginPresenter;
import com.manpdev.appointment.ui.presenter.ServiceInfoPresenter;

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
    ServiceInfoContract.Presenter provideServiceInfoPresenter(Context context, AuthProvider authProvider, DataProvider<ServiceModel> serviceProvider){
        return new ServiceInfoPresenter(context, authProvider, serviceProvider);
    }
}
