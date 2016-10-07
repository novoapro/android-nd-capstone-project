package com.manpdev.appointment.data.model;

import com.google.firebase.database.Exclude;

/**
 * novoa on 9/11/16.
 */

public class RatingModel {

    public static final String MODEL_ROOT_ID = "service-ratings";

    private String uId;
    private double count;
    private double total;

    public RatingModel() {
    }

    @Exclude
    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
