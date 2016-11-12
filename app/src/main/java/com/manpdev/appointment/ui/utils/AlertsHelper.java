package com.manpdev.appointment.ui.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.widget.Toast;

import javax.inject.Inject;

/**
 * novoa on 11/12/16.
 */

public class AlertsHelper {

    private Context mContext;
    private Dialog mDialog;

    @Inject
    public AlertsHelper() {
    }

    public void setContext(Context context){
        mContext = context;
    }

    public void showError(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
    }

    public void showError(int msgRes) {
        Toast.makeText(mContext, msgRes, Toast.LENGTH_LONG).show();
    }

    public void showProgressDialog(String msg) {
        hideDialog();

        ProgressDialog.Builder builder = new ProgressDialog.Builder(mContext);
        builder.setMessage(msg);
        builder.setCancelable(false);
        mDialog = builder.show();
    }

    public void showProgressDialog(int msgRes) {
        hideDialog();

        mDialog = ProgressDialog.show(mContext, "", mContext.getString(msgRes));
    }

    public void showConfirmationDialog(@StringRes int msgRes,
                                       @StringRes int positiveText,
                                       @StringRes int negativeText,
                                       DialogInterface.OnClickListener positiveListener,
                                       DialogInterface.OnClickListener negativeListener) {
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();


        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setMessage(msgRes);
        if (positiveListener != null)
            builder.setPositiveButton(positiveText, positiveListener);

        if(negativeListener != null)
            builder.setNegativeButton(negativeText, negativeListener);

        mDialog = builder.show();
        mDialog.setCanceledOnTouchOutside(false);
    }


    public void hideDialog() {
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();
    }
}
