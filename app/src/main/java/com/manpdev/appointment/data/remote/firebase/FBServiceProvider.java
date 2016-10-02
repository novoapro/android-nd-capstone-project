package com.manpdev.appointment.data.remote.firebase;

import android.support.annotation.NonNull;

import com.google.firebase.database.FirebaseDatabase;
import com.manpdev.appointment.data.model.ServiceModel;
import com.manpdev.appointment.data.remote.DataProvider;
import com.manpdev.appointment.data.remote.firebase.base.FBBaseDatabaseProvider;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;

/**
 * novoa on 9/24/16.
 */

public class FBServiceProvider extends FBBaseDatabaseProvider implements DataProvider<ServiceModel> {

    @Inject
    public FBServiceProvider(FirebaseDatabase firebaseDatabase) {
        super(firebaseDatabase);
    }

    @Override
    public Single<ServiceModel> getSingleValueObservable(String id) {
        return observeSingleValue(
                firebaseDatabase.getReference()
                        .child(ServiceModel.MODEL_ROOT_ID)
                        .child(id),
                ServiceModel.class
        );
    }

    @Override
    public Observable<ServiceModel> getCollectionObservable() {
        return Observable.empty();
    }

    @Override
    public Single<Void> insert(@NonNull ServiceModel element) {
        return observeSingleValue(
                firebaseDatabase.getReference()
                        .child(ServiceModel.MODEL_ROOT_ID)
                        .child(element.getId())
                        .setValue(element));
    }

    @Override
    public Single<Void> update(@NonNull ServiceModel element) {
        return insert(element);
    }

    @Override
    public Single<Void> remove(@NonNull ServiceModel element) {
        return observeSingleValue(
                firebaseDatabase.getReference()
                        .child(ServiceModel.MODEL_ROOT_ID)
                        .child(element.getId())
                        .removeValue());
    }
}
