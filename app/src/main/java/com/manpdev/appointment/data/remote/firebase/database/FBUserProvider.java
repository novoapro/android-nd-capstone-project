package com.manpdev.appointment.data.remote.firebase.database;

import android.support.annotation.NonNull;

import com.manpdev.appointment.data.model.ServiceModel;
import com.manpdev.appointment.data.model.UserEmailModel;
import com.manpdev.appointment.data.remote.DatabaseProvider;

import javax.inject.Inject;

import rx.Single;

/**
 * novoa on 9/24/16.
 */

public class FBUserProvider{

    private final DatabaseProvider mDatabaseProvider;

    @Inject
    public FBUserProvider(DatabaseProvider databaseProvider) {
        mDatabaseProvider = databaseProvider;
    }

    public Single<String> getSingleValueObservable(String email) {
        return mDatabaseProvider.observeSingleObjectKey(
                mDatabaseProvider.getReference()
                        .child(UserEmailModel.MODEL_ROOT_ID)
                        .orderByValue()
                        .equalTo(email)
        );
    }

    public Single<Void> insert(@NonNull UserEmailModel element) {
        return mDatabaseProvider.observeSingleValue(
                mDatabaseProvider.getReference()
                        .child(UserEmailModel.MODEL_ROOT_ID)
                        .child(element.getuId())
                        .setValue(element.getEmail()));
    }

    public Single<Void> remove(@NonNull ServiceModel element) {
        return mDatabaseProvider.observeSingleValue(
                mDatabaseProvider.getReference()
                        .child(UserEmailModel.MODEL_ROOT_ID)
                        .child(element.getuId())
                        .removeValue());
    }
}
