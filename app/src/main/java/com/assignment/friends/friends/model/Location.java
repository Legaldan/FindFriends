package com.assignment.friends.friends.model;

import java.util.Date;

/**
 * Created by Li Yan on 2017-05-06.
 */

public class Location {
    private int id;
    private String locName;
    private String latitude;
    private String longitude;
    private Date date;
    private int stuId;

    public Location(int id, String locName, String latitude, String longitude, Date date,int stuId) {
        this.id = id;
        this.locName = locName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.stuId = stuId;
    }

    public int getId() {
        return id;
    }

    public String getLocName() {
        return locName;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public Date getDate() {
        return date;
    }

    public int getStuId() {
        return stuId;
    }
}
