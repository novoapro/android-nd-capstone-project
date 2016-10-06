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
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.manpdev.appointment.AppointmentApplication;
import com.manpdev.appointment.R;
import com.manpdev.appointment.data.remote.AuthProvider;
import com.manpdev.appointment.databinding.ActivityNavigationBinding;
import com.manpdev.appointment.databinding.NavHeaderNavigationBinding;
import com.manpdev.appointment.ui.activity.ClientAppointmentListActivity;
import com.manpdev.appointment.ui.activity.ClientProviderListActivity;
import com.manpdev.appointment.ui.activity.LoginActivity;
import com.manpdev.appointment.ui.activity.ProviderAppointmentListActivity;
import com.manpdev.appointment.ui.activity.ProviderServiceInfoActivity;
import com.manpdev.appointment.ui.activity.ProviderServiceReviewListActivity;
import com.manpdev.appointment.ui.mvp.base.MVPContract;
import com.manpdev.appointment.ui.utils.CircularTransformation;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

public abstract class BaseNavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MVPContract.View {

    public static Intent getFirstNavigationActivityIntent(Activity host) {
        return new Intent(host, ClientAppointmentListActivity.class);
    }

    protected ActionBarDrawerToggle mToggle;

    private int mSelectedItem;
    private NavHeaderNavigationBinding mNavigationHeaderView;
    protected ActivityNavigationBinding mBaseViewBinding;

    @Inject
    public AuthProvider mAuthProvider;

    @Inject
    public Picasso mPicasso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBaseViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_navigation);
        setActivityTransition();

        inflateChildLayout();

        setSupportActionBar(mBaseViewBinding.toolbarContainer.toolbar);

        mToggle = new ActionBarDrawerToggle(
                this, mBaseViewBinding.drawerLayout,
                mBaseViewBinding.toolbarContainer.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                launchChildActivity();
            }
        };

        mBaseViewBinding.navView.setNavigationItemSelectedListener(this);
        mBaseViewBinding.navView.inflateMenu(getNavigationMenuRes());
        mNavigationHeaderView = DataBindingUtil.bind(mBaseViewBinding.navView.getHeaderView(0));

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
        mBaseViewBinding.navView.setCheckedItem(mSelectedItem);
        mBaseViewBinding.drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        updateHeaderInformation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBaseViewBinding.drawerLayout.removeDrawerListener(mToggle);
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
        mBaseViewBinding.drawerLayout.closeDrawer(GravityCompat.START);
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

        toLaunch.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(toLaunch);
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showError(int msgRes) {
        Toast.makeText(this, msgRes, Toast.LENGTH_LONG).show();
    }

    @MenuRes
    protected int getNavigationMenuRes() {
        return R.menu.default_navigation_menu;
    }

    @LayoutRes
    protected abstract int getContentLayoutId();

    @IdRes
    protected abstract int getCheckedItemId();

    protected void inflateChildLayout(){
        DataBindingUtil.inflate(getLayoutInflater(), getContentLayoutId(), mBaseViewBinding.toolbarContainer.navContent, true);
    }

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
