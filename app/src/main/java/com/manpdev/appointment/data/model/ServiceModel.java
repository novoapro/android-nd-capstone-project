package com.manpdev.appointment.data.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * novoa on 9/11/16.
 */

public class ServiceModel{
    
    public static final String MODEL_ROOT_ID = "user-service";

    private String uId;
    private String name;
    private String type;
    private String address;
    private String description;
    private String banner;
    private String phone;
    private Map<String, Object> mInstanceMap;

    public ServiceModel() {
    }

    @Exclude
    public String getuId() {
        return uId;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    @Exclude
    public Map<String, Object> getMap(){
        if(mInstanceMap == null)
            mInstanceMap = new HashMap<>();

        mInstanceMap.clear();
        mInstanceMap.put("name", name);
        mInstanceMap.put("type", type);
        mInstanceMap.put("address", address);
        mInstanceMap.put("phone", phone);
        mInstanceMap.put("description", description);
        mInstanceMap.put("banner", banner);
        return mInstanceMap;
    }
}
