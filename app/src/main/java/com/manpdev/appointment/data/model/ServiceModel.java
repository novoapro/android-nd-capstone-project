package com.manpdev.appointment.data.model;

import com.google.firebase.database.Exclude;

/**
 * novoa on 9/11/16.
 */

public class ServiceModel{
    
    public static final String MODEL_ROOT_ID = "services";

    private String id;

    private String name;
    private String type;
    private String description;
    private String email;
    private String phone;

    public ServiceModel() {
    }

    @Exclude
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
