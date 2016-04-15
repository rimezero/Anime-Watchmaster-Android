package com.example.admin.animewatchmaster.model;

/**
 * Created by admin on 4/11/2016.
 */
public class Anime {
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
