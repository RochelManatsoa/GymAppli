package com.example.gymnastiqueapp.Models;

public class Events {

    String EVENTS, TIME, DATE, MONTH, YEAR;

    public Events(String EVENTS, String TIME, String DATE, String MONTH, String YEAR) {
        this.EVENTS = EVENTS;
        this.TIME = TIME;
        this.DATE = DATE;
        this.MONTH = MONTH;
        this.YEAR = YEAR;
    }

    public String getEVENTS() {
        return EVENTS;
    }

    public void setEVENTS(String EVENTS) {
        this.EVENTS = EVENTS;
    }

    public String getTIME() {
        return TIME;
    }

    public void setTIME(String TIME) {
        this.TIME = TIME;
    }

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    public String getMONTH() {
        return MONTH;
    }

    public void setMONTH(String MONTH) {
        this.MONTH = MONTH;
    }

    public String getYEAR() {
        return YEAR;
    }

    public void setYEAR(String YEAR) {
        this.YEAR = YEAR;
    }
}
