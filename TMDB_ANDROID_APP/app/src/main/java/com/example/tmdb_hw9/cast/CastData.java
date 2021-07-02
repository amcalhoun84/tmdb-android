package com.example.tmdb_hw9.cast;

import java.lang.reflect.Array;

public class CastData {
    private String name;
    private String img;

    public CastData(String name, String img) {
        this.name = name;
        this.img = img;
    }

    // GETTERS
    public String getImgUrl() {
        return img;
    }

    public String getName() {
        return name;
    }

    // SETTERS
    public void setImgUrl (String img) {
        this.img = img;
    }

    public void setName(String title) {
        this.name = title;
    }

}
