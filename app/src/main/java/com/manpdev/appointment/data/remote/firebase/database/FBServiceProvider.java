package com.manpdev.appointment.data.remote.firebase.database;

import android.support.annotation.NonNull;

import com.google.firebase.database.FirebaseDatabase;
import com.manpdev.appointment.data.model.ServiceModel;
import com.manpdev.appointment.data.remote.firebase.database.base.FBBaseDatabaseProvider;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;

/**
 * novoa on 9/24/16.
 */

public class FBServiceProvider extends FBBaseDatabaseProvider{

    @Inject
    public FBServiceProvider(FirebaseDatabase firebaseDatabase) {
        super(firebaseDatabase);
    }

    public Single<ServiceModel> getSingleValueObservable(String id) {
        return observeSingleValue(
                firebaseDatabase.getReference()
                        .child(ServiceModel.MODEL_ROOT_ID)
                        .child(id),
                ServiceModel.class
        );
    }

    public Observable<ServiceModel> getCollectionObservable(String id) {
        return Observable.empty();
    }

    public Single<Void> insert(@NonNull ServiceModel element) {
        return observeSingleValue(
                firebaseDatabase.getReference()
                        .child(ServiceModel.MODEL_ROOT_ID)
                        .child(element.getuId())
                        .setValue(element));
    }

    public Single<Void> remove(@NonNull ServiceModel element) {
        return observeSingleValue(
                firebaseDatabase.getReference()
                        .child(ServiceModel.MODEL_ROOT_ID)
                        .child(element.getuId())
                        .removeValue());
    }
}
