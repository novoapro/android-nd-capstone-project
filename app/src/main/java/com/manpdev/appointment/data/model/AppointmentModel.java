package com.manpdev.appointment.data.model;

import com.google.firebase.database.Exclude;

import java.util.Date;

/**
 * novoa on 9/11/16.
 */

public class AppointmentModel {

    public static final String MODEL_ROOT_ID = "appointments";

    private String id;

    private Date datetime;
    private int duration;
    private int state;

    private String serviceName;
    private String serviceId;

    private String clientId;
    private String clientName;

    public AppointmentModel() {
    }


    @Exclude
    public String getId() {
        return id;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
