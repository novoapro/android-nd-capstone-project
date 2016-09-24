package com.manpdev.appointment.ui.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.manpdev.appointment.AppointmentApplication;
import com.manpdev.appointment.R;
import com.manpdev.appointment.databinding.ActivityUserRegistrationBinding;
import com.manpdev.appointment.ui.di.modules.PresentersModule;
import com.manpdev.appointment.ui.mvp.UserRegistrationContract;

import javax.inject.Inject;

public class UserRegistrationActivity extends AppCompatActivity implements UserRegistrationContract.View{

    private ActivityUserRegistrationBinding mViewBinding;

    @Inject
    public UserRegistrationContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_registration);

        ((AppointmentApplication) getApplication())
                .getApplicationComponent()
                .plus(new PresentersModule())
                .inject(this);


        mViewBinding.btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        mViewBinding.btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchLoginActivity();
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
    public void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        mViewBinding.progressbar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showError(int msgRes) {
        showError(getResources().getString(msgRes));
    }

    @Override
    public void launchLoginActivity() {
        Intent toLaunch = new Intent(this, LoginActivity.class);
        startActivity(toLaunch);
        finish();
    }

    @Override
    public void launchFirstActivity() {
        Intent intent = new Intent(UserRegistrationActivity.this, ClientAppointmentListActivity.class);
        startActivity(intent);
        finish();
    }

    private void registerUser() {
        if(TextUtils.isEmpty(mViewBinding.etFullName.getText())) {
            mViewBinding.etFullName.setError(getResources().getString(R.string.empty_fullname_error_msg));
            return;
        }
        if(TextUtils.isEmpty(mViewBinding.etPhoneNumber.getText())) {
            mViewBinding.etPhoneNumber.setError(getResources().getString(R.string.empty_phone_number_error_msg));
            return;
        }

        mPresenter.registerUser(mViewBinding.etFullName.getText().toString(),
                mViewBinding.etPhoneNumber.getText().toString());

        mViewBinding.progressbar.setVisibility(View.VISIBLE);
    }
}
