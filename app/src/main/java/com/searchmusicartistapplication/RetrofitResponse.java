package com.searchmusicartistapplication;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RetrofitResponse {

    @SerializedName("artists")
    private ArtistDetails artistDetails;

    public ArtistDetails getArtistDetails() {
        return artistDetails;
    }

    public void setArtistDetails(ArtistDetails artistDetails) {
        this.artistDetails = artistDetails;
    }

    public class ArtistDetails {
        @SerializedName("artist")
        List<Artist> artistList;

        public List<Artist> getArtistList() {
            return artistList;
        }

        public void setArtistList(List<Artist> artistList) {
            this.artistList = artistList;
        }
    }
}
