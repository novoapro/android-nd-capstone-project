package com.manpdev.appointment.ui.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.manpdev.appointment.R;
import com.manpdev.appointment.data.model.AppointmentModel;
import com.manpdev.appointment.databinding.ListItemClientAppointmentBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;

/**
 * novoa on 10/23/16.
 */

public class ClientAppointmentAdapter extends RecyclerView.Adapter<ClientAppointmentAdapter.ClientAppointmentItemHolder> {


    private final Picasso mPicasso;
    private List<AppointmentModel> mAppointmentList;

    public ClientAppointmentAdapter(List<AppointmentModel> appointmentList, Picasso picasso) {
        if (appointmentList == null)
            mAppointmentList = new ArrayList<>();
        else
            mAppointmentList = appointmentList;

        this.mPicasso = picasso;
    }

    public void updateDataFromObservable(Observable<List<AppointmentModel>> appointmentModelObservable){
        mAppointmentList.clear();
        appointmentModelObservable.subscribe(new Observer<List<AppointmentModel>>() {
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
            }
        });
    }

    @Override
    public ClientAppointmentItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_client_appointment, parent, false);

        return new ClientAppointmentItemHolder(v);
    }

    @Override
    public void onBindViewHolder(ClientAppointmentItemHolder holder, int position) {
        holder.mViewBinding.tvProviderName.setText(mAppointmentList.get(position).getProvider());
        holder.mViewBinding.tvAppState.setText(mAppointmentList.get(position).getStateString());
        holder.mViewBinding.tvAppDate.setText(mAppointmentList.get(position).getDate().toString());
    }

    @Override
    public int getItemCount() {
        return mAppointmentList.size();
    }

    class ClientAppointmentItemHolder extends RecyclerView.ViewHolder {

        private ListItemClientAppointmentBinding mViewBinding;

        ClientAppointmentItemHolder(View itemView) {
            super(itemView);
            mViewBinding = DataBindingUtil.bind(itemView);
        }

        public ListItemClientAppointmentBinding getViewBinding() {
            return mViewBinding;
        }
    }


}
