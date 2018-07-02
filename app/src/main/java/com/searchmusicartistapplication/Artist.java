package com.searchmusicartistapplication;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Artist {
    @SerializedName("image")
    ArrayList<AtristImage> artsitImage;
    @SerializedName("name")
    private String name;
    @SerializedName("listeners")
    private String playcount;
    @SerializedName("url")
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaycount() {
        return playcount;
    }

    public void setPlaycount(String playcount) {
        this.playcount = playcount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<AtristImage> getArtsitImage() {
        return artsitImage;
    }

    public void setArtsitImage(ArrayList<AtristImage> artsitImage) {
        this.artsitImage = artsitImage;
    }

    public class AtristImage {
        @SerializedName("#text")
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
