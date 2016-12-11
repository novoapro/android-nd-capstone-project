package com.manpdev.appointment.data.remote.firebase.database;

import android.support.annotation.NonNull;

import com.manpdev.appointment.data.model.AppointmentModel;
import com.manpdev.appointment.data.remote.DatabaseProvider;
import com.manpdev.appointment.data.remote.firebase.database.base.FBAppointmentProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;

/**
 * novoa on 9/24/16.
 */

public class FBCAppointmentProvider extends FBAppointmentProvider{

    @Inject
    public FBCAppointmentProvider(DatabaseProvider databaseProvider) {
        super(databaseProvider);
    }

    @Override
    public Observable<List<AppointmentModel>> getCollectionObservable(String id) {
        return mDatabaseProvider.observeValuesList(mDatabaseProvider.getReference()
                        .child(AppointmentModel.MODEL_ROOT_ID_CLIENT)
                        .child(id).orderByKey(),
                AppointmentModel.class);
    }
}
