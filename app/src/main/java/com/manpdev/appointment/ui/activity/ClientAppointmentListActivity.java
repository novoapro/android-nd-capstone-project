package com.manpdev.appointment.ui.activity;

import android.os.Bundle;

import com.manpdev.appointment.R;
import com.manpdev.appointment.ui.activity.base.BaseNavigationActivity;

public class ClientAppointmentListActivity extends BaseNavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_client_appointment_list;
    }

    @Override
    protected int getCheckedItemId() {
        return R.id.nav_client_appointment;
    }
}
