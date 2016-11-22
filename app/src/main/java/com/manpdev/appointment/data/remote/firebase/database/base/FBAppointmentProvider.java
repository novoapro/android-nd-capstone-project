package com.manpdev.appointment.data.remote.firebase.database.base;

import android.support.annotation.NonNull;

import com.manpdev.appointment.data.model.AppointmentModel;
import com.manpdev.appointment.data.remote.DatabaseProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Single;

/**
 * novoa on 9/24/16.
 */

public abstract class FBAppointmentProvider{

    protected DatabaseProvider mDatabaseProvider;

    protected FBAppointmentProvider(DatabaseProvider databaseProvider) {
       mDatabaseProvider = databaseProvider;
    }

    public Single<AppointmentModel> getSingleValueObservable(String id) {
        return Single.just(null);
    }

    public Single<Void> insert(@NonNull AppointmentModel element) {
        String appointmentId = mDatabaseProvider.getReference()
                .child(AppointmentModel.MODEL_ROOT_ID_CLIENT)
                .child(element.getCid())
                .push().getKey();

        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put(AppointmentModel.MODEL_ROOT_ID_CLIENT + "/" + element.getCid() + "/" + appointmentId,
                element);

        childUpdates.put(AppointmentModel.MODEL_ROOT_ID_PROVIDER + "/" + element.getPid() + "/" + appointmentId,
                element);

        return mDatabaseProvider.observeSingleValue(
                mDatabaseProvider.getReference()
                        .updateChildren(childUpdates)
        );
    }

    public Single<Void> update(@NonNull AppointmentModel element) {
        return insert(element);
    }

    public Single<Void> remove(@NonNull AppointmentModel element) {
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put(AppointmentModel.MODEL_ROOT_ID_CLIENT + "/" + element.getCid(), null);
        childUpdates.put(AppointmentModel.MODEL_ROOT_ID_PROVIDER + "/" + element.getPid(), null);

        return mDatabaseProvider.observeSingleValue(
                mDatabaseProvider.getReference()
                        .updateChildren(childUpdates)
        );
    }

    public abstract Observable<List<AppointmentModel>> getCollectionObservable(String id);
}
