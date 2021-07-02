package com.example.tmdb_hw9.sliders;

public class SliderData {

    private String imgUrl;
    private String title;
    private String id;
    private String mediaType;

    public SliderData(String id, String imgUrl, String title, String mediaType) {
        this.id = id;
        this.title = title;
        this.imgUrl = imgUrl;
        this.mediaType = mediaType;
    }

    // GETTERS
    public String getImgUrl() {
        return imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getMedia() {
        return mediaType;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMedia(String mediaType) {
        this.mediaType = mediaType;
    }
}
