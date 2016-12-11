package com.manpdev.appointment.ui.mvp;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.manpdev.appointment.data.model.AppointmentModel;
import com.manpdev.appointment.ui.mvp.base.MVPContract;

import java.util.List;

import rx.Observable;

/**
 * novoa on 9/11/16.
 */

public interface ProviderAppointmentContract extends MVPContract{
    interface Presenter extends MVPContract.Presenter{
        void loadList();
        Intent getCalendarIntent(AppointmentModel model);
        void insertCalendarEvent(@NonNull AppointmentModel model);
        void editAppointment(AppointmentModel appointment);
    }

    interface View extends MVPContract.View{
        void showList(Observable<List<AppointmentModel>> appointments);
        void hideProgressDialog();
        void calendarEventInserted();
    }
}
