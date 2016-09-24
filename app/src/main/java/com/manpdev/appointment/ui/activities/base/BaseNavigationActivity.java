package com.manpdev.appointment.ui.activities.base;

import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.manpdev.appointment.R;
import com.manpdev.appointment.ui.activities.ClientAppointmentListActivity;
import com.manpdev.appointment.ui.activities.ClientProviderListActivity;
import com.manpdev.appointment.ui.activities.ProviderAppointmentListActivity;
import com.manpdev.appointment.ui.activities.ProviderServiceInfoActivity;
import com.manpdev.appointment.ui.activities.ProviderServiceReviewListActivity;

public abstract class BaseNavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected ActionBarDrawerToggle mToggle;
    protected DrawerLayout mDrawer;
    protected Toolbar mToolBar;
    protected ViewGroup mContainer;
    protected FloatingActionButton mActionFab;
    protected NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        mContainer = (ViewGroup) findViewById(R.id.view_content);
        View.inflate(this, getContentLayoutId(), mContainer);

        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.inflateMenu(getNavigationMenuRes());

        mActionFab = (FloatingActionButton) findViewById(R.id.fab);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mNavigationView.setCheckedItem(getCheckedItemId());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();
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
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == getCheckedItemId()) {
            mDrawer.closeDrawer(GravityCompat.START);
            return true;
        }

        Intent toLaunch;

        switch (item.getItemId()) {
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

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @MenuRes
    protected int getNavigationMenuRes() {
        return R.menu.default_navigation_menu;
    }

    @LayoutRes
    protected abstract int getContentLayoutId();

    @IdRes
    protected abstract int getCheckedItemId();

}
