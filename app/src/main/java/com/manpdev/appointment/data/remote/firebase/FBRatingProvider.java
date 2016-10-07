package com.manpdev.appointment.data.remote.firebase;

import android.support.annotation.NonNull;

import com.google.firebase.database.FirebaseDatabase;
import com.manpdev.appointment.data.model.RatingModel;
import com.manpdev.appointment.data.model.ReviewModel;
import com.manpdev.appointment.data.remote.DataProvider;
import com.manpdev.appointment.data.remote.firebase.base.FBBaseDatabaseProvider;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;

/**
 * novoa on 9/24/16.
 */

public class FBRatingProvider extends FBBaseDatabaseProvider implements DataProvider<RatingModel> {

    @Inject
    public FBRatingProvider(FirebaseDatabase firebaseDatabase) {
        super(firebaseDatabase);
    }

    @Override
    public Single<RatingModel> getSingleValueObservable(String id) {
        return observeSingleValue(
                firebaseDatabase.getReference()
                        .child(RatingModel.MODEL_ROOT_ID)
                        .child(id),
                RatingModel.class
        );
    }

    @Override
    public Observable<RatingModel> getCollectionObservable(String id) {
        return observeValue(firebaseDatabase.getReference()
                        .child(RatingModel.MODEL_ROOT_ID)
                        .child(id),
                RatingModel.class);
    }

    @Override
    public Single<Void> insert(@NonNull RatingModel element) {
        return observeSingleValue(
                firebaseDatabase.getReference()
                        .child(RatingModel.MODEL_ROOT_ID)
                        .child(element.getuId())
                        .setValue(element));
    }

    @Override
    public Single<Void> update(@NonNull RatingModel element) {
        return insert(element);
    }

    @Override
    public Single<Void> remove(@NonNull RatingModel element) {
        return Single.just(null);
    }
}
