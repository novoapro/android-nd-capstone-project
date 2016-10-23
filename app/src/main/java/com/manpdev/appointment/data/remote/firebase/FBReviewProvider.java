package com.manpdev.appointment.data.remote.firebase;

import android.support.annotation.NonNull;

import com.google.firebase.database.FirebaseDatabase;
import com.manpdev.appointment.data.model.ReviewModel;
import com.manpdev.appointment.data.remote.firebase.base.FBBaseDatabaseProvider;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;

/**
 * novoa on 9/24/16.
 */

public class FBReviewProvider extends FBBaseDatabaseProvider {

    @Inject
    public FBReviewProvider(FirebaseDatabase firebaseDatabase) {
        super(firebaseDatabase);
    }

    public Single<ReviewModel> getSingleValueObservable(String id) {
        return observeSingleValue(
                firebaseDatabase.getReference()
                        .child(ReviewModel.MODEL_ROOT_ID)
                        .child(id),
                ReviewModel.class
        );
    }

    public Observable<ReviewModel> getCollectionObservable(String id) {
        return observeValue(firebaseDatabase.getReference()
                        .child(ReviewModel.MODEL_ROOT_ID)
                        .child(id),
                ReviewModel.class);
    }


    public Single<Void> insert(@NonNull ReviewModel element) {
        return observeSingleValue(
                firebaseDatabase.getReference()
                        .child(ReviewModel.MODEL_ROOT_ID)
                        .child(element.getuId())
                        .setValue(element));
    }

    public Single<Void> remove(@NonNull ReviewModel element) {
        return observeSingleValue(
                firebaseDatabase.getReference()
                        .child(ReviewModel.MODEL_ROOT_ID)
                        .child(element.getuId())
                        .child(element.getId())
                        .removeValue());
    }
}
