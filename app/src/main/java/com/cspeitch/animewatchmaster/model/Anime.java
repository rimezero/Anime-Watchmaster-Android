package com.cspeitch.animewatchmaster.model;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by admin on 4/11/2016.
 */
public class Anime implements Serializable,Comparable {

    private int id;
    private String title;
    private String imgurl;
    private String genre;
    private String episodes;
    private String animetype;
    private String agerating;
    private String description;


    public Anime(int id, String title, String imgurl, String genre, String episodes, String animetype, String agerating, String description) {
        this.id = id;
        this.title = title;
        this.imgurl = imgurl;
        this.genre = genre;
        this.episodes = episodes;
        this.animetype = animetype;
        this.agerating = agerating;
        this.description = description;
    }

    @Override
    public int compareTo(Object another) {
        try {
            return this.title.toLowerCase().compareTo(((Anime) another).getTitle().toLowerCase());
        }catch (ClassCastException ex){
            Log.e("Anime - compareTo"," Cannot cast object to Anime");
            ex.printStackTrace();
        }
        return 0;
    }

    public Anime() {}

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getGenre() {
        return genre;
    }

    public String getEpisodes() {
        return episodes;
    }

    public String getAnimetype() {
        return animetype;
    }

    public String getAgerating() {
        return agerating;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setEpisodes(String episodes) {
        this.episodes = episodes;
    }

    public void setAnimetype(String animetype) {
        this.animetype = animetype;
    }

    public void setAgerating(String agerating) {
        this.agerating = agerating;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        return id;
    }


    @Override
    public boolean equals(Object ob) {
        if(ob == null) {
            return false;
        }
        if(!(ob instanceof Anime)) {
            return false;
        }

        return ((Anime)ob).getId() == this.id;
    }

    @Override
    public String toString() {
        return "Anime{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", genre='" + genre + '\'' +
                ", episodes=" + episodes +
                ", animetype='" + animetype + '\'' +
                ", agerating='" + agerating + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
