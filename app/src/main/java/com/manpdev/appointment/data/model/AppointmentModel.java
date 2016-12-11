package com.manpdev.appointment.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;

import com.google.firebase.database.Exclude;
import com.manpdev.appointment.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;

/**
 * novoa on 9/11/16.
 */
public class AppointmentModel implements Parcelable{

    public static final String MODEL_ROOT_ID_CLIENT = "client-appointments";
    public static final String MODEL_ROOT_ID_PROVIDER = "provider-appointments";

    private String cid;
    private String pid;
    private String client;
    private String provider;
    private long datetime;
    private int state;
    private String notes;

    private Date date;

    public AppointmentModel() {

    }

    protected AppointmentModel(Parcel in) {
        cid = in.readString();
        pid = in.readString();
        client = in.readString();
        provider = in.readString();
        datetime = in.readLong();
        state = in.readInt();
        notes = in.readString();
        setDate(new Date(in.readLong()));
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cid);
        dest.writeString(pid);
        dest.writeString(client);
        dest.writeString(provider);
        dest.writeLong(datetime);
        dest.writeInt(state);
        dest.writeString(notes);
        dest.writeLong(date.getTime());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AppointmentModel> CREATOR = new Creator<AppointmentModel>() {
        @Override
        public AppointmentModel createFromParcel(Parcel in) {
            return new AppointmentModel(in);
        }

        @Override
        public AppointmentModel[] newArray(int size) {
            return new AppointmentModel[size];
        }
    };

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Long getDatetime() {
        return datetime;
    }

    public void setDatetime(Long datetime) {
        this.datetime = datetime;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Exclude
    public String getStateString() {
        switch (this.state) {
            case REQUESTED:
                return "Requested";
            case ACCEPTED:
                return "Accepted";
            case DENIED:
                return "Denied";

            case COMPLETED:
                return "Completed";

            default:
                return "Unknown";
        }
    }

    @Exclude
    @DrawableRes
    public Integer getIconRes() {
        switch (state) {
            case AppointmentModel.COMPLETED:
                return R.drawable.ic_completed;

            case AppointmentModel.DENIED:
                return R.drawable.ic_denied;

            case AppointmentModel.ACCEPTED:
                return R.drawable.ic_accepted;

            case AppointmentModel.REQUESTED:
                return R.drawable.ic_requested;

            default:
                return R.drawable.ic_loading;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({REQUESTED, ACCEPTED, DENIED, COMPLETED})
    @interface StateInt {
    }

    public static final int REQUESTED = 0;
    public static final int ACCEPTED = 1;
    public static final int DENIED = 2;
    public static final int COMPLETED = 3;

}
