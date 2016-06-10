package com.example.admin.animewatchmaster.model;

/**
 * Created by admin on 6/10/2016.
 */
public class WatchlaterlistModel {
    int id;
    String title;
    String imgurl;

    public WatchlaterlistModel(){}

    public WatchlaterlistModel(int id, String title, String imgurl) {
        this.id = id;
        this.title = title;
        this.imgurl = imgurl;
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
