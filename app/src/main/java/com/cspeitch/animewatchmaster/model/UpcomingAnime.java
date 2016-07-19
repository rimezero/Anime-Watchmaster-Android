package com.cspeitch.animewatchmaster.model;

import java.io.Serializable;

/**
 * Created by admin on 7/19/2016.
 */
public class UpcomingAnime implements Serializable {

    private int id;
    private String imageurl;
    private String type;
    private String desc;
    private String link;
    private String title;
    private String genres;

    public UpcomingAnime() {}

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
