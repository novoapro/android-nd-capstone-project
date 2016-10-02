package com.manpdev.appointment.ui.mvp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.manpdev.appointment.ui.mvp.base.MVPContract;

/**
 * novoa on 9/11/16.
 */

public interface LoginContract extends MVPContract{
    interface Presenter extends MVPContract.Presenter{
        void userSignedWithGoogle(String idToken);
        void userSignFailed();
        void signUserWithGoogle(AppCompatActivity hostActivity);
    }

    interface View extends MVPContract.View{
        void launchFirstActivity();
        void launchGoogleAuthentication(Intent intent);

        void enableSigninControllers(boolean b);
    }
}
