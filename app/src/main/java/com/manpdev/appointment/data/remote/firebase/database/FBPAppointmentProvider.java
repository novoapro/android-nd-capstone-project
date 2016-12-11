package com.manpdev.appointment.data.remote.firebase.database;

import com.manpdev.appointment.data.model.AppointmentModel;
import com.manpdev.appointment.data.remote.DatabaseProvider;
import com.manpdev.appointment.data.remote.firebase.database.base.FBAppointmentProvider;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * novoa on 9/24/16.
 */

public class FBPAppointmentProvider extends FBAppointmentProvider{

    @Inject
    public FBPAppointmentProvider(DatabaseProvider databaseProvider) {
        super(databaseProvider);
    }

    @Override
    public Observable<List<AppointmentModel>> getCollectionObservable(String id) {
        return mDatabaseProvider.observeValuesList(mDatabaseProvider.getReference()
                        .child(AppointmentModel.MODEL_ROOT_ID_PROVIDER)
                        .child(id).orderByKey(),
                AppointmentModel.class);
    }
}
