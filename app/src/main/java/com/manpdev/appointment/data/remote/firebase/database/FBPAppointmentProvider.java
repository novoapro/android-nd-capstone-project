package com.manpdev.appointment.data.remote.firebase.database;

import com.google.firebase.database.FirebaseDatabase;
import com.manpdev.appointment.data.model.AppointmentModel;
import com.manpdev.appointment.data.remote.firebase.database.base.FBAppointmentProvider;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * novoa on 9/24/16.
 */

public class FBPAppointmentProvider extends FBAppointmentProvider{

    @Inject
    public FBPAppointmentProvider(FirebaseDatabase firebaseDatabase) {
        super(firebaseDatabase);
    }

    @Override
    public Observable<List<AppointmentModel>> getCollectionObservable(String id) {
        return observeValuesList(firebaseDatabase.getReference()
                        .child(AppointmentModel.MODEL_ROOT_ID_PROVIDER)
                        .child(id),
                AppointmentModel.class);
    }
}
