package com.essejose.artederua.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jose on 02/09/2017.
 */

public class Event implements Parcelable {

    private Integer _id;
    private Integer _id_user;
    private String title;
    private String descripion;
    private String image;
    private Double latiude;
    private Double longitude;

    public Event(){

    }

    protected Event(Parcel in) {
        _id = in.readInt();
        _id_user = in.readInt();
        title = in.readString();
        descripion = in.readString();
        image = in.readString();
        longitude = in.readDouble();
        latiude = in.readDouble();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatiude() {
        return latiude;
    }

    public void setLatiude(Double latiude) {
        this.latiude = latiude;
    }

    public String getDescripion() {
        return descripion;
    }

    public void setDescripion(String descripion) {
        this.descripion = descripion;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer get_id_user() {
        return _id_user;
    }

    public void set_id_user(Integer _id_user) {
        this._id_user = _id_user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeInt(_id_user);
        dest.writeString(title);
        dest.writeString(descripion);
        dest.writeString(image);
        dest.writeDouble(longitude);
        dest.writeDouble(latiude);

    }
}
