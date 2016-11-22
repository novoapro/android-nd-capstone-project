package com.manpdev.appointment.data.remote;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.List;

import rx.Observable;
import rx.Single;

/**
 * novoa on 9/24/16.
 */

public interface DatabaseProvider {

    DatabaseReference getReference();

    <T> Single<T> observeSingleValue(@NonNull final Task<T> task);

    @NonNull
    <T> Single<T> observeSingleValue(@NonNull final Query query, @NonNull final Class<T> clazz);

    @NonNull
    Single<String> observeSingleObjectKey(@NonNull final Query query);

    @NonNull
    <T> Observable<T> observeValue(@NonNull final Query query, @NonNull final Class<T> clazz);

    @NonNull
    <T> Observable<List<T>> observeValuesList(@NonNull final Query query, @NonNull final Class<T> clazz);
}
