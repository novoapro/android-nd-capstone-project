package com.manpdev.appointment.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.manpdev.appointment.AppointmentApplication;
import com.manpdev.appointment.R;
import com.manpdev.appointment.data.local.AppointmentCalendarContract;
import com.manpdev.appointment.data.local.AppointmentCalendarProvider;
import com.manpdev.appointment.data.model.AppointmentModel;
import com.manpdev.appointment.databinding.ActivityProviderAppointmentListBinding;
import com.manpdev.appointment.ui.activity.base.BaseNavigationActivity;
import com.manpdev.appointment.ui.adapter.ProviderAppointmentAdapter;
import com.manpdev.appointment.ui.adapter.ProviderAppointmentItemListener;
import com.manpdev.appointment.ui.di.module.PresentersModule;
import com.manpdev.appointment.ui.mvp.ProviderAppointmentContract;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class ProviderAppointmentListActivity extends BaseNavigationActivity implements ProviderAppointmentContract.View,
        ProviderAppointmentItemListener, LoaderManager.LoaderCallbacks<Cursor> {

    public static final String EDITED_APPOINTMENT_EXTRA = "::edited_appointment_extra";
    private static final int EDIT_APPOINTMENR_REQUEST = 346;
    public static final int APPOINTMENT_CALENDAR_LOADER_ID = 123;
    public static final String APPOINTMENT_ID_EXTRA = "::appointment_id_extra";

    public static final int MY_PERMISSIONS_REQUEST_WRITE_CONTACT = 234;

    private ActivityProviderAppointmentListBinding mViewBinding;
    private ProviderAppointmentAdapter mAdapter;

    @Inject
    ProviderAppointmentContract.Presenter mPresenter;
    private AppointmentModel mSelectedAppointment;
    private LoaderManager mLoadManager;

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
        mLoadManager = getSupportLoaderManager();
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
        mLoadManager.destroyLoader(APPOINTMENT_CALENDAR_LOADER_ID);
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
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_CONTACT: {

                if (grantResults.length == 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, R.string.add_calendar_permission, Toast.LENGTH_LONG).show();
                }
            }
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
    public void calendarEventInserted() {
        Toast.makeText(this, R.string.calendar_event_inserted, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCalendar(AppointmentModel model) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            requestCalendarPermissions();
        } else {
            Bundle arg = new Bundle();
            arg.putString(APPOINTMENT_ID_EXTRA, model.getId());

            mLoadManager.restartLoader(APPOINTMENT_CALENDAR_LOADER_ID, arg, this);

            mSelectedAppointment = model;
        }
    }

    @Override
    public void onEdit(AppointmentModel model) {
        Intent intent = new Intent(this, UpdateAppointmentActivity.class);
        intent.putExtra(UpdateAppointmentActivity.APPOINTMENT_MODEL_EXTRA, model);
        startActivityForResult(intent, EDIT_APPOINTMENR_REQUEST);
    }

    private void requestCalendarPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_CALENDAR)) {

            Toast.makeText(this, R.string.add_calendar_permission, Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_CALENDAR},
                    MY_PERMISSIONS_REQUEST_WRITE_CONTACT);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_CALENDAR},
                    MY_PERMISSIONS_REQUEST_WRITE_CONTACT);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                AppointmentCalendarContract.getContentUriForAppointment(
                        AppointmentCalendarProvider.sAUTHORITY, mAuthProvider.getUserEmail(), args.getString(APPOINTMENT_ID_EXTRA)),
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.getCount() > 0 && data.moveToFirst())
            openCalendar(data.getLong(0));
        else if (mSelectedAppointment != null)
            mPresenter.insertCalendarEvent(mSelectedAppointment);
    }

    private void openCalendar(Long eventId) {
        Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId);
        Intent intent = new Intent(Intent.ACTION_VIEW).setData(uri);
        startActivity(intent);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
