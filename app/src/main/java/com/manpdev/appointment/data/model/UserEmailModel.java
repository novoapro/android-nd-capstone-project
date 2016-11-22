package com.manpdev.appointment.data.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * novoa on 9/11/16.
 */

public class UserEmailModel {

    public static final String MODEL_ROOT_ID = "user-email";

    private String uId;
    private String email;

    public UserEmailModel() {
    }

    @Exclude
    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    @Exclude
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
