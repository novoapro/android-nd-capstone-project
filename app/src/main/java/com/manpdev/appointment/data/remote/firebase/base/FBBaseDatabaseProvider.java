package com.manpdev.appointment.data.remote.firebase.base;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * novoa on 24/05/2016.
 */
public abstract class FBBaseDatabaseProvider {
    private static final String TAG = "FBBaseDatabaseProvider";

    protected FirebaseDatabase firebaseDatabase;

    protected FBBaseDatabaseProvider(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    @NonNull
    protected <T> Single<T> observeSingleValue(@NonNull final Query query, @NonNull final Class<T> clazz) {
        return Single.create(new Single.OnSubscribe<T>() {
            @Override
            public void call(final SingleSubscriber<? super T> subscriber) {
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            T value = dataSnapshot.getValue(clazz);
                            if (value != null) {
                                if (!subscriber.isUnsubscribed())
                                    subscriber.onSuccess(value);
                            }
                        } catch (Exception ex) {
                            if (!subscriber.isUnsubscribed())
                                subscriber.onError(ex);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onError(new Throwable(error.getMessage()));
                        }
                    }
                });
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    protected <T> Observable<T> observeValue(@NonNull final Query query, @NonNull final Class<T> clazz) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(final Subscriber<? super T> subscriber) {
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            T value = dataSnapshot.getValue(clazz);
                            if (value != null) {
                                if (!subscriber.isUnsubscribed())
                                    subscriber.onNext(value);

                            }
                        } catch (Exception ex) {
                            if (!subscriber.isUnsubscribed())
                                subscriber.onError(ex);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        if (!subscriber.isUnsubscribed())
                            subscriber.onError(new Throwable(error.getMessage()));

                    }
                });
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    protected <T> Single<T> observeSingleValue(@NonNull final Task<T> task) {
        return Single.create(new Single.OnSubscribe<T>() {
            @Override
            public void call(final SingleSubscriber<? super T> subscriber) {
                task.addOnSuccessListener(new OnSuccessListener<T>() {
                    @Override
                    public void onSuccess(T result) {
                        if (!subscriber.isUnsubscribed())
                            subscriber.onSuccess(result);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (!subscriber.isUnsubscribed())
                            subscriber.onError(e);

                    }
                });
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    protected <T> Observable<List<T>> observeValuesList(@NonNull final Query query, @NonNull final Class<T> clazz) {
        return Observable.create(new Observable.OnSubscribe<List<T>>() {
            @Override
            public void call(final Subscriber<? super List<T>> subscriber) {
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d(TAG, dataSnapshot.toString());
                        List<T> items = new ArrayList<>();
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            T value = childSnapshot.getValue(clazz);
                            if (value == null)
                                continue;
                            items.add(value);
                        }

                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(items);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        if (!subscriber.isUnsubscribed())
                            subscriber.onError(new Throwable(error.getMessage()));
                    }
                });
            }
        });
    }

}
