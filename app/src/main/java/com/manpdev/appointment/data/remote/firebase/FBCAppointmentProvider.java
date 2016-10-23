package com.manpdev.appointment.data.remote.firebase;

import com.google.firebase.database.FirebaseDatabase;
import com.manpdev.appointment.data.model.AppointmentModel;
import com.manpdev.appointment.data.remote.firebase.base.FBAppointmentProvider;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * novoa on 9/24/16.
 */

public class FBCAppointmentProvider extends FBAppointmentProvider{

    @Inject
    public FBCAppointmentProvider(FirebaseDatabase firebaseDatabase) {
        super(firebaseDatabase);
    }

    @Override
    public Observable<List<AppointmentModel>> getCollectionObservable(String id) {
        return observeValuesList(firebaseDatabase.getReference()
                        .child(AppointmentModel.MODEL_ROOT_ID_CLIENT)
                        .child(id),
                AppointmentModel.class);
    }
}
