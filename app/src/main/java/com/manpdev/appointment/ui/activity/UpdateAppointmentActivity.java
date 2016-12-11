package com.manpdev.appointment.ui.activity;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TimePicker;

import com.manpdev.appointment.R;
import com.manpdev.appointment.data.model.AppointmentModel;
import com.manpdev.appointment.databinding.ActivityUpdateAppointmentBinding;
import com.manpdev.appointment.ui.utils.DateFormatter;
import com.manpdev.appointment.ui.utils.KeyboardUtil;

import java.util.Calendar;

public class UpdateAppointmentActivity extends AppCompatActivity {

    public static final String APPOINTMENT_MODEL_EXTRA = "::appointment_model_extra";
    private ActivityUpdateAppointmentBinding mViewBinding;
    private AppointmentModel mAppointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_update_appointment);
        mAppointment = getIntent().getParcelableExtra(APPOINTMENT_MODEL_EXTRA);

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

        OnTimeFieldClick onTimeFieldClick = new OnTimeFieldClick();
        mViewBinding.rbAccept.setOnClickListener(onTimeFieldClick);
        mViewBinding.rbAccept.setOnFocusChangeListener(onTimeFieldClick);

        mViewBinding.tvTime.setText(DateFormatter.getFullDateTimeFormat(mAppointment.getDate()));

        checkCurrentState();
    }

    private void checkCurrentState() {
        switch (mAppointment.getState()) {
            case AppointmentModel.REQUESTED:
                mViewBinding.rbRequested.setChecked(true);
                break;
            case AppointmentModel.ACCEPTED:
                mViewBinding.rbAccept.setChecked(true);
                break;
            case AppointmentModel.DENIED:
                mViewBinding.rbReject.setChecked(true);
                break;
            case AppointmentModel.COMPLETED:
                mViewBinding.rbCompleted.setChecked(true);
                break;
        }
    }

    @AppointmentModel.StateInt
    private int getCurrentState() {
        switch (mViewBinding.rgAppointmentState.getCheckedRadioButtonId()) {
            case R.id.rb_accept:
                return AppointmentModel.ACCEPTED;
            case R.id.rb_requested:
                return AppointmentModel.REQUESTED;
            case R.id.rb_reject:
                return AppointmentModel.DENIED;
            case R.id.rb_completed:
                return AppointmentModel.COMPLETED;
        }

        return AppointmentModel.REQUESTED;
    }

    private void validateAndSaveAppointment() {
        mAppointment.setDate(DateFormatter.fromFullDateTimeFormat(mViewBinding.tvTime.getText().toString()));
        mAppointment.setState(getCurrentState());

        Intent data = new Intent();
        data.putExtra(ProviderAppointmentListActivity.EDITED_APPOINTMENT_EXTRA, mAppointment);
        setResult(Activity.RESULT_OK, data);
        KeyboardUtil.hideKeyboard(mViewBinding.bnSave);
        finish();
    }

    private class OnTimeFieldClick implements View.OnClickListener, View.OnFocusChangeListener, TimePickerDialog.OnTimeSetListener {
        private Calendar calendar;

        OnTimeFieldClick() {
            calendar = Calendar.getInstance();
        }

        @Override
        public void onClick(View view) {
            openPicker();
        }

        private void openPicker() {
            TimePickerDialog dialog = new TimePickerDialog(UpdateAppointmentActivity.this, this, 0, 0, false);
            dialog.show();
        }

        @Override
        public void onFocusChange(View view, boolean b) {
            if (b)
                openPicker();
        }

        @Override
        public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
            calendar.set(Calendar.HOUR_OF_DAY, hours);
            calendar.set(Calendar.MINUTE, minutes);
            String formattedTime = DateFormatter.getFullDateTimeFormat(calendar.getTime());
            mViewBinding.tvTime.setText(formattedTime);
        }
    }
}
