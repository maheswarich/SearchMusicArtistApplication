package com.searchmusicartistapplication.HttpCalls;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class ServiceHandler
{
static Context context;
    /**
     * Generic method to execute all the IO operations, helps to connect to
     * given url and get response.
     *
     * @param url url to be connected.
     * @return Raw response from server String
     */

    public static String processGetRequest(String url, int timeout)
    {
        HttpURLConnection urlConnection = null;
        String results = null;
        try
        {
            URL urlCopy = new URL(url); //Assuming the given path, this can be anything
            urlConnection =  (HttpURLConnection) urlCopy.openConnection();  //Open the connection with the DB

            /*** Online Authorization **/
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setUseCaches(false);
            urlConnection.setAllowUserInteraction(false);
            urlConnection.setConnectTimeout(timeout);
            urlConnection.setReadTimeout(timeout);
            urlConnection.connect();
            int status = urlConnection.getResponseCode();

            switch (status)
            {

                case 201:
                case 200:
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null)
                    {
                        sb.append(line+"\n");
                    }
                    br.close();
                    Log.e("ServiceResponse= ", sb.toString());
                    return sb.toString();

            }
        }
        catch (MalformedURLException ex)
        {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                try {
                    urlConnection.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return "";
    }


    public static String postLoginCall(String postCallUrl, HashMap<String, String> postDataParams) throws Exception {

        String response = "";
        URL url = new URL(postCallUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("charset", "utf-8");
        connection.setUseCaches(false);

        DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
        wr.writeBytes(getPostDataString(postDataParams));
        wr.flush();
        System.out.println("Sending Json Params="+postDataParams.toString());
        int responseCode=connection.getResponseCode();

        if (responseCode == HttpsURLConnection.HTTP_OK) {
            String line;
            BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line=br.readLine()) != null) {
                response+=line;
            }
        }
        else {
            response="";
        }
        wr.close();
        return response;
    }

    public static String processPostRequest(String postCallUrl,JSONObject jsonParam) throws Exception {


        URL url = new URL(postCallUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        /*** Online Authorization **/
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty("Content-Type", "application/json");

        connection.setConnectTimeout(20000);
        connection.setReadTimeout(2000000);
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

        wr.writeBytes(jsonParam.toString());
        wr.flush();
        wr.close();

        //do somehting with response
        InputStream is = connection.getInputStream();
        String contentAsString = convertInputStreamToString(is);
        int status = connection.getResponseCode();
        is.close();
        return contentAsString;

    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    private static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

}
