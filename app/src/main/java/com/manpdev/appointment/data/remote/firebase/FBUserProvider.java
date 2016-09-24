package com.manpdev.appointment.data.remote.firebase;

import android.support.annotation.NonNull;

import com.google.firebase.database.FirebaseDatabase;
import com.manpdev.appointment.data.model.UserModel;
import com.manpdev.appointment.data.remote.DataProvider;
import com.manpdev.appointment.data.remote.firebase.base.FBBaseDatabaseProvider;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;

/**
 * novoa on 9/24/16.
 */

public class FBUserProvider extends FBBaseDatabaseProvider implements DataProvider<UserModel> {

    @Inject
    public FBUserProvider(FirebaseDatabase firebaseDatabase) {
        super(firebaseDatabase);
    }

    @Override
    public Single<UserModel> getSingleValueObservable(String id) {
        return observeSingleValue(
                firebaseDatabase.getReference()
                        .child(UserModel.MODEL_ROOT_ID)
                        .child(id),
                UserModel.class
        );
    }

    @Override
    public Observable<UserModel> getCollectionObservable() {
        return Observable.empty();
    }

    @Override
    public Single<Void> insert(@NonNull UserModel element) {
        return observeSingleValue(
                firebaseDatabase.getReference()
                        .child(UserModel.MODEL_ROOT_ID)
                        .child(element.getId())
                        .setValue(element));
    }

    @Override
    public Single<Void> update(@NonNull UserModel element) {
        return insert(element);
    }

    @Override
    public Single<Void> remove(@NonNull UserModel element) {
        return observeSingleValue(
                firebaseDatabase.getReference()
                        .child(UserModel.MODEL_ROOT_ID)
                        .child(element.getId())
                        .removeValue());
    }
}
