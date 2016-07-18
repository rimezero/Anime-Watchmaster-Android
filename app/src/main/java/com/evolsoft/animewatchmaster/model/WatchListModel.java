package com.evolsoft.animewatchmaster.model;

/**
 * Created by abraham on 10/6/2016.
 */
public class WatchListModel {


    private int id;
    private String title;
    private String imgurl;
    private int episodeswatched;
    private int currentEpisode;
    private String lastupdated;


    public WatchListModel() {

    }

    public WatchListModel(int id, String title, String imgurl, int currentEpisode, int episodeswatched, String lastupdated) {
        this.id = id;
        this.title = title;
        this.imgurl = imgurl;
        this.currentEpisode = currentEpisode;
        this.episodeswatched = episodeswatched;
        this.lastupdated = lastupdated;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCurrentEpisode() {
        return currentEpisode;
    }

    public void setCurrentEpisode(int currentEpisode) {
        this.currentEpisode = currentEpisode;
    }

    public int getEpisodeswatched() {
        return episodeswatched;
    }

    public void setEpisodeswatched(int episodeswatched) {
        this.episodeswatched = episodeswatched;
    }

    public String getLastupdated() {
        return lastupdated;
    }

    public void setLastupdated(String lastupdated) {
        this.lastupdated = lastupdated;
    }
}
