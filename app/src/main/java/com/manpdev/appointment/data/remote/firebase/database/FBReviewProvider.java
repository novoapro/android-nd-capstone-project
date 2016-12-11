package com.manpdev.appointment.data.remote.firebase.database;

import android.support.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.manpdev.appointment.data.model.ReviewModel;
import com.manpdev.appointment.data.remote.DatabaseProvider;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;

/**
 * novoa on 9/24/16.
 */

public class FBReviewProvider {

    private final DatabaseProvider mDatabaseProvider;

    @Inject
    public FBReviewProvider(DatabaseProvider databaseProvider) {
        mDatabaseProvider = databaseProvider;
    }

    public Single<ReviewModel> getSingleValueObservable(String id) {
        return mDatabaseProvider.observeSingleValue(
                mDatabaseProvider.getReference()
                        .child(ReviewModel.MODEL_ROOT_ID)
                        .child(id),
                ReviewModel.class
        );
    }

    public Observable<List<ReviewModel>> getCollectionObservable(String id) {
        return mDatabaseProvider.observeValuesList(mDatabaseProvider.getReference()
                        .child(ReviewModel.MODEL_ROOT_ID)
                        .child(id),
                ReviewModel.class);
    }


    public Single<Void> insert(@NonNull ReviewModel element) {
        DatabaseReference reference = mDatabaseProvider.getReference()
                .child(ReviewModel.MODEL_ROOT_ID)
                .child(element.getUid())
                .push();


        return mDatabaseProvider.observeSingleValue(
                reference.setValue(element)
        );
    }

    public Single<Void> remove(@NonNull ReviewModel element) {
        return mDatabaseProvider.observeSingleValue(
                mDatabaseProvider.getReference()
                        .child(ReviewModel.MODEL_ROOT_ID)
                        .child(element.getUid())
                        .child(element.getId())
                        .removeValue());
    }
}
