package com.manpdev.appointment.ui.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.manpdev.appointment.service.WidgetRatingUpdateService;

/**
 * novoa on 12/11/16.
 */

public class ServiceRatingWidgetProvider extends AppWidgetProvider {

    private static final String TAG = "ServiceRatingWidgetProv";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "onUpdate");
        Intent updateService = new Intent(context, WidgetRatingUpdateService.class);
        context.startService(updateService);
    }

}
