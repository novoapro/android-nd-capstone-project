package com.manpdev.appointment.data.local;

import android.Manifest;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.manpdev.appointment.R;
import com.manpdev.appointment.data.model.AppointmentModel;

import java.util.Locale;
import java.util.TimeZone;

/**
 * novoa on 12/10/16.
 */

public class AppointmentCalendarContract{

    public static final String ACCOUNT_EMAIL_KEY = "::account_email_key";
    public static final String APPOINTMENT_ENTITY = "appointment";

    public static Uri getBaseContentUri(String authority) {
        return Uri.parse(ContentResolver.SCHEME_CONTENT + "://" + authority + "/" + APPOINTMENT_ENTITY);
    }

    public static Uri getContentUriForAppointment(String authority, String account, String appointmentId) {
        return Uri.parse(ContentResolver.SCHEME_CONTENT + "://" + authority + "/" + APPOINTMENT_ENTITY + "/" + account +"/" + appointmentId);
    }

    public static ContentValues getContentValue(@NonNull Context context, @NonNull String accountEmail, @NonNull AppointmentModel model){
        long startMillis = model.getDatetime();
        long endMillis = model.getDatetime() + 60 * 60 * 1000;
        ContentValues values = new ContentValues();
        values.put(ACCOUNT_EMAIL_KEY, accountEmail);
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, String.format(Locale.getDefault(), "%s %s",
                context.getString(R.string.appointment_pre_name), model.getProvider()));
        values.put(CalendarContract.Events.DESCRIPTION, model.getId());

        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        return values;
    }

}
