package com.cspeitch.animewatchmaster.model;

/**
 * Created by admin on 7/1/2016.
 */
public class TopanimeModel {
    private int spot;
    private int id;
    private double score;
    private String title;
    private String imgurl;

    public TopanimeModel(int spot, int id, double score, String title, String imgurl) {
        this.spot = spot;
        this.id = id;
        this.score = score;
        this.title = title;
        this.imgurl = imgurl;
    }

    public TopanimeModel() {
    }

    public int getSpot() {
        return spot;
    }

    public void setSpot(int spot) {
        this.spot = spot;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
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
}
