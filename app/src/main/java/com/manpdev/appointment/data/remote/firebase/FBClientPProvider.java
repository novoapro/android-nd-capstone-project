package com.manpdev.appointment.data.remote.firebase;

import android.support.annotation.NonNull;

import com.google.firebase.database.FirebaseDatabase;
import com.manpdev.appointment.data.model.ClientProviderModel;
import com.manpdev.appointment.data.model.ReviewModel;
import com.manpdev.appointment.data.remote.DataProvider;
import com.manpdev.appointment.data.remote.firebase.base.FBBaseDatabaseProvider;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;

/**
 * novoa on 9/24/16.
 */

public class FBClientPProvider extends FBBaseDatabaseProvider implements DataProvider<ClientProviderModel> {

    private static final String TAG = "FBRatingProvider";

    @Inject
    public FBClientPProvider(FirebaseDatabase firebaseDatabase) {
        super(firebaseDatabase);
    }

    @Override
    public Single<ClientProviderModel> getSingleValueObservable(String id) {
        return observeSingleValue(firebaseDatabase.getReference()
                        .child(ReviewModel.MODEL_ROOT_ID)
                        .child(id),
                ClientProviderModel.class);
    }

    @Override
    public Observable<ClientProviderModel> getCollectionObservable(String id) {
        return Observable.empty();
    }

    @Override
    public Single<Void> insert(@NonNull final ClientProviderModel element) {
        return observeSingleValue(
                firebaseDatabase.getReference()
                        .child(ReviewModel.MODEL_ROOT_ID)
                        .child(element.getuId())
                        .setValue(element));
    }

    @Override
    public Single<Void> update(@NonNull ClientProviderModel element) {
        return insert(element);
    }

    @Override
    public Single<Void> remove(@NonNull ClientProviderModel element) {
        return Single.just(null);
    }
}
