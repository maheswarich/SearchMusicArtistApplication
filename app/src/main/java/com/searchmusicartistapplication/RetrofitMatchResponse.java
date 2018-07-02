package com.searchmusicartistapplication;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RetrofitMatchResponse {
    @SerializedName("results")
    private ResultMatches resultMatches;

    public ResultMatches getResultMatches() {
        return resultMatches;
    }

    public void setResultMatches(ResultMatches resultMatches) {
        this.resultMatches = resultMatches;
    }

    public class ResultMatches {
        @SerializedName("artistmatches")
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
}
