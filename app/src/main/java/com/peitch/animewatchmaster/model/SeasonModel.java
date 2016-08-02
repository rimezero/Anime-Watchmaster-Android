package com.peitch.animewatchmaster.model;

/**
 * Created by admin on 7/4/2016.
 */
public class SeasonModel {
    private int animeinfo_id;
    private String title;
    private String imgurl;
    private String animetype;
    private double rating;

    public SeasonModel() {
    }

    public SeasonModel(int animeinfo_id, String title, String imgurl, String animetype, double rating) {
        this.animeinfo_id = animeinfo_id;
        this.title = title;
        this.imgurl = imgurl;
        this.animetype = animetype;
        this.rating = rating;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getAnimeinfo_id() {
        return animeinfo_id;
    }

    public void setAnimeinfo_id(int animeinfo_id) {
        this.animeinfo_id = animeinfo_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getAnimetype() {
        return animetype;
    }

    public void setAnimetype(String animetype) {
        this.animetype = animetype;
    }
}
