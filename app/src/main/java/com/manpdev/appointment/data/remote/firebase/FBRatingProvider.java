package com.manpdev.appointment.data.remote.firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.manpdev.appointment.data.model.RatingModel;
import com.manpdev.appointment.data.remote.DataProvider;
import com.manpdev.appointment.data.remote.firebase.base.FBBaseDatabaseProvider;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;

/**
 * novoa on 9/24/16.
 */

public class FBRatingProvider extends FBBaseDatabaseProvider implements DataProvider<RatingModel> {

    private static final String TAG = "FBRatingProvider";
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
        return Observable.empty();
    }

    @Override
    public Single<Void> insert(@NonNull final RatingModel element) {
        final DatabaseReference ref = firebaseDatabase.getReference()
                .child(RatingModel.MODEL_ROOT_ID)
                .child(element.getuId());


        ref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction (MutableData mutableData){
                RatingModel model = mutableData.getValue(RatingModel.class);
                if (model == null) {
                    mutableData.setValue(element);
                    return Transaction.success(mutableData);
                }

                model.increaseCount();
                model.increaseTotal(element.getRating());

                mutableData.setValue(model);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete (DatabaseError databaseError, boolean b,
                                    DataSnapshot dataSnapshot){
                Log.w(TAG, "onComplete: " + databaseError.getMessage());
            }
        });

        return Single.just(null);
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
