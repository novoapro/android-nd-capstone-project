package com.manpdev.appointment.ui.activities;

import android.os.Bundle;

import com.manpdev.appointment.R;
import com.manpdev.appointment.ui.activities.base.BaseNavigationActivity;

public class ClientProviderListActivity extends BaseNavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_client_provider_list;
    }

    @Override
    protected int getCheckedItemId() {
        return R.id.nav_client_list_providers;
    }
}
