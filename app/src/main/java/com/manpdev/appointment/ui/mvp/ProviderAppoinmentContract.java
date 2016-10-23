package com.manpdev.appointment.ui.mvp;

import com.manpdev.appointment.data.model.ServiceModel;
import com.manpdev.appointment.ui.mvp.base.MVPContract;

/**
 * novoa on 9/11/16.
 */

public interface ProviderAppoinmentContract extends MVPContract{
    interface Presenter extends MVPContract.Presenter{

    }

    interface View extends MVPContract.View{
        void updateServiceInformation(ServiceModel service);
        void launchAddServiceView();
    }
}
