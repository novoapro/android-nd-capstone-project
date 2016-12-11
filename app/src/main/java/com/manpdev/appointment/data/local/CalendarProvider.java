package com.manpdev.appointment.data.local;

import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;

import com.manpdev.appointment.data.model.AppointmentModel;

import javax.inject.Inject;

/**
 * novoa on 12/10/16.
 */

public class CalendarProvider {

    private Context mContext;

    @Inject
    public CalendarProvider(Context context) {
        mContext = context;
    }

    public Intent getCalendarIntent(@NonNull AppointmentModel model){
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.Events.TITLE, model.getProvider());
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                model.getDate().getTime());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                model.getDate().getTime() + 60 * 60 * 1000);
        intent.putExtra(CalendarContract.Events.ALL_DAY, false);
        intent.putExtra(CalendarContract.Events.DESCRIPTION, model.getNotes());

        return intent;
    }
}
