package com.manpdev.appointment.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.manpdev.appointment.AppointmentApplication;
import com.manpdev.appointment.R;
import com.manpdev.appointment.data.model.AppointmentModel;
import com.manpdev.appointment.databinding.ActivityClientAppointmentListBinding;
import com.manpdev.appointment.ui.activity.base.BaseNavigationActivity;
import com.manpdev.appointment.ui.adapter.ClientAppointmentAdapter;
import com.manpdev.appointment.ui.adapter.ClientAppointmentItemListener;
import com.manpdev.appointment.ui.di.module.PresentersModule;
import com.manpdev.appointment.ui.mvp.ClientAppoinmentContract;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class ClientAppointmentListActivity extends BaseNavigationActivity implements ClientAppoinmentContract.View, ClientAppointmentItemListener {

    private static final int CREATE_NEW_APPOINTMENT_CODE = 345;
    public static final String NEW_APPOINTMENT_EXTRA = "::new_appointment_extra";
    private ActivityClientAppointmentListBinding mViewBinding;
    private ClientAppointmentAdapter mAdapter;

    @Inject
    ClientAppoinmentContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((AppointmentApplication)getApplication()).getApplicationComponent()
                .activity()
                .mvp(new PresentersModule())
                .inject(this);

        mAdapter = new ClientAppointmentAdapter(null);
        mAdapter.addItemListener(this);

        mViewBinding.rvList.setLayoutManager(new LinearLayoutManager(this));
        mViewBinding.rvList.setHasFixedSize(true);
        mViewBinding.rvList.setAdapter(mAdapter);

        addFABButton(R.drawable.ic_add, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAppointmentCreatorView();
            }
        });
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

        if(requestCode != CREATE_NEW_APPOINTMENT_CODE || resultCode != Activity.RESULT_OK)
            return;

        AppointmentModel newAppointment = data.getParcelableExtra(NEW_APPOINTMENT_EXTRA);

        if(newAppointment != null)
            mPresenter.createNewAppointment(newAppointment);

        mAlertHelper.showProgressDialog(R.string.appointment_creation_msg);
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
    protected int getContentLayoutId() {
        return R.layout.activity_client_appointment_list;
    }

    @Override
    protected int getCheckedItemId() {
        return R.id.nav_client_appointment;
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
    public void onCalendarClicked(AppointmentModel model) {
        Toast.makeText(this, model.getProvider(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onReviewClicked(AppointmentModel model) {
        Toast.makeText(this, model.getNotes(), Toast.LENGTH_LONG).show();
    }

    private void openAppointmentCreatorView() {
        Intent intent = new Intent(ClientAppointmentListActivity.this, CreateAppointmentActivity.class);
        startActivityForResult(intent, CREATE_NEW_APPOINTMENT_CODE);
    }
}
