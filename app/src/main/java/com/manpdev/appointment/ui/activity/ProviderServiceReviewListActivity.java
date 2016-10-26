package com.manpdev.appointment.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.manpdev.appointment.AppointmentApplication;
import com.manpdev.appointment.R;
import com.manpdev.appointment.data.model.RatingModel;
import com.manpdev.appointment.data.model.ReviewModel;
import com.manpdev.appointment.databinding.ActivityProviderServiceReviewListBinding;
import com.manpdev.appointment.ui.activity.base.BaseNavigationActivity;
import com.manpdev.appointment.ui.adapter.ServiceReviewAdapter;
import com.manpdev.appointment.ui.di.module.PresentersModule;
import com.manpdev.appointment.ui.mvp.ServiceReviewContract;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

public class ProviderServiceReviewListActivity extends BaseNavigationActivity implements ServiceReviewContract.View {

    private ActivityProviderServiceReviewListBinding mViewBinding;
    private ServiceReviewAdapter mAdapter;

    @Inject
    ServiceReviewContract.Presenter mPresenter;
    private Subscription mRatingSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((AppointmentApplication)getApplication()).getApplicationComponent()
                .activity()
                .mvp(new PresentersModule())
                .inject(this);

        mAdapter = new ServiceReviewAdapter(null);

        mViewBinding.rvReviews.setLayoutManager(new LinearLayoutManager(this));
        mViewBinding.rvReviews.setHasFixedSize(true);
        mViewBinding.rvReviews.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.attachView(this);
        mPresenter.loadReviewList();
        mPresenter.loadServiceRating();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.detachView();
        mAdapter.stopUpdateFromObservable();

        if(mRatingSubscription != null)
            mRatingSubscription.unsubscribe();
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
        return R.layout.activity_provider_service_review_list;
    }

    @Override
    protected int getCheckedItemId() {
        return R.id.nav_provider_reviews;
    }

    @Override
    public void showList(Observable<List<ReviewModel>> reviews) {
        mAdapter.updateDataFromObservable(reviews);
    }

    @Override
    public void showServiceRating(Observable<RatingModel> rating) {
        mRatingSubscription = rating.subscribe(new Action1<RatingModel>() {
            @Override
            public void call(RatingModel ratingModel) {
                mViewBinding.rbServiceRating.setRating((float) ratingModel.getRating());
            }
        });
    }
}
