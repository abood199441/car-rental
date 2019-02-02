package com.example.abdelrahmanapps.carrentaltrial.models;

import java.util.Calendar;

public class BookingRequest {


    private Calendar start;
    private Calendar end;
    private String userName;
    private String userId;
    private String userPhone;
    private boolean status;

    public BookingRequest(Calendar start, Calendar end, String userName, String userId, String userPhone) {
        this.start = start;
        this.end = end;
        this.userName = userName;
        this.userId = userId;
        this.userPhone = userPhone;
    }
    public BookingRequest(){
        //for firebase.
    }

    public Calendar getStart() {
        return start;
    }

    public void setStart(Calendar start) {
        this.start = start;
    }

    public Calendar getEnd() {
        return end;
    }

    public void setEnd(Calendar end) {
        this.end = end;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
