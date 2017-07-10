package com.ngdat.studymore.models.item;

import java.io.Serializable;

public class ItemNote implements Serializable {
    private String titlePost;
    private String des;
    private String id;

    private int day;
    private int year;

    public ItemNote() {
    }

    public ItemNote(String titlePost, String des, String id, int day, int year) {
        this.titlePost = titlePost;
        this.des = des;
        this.id = id;
        this.day = day;
        this.year = year;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}