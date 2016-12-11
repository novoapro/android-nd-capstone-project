package com.manpdev.appointment.service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.manpdev.appointment.AppointmentApplication;
import com.manpdev.appointment.R;
import com.manpdev.appointment.data.model.RatingModel;
import com.manpdev.appointment.data.model.ServiceModel;
import com.manpdev.appointment.data.remote.AuthProvider;
import com.manpdev.appointment.data.remote.firebase.database.FBRatingProvider;
import com.manpdev.appointment.data.remote.firebase.database.FBServiceProvider;
import com.manpdev.appointment.ui.activity.ProviderServiceReviewListActivity;
import com.manpdev.appointment.ui.widget.ServiceRatingWidgetProvider;

import javax.inject.Inject;

import rx.functions.Action1;

public class WidgetRatingUpdateService extends IntentService {

    private static final String TAG = "WidgetRatingUpdateServi";

    @Inject
    public AuthProvider mAuthProvider;

    @Inject
    public FBServiceProvider mServiceProvider;

    @Inject
    public FBRatingProvider mRatingProvider;

    private RemoteViews mRemoveView;

    public WidgetRatingUpdateService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((AppointmentApplication) getApplication()).getApplicationComponent()
                .service()
                .inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent");
        if(!mAuthProvider.isUserLoggedIn())
            return;

        mRemoveView = new RemoteViews(getPackageName(), R.layout.widget_service_ratings);
        getServiceName();
    }

    private void getServiceName() {
        Log.d(TAG, "getServiceName");
        mServiceProvider.getSingleValueObservable(mAuthProvider.getUserId())
                .subscribe(new Action1<ServiceModel>() {
                    @Override
                    public void call(ServiceModel serviceModel) {
                        if (serviceModel == null)
                            return;

                        mRemoveView.setTextViewText(R.id.tv_service_name, serviceModel.getName());
                        getServiceRatings();
                    }
                });
    }

    public void getServiceRatings() {
        Log.d(TAG, "getServiceRatings");
        mRatingProvider.getSingleValueObservable(mAuthProvider.getUserId())
                .subscribe(new Action1<RatingModel>() {
                    @Override
                    public void call(RatingModel ratingModel) {
                        if (ratingModel == null)
                            return;

                        mRemoveView.setTextViewText(R.id.tv_service_rating_count, getResources().getString(R.string.widget_review_count, ratingModel.getCount()));
                        mRemoveView.setTextViewText(R.id.tv_ratings, String.valueOf(ratingModel.getRating()));
                        updateWidgetData();
                    }
                });
    }

    private void updateWidgetData(){
        Log.d(TAG, "updateWidgetData");
        initOnClickListener(this);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        ComponentName thisWidget = new ComponentName(this, ServiceRatingWidgetProvider.class);
        appWidgetManager.updateAppWidget(thisWidget, mRemoveView);
    }

    private void initOnClickListener(Context context) {
        Intent intent = new Intent(context, ProviderServiceReviewListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        mRemoveView.setOnClickPendingIntent(R.id.rl_widget_container, pendingIntent);
    }
}
