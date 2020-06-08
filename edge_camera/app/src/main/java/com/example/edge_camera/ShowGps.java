package com.example.edge_camera;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Vector;

public class ShowGps extends AppCompatActivity {
    private static final String TAG = "imagesearchexample";
    public static final int LOAD_SUCCESS = 101;
    private ProgressDialog progressDialog = null;
    private SimpleAdapter adapter = null;
    private HashMap<String,String> GPSinfo = null;
    private Vector<String> gpvector = null;
    private String longtitude, latitude;
    private String gpsinfo;

    TextView textview;


    private String SEARCH_URL = " https://secure.flickr.com/services/rest/?method=flickr.photos.getExif";
    private String API_KEY = "&api_key=10521af729a2985c4261b44a7b2d36be";
    private String PHOTO_ID = "&photo_id=";
    private String FORMAT = "&format=json";
    private String REQUEST_URL = SEARCH_URL+API_KEY+FORMAT+PHOTO_ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showgps);

        textview = (TextView)findViewById(R.id.text_gps);
        Button buttonBack = (Button)findViewById(R.id.button_showgps_back);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String id = bundle.getString("id");

        getJSON(id);

    }

    private final ShowGps.MyHandler mHandler = new ShowGps.MyHandler(this);
    private static class MyHandler extends Handler {

        private final WeakReference<ShowGps> weakReference;

        public MyHandler(ShowGps showgps){
            weakReference = new WeakReference<ShowGps>(showgps);
        }

        @Override
        public void handleMessage(Message msg) {

            ShowGps showgps = weakReference.get();

            if (showgps != null) {
                switch (msg.what) {

                    case LOAD_SUCCESS:
                        //showgps.progressDialog.dismiss();
                        //showgps.adapter.notifyDataSetChanged();
                        String jsonString = (String)msg.obj;
                        showgps.textview.setText(jsonString);
                        break;
                }
            }
        }
    }


    public void  getJSON(final String keyword) {

        if ( keyword == null) return;

        Thread thread = new Thread(new Runnable() {

            public void run() {

                String result;

                try {

                    Log.d(TAG, REQUEST_URL+keyword);
                    URL url = new URL(REQUEST_URL+keyword);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                    httpURLConnection.setReadTimeout(3000);
                    httpURLConnection.setConnectTimeout(3000);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setUseCaches(false);
                    httpURLConnection.connect();


                    int responseStatusCode = httpURLConnection.getResponseCode();

                    InputStream inputStream;
                    if (responseStatusCode == HttpURLConnection.HTTP_OK) {

                        inputStream = httpURLConnection.getInputStream();
                    } else {
                        inputStream = httpURLConnection.getErrorStream();

                    }


                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    StringBuilder sb = new StringBuilder();
                    String line;


                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }

                    bufferedReader.close();
                    httpURLConnection.disconnect();

                    result = sb.toString().trim();


                } catch (Exception e) {
                    result = e.toString();
                }



                if (jsonParser(result)){

                    Message message = mHandler.obtainMessage(LOAD_SUCCESS, gpsinfo);
                    mHandler.sendMessage(message);
                }
            }

        });
        thread.start();
    }

    public boolean jsonParser(String jsonString){

        if (jsonString == null ) return false;

        jsonString = jsonString.replace("jsonFlickrApi(", "");
        jsonString = jsonString.replace(")", "");

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject photo = jsonObject.getJSONObject("photo");
            JSONArray exif = photo.getJSONArray("exif");



            for (int i = 0; i < exif.length(); i++) {
                JSONObject photoInfo = exif.getJSONObject(i);

                String tag = photoInfo.getString("tag");

                JSONObject raw = photoInfo.getJSONObject("raw");
                GPSinfo= new HashMap<String, String>();
                if(tag.equals("GPSLatitude"))
                {

                    latitude = raw.getString("_content");
                    latitude = latitude.replace(" deg ","º");
                    latitude = latitude.substring(0,latitude.length()-1);
                    System.out.println(latitude);

                }

                if(tag.equals("GPSLongitude")) {
                    longtitude = raw.getString("_content");
                    longtitude = longtitude.replace(" deg ","º");
                    longtitude = longtitude.substring(0,longtitude.length()-1);
                    System.out.println(longtitude);
                }
            }
            gpsinfo = "위도 : "+latitude+"\n"+"경도 : "+longtitude;
            System.out.println(gpsinfo);

            return true;
        } catch (JSONException e) {

            Log.d(TAG, e.toString() );
        }

        return false;
    }





}
