package com.manpdev.appointment.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.Date;

/**
 * novoa on 9/11/16.
 */

public class ReviewModel implements Parcelable{

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

    protected ReviewModel(Parcel in) {
        id = in.readString();
        uid = in.readString();
        author = in.readString();
        rating = in.readFloat();
        review = in.readString();
        setDate(new Date(in.readLong()));
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(uid);
        dest.writeString(author);
        dest.writeFloat(rating);
        dest.writeString(review);
        dest.writeLong(date.getTime());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ReviewModel> CREATOR = new Creator<ReviewModel>() {
        @Override
        public ReviewModel createFromParcel(Parcel in) {
            return new ReviewModel(in);
        }

        @Override
        public ReviewModel[] newArray(int size) {
            return new ReviewModel[size];
        }
    };

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
