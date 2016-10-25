package com.manpdev.appointment.data.model;

import com.google.firebase.database.Exclude;

import java.util.Date;

/**
 * novoa on 9/11/16.
 */

public class ReviewModel{

    public static final String MODEL_ROOT_ID = "service-reviews";

    private String id;
    private String uid;
    private String author;
    private Long datetime;
    private Date date;
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
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Long getDatetime() {
        return datetime;
    }

    public void setDatetime(Long datetime) {
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

    @Exclude
    public Date getDate() {
        if (date == null)
            date = new Date(datetime);

        date.setTime(datetime);
        return date;
    }

    @Exclude
    public void setDate(Date date) {
        this.datetime = date.getTime();
        this.date = date;
    }
}
