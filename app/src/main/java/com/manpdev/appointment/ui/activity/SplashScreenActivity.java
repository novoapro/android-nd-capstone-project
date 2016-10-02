package com.manpdev.appointment.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.view.View;

import com.manpdev.appointment.AppointmentApplication;
import com.manpdev.appointment.R;
import com.manpdev.appointment.data.model.UserModel;
import com.manpdev.appointment.data.remote.AuthProvider;
import com.manpdev.appointment.data.remote.DataProvider;
import com.manpdev.appointment.ui.activity.base.BaseNavigationActivity;

import javax.inject.Inject;

import rx.functions.Action1;


public class SplashScreenActivity extends AppCompatActivity {

    public static final int DELAY_MILLIS = 2000;
    @Inject
    public DataProvider<UserModel> mUserProvider;

    @Inject
    public AuthProvider mAuthProvider;

    private View mContentView;

    private Runnable mAction = new Runnable() {
        @Override
        public void run() {
            openProperActivity();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mContentView = findViewById(R.id.splash_container);

        ((AppointmentApplication) getApplication()).getApplicationComponent()
                .activity()
                .inject(this);

        setActivityTransitions();
    }

    private void setActivityTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(new Explode());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mContentView.postDelayed(mAction, DELAY_MILLIS);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mContentView.removeCallbacks(mAction);
    }

    private void openProperActivity() {
        if (mAuthProvider.isUserLoggedIn()) {
            mUserProvider
                    .getSingleValueObservable(mAuthProvider.getUserId())
                    .subscribe(
                            new Action1<UserModel>() {
                                @Override
                                public void call(UserModel userModel) {
                                    launchFirstNavigationActivity();
                                }
                            },
                            new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    launchLoginActivity();
                                }
                            });
        } else
            launchLoginActivity();
    }

    private void launchFirstNavigationActivity() {
        startActivity(BaseNavigationActivity.getFirstNavigationActivityIntent(SplashScreenActivity.this));
        finish();

    }

    private void launchLoginActivity() {
        startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
        finish();
    }
}
