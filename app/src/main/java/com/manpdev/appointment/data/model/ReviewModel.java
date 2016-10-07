package com.manpdev.appointment.data.model;

import com.google.firebase.database.Exclude;

import java.util.Date;

/**
 * novoa on 9/11/16.
 */

public class ReviewModel{

    public static final String MODEL_ROOT_ID = "service-reviews";

    private String id;
    private String uId;
    private String author;
    private Date datetime;
    private float rating;
    private String review;

    public ReviewModel() {
    }

    public String getAuthor() {
        return author;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Exclude
    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
