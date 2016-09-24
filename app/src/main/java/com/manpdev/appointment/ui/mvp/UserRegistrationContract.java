package com.manpdev.appointment.ui.mvp;

import com.manpdev.appointment.ui.mvp.base.MVPContract;

/**
 * novoa on 9/11/16.
 */

public interface UserRegistrationContract extends MVPContract{
    interface Presenter extends MVPContract.Presenter{
        void registerUser(String fullName, String phoneNumber);
    }

    interface View extends MVPContract.View{
        void launchLoginActivity();
        void launchFirstActivity();
    }
}
