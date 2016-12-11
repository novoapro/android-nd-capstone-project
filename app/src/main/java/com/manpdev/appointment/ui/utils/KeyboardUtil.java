package com.manpdev.appointment.ui.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * novoa on 12/10/16.
 */

public class KeyboardUtil {

    public static void hideKeyboard(View view){
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
