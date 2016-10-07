package com.manpdev.appointment.data.remote.firebase;

import com.google.firebase.database.FirebaseDatabase;
import com.manpdev.appointment.data.model.AppointmentModel;
import com.manpdev.appointment.data.remote.DataProvider;
import com.manpdev.appointment.data.remote.firebase.base.FBAppointmentProvider;

import javax.inject.Inject;

import rx.Observable;

/**
 * novoa on 9/24/16.
 */

public class FBPAppointmentProvider extends FBAppointmentProvider implements DataProvider<AppointmentModel> {

    @Inject
    public FBPAppointmentProvider(FirebaseDatabase firebaseDatabase) {
        super(firebaseDatabase);
    }

    @Override
    public Observable<AppointmentModel> getCollectionObservable(String id) {
        return observeValue(firebaseDatabase.getReference()
                        .child(AppointmentModel.MODEL_ROOT_ID_PROVIDER)
                        .child(id),
                AppointmentModel.class);
    }
}
