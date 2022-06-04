package com.example.android.quakereport;

import java.util.Date;

public class Earthquake {
    private String where;
    private Double magnitude;
    private String date;

    public Earthquake(String where, Double magnitude, String date) {
        this.where = where;
        this.magnitude = magnitude;
        this.date = date;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public Double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(Double magnitude) {
        this.magnitude = magnitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
