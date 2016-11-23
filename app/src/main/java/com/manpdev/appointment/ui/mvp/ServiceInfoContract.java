package com.manpdev.appointment.ui.mvp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;

import com.manpdev.appointment.data.model.ServiceModel;
import com.manpdev.appointment.ui.mvp.base.MVPContract;

import java.io.InputStream;

/**
 * novoa on 9/11/16.
 */

public interface ServiceInfoContract extends MVPContract{
    interface Presenter extends MVPContract.Presenter{
        void updateServiceInfo(ServiceModel service);
        void uploadNewBanner(InputStream stream);
    }

    interface View extends MVPContract.View{
        void updateServiceInformation(ServiceModel service);
        void showConfirmationDialog(@StringRes int msgRes);
        void serviceUpdated();
        void bannerCallback(boolean success);
    }
}
