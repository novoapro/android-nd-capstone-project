package com.manpdev.appointment.ui.activity.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.manpdev.appointment.AppointmentApplication;
import com.manpdev.appointment.R;
import com.manpdev.appointment.data.remote.AuthProvider;
import com.manpdev.appointment.databinding.NavHeaderNavigationBinding;
import com.manpdev.appointment.ui.activity.ClientAppointmentListActivity;
import com.manpdev.appointment.ui.activity.ClientProviderListActivity;
import com.manpdev.appointment.ui.activity.LoginActivity;
import com.manpdev.appointment.ui.activity.ProviderAppointmentListActivity;
import com.manpdev.appointment.ui.activity.ProviderServiceInfoActivity;
import com.manpdev.appointment.ui.activity.ProviderServiceReviewListActivity;
import com.manpdev.appointment.ui.helper.TransitionHelper;
import com.manpdev.appointment.ui.utils.CircularTransformation;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

public abstract class BaseNavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public static Intent getFirstNavigationActivityIntent(Activity host) {
        return new Intent(host, ClientAppointmentListActivity.class);
    }

    protected ActionBarDrawerToggle mToggle;
    protected DrawerLayout mDrawer;
    protected Toolbar mToolBar;
    protected ViewGroup mContainer;
    protected FloatingActionButton mActionFab;
    protected NavigationView mNavigationView;

    private int mSelectedItem;
    private NavHeaderNavigationBinding mNavigationHeaderView;

    @Inject
    public AuthProvider mAuthProvider;

    @Inject
    public Picasso mPicasso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        setActivityTransition();

        mContainer = (ViewGroup) findViewById(R.id.view_content);
        View.inflate(this, getContentLayoutId(), mContainer);

        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                launchChildActivity();
            }
        };

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.inflateMenu(getNavigationMenuRes());
        mNavigationHeaderView = DataBindingUtil.bind(mNavigationView.getHeaderView(0));

        mActionFab = (FloatingActionButton) findViewById(R.id.fab);
        ((AppointmentApplication) getApplication()).getApplicationComponent()
                .activity()
                .inject(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSelectedItem = getCheckedItemId();
        mNavigationView.setCheckedItem(mSelectedItem);
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();
        updateHeaderInformation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDrawer.removeDrawerListener(mToggle);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout)
            logout();

        return id == R.id.action_logout || super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        mSelectedItem = item.getItemId();
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void launchChildActivity() {
        Intent toLaunch;

        if (mSelectedItem == getCheckedItemId())
            return;

        switch (mSelectedItem) {
            case R.id.nav_provider_service_info:
                toLaunch = new Intent(BaseNavigationActivity.this, ProviderServiceInfoActivity.class);
                break;

            case R.id.nav_provider_appointment:
                toLaunch = new Intent(BaseNavigationActivity.this, ProviderAppointmentListActivity.class);
                break;

            case R.id.nav_provider_reviews:
                toLaunch = new Intent(BaseNavigationActivity.this, ProviderServiceReviewListActivity.class);
                break;

            case R.id.nav_client_list_providers:
                toLaunch = new Intent(BaseNavigationActivity.this, ClientProviderListActivity.class);
                break;

            default:
            case R.id.nav_client_appointment:
                toLaunch = new Intent(BaseNavigationActivity.this, ClientAppointmentListActivity.class);
                break;
        }

        TransitionHelper.transitionToActivity(this, toLaunch);
    }

    @MenuRes
    protected int getNavigationMenuRes() {
        return R.menu.default_navigation_menu;
    }

    @LayoutRes
    protected abstract int getContentLayoutId();

    @IdRes
    protected abstract int getCheckedItemId();

    @TargetApi(value = Build.VERSION_CODES.LOLLIPOP)
    private void setActivityTransition() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return;

        Fade fade = new Fade();
        fade.excludeTarget(findViewById(R.id.toolbar), true);
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);
    }

    private void updateHeaderInformation() {
        mPicasso.load(mAuthProvider.getUserAvatarUri())
                .transform(new CircularTransformation())
                .into(mNavigationHeaderView.ivUserImage);

        mNavigationHeaderView.tvUserEmail.setText(mAuthProvider.getUserEmail());
        mNavigationHeaderView.tvUserFullName.setText(mAuthProvider.getUserFullName());
    }

    private void logout() {
        mAuthProvider.logoutUser();
        Intent intent = new Intent(BaseNavigationActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
