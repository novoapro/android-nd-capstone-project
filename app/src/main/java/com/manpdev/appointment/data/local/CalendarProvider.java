package com.manpdev.appointment.data.local;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import com.manpdev.appointment.R;
import com.manpdev.appointment.data.model.AppointmentModel;

import java.util.Locale;
import java.util.TimeZone;

import javax.inject.Inject;

import rx.Single;
import rx.SingleSubscriber;

/**
 * novoa on 12/10/16.
 */

public class CalendarProvider {

    private Context mContext;

    private static final String[] EVENT_PROJECTION = new String[]{
            CalendarContract.Calendars._ID,
    };

    private static final int PROJECTION_ID_INDEX = 0;

    @Inject
    public CalendarProvider(Context context) {
        mContext = context;
    }

    public Single<String> insertCalendarEvent(@NonNull final String accountCalendar, @NonNull final AppointmentModel model) {
        return Single.create(new Single.OnSubscribe<String>() {
            @Override
            public void call(SingleSubscriber<? super String> singleSubscriber) {
                long calendarId = getCalendarId(accountCalendar);
                if (calendarId == -1)
                    singleSubscriber.onError(new Throwable(mContext.getString(R.string.no_calendar_available)));
                else {
                    String uri = insertEvent(calendarId, model);
                    if (TextUtils.isEmpty(uri))
                        singleSubscriber.onError(new Throwable(mContext.getString(R.string.invalid_calendar_entry)));
                    else
                        singleSubscriber.onSuccess(uri);
                }
            }
        });
    }

    private String insertEvent(long calendarId, @NonNull AppointmentModel model) {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED)
            return "";

        long startMillis = model.getDatetime();
        long endMillis = model.getDatetime() + 60 * 60 * 1000;
        ContentResolver cr = mContext.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, String.format(Locale.getDefault(), "%s %s", mContext.getString(R.string.appointment_pre_name), model.getProvider()));
        values.put(CalendarContract.Events.DESCRIPTION, model.getNotes());
        values.put(CalendarContract.Events.CALENDAR_ID, calendarId);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

        return uri == null ? "" : uri.toString();
    }

    public Intent getCalendarIntent(@NonNull AppointmentModel model) {
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

    private long getCalendarId(String accountCalendar) {
        Cursor cur;
        ContentResolver cr = mContext.getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = "(" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) ";
        String[] selectionArgs = new String[]{accountCalendar};

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED)
            return -1;

        cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);

        if (cur == null || !cur.moveToFirst())
            return -1;

        long calID = cur.getLong(PROJECTION_ID_INDEX);
        cur.close();

        return calID;
    }
}
