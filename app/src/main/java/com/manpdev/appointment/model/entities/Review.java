package com.manpdev.appointment.model.entities;

/**
 * novoa on 9/11/16.
 */

public class Review {

    private String id;
    private String title;
    private String body;
    private float rating;

    public Review() {
    }

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
