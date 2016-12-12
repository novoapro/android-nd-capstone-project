package com.manpdev.appointment.data.local;

import android.Manifest;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

/**
 * novoa on 12/10/16.
 */

public class AppointmentCalendarProvider extends ContentProvider {

    private static final String[] EVENT_PROJECTION = new String[]{
            CalendarContract.Calendars._ID,
    };
    private static final int PROJECTION_ID_INDEX = 0;

    public static final String sAUTHORITY = "com.manpdev.appointment.provider";
    private static UriMatcher sUriMatcher;

    private static final int URI_MATCH_APPOINTMENT = 0;
    private static final int URI_MATCH_APPOINTMENT_ID = 1;

    private Context mContext;

    @Override
    public boolean onCreate() {
        mContext = getContext();
        initUriMatcher();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] strings, String s, String[] strings1, String s1) {
        switch (sUriMatcher.match(uri)) {
            case URI_MATCH_APPOINTMENT_ID:
                ContentResolver cr = mContext.getContentResolver();
                Uri cUri = CalendarContract.Events.CONTENT_URI;
                String selection = "(" + CalendarContract.Events.DESCRIPTION + " = ?) ";
                String[] selectionArgs = new String[]{uri.getLastPathSegment()};

                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED)
                    return null;

                return cr.query(cUri, new String[]{CalendarContract.Events._ID}, selection, selectionArgs, null);
        }

        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return ContentResolver.CURSOR_DIR_BASE_TYPE;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED)
            return null;

        switch (sUriMatcher.match(uri)) {
            case URI_MATCH_APPOINTMENT:
                ContentResolver cr = mContext.getContentResolver();

                String accountEmail = contentValues.getAsString(AppointmentCalendarContract.ACCOUNT_EMAIL_KEY);
                contentValues.remove(AppointmentCalendarContract.ACCOUNT_EMAIL_KEY);
                long calendarId = getCalendarId(accountEmail);
                if (calendarId < 0)
                    return null;

                contentValues.put(CalendarContract.Events.CALENDAR_ID, calendarId);
                return cr.insert(CalendarContract.Events.CONTENT_URI, contentValues);
        }

        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    private void initUriMatcher() {
        if (sUriMatcher != null)
            return;

        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        //../appointment
        sUriMatcher.addURI(sAUTHORITY, AppointmentCalendarContract.APPOINTMENT_ENTITY, URI_MATCH_APPOINTMENT);
        //../appointment/{appointment_id}
        sUriMatcher.addURI(sAUTHORITY, AppointmentCalendarContract.APPOINTMENT_ENTITY + "/*/*", URI_MATCH_APPOINTMENT_ID);
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
