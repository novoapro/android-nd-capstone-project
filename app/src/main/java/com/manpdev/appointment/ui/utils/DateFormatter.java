package com.manpdev.appointment.ui.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * novoa on 10/26/16.
 */

public class DateFormatter {

    private static final String SHORT_DATE_FORMAT = "MM/dd/yyyy";
    private static final String DATE_TIME_FORMAT = "EEE, MMM dd, yyyy";

    private static DateFormat sShortDateFormatter = new SimpleDateFormat(SHORT_DATE_FORMAT, Locale.getDefault());
    private static DateFormat sDateTimeFormatter = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault());


    public static String getShortDate(Date date){
        if(date == null)
            return "";
        return sShortDateFormatter.format(date);
    }

    public static String getDateTimeFormat(Date date){
        if(date == null)
            return "";
        return sDateTimeFormatter.format(date);
    }

}
