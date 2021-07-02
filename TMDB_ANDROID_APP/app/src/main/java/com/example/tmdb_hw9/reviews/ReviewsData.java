package com.example.tmdb_hw9.reviews;

public class ReviewsData {
    private String rating;
    private String username;
    private String content;
    private String created_date;

    public ReviewsData(String rating, String username, String content, String created_date) {
        this.rating = rating;
        this.username = username;
        this.content = content;
        this.created_date = created_date;
    }

    // getters

    public String getRating() {
        return rating;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public String getCreatedDate() {
        return created_date;
    }

    // setters
    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreatedDate(String rating) {
        this.created_date = created_date;
    }

}

