package com.manpdev.appointment.ui.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.transition.Explode;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.manpdev.appointment.R;
import com.manpdev.appointment.data.model.AppointmentModel;
import com.manpdev.appointment.databinding.ActivityCreateAppointmentBinding;
import com.manpdev.appointment.ui.utils.DateFormatter;
import com.manpdev.appointment.ui.utils.KeyboardUtil;

import java.util.Calendar;
import java.util.Date;

public class CreateAppointmentActivity extends AppCompatActivity {

    private ActivityCreateAppointmentBinding mViewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_appointment);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(new Explode());
            getWindow().setEnterTransition(new Explode());
        }

        mViewBinding.bnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateAndSaveAppointment();
            }
        });

        mViewBinding.bnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED);
                KeyboardUtil.hideKeyboard(mViewBinding.bnSave);
                finish();
            }
        });

        OnDateFieldClick onDateFieldClick = new OnDateFieldClick();
        mViewBinding.etAppointmentDate.setOnClickListener(onDateFieldClick);
        mViewBinding.etAppointmentDate.setOnFocusChangeListener(onDateFieldClick);
    }

    private void validateAndSaveAppointment() {
        if(isInvalid())
            return;

        AppointmentModel model = new AppointmentModel();
        model.setDate(DateFormatter.fromDateTimeFormat(mViewBinding.etAppointmentDate.getText().toString()));
        model.setNotes(mViewBinding.etAppointmentNotes.getText().toString());
        model.setProvider(mViewBinding.etProviderEmail.getText().toString());

        Intent data = new Intent();
        data.putExtra(ClientAppointmentListActivity.NEW_APPOINTMENT_EXTRA, model);
        setResult(Activity.RESULT_OK, data);
        KeyboardUtil.hideKeyboard(mViewBinding.bnSave);
        finish();
    }

    private boolean isInvalid() {
        if(TextUtils.isEmpty(mViewBinding.etProviderEmail.getText()) ||
                TextUtils.isEmpty(mViewBinding.etAppointmentDate.getText())) {

            Toast.makeText(this, R.string.invalid_appointment_information, Toast.LENGTH_LONG).show();
            return true;
        }

        return false;
    }

    private class OnDateFieldClick implements View.OnClickListener, View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener{

        private Calendar calendar;

        OnDateFieldClick() {
            calendar = Calendar.getInstance();
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String formattedTime = DateFormatter.getShortDate(calendar.getTime());
            mViewBinding.etAppointmentDate.setText(formattedTime);
        }

        @Override
        public void onClick(View view) {
            openPicker();
        }

        private void openPicker() {
            if (!TextUtils.isEmpty(mViewBinding.etAppointmentDate.getText())) {
                Date date = DateFormatter.fromDateTimeFormat(mViewBinding.etAppointmentDate.getText().toString());
                if (date != null) {
                    calendar.setTime(date);
                }
            }
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(CreateAppointmentActivity.this, this, year, month, day);
            dialog.show();
        }

        @Override
        public void onFocusChange(View view, boolean b) {
            if(b)
                openPicker();

        }
    }
}
