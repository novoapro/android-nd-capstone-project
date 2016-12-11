package com.manpdev.appointment.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.manpdev.appointment.AppointmentApplication;
import com.manpdev.appointment.R;
import com.manpdev.appointment.data.model.AppointmentModel;
import com.manpdev.appointment.data.model.ReviewModel;
import com.manpdev.appointment.databinding.ActivityProviderAppointmentListBinding;
import com.manpdev.appointment.ui.activity.base.BaseNavigationActivity;
import com.manpdev.appointment.ui.adapter.ProviderAppointmentAdapter;
import com.manpdev.appointment.ui.adapter.ProviderAppointmentItemListener;
import com.manpdev.appointment.ui.di.module.PresentersModule;
import com.manpdev.appointment.ui.mvp.ProviderAppoinmentContract;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

import static com.manpdev.appointment.ui.activity.ClientAppointmentListActivity.NEW_REVIEW_EXTRA;

public class ProviderAppointmentListActivity extends BaseNavigationActivity implements ProviderAppoinmentContract.View, ProviderAppointmentItemListener {

    public static final String EDITED_APPOINTMENT_EXTRA = "::edited_appointment_extra";
    private static final int INSERT_CALENDAR_REQUEST = 345;
    private static final int EDIT_APPOINTMENR_REQUEST = 346;

    private ActivityProviderAppointmentListBinding mViewBinding;
    private ProviderAppointmentAdapter mAdapter;

    @Inject
    ProviderAppoinmentContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((AppointmentApplication)getApplication()).getApplicationComponent()
                .activity()
                .mvp(new PresentersModule())
                .inject(this);

        mAdapter = new ProviderAppointmentAdapter(null);
        mAdapter.addItemListener(this);

        mViewBinding.rvList.setLayoutManager(new LinearLayoutManager(this));
        mViewBinding.rvList.setHasFixedSize(true);
        mViewBinding.rvList.setAdapter(mAdapter);

        mAlertHelper.setContext(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.attachView(this);
        mPresenter.loadList();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.detachView();
        mAdapter.stopUpdateFromObservable();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK)
            return;

        switch (requestCode) {
            case EDIT_APPOINTMENR_REQUEST:
                AppointmentModel appointment = data.getParcelableExtra(EDITED_APPOINTMENT_EXTRA);

                if (appointment != null)
                    mPresenter.editAppointment(appointment);

                mAlertHelper.showProgressDialog(R.string.appointment_creation_msg);
                break;
        }
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_provider_appointment_list;
    }

    @Override
    protected int getCheckedItemId() {
        return R.id.nav_provider_appointment;
    }

    @Override
    protected void inflateChildLayout() {
        mViewBinding = DataBindingUtil.inflate(
                getLayoutInflater(),
                getContentLayoutId(),
                mBaseViewBinding.toolbarContainer.navContent,
                true);
    }

    @Override
    public void showList(Observable<List<AppointmentModel>> appointments) {
        mAdapter.updateDataFromObservable(appointments);
    }

    @Override
    public void hideProgressDialog() {
        mAlertHelper.hideDialog();
    }

    @Override
    public void onCalendar(AppointmentModel model) {
        Intent calendarIntent = mPresenter.getCalendarIntent(model);

        if (calendarIntent != null)
            startActivityForResult(calendarIntent, INSERT_CALENDAR_REQUEST);
    }

    @Override
    public void onEdit(AppointmentModel model) {
        Intent intent = new Intent(this, UpdateAppointmentActivity.class);
        intent.putExtra(UpdateAppointmentActivity.APPOINTMENT_MODEL_EXTRA, model);
        startActivityForResult(intent, EDIT_APPOINTMENR_REQUEST);
    }
}
