package com.example.admin.animewatchmaster.model;

/**
 * Created by abraham on 10/6/2016.
 */
public class WatchListModel {


    private int id;
    private int currentEpisode;
    private int lastEpisode;
    private String date;


    public WatchListModel() {

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

    public int getLastEpisode() {
        return lastEpisode;
    }

    public void setLastEpisode(int lastEpisode) {
        this.lastEpisode = lastEpisode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
