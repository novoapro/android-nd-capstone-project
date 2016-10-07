package com.manpdev.appointment.data.model;

import android.util.ArrayMap;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * novoa on 9/11/16.
 */

public class ClientProviderModel {

    public static final String MODEL_ROOT_ID = "client-providers";

    private String uId;
    private Map<String, Boolean> subscriptions;


    public ClientProviderModel() {
        subscriptions = new HashMap<>();
    }

    @Exclude
    public String getuId() {
        return uId;
    }

    @Exclude
    public Map<String, Boolean> getSubscriptions() {
        return subscriptions;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public void addSubscription(String providerId){
        subscriptions.put(providerId, false);
    }

    public void removeSubscription(String providerId){
        subscriptions.remove(providerId);
    }
}
