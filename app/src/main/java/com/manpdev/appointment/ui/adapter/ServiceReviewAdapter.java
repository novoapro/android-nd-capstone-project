package com.manpdev.appointment.ui.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.manpdev.appointment.R;
import com.manpdev.appointment.data.model.ReviewModel;
import com.manpdev.appointment.databinding.ListItemProviderReviewsBinding;
import com.manpdev.appointment.ui.utils.DateFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

/**
 * novoa on 10/23/16.
 */

public class ServiceReviewAdapter extends RecyclerView.Adapter<ServiceReviewAdapter.ServiceReviewHolder>{
    private static final String TAG = "ClientAppointmentAdapte";
    private List<ReviewModel> mReviews;

    private Subscription mSubscription;
    private Observer<List<ReviewModel>> mReviewObserver = new Observer<List<ReviewModel>>() {
        @Override
        public void onCompleted() {
            notifyDataSetChanged();
        }

        @Override
        public void onError(Throwable e) {
            notifyDataSetChanged();
        }

        @Override
        public void onNext(List<ReviewModel> reviews) {
            mReviews = reviews;
            Collections.reverse(mReviews);
            notifyDataSetChanged();
        }
    };


    public ServiceReviewAdapter(List<ReviewModel> reviews) {
        if (reviews == null)
            mReviews = new ArrayList<>();
        else
            mReviews = reviews;
    }

    public void updateDataFromObservable(Observable<List<ReviewModel>> reviewModelObservable) {
        Log.d(TAG, "updateDataFromObservable:()");
        mReviews.clear();
        stopUpdateFromObservable();
        mSubscription = reviewModelObservable.subscribe(mReviewObserver);
    }

    public void stopUpdateFromObservable() {
        if (mSubscription != null && !mSubscription.isUnsubscribed())
            mSubscription.unsubscribe();
    }

    @Override
    public ServiceReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_provider_reviews, parent, false);

        return new ServiceReviewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ServiceReviewHolder holder, final int position) {
        holder.mViewBinding.rbRating.setRating(mReviews.get(position).getRating());
        holder.mViewBinding.tvReviewText.setText(mReviews.get(position).getReview());
        holder.mViewBinding.tvAuthorName.setText(mReviews.get(position).getAuthor());
        holder.mViewBinding.tvReviewDate.setText(DateFormatter.getShortDate(mReviews.get(position).getDate()));
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    class ServiceReviewHolder extends RecyclerView.ViewHolder {

        private ListItemProviderReviewsBinding mViewBinding;

        ServiceReviewHolder(View itemView) {
            super(itemView);
            mViewBinding = DataBindingUtil.bind(itemView);
        }
    }


}
