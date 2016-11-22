package com.manpdev.appointment.data.remote.firebase.database.base;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.manpdev.appointment.data.remote.DatabaseProvider;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * novoa on 24/05/2016.
 */
public class FBBaseDatabaseProvider implements DatabaseProvider {
    private static final String TAG = "FBBaseDatabaseProvider";

    private FirebaseDatabase firebaseDatabase;

    public FBBaseDatabaseProvider(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    @Override
    public DatabaseReference getReference() {
        return firebaseDatabase.getReference();
    }

    @NonNull
    public <T> Single<T> observeSingleValue(@NonNull final Task<T> task) {
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
    public <T> Single<T> observeSingleValue(@NonNull final Query query, @NonNull final Class<T> clazz) {
        return Single.create(new Single.OnSubscribe<T>() {
            @Override
            public void call(final SingleSubscriber<? super T> subscriber) {
                final ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            T value = dataSnapshot.getValue(clazz);
                            if (!subscriber.isUnsubscribed())
                                subscriber.onSuccess(value);
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
                };

                query.addListenerForSingleValueEvent(valueEventListener);

                subscriber.add(Subscriptions.create(new Action0() {
                    @Override
                    public void call() {
                        query.removeEventListener(valueEventListener);
                    }
                }));
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    public Single<String> observeSingleObjectKey(@NonNull final Query query) {
        return Single.create(new Single.OnSubscribe<String>() {
            @Override
            public void call(final SingleSubscriber<? super String> subscriber) {
                final ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            String key = null;
                            if (dataSnapshot.getChildrenCount() > 0)
                                key = dataSnapshot.getChildren().iterator().next().getKey();

                            if (!subscriber.isUnsubscribed())
                                subscriber.onSuccess(key);
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
                };

                query.addListenerForSingleValueEvent(valueEventListener);

                subscriber.add(Subscriptions.create(new Action0() {
                    @Override
                    public void call() {
                        query.removeEventListener(valueEventListener);
                    }
                }));
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    public <T> Observable<T> observeValue(@NonNull final Query query, @NonNull final Class<T> clazz) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(final Subscriber<? super T> subscriber) {
                final ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            T value = dataSnapshot.getValue(clazz);

                            if (!subscriber.isUnsubscribed())
                                subscriber.onNext(value);

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
                };

                query.addValueEventListener(valueEventListener);
                subscriber.add(Subscriptions.create(new Action0() {
                    @Override
                    public void call() {
                        query.removeEventListener(valueEventListener);
                    }
                }));
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    public <T> Observable<List<T>> observeValuesList(@NonNull final Query query, @NonNull final Class<T> clazz) {
        return Observable.create(new Observable.OnSubscribe<List<T>>() {

            @Override
            public void call(final Subscriber<? super List<T>> subscriber) {
                final ValueEventListener valueEventListener = getFBListValueEventListener(clazz, subscriber);
                query.addValueEventListener(valueEventListener);

                subscriber.add(Subscriptions.create(new Action0() {
                    @Override
                    public void call() {
                        query.removeEventListener(valueEventListener);
                    }
                }));
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    private <T> ValueEventListener getFBListValueEventListener(@NonNull final Class<T> clazz,
                                                               final Subscriber<? super List<T>> subscriber) {
        return new ValueEventListener() {
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
                if (!subscriber.isUnsubscribed())
                    subscriber.onNext(items);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                if (!subscriber.isUnsubscribed())
                    subscriber.onError(new Throwable(error.getMessage()));
            }
        };
    }

}
