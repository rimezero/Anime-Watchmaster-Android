package com.example.admin.animewatchmaster.model;

/**
 * Created by admin on 6/29/2016.
 */
public class WatchedModel {
    private int id;
    private String title;
    private String imgurl;
    private String genre;

    public WatchedModel(){}

    public WatchedModel(int id, String title, String imgurl,String genre) {
        this.id = id;
        this.title = title;
        this.imgurl = imgurl;
        this.genre=genre;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}