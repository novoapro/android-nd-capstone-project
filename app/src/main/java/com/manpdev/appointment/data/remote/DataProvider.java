package com.manpdev.appointment.data.remote;

import android.support.annotation.NonNull;

import rx.Observable;
import rx.Single;

/**
 * novoa on 9/24/16.
 */

public interface DataProvider<T> {
    Single<T> getSingleValueObservable(String id);
    Observable<T> getCollectionObservable(String id);

    Single<Void> insert(@NonNull T element);
    Single<Void> update(@NonNull T element);
    Single<Void> remove(@NonNull T element);
}
