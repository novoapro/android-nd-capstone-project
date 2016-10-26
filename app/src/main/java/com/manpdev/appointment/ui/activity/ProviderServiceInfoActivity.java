package com.manpdev.appointment.ui.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.manpdev.appointment.AppointmentApplication;
import com.manpdev.appointment.R;
import com.manpdev.appointment.data.model.ServiceModel;
import com.manpdev.appointment.databinding.ActivityProviderServiceInfoBinding;
import com.manpdev.appointment.ui.activity.base.BaseNavigationActivity;
import com.manpdev.appointment.ui.di.module.PresentersModule;
import com.manpdev.appointment.ui.mvp.ServiceInfoContract;

import javax.inject.Inject;

public class ProviderServiceInfoActivity extends BaseNavigationActivity implements ServiceInfoContract.View{

    private static final String TAG = "ProviderServiceInfoActi";
    
    @Inject
    ServiceInfoContract.Presenter mPresenter;

    private ActivityProviderServiceInfoBinding mViewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((AppointmentApplication)getApplication()).getApplicationComponent()
                .activity()
                .mvp(new PresentersModule())
                .inject(this);
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
    protected int getContentLayoutId() {
        return R.layout.activity_provider_service_info;
    }

    @Override
    protected int getCheckedItemId() {
        return R.id.nav_provider_service_info;
    }

    @Override
    public void updateServiceInformation(final ServiceModel service) {
        Log.d(TAG, "updateServiceInformation");
        
        if(service == null) {
            launchAddServiceView();
            return;
        }
        
        mPicasso.load(service.getBanner())
                .placeholder(R.drawable.ic_logo_no_text)
                .into(mViewBinding.ivServiceBanner);

        mViewBinding.tvServiceName.setText(service.getName());
        mViewBinding.tvServiceDescription.setText(service.getDescription());
        mViewBinding.tvServiceType.setText(service.getType());
        mViewBinding.tvServicePhone.setText(service.getPhone());
        mViewBinding.tvServiceAddress.setText(service.getAddress());

        mViewBinding.ibEditService.setVisibility(View.VISIBLE);
        mViewBinding.ibEditService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openServiceEditorActivity();
            }
        });

        mViewBinding.tvServiceAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap(service.getAddress());
            }
        });
    }

    @Override
    public void launchAddServiceView() {
        openServiceEditorActivity();
    }


    private void openServiceEditorActivity() {
        Intent intent = new Intent(ProviderServiceInfoActivity.this, EditServiceInfoActivity.class);
        startActivity(intent);
    }

    private void openMap(String address) {
        Uri gmmIntentUri = Uri.parse(String.format("geo:0,0?q=%s", address));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        try {
            startActivity(mapIntent);
        } catch (ActivityNotFoundException e) {
            Log.w(TAG, "openMap: ", e);
        }
    }
}
