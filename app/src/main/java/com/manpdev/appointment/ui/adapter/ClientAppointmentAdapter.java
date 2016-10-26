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
import com.manpdev.appointment.databinding.ListItemClientAppointmentBinding;
import com.manpdev.appointment.ui.utils.DateFormatter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

/**
 * novoa on 10/23/16.
 */

public class ClientAppointmentAdapter extends RecyclerView.Adapter<ClientAppointmentAdapter.ClientAppointmentItemHolder>{
    private static final String TAG = "ClientAppointmentAdapte";
    private List<AppointmentModel> mAppointmentList;
    private ClientAppointmentItemListener mListener;

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


    public ClientAppointmentAdapter(List<AppointmentModel> appointmentList) {
        if (appointmentList == null)
            mAppointmentList = new ArrayList<>();
        else
            mAppointmentList = appointmentList;
    }

    public void addItemListener(@NonNull ClientAppointmentItemListener listener){
        mListener = listener;
    }

    public void updateDataFromObservable(Observable<List<AppointmentModel>> appointmentModelObservable) {
        mAppointmentList.clear();
        stopUpdateFromObservable();
        mSubscription = appointmentModelObservable.subscribe(mAppointmentObserver);
    }

    public void stopUpdateFromObservable() {
        if (mSubscription != null && !mSubscription.isUnsubscribed())
            mSubscription.unsubscribe();
    }

    @Override
    public ClientAppointmentItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_client_appointment, parent, false);

        return new ClientAppointmentItemHolder(v);
    }

    @Override
    public void onBindViewHolder(final ClientAppointmentItemHolder holder, final int position) {
        int iconRes = (mAppointmentList.get(position).getIconRes());
        holder.mViewBinding.ivAppointmentState.setImageResource(iconRes);
        holder.mViewBinding.tvProviderName.setText(mAppointmentList.get(position).getProvider());
        holder.mViewBinding.tvAppState.setText(mAppointmentList.get(position).getStateString());
        holder.mViewBinding.tvAppDate.setText(DateFormatter.getDateTimeFormat(mAppointmentList.get(position).getDate()));

        setItemListener(holder);
    }

    @Override
    public int getItemCount() {
        return mAppointmentList.size();
    }


    private void setItemListener(final ClientAppointmentItemHolder holder) {
        if(mListener == null)
            return;

        holder.mViewBinding.ivCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCalendarClicked(mAppointmentList.get(holder.getAdapterPosition()));
            }
        });
        holder.mViewBinding.ivReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onReviewClicked(mAppointmentList.get(holder.getAdapterPosition()));
            }
        });
    }

    class ClientAppointmentItemHolder extends RecyclerView.ViewHolder {

        private ListItemClientAppointmentBinding mViewBinding;

        ClientAppointmentItemHolder(View itemView) {
            super(itemView);
            mViewBinding = DataBindingUtil.bind(itemView);
        }
    }


}
