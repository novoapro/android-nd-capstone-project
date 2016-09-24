package com.manpdev.appointment.data.model;

import com.google.firebase.database.Exclude;

/**
 * novoa on 9/11/16.
 */

public class UserModel{

    public static final String MODEL_ROOT_ID = "users";

    private String id;
    private String email;
    private String fullName;
    private String phone;
    private String avatar;

    public UserModel() {
    }

    public UserModel(String id) {
        this.id = id;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
