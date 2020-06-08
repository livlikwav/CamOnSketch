package com.example.edge_camera;


//import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by webnautes on 2017-12-22.
 */

public class ShowImage extends AppCompatActivity {

    private static final String TAG = "imagesearchexample";
    //ImageView imageviewThumbnailPhoto;
    ImageView imageviewLargePhoto;

    Bundle bundle;
    String largePhotoURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showimage);

        //imageviewThumbnailPhoto = (ImageView)findViewById(R.id.imageview_showimage_small);

        imageviewLargePhoto = (ImageView) findViewById(R.id.imageview_showimage_large);
        Button buttonBack = (Button) findViewById(R.id.button_showimage_back);
        Button buttonGps = (Button) findViewById(R.id.btn_gps);


        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        bundle = intent.getExtras();
        String id = bundle.getString("id");
        String title = bundle.getString("title");
        String secret = bundle.getString("secret");
        String server = bundle.getString("server");
        String farm = bundle.getString("farm");

        buttonGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(ShowImage.this, ShowGps.class);
                intent2.putExtras(bundle);
                startActivity(intent2);
            }
        });

        //String thumbnailPhotoURL = "http://farm"+farm+".staticflickr.com/"+server+"/"
        //+id+"_"+secret+"_t.jpg";
        largePhotoURL = "https://farm" + farm + ".staticflickr.com/" + server + "/"
                + id + "_" + secret + "_b.jpg";


        new showimageTask()
                .execute(largePhotoURL);
        System.out.println(largePhotoURL);


    }


    private class showimageTask extends AsyncTask<String, Void, Bitmap> {


        ProgressDialog progressDialog;
        Bitmap mBitmap;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(ShowImage.this);
            progressDialog.setMessage("Please wait.....");
            progressDialog.show();
        }


        @Override
        protected Bitmap doInBackground(String... args) {

            try {
                mBitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return mBitmap;
        }


        @Override
        protected void onPostExecute(Bitmap image) {
            if (image != null) {
                imageviewLargePhoto.setImageBitmap(image);
                progressDialog.dismiss();
            } else {
                progressDialog.dismiss();
                Toast.makeText(ShowImage.this, "이미지가 없습니다.", Toast.LENGTH_SHORT).show();

            }
        }

    }
}