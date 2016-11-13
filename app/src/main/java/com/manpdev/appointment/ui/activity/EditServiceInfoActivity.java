package com.manpdev.appointment.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.manpdev.appointment.AppointmentApplication;
import com.manpdev.appointment.R;
import com.manpdev.appointment.data.model.ServiceModel;
import com.manpdev.appointment.databinding.ActivityEditServiceInfoBinding;
import com.manpdev.appointment.ui.di.module.PresentersModule;
import com.manpdev.appointment.ui.mvp.ServiceInfoContract;
import com.manpdev.appointment.ui.helper.AlertsHelper;

import javax.inject.Inject;

public class EditServiceInfoActivity extends AppCompatActivity implements ServiceInfoContract.View {

    private static final String TAG = "EditServiceInfoActivity";
    private static final int SELECT_FILE_REQUEST_CODE = 333;

    @Inject
    ServiceInfoContract.Presenter mPresenter;

    @Inject
    AlertsHelper mAlertHelper;

    private ActivityEditServiceInfoBinding mViewBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_service_info);

        ((AppointmentApplication)getApplication()).getApplicationComponent()
                .activity()
                .mvp(new PresentersModule())
                .inject(this);

        mAlertHelper.setContext(this);

        mViewBinding.bnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mViewBinding.bnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmationDialog(R.string.save_service_changes_warning);
            }
        });

        mViewBinding.ibBannerUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.attachView(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.detachView();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode != SELECT_FILE_REQUEST_CODE || resultCode != Activity.RESULT_OK)
            return;

        Uri uri;
        if (data != null) {
            uri = data.getData();
            Log.i(TAG, "Uri: " + uri.toString());
            dumpImageMetaData(uri);
        }
    }

    @Override
    public void updateServiceInformation(ServiceModel service) {
        Log.d(TAG, "updateServiceInformation: ");

        mAlertHelper.hideDialog();

        mViewBinding.swEnable.setChecked(service.isActive());
        mViewBinding.etServicename.setText(service.getName());
        mViewBinding.etServicetype.setText(service.getType());
        mViewBinding.etServiceaddress.setText(service.getAddress());
        mViewBinding.etServicephone.setText(service.getPhone());
        mViewBinding.etServicedescription.setText(service.getDescription());
    }

    @Override
    public void showConfirmationDialog(@StringRes int msgRes) {
        mAlertHelper.showConfirmationDialog(msgRes,
                android.R.string.ok,
                android.R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mAlertHelper.showProgressDialog(R.string.saving_action);

                        ServiceModel model = new ServiceModel();
                        model.setActive(mViewBinding.swEnable.isChecked());
                        model.setName(mViewBinding.etServicename.getText().toString());
                        model.setAddress(mViewBinding.etServiceaddress.getText().toString());
                        model.setDescription(mViewBinding.etServicedescription.getText().toString());
                        model.setPhone(mViewBinding.etServicephone.getText().toString());
                        model.setType(mViewBinding.etServicetype.getText().toString());
                        mPresenter.updateServiceInfo(model);
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        serviceUpdated();
                    }
                }
        );
    }

    @Override
    public void serviceUpdated() {
        mAlertHelper.hideDialog();
        finish();
    }

    @Override
    public void showError(String msg) {
        mAlertHelper.showError(msg);
    }

    @Override
    public void showError(int msgRes) {
        mAlertHelper.showError(msgRes);
    }

    private void openFileChooser(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_FILE_REQUEST_CODE);
    }

    public void dumpImageMetaData(Uri uri) {

        // The query, since it only applies to a single document, will only return
        // one row. There's no need to filter, sort, or select fields, since we want
        // all fields for one document.
        Cursor cursor = getContentResolver()
                .query(uri, null, null, null, null, null);

        try {
            // moveToFirst() returns false if the cursor has 0 rows.  Very handy for
            // "if there's anything to look at, look at it" conditionals.
            if (cursor != null && cursor.moveToFirst()) {

                // Note it's called "Display Name".  This is
                // provider-specific, and might not necessarily be the file name.
                String displayName = cursor.getString(
                        cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                Log.i(TAG, "Display Name: " + displayName);

                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                // If the size is unknown, the value stored is null.  But since an
                // int can't be null in Java, the behavior is implementation-specific,
                // which is just a fancy term for "unpredictable".  So as
                // a rule, check if it's null before assigning to an int.  This will
                // happen often:  The storage API allows for remote files, whose
                // size might not be locally known.
                String size = null;
                if (!cursor.isNull(sizeIndex)) {
                    // Technically the column stores an int, but cursor.getString()
                    // will do the conversion automatically.
                    size = cursor.getString(sizeIndex);
                } else {
                    size = "Unknown";
                }
                Log.i(TAG, "Size: " + size);
            }
        } finally {
            cursor.close();
        }
    }

}
