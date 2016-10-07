package com.manpdev.appointment.data.model;

import java.util.Date;

/**
 * novoa on 9/11/16.
 */
public class AppointmentModel {

    public static final String MODEL_ROOT_ID_CLIENT = "client-appointments";
    public static final String MODEL_ROOT_ID_PROVIDER = "provider-appointments";

    private String cId;
    private String pId;
    private String client;
    private String provider;
    private Date datetime;
    private int state;
    private String notes;

    public AppointmentModel() {

    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
