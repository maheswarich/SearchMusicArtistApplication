package com.searchmusicartistapplication;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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

public class MainActivity extends AppCompatActivity implements AsyncRequest.OnAsyncRequestComplete {
    EditText searchText;
    ListView artsitList,searchList;
    ArrayList<ArtistInfo> totalArtistList;
    ArrayList<ArtistInfo> searchArtsiList;
    ArtistAdapter totalAtristAdapter = null,searchArtistAdapter = null;
    Button searchArtistBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initalizeView();
        totalArtistList = new ArrayList<>();
        getArtistInformation();
         hideSoftKeyboard();
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            System.out.println("edittext before called");
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count==0)
                {
                    hideSoftKeyboard();
                    searchList.setVisibility(View.GONE);
                    artsitList.setVisibility(View.VISIBLE);
                    totalAtristAdapter = new ArtistAdapter(MainActivity.this,totalArtistList);
                    artsitList.setAdapter(totalAtristAdapter);
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
                        String searchUrl = ServiceCall.BASE_URL+"method=artist.search&artist="+searchText.getText().toString()+"&api_key="+ServiceCall.MUSIC_API_KEY+"&format=json";
                        AsyncRequest asyncRequest = new AsyncRequest(MainActivity.this, MainActivity.this, "GET", searchUrl, "SEARCH ALL ARTISTS");
                        asyncRequest.execute();

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
    private void initalizeView() {
        searchText = (EditText)findViewById(R.id.searchArtistText);
        artsitList = (ListView)findViewById(R.id.artistList);
        searchList = (ListView)findViewById(R.id.searchList);
        searchArtistBtn = (Button) findViewById(R.id.searchArtistBtn);
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
    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }
    public void hideSoftKeyboard() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }
}
