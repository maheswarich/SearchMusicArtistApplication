package com.searchmusicartistapplication.HttpCalls;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

/**
 * Created by Phaneendra on 5/25/2016.
 */
public class AsyncRequest extends AsyncTask<String, Integer, String> {
    OnAsyncRequestComplete caller;
    Activity context;
    String method = "GET";
    JSONObject postJsonObject;
    ProgressDialog pDialog = null;
    String response = "";
    String artistURL;
    String flagTypeValue;

    // Three Constructors
    public AsyncRequest(OnAsyncRequestComplete requestComplete, Activity activity, String method, String url, JSONObject jsonObject, String flagType) {
        caller = requestComplete;
        this.context = activity;
        this.method = method;
        this.artistURL = url;
        this.postJsonObject = jsonObject;
        this.flagTypeValue = flagType;
    }

    /*** GET CALL Constructor ***/
    public AsyncRequest(OnAsyncRequestComplete requestComplete, Activity activity, String method, String url, String flagType) {
        //caller = (OnAsyncRequestComplete) activity;
        this.caller = requestComplete;
        this.context = activity;
        this.method = method;
        this.artistURL = url;
        this.flagTypeValue = flagType;
    }
    // Interface to be implemented by calling activity
    public interface OnAsyncRequestComplete {
        public void asyncResponse(String response, String flagType);
    }

    public String doInBackground(String... urls) {
        // get url pointing to entry point of API

        //String address = urls[0].toString();
        if (method == "POST")
        {
            try {
                System.out.println("POST_URL = "+artistURL);
                response = ServiceHandler.processPostRequest(artistURL,postJsonObject);
            } catch (Exception e)
            {
                e.printStackTrace();
                Log.e("TAG", "doInBackground:AsyncTask "+e );
            }
            return response;
        }

        if (method == "GET") {
            System.out.println("GET_URL = "+artistURL);
            response = ServiceHandler.processGetRequest(artistURL,30000);
            return response;
        }

        return null;
    }

    public void onPreExecute() {
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading.\nPlease wait...");
        pDialog.setCancelable(false);
        pDialog.show();

    }


    public void onProgressUpdate(Integer... progress) {
        // you can implement some progressBar and update it in this record
        // setProgressPercent(progress[0]);
    }

    public void onPostExecute(String response)
    {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
        caller.asyncResponse(response,flagTypeValue);
    }
    protected void onCancelled(String response) {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
        caller.asyncResponse(response,flagTypeValue);
    }
}