package com.manpdev.appointment.ui.mvp;

import com.manpdev.appointment.data.model.AppointmentModel;
import com.manpdev.appointment.data.model.ReviewModel;
import com.manpdev.appointment.ui.mvp.base.MVPContract;

import java.util.List;

import rx.Observable;

/**
 * novoa on 9/11/16.
 */

public interface ClientAppoinmentContract extends MVPContract{
    interface Presenter extends MVPContract.Presenter{
        void loadList();
        void createNewAppointment(AppointmentModel model);
    }

    interface View extends MVPContract.View{
        void showList(Observable<List<AppointmentModel>> appointments);
        void hideProgressDialog();
    }
}
