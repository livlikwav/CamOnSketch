package com.example.edge_camera;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;




public class FlickrActivity extends AppCompatActivity {

    private static final String TAG = "imagesearchexample";
    public static final int LOAD_SUCCESS = 101;

    private String SEARCH_URL = "https://secure.flickr.com/services/rest/?method=flickr.photos.search";
    private String API_KEY = "&api_key=10521af729a2985c4261b44a7b2d36be";
    private String PER_PAGE = "&per_page=50";
    private String SORT = "&sort=interestingness-desc";
    private String FORMAT = "&format=json";
    private String CONTECT_TYPE = "&content_type=1";
    private String SEARCH_TEXT = "&text=";
    private String HAS_GEO = "&has_geo=1";
    private String REQUEST_URL = SEARCH_URL + API_KEY + PER_PAGE + SORT + FORMAT + CONTECT_TYPE + HAS_GEO + SEARCH_TEXT;

    private ProgressDialog progressDialog = null;
//    private SimpleAdapter adapter = null;
//    private List<HashMap<String,String>> photoinfoList = null;
    private EditText searchKeyword = null;

    private RvAdapter adapter = null;
    private ArrayList<CvModel> photoURIList = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flickr);

        Button buttonRequestJSON = (Button)findViewById(R.id.button_main_requestjson);
//        ListView listviewPhtoList = (ListView)findViewById(R.id.listview_main_list);
        searchKeyword = (EditText)findViewById(R.id.edittext_main_searchkeyword);


// ========== 테스트용 데이터 정의 영역 ==========

        ArrayList<CvModel> photoURIList = new ArrayList<CvModel>(Arrays.asList(
                new CvModel("https://images.unsplash.com/reserve/iZkDQMqeQlqhoCIywoFv_6005485056_c8622e0824_o.jpg?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"),
                new CvModel("https://images.unsplash.com/photo-1464254786740-b97e5420c299?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"),
                new CvModel( "https://images.unsplash.com/photo-1470472304068-4398a9daab00?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"),
                new CvModel( "https://images.unsplash.com/photo-1500408921219-79e2a10faaff?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"),
                new CvModel( "https://images.unsplash.com/photo-1464061884326-64f6ebd57f83?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjExMjU4fQ&auto=format&fit=crop&w=500&q=60"),
                new CvModel("https://images.unsplash.com/photo-1488711500009-f9111944b1ab?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"),
                new CvModel("https://images.unsplash.com/reserve/91JuTaUSKaMh2yjB1C4A_IMG_9284.jpg?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"),
                new CvModel( "https://images.unsplash.com/photo-1465188162913-8fb5709d6d57?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"),
                new CvModel("https://images.unsplash.com/photo-1486720912533-796a026d93ed?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjIxMTIzfQ&auto=format&fit=crop&w=500&q=60"),
                new CvModel( "https://images.unsplash.com/photo-1522442902874-270c4b3a04df?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"),
                new CvModel("https://images.unsplash.com/photo-1489617482379-fc98cdb77efb?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60")
        ));

// ========== RecyclerView 연결 영역 ==========

        RecyclerView rv_layout = findViewById(R.id.rv_layout);
        // RecyclerView의 레이아웃 매니저를 통해 레이아웃 정의
        rv_layout.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        // RecyclerView에 정의한 Adapter를 연결
        adapter = new RvAdapter(photoURIList, this);
        rv_layout.setAdapter(adapter);

//        photoinfoList = new ArrayList<HashMap<String,String>>();
//
//        String[] from = new String[]{"id", "title","secret","server","farm"};
//        int[] to = new int[] {R.id.textview_main_listviewdata1, R.id.textview_main_listviewdata2,
//                R.id.textview_main_listviewdata3, R.id.textview_main_listviewdata4, R.id.textview_main_listviewdata5};
//        adapter = new SimpleAdapter(this, photoinfoList, R.layout.listview_items, from, to);
//        listviewPhtoList.setAdapter(adapter);




        buttonRequestJSON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog( FlickrActivity.this );
                progressDialog.setMessage("Please wait.....");
                progressDialog.show();

                String keyword = searchKeyword.getText().toString();
                getJSON(keyword);
            }
        });

        /*listviewPhtoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView textviewId = (TextView)view.findViewById(R.id.textview_main_listviewdata1);
                TextView textviewTitle = (TextView)view.findViewById(R.id.textview_main_listviewdata2);
                TextView textviewSecret = (TextView)view.findViewById(R.id.textview_main_listviewdata3);
                TextView textviewServer = (TextView)view.findViewById(R.id.textview_main_listviewdata4);
                TextView textviewFarm = (TextView)view.findViewById(R.id.textview_main_listviewdata5);

                Bundle bundle = new Bundle();
                bundle.putString( "id", textviewId.getText().toString());
                bundle.putString( "title", textviewTitle.getText().toString());
                bundle.putString( "secret", textviewSecret.getText().toString());
                bundle.putString( "server", textviewServer.getText().toString());
                bundle.putString( "farm", textviewFarm.getText().toString());

                Intent intent = new Intent(FlickrActivity.this, ShowImage.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });*/


    }



    private final MyHandler mHandler = new MyHandler(this);


    private static class MyHandler extends Handler {
        private final WeakReference<FlickrActivity> weakReference;

        public MyHandler(FlickrActivity flickractivity) {
            weakReference = new WeakReference<FlickrActivity>(flickractivity);
        }

        @Override
        public void handleMessage(Message msg) {

            FlickrActivity flickractivity = weakReference.get();

            if (flickractivity != null) {
                switch (msg.what) {

                    case LOAD_SUCCESS:
                        flickractivity.progressDialog.dismiss();
                        flickractivity.adapter.notifyDataSetChanged();
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

                    Message message = mHandler.obtainMessage(LOAD_SUCCESS);
                    mHandler.sendMessage(message);
                }
            }

        });
        try {
            thread.start();
            thread.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }



    public boolean jsonParser(String jsonString){

        if (jsonString == null ) return false;

        jsonString = jsonString.replace("jsonFlickrApi(", "");
        jsonString = jsonString.replace(")", "");

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject photos = jsonObject.getJSONObject("photos");
            JSONArray photo = photos.getJSONArray("photo");

            photoURIList.clear();

            for (int i = 0; i < photo.length(); i++) {
                JSONObject photoInfo = photo.getJSONObject(i);

                String id = photoInfo.getString("id");
                String secret = photoInfo.getString("secret");
                String server = photoInfo.getString("server");
                String farm = photoInfo.getString("farm");
                String title = photoInfo.getString("title");

                String thumbnailPhotoURL = "http://farm"+farm+".staticflickr.com/"+server+"/"
                +id+"_"+secret+"_t.jpg";
//                String largePhotoURL = "https://farm" + farm + ".staticflickr.com/" + server + "/"
//                        + id + "_" + secret + "_b.jpg";

//                photoURIList.add(new CvModel(largePhotoURL));
                photoURIList.add(new CvModel(thumbnailPhotoURL));

            }

            return true;
        } catch (JSONException e) {

            Log.d(TAG, e.toString() );
        }

        return false;
    }

}