package com.ngdat.studymore.models.item;

import java.io.Serializable;

public class ItemPost implements Serializable {

    private String userID;
    private String userKey;
    private String userName;
    private String postID;

    private String location;
    private String phoneNumber;

    private String titlePost;
    private String des;
    private String fee;
    private int day;
    private int year;

    private boolean isCheck;

    public ItemPost() {
    }

    public ItemPost(String userID,
                    String userKey,
                    String userName,
                    String postID,
                    String location,
                    String phoneNumber,
                    String titlePost,
                    String des,
                    String fee,
                    int day,
                    int year) {
        this.userID = userID;
        this.userKey = userKey;
        this.userName = userName;
        this.postID = postID;
        this.location = location;
        this.phoneNumber = phoneNumber;
        this.titlePost = titlePost;
        this.des = des;
        this.fee = fee;
        this.day = day;
        this.year = year;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTitlePost() {
        return titlePost;
    }

    public void setTitlePost(String titlePost) {
        this.titlePost = titlePost;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean getCheck() {
        return isCheck;
    }

    public void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }
}