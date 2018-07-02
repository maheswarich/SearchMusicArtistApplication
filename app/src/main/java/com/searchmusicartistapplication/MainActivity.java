package com.searchmusicartistapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.searchmusicartistapplication.HttpCalls.AsyncRequest;
import com.searchmusicartistapplication.HttpCalls.ServiceCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements AsyncRequest.OnAsyncRequestComplete {
    EditText searchText;
    ListView artsitList,searchList;
    ArrayList<ArtistInfo> totalArtistList;
    ArrayList<ArtistInfo> searchArtsiList;
    ArtistAdapter totalAtristAdapter = null,searchArtistAdapter = null;
    Button searchArtistBtn;
    Retrofit retrofit;
    RetrofitInterface service;

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initalizeView();
        totalArtistList = new ArrayList<>();
        // getArtistInformation();
        getRetrofitObject();
         hideSoftKeyboard();
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count==0)
                {
                    hideSoftKeyboard();
                    searchList.setVisibility(View.GONE);
                    artsitList.setVisibility(View.VISIBLE);
                    //  totalAtristAdapter = new ArtistAdapter(MainActivity.this,totalArtistList);
                    //  artsitList.setAdapter(totalAtristAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchArtistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchText.getText().toString().length()>0)
                {
                    if(isOnline(MainActivity.this)) {
                        hideSoftKeyboard();
                        //  String searchUrl = ServiceCall.BASE_URL+"method=artist.search&artist="+searchText.getText().toString()+"&api_key="+ServiceCall.MUSIC_API_KEY+"&format=json";
                        //  AsyncRequest asyncRequest = new AsyncRequest(MainActivity.this, MainActivity.this, "GET", searchUrl, "SEARCH ALL ARTISTS");
                        //  asyncRequest.execute();
                        getMatchedArtistDetails(searchText.getText().toString());

                    }
                    else
                    {
                        Toast.makeText(MainActivity.this,"Please check your internet connection here",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Please enter artist name to search",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getMatchedArtistDetails(String artistname) {
        Call<RetrofitMatchResponse> call = service.getJsonMatchedResults("artist.search", artistname, ServiceCall.MUSIC_API_KEY, "json");
        call.enqueue(new Callback<RetrofitMatchResponse>() {
            @Override
            public void onResponse(Call<RetrofitMatchResponse> call, Response<RetrofitMatchResponse> response) {
                List<Artist> matchedList = response.body().getResultMatches().getArtistDetails().getArtistList();
                searchList.setVisibility(View.VISIBLE);
                artsitList.setVisibility(View.GONE);
                RetrofitInfoAdapter totalRetrofitAdapter = new RetrofitInfoAdapter(MainActivity.this, matchedList);
                searchList.setAdapter(totalRetrofitAdapter);
            }

            @Override
            public void onFailure(Call<RetrofitMatchResponse> call, Throwable t) {

            }
        });

    }

    private void getArtistInformation() {
        String artistUrl = ServiceCall.BASE_URL+"method=chart.gettopartists&api_key="+ServiceCall.MUSIC_API_KEY+"&format=json";
        if(isOnline(MainActivity.this)) {
            AsyncRequest asyncRequest = new AsyncRequest(MainActivity.this, MainActivity.this, "GET", artistUrl, "GET ALL ARTISTS");
            asyncRequest.execute();
        }
        else
        {
            Toast.makeText(MainActivity.this,"Please check your internet connection here",Toast.LENGTH_SHORT).show();
        }
    }

    private void getRetrofitObject() {
        retrofit = new Retrofit.Builder()
                .baseUrl(ServiceCall.BASE_URL1)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(RetrofitInterface.class);
        Call<RetrofitResponse> call = service.getJSON("chart.gettopartists", ServiceCall.MUSIC_API_KEY, "json");
        call.enqueue(new Callback<RetrofitResponse>() {
            @Override
            public void onResponse(Call<RetrofitResponse> call, Response<RetrofitResponse> response) {
                List<Artist> artsitlist = response.body().getArtistDetails().getArtistList();
                searchList.setVisibility(View.GONE);
                artsitList.setVisibility(View.VISIBLE);
                RetrofitInfoAdapter totalRetrofitAdapter = new RetrofitInfoAdapter(MainActivity.this, artsitlist);
                artsitList.setAdapter(totalRetrofitAdapter);

            }

            @Override
            public void onFailure(Call<RetrofitResponse> call, Throwable t) {
                Log.e("MainActivity ", "  error " + t.toString());

            }
        });
    }
    @Override
    public void asyncResponse(String response, String flagType) {
     System.out.println("artsit response is..."+response);

     switch (flagType)
     {
         case "GET ALL ARTISTS":
             try {
                 JSONObject resultObj = new JSONObject(response);
                 JSONObject artistObj = resultObj.getJSONObject("artists");
                 JSONArray resultArray = artistObj.getJSONArray("artist");
                 for (int i = 0;i<resultArray.length();i++)
                 {
                     JSONObject totalInfoObj = resultArray.getJSONObject(i);
                     String artistName = totalInfoObj.getString("name");
                     String artistUrl = totalInfoObj.getString("url");
                     String artistCount = totalInfoObj.getString("listeners");
                     JSONArray imagesArray = totalInfoObj.getJSONArray("image");
                     JSONObject imageObj = imagesArray.getJSONObject(0);
                     String artistIcon = imageObj.getString("#text");

                     ArtistInfo artistInfo = new ArtistInfo();
                     artistInfo.setName(artistName);
                     artistInfo.setUrl(artistUrl);
                     artistInfo.setPlaycount(artistCount);
                     artistInfo.setArtsitImage(artistIcon);
                     totalArtistList.add(artistInfo);
                 }
                 searchList.setVisibility(View.GONE);
                 artsitList.setVisibility(View.VISIBLE);
                 totalAtristAdapter = new ArtistAdapter(MainActivity.this,totalArtistList);
                 artsitList.setAdapter(totalAtristAdapter);
             }
             catch (JSONException je)
             {

             }
             break;
         case "SEARCH ALL ARTISTS":
             try {
                 searchArtsiList = new ArrayList<>();
                 JSONObject resultObj = new JSONObject(response);
                 JSONObject artistObj = resultObj.getJSONObject("results");
                 JSONObject searchObj = artistObj.getJSONObject("artistmatches");
                 JSONArray resultArray = searchObj.getJSONArray("artist");
                 for (int i = 0;i<resultArray.length();i++)
                 {
                     JSONObject totalInfoObj = resultArray.getJSONObject(i);
                     String artistName = totalInfoObj.getString("name");
                     String artistUrl = totalInfoObj.getString("url");
                     String artistCount = totalInfoObj.getString("listeners");
                     JSONArray imagesArray = totalInfoObj.getJSONArray("image");
                     JSONObject imageObj = imagesArray.getJSONObject(0);
                     String artistIcon = imageObj.getString("#text");

                     ArtistInfo artistInfo = new ArtistInfo();
                     artistInfo.setName(artistName);
                     artistInfo.setUrl(artistUrl);
                     artistInfo.setPlaycount(artistCount);
                     artistInfo.setArtsitImage(artistIcon);
                     searchArtsiList.add(artistInfo);
                 }
                 searchList.setVisibility(View.VISIBLE);
                 artsitList.setVisibility(View.GONE);
                 searchArtistAdapter = new ArtistAdapter(MainActivity.this,searchArtsiList);
                 searchList.setAdapter(searchArtistAdapter);
             }
             catch (JSONException je)
             {
             }
             break;
             default:
                 break;

     }

    }

    private void initalizeView() {
        searchText = findViewById(R.id.searchArtistText);
        artsitList = findViewById(R.id.artistList);
        searchList = findViewById(R.id.searchList);
        searchArtistBtn = findViewById(R.id.searchArtistBtn);
    }
    public void hideSoftKeyboard() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }
}
