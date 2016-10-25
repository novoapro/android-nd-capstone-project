package com.manpdev.appointment.ui.adapter;

import com.manpdev.appointment.data.model.AppointmentModel;

/**
 * novoa on 10/24/16.
 */

public interface ProviderAppointmentItemListener {
    void onCalendar(AppointmentModel model);
    void onEdit(AppointmentModel model);
}
