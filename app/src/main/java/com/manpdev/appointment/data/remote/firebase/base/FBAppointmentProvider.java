package com.manpdev.appointment.data.remote.firebase.base;

import android.support.annotation.NonNull;

import com.google.firebase.database.FirebaseDatabase;
import com.manpdev.appointment.data.model.AppointmentModel;
import com.manpdev.appointment.data.remote.DataProvider;
import com.manpdev.appointment.data.remote.firebase.base.FBBaseDatabaseProvider;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Single;

/**
 * novoa on 9/24/16.
 */

public abstract class FBAppointmentProvider extends FBBaseDatabaseProvider implements DataProvider<AppointmentModel> {

    protected FBAppointmentProvider(FirebaseDatabase firebaseDatabase) {
        super(firebaseDatabase);
    }

    @Override
    public Single<AppointmentModel> getSingleValueObservable(String id) {
        return Single.just(null);
    }

    @Override
    public Single<Void> insert(@NonNull AppointmentModel element) {
        String appointmentId = firebaseDatabase.getReference()
                .child(AppointmentModel.MODEL_ROOT_ID_CLIENT)
                .child(element.getcId())
                .push().getKey();

        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put(AppointmentModel.MODEL_ROOT_ID_CLIENT + "/" + element.getcId() + "/" + appointmentId,
                element);

        childUpdates.put(AppointmentModel.MODEL_ROOT_ID_PROVIDER + "/" + element.getpId() + "/" + appointmentId,
                element);

        return observeSingleValue(
                firebaseDatabase.getReference()
                        .updateChildren(childUpdates)
        );
    }

    @Override
    public Single<Void> update(@NonNull AppointmentModel element) {
        return insert(element);
    }

    @Override
    public Single<Void> remove(@NonNull AppointmentModel element) {
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put(AppointmentModel.MODEL_ROOT_ID_CLIENT + "/" + element.getcId(), null);
        childUpdates.put(AppointmentModel.MODEL_ROOT_ID_PROVIDER + "/" + element.getpId(), null);

        return observeSingleValue(
                firebaseDatabase.getReference()
                        .updateChildren(childUpdates)
        );
    }

    @Override
    public abstract Observable<AppointmentModel> getCollectionObservable(String id);
}
