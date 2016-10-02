package com.manpdev.appointment.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.manpdev.appointment.AppointmentApplication;
import com.manpdev.appointment.R;
import com.manpdev.appointment.databinding.ActivityLoginBinding;
import com.manpdev.appointment.ui.mvp.LoginContract;
import com.manpdev.appointment.ui.di.module.PresentersModule;

import javax.inject.Inject;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    private static final String TAG = "LoginActivity";
    private static final int GOOGLE_SIGN_IN = 345;

    private ActivityLoginBinding mViewDataBinding;

    @Inject
    public LoginContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mViewDataBinding.signInButton.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                mPresenter.signUserWithGoogle(LoginActivity.this);
                mViewDataBinding.progressBar.setVisibility(android.view.View.VISIBLE);
            }
        });

        ((AppointmentApplication)getApplication()).getApplicationComponent()
                .activity()
                .mvp(new PresentersModule())
                .inject(this);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");

        if (requestCode == GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess() && result.getSignInAccount() != null)
                mPresenter.userSignedWithGoogle(result.getSignInAccount().getIdToken());
            else
                mPresenter.userSignFailed();

            mViewDataBinding.progressBar.setVisibility(android.view.View.INVISIBLE);
        }
    }

    @Override
    public void launchFirstActivity() {
        Intent intent = new Intent(LoginActivity.this, ClientAppointmentListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void launchGoogleAuthentication(Intent intent) {
        startActivityForResult(intent, GOOGLE_SIGN_IN);
    }

    @Override
    public void enableSigninControllers(boolean b) {
        mViewDataBinding.signInButton.setEnabled(b);
        mViewDataBinding.progressBar.setVisibility(b ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showError(int msgRes) {
        Toast.makeText(this, msgRes, Toast.LENGTH_LONG).show();
    }
}

