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
import android.view.View;
import android.widget.Toast;

import com.manpdev.appointment.AppointmentApplication;
import com.manpdev.appointment.R;
import com.manpdev.appointment.data.local.AppointmentCalendarContract;
import com.manpdev.appointment.data.local.AppointmentCalendarProvider;
import com.manpdev.appointment.data.model.AppointmentModel;
import com.manpdev.appointment.data.model.ReviewModel;
import com.manpdev.appointment.databinding.ActivityClientAppointmentListBinding;
import com.manpdev.appointment.ui.activity.base.BaseNavigationActivity;
import com.manpdev.appointment.ui.adapter.ClientAppointmentAdapter;
import com.manpdev.appointment.ui.adapter.ClientAppointmentItemListener;
import com.manpdev.appointment.ui.di.module.PresentersModule;
import com.manpdev.appointment.ui.helper.TransitionHelper;
import com.manpdev.appointment.ui.mvp.ClientAppointmentContract;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class ClientAppointmentListActivity extends BaseNavigationActivity implements ClientAppointmentContract.View,
        ClientAppointmentItemListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CREATE_NEW_APPOINTMENT_CODE = 345;
    private static final int CREATE_NEW_REVIEW_CODE = 347;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_CONTACT = 234;

    public static final String NEW_APPOINTMENT_EXTRA = "::new_appointment_extra";
    public static final String NEW_REVIEW_EXTRA = "::new_review_extra";
    public static final int APPOINTMENT_CALENDAR_LOADER_ID = 123;
    public static final String APPOINTMENT_ID_EXTRA = "::appointment_id_extra";

    private ActivityClientAppointmentListBinding mViewBinding;
    private ClientAppointmentAdapter mAdapter;

    @Inject
    ClientAppointmentContract.Presenter mPresenter;
    private LoaderManager mLoadManager;
    private AppointmentModel mSelectedAppointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((AppointmentApplication) getApplication()).getApplicationComponent()
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
            case CREATE_NEW_APPOINTMENT_CODE:
                AppointmentModel newAppointment = data.getParcelableExtra(NEW_APPOINTMENT_EXTRA);

                if (newAppointment != null)
                    mPresenter.createNewAppointment(newAppointment);

                mAlertHelper.showProgressDialog(R.string.appointment_creation_msg);
                break;

            case CREATE_NEW_REVIEW_CODE:
                ReviewModel newReview = data.getParcelableExtra(NEW_REVIEW_EXTRA);

                if (newReview != null)
                    mPresenter.createNewServiceReview(newReview);
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
    public void onReviewClicked(AppointmentModel model) {
        Intent intent = new Intent(this, CreateReviewActivity.class);
        intent.putExtra(CreateReviewActivity.APPOINTMENT_MODEL_EXTRA, model);
        TransitionHelper.transitionToActivityForResult(this, intent, CREATE_NEW_REVIEW_CODE);
    }

    private void openAppointmentCreatorView() {
        Intent intent = new Intent(ClientAppointmentListActivity.this, CreateAppointmentActivity.class);
        TransitionHelper.transitionToActivityForResult(this, intent, CREATE_NEW_APPOINTMENT_CODE);
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
    public void calendarEventInserted() {
        Toast.makeText(this, R.string.calendar_event_inserted, Toast.LENGTH_LONG).show();
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
