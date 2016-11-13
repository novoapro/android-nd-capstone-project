package com.manpdev.appointment.data.remote.firebase.database;

import android.support.annotation.NonNull;

import com.google.firebase.database.FirebaseDatabase;
import com.manpdev.appointment.data.model.ReviewModel;
import com.manpdev.appointment.data.remote.firebase.database.base.FBBaseDatabaseProvider;

import java.util.List;

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

    public Observable<List<ReviewModel>> getCollectionObservable(String id) {
        return observeValuesList(firebaseDatabase.getReference()
                        .child(ReviewModel.MODEL_ROOT_ID)
                        .child(id),
                ReviewModel.class);
    }


    public Single<Void> insert(@NonNull ReviewModel element) {
        return observeSingleValue(
                firebaseDatabase.getReference()
                        .child(ReviewModel.MODEL_ROOT_ID)
                        .child(element.getUid())
                        .setValue(element));
    }

    public Single<Void> remove(@NonNull ReviewModel element) {
        return observeSingleValue(
                firebaseDatabase.getReference()
                        .child(ReviewModel.MODEL_ROOT_ID)
                        .child(element.getUid())
                        .child(element.getId())
                        .removeValue());
    }
}
