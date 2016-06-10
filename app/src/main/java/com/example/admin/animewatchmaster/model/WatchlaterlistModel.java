package com.example.admin.animewatchmaster.model;

/**
 * Created by admin on 6/10/2016.
 */
public class WatchlaterlistModel {
    int id;
    String title;

    public WatchlaterlistModel(int id, String title) {
        this.id = id;
        this.title = title;
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
