package com.manpdev.appointment.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.manpdev.appointment.AppointmentApplication;
import com.manpdev.appointment.R;
import com.manpdev.appointment.data.model.AppointmentModel;
import com.manpdev.appointment.databinding.ActivityProviderAppointmentListBinding;
import com.manpdev.appointment.ui.activity.base.BaseNavigationActivity;
import com.manpdev.appointment.ui.adapter.ProviderAppointmentAdapter;
import com.manpdev.appointment.ui.adapter.ProviderAppointmentItemListener;
import com.manpdev.appointment.ui.di.module.PresentersModule;
import com.manpdev.appointment.ui.mvp.ProviderAppoinmentContract;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class ProviderAppointmentListActivity extends BaseNavigationActivity implements ProviderAppoinmentContract.View, ProviderAppointmentItemListener {

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
    public void onCalendar(AppointmentModel model) {
        Toast.makeText(this, model.getClient(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onEdit(AppointmentModel model) {
        Toast.makeText(this, model.getNotes(), Toast.LENGTH_LONG).show();
    }
}
