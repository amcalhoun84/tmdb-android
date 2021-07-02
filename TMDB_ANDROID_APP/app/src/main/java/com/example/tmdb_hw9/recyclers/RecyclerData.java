package com.example.tmdb_hw9.recyclers;

public class RecyclerData {
    private String img;
    private String title;
    private String id;
    private String media;

    public RecyclerData(String id, String img, String title, String media) {
        this.id = id;
        this.title = title;
        this.img = img;
        this.media = media;
    }

    // GETTERS
    public String getImgUrl() {
        return img;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getMedia() { return media; }

    // SETTERS
    public void setImgUrl(String imgUrl) {
        this.img = img;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMedia() {
        this.media = media;
    }

}
