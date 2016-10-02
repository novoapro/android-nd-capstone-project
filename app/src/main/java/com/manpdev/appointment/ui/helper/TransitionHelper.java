package com.manpdev.appointment.ui.helper;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;

/**
 * novoa on 9/17/16.
 */

public class TransitionHelper {

    public static void transitionToActivity(Activity activity, Intent intent) {
        final Pair<View, String>[] pairs = null;
        TransitionHelper.startActivity(activity, intent, pairs);
    }

    public static void transitionToActivity(Activity activity, Intent intent, @Nullable Pair<View, String>[] members) {
        TransitionHelper.startActivity(activity, intent, members);
    }

    private static void startActivity(Activity activity, Intent intent, Pair<View, String>[] pairs) {
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pairs);
        activity.startActivity(intent, transitionActivityOptions.toBundle());
    }


}
