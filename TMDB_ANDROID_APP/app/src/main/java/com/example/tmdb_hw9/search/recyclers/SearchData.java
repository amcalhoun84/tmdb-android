package com.example.tmdb_hw9.search.recyclers;

public class SearchData {
    private String img;
    private String title;
    private String id;
    private String rating;
    private String release_date;
    private String media_type;

    public SearchData(
            String id,
            String img,
            String title,
            String rating,
            String releaseDate,
            String mediaType
    ) {
        this.id = id;
        this.title = title;
        this.img = img;
        this.rating = rating;
        this.release_date = releaseDate;
        this.media_type = mediaType;
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

    public String getMediaType() {
        return media_type;
    }

    public String getReleaseDate() {
        if (release_date.equals("")) {
            return "N/A";
        }
        return release_date;
    }

    public String getRating() {
        return rating;
    }

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

    public void setMediaType(String mediaType) {

        this.media_type = mediaType;
    }

    public void setReleaseDate(String releaseDate) {

        this.release_date = releaseDate;
    }

    public void setRating(String rating) {

        this.rating = rating;
    }
}
