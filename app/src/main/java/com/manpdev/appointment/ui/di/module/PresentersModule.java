package com.manpdev.appointment.ui.di.module;

import android.content.Context;

import com.manpdev.appointment.data.remote.AuthProvider;
import com.manpdev.appointment.ui.mvp.LoginContract;
import com.manpdev.appointment.ui.presenter.LoginPresenter;

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
}
