package com.manpdev.appointment.ui.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.manpdev.appointment.R;
import com.manpdev.appointment.data.model.AppointmentModel;
import com.manpdev.appointment.databinding.ListItemProviderAppointmentBinding;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

/**
 * novoa on 10/23/16.
 */

public class ProviderAppointmentAdapter extends RecyclerView.Adapter<ProviderAppointmentAdapter.ProviderAppointmentItemHolder>{
    private static final String TAG = "ProviderAppointmentAd";
    private List<AppointmentModel> mAppointmentList;
    private ProviderAppointmentItemListener mListener;

    private Subscription mSubscription;
    private Observer<List<AppointmentModel>> mAppointmentObserver = new Observer<List<AppointmentModel>>() {
        @Override
        public void onCompleted() {
            notifyDataSetChanged();
        }

        @Override
        public void onError(Throwable e) {
            notifyDataSetChanged();
        }

        @Override
        public void onNext(List<AppointmentModel> appointmentModel) {
            mAppointmentList = appointmentModel;
            notifyDataSetChanged();
        }
    };


    public ProviderAppointmentAdapter(List<AppointmentModel> appointmentList) {
        if (appointmentList == null)
            mAppointmentList = new ArrayList<>();
        else
            mAppointmentList = appointmentList;
    }

    public void addItemListener(@NonNull ProviderAppointmentItemListener listener){
        mListener = listener;
    }

    public void updateDataFromObservable(Observable<List<AppointmentModel>> appointmentModelObservable) {
        Log.d(TAG, "updateDataFromObservable()");
        mAppointmentList.clear();
        stopUpdateFromObservable();
        mSubscription = appointmentModelObservable.subscribe(mAppointmentObserver);
    }

    public void stopUpdateFromObservable() {
        if (mSubscription != null && !mSubscription.isUnsubscribed())
            mSubscription.unsubscribe();
    }

    @Override
    public ProviderAppointmentItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_provider_appointment, parent, false);

        return new ProviderAppointmentItemHolder(v);
    }

    @Override
    public void onBindViewHolder(final ProviderAppointmentItemHolder holder, final int position) {
        holder.mViewBinding.tvClientName.setText(mAppointmentList.get(position).getClient());
        holder.mViewBinding.tvAppState.setText(mAppointmentList.get(position).getStateString());
        holder.mViewBinding.tvAppDate.setText(mAppointmentList.get(position).getDate().toString());
        setItemListener(holder);
    }

    @Override
    public int getItemCount() {
        return mAppointmentList.size();
    }


    private void setItemListener(final ProviderAppointmentItemHolder holder) {
        if(mListener == null)
            return;

        holder.mViewBinding.ivCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCalendar(mAppointmentList.get(holder.getAdapterPosition()));
            }
        });
        holder.mViewBinding.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onEdit(mAppointmentList.get(holder.getAdapterPosition()));
            }
        });
    }

    class ProviderAppointmentItemHolder extends RecyclerView.ViewHolder {

        private ListItemProviderAppointmentBinding mViewBinding;

        ProviderAppointmentItemHolder(View itemView) {
            super(itemView);
            mViewBinding = DataBindingUtil.bind(itemView);
        }
    }


}
