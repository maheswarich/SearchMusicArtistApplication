package com.searchmusicartistapplication;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitInterface {
    @FormUrlEncoded
    @POST("/2.0/?")
    Call<RetrofitResponse> getJSON(@Field("method") String method, @Field("api_key") String api_key, @Field("format") String format);

    @FormUrlEncoded
    @POST("/2.0/?")
    Call<RetrofitMatchResponse> getJsonMatchedResults(@Field("method") String method, @Field("artist") String artist, @Field("api_key") String api_key, @Field("format") String format);
}
