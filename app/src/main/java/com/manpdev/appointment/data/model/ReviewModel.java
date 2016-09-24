package com.manpdev.appointment.data.model;

import com.google.firebase.database.Exclude;

/**
 * novoa on 9/11/16.
 */

public class ReviewModel{

    public static final String MODEL_ROOT_ID = "reviews";

    private String id;

    private String title;
    private String body;
    private float rating;

    public ReviewModel() {
    }

    @Exclude
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
