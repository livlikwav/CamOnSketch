package com.example.edge_camera;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button1 = (Button) findViewById(R.id.button1) ;
        button1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ImageActivity.class) ;

                startActivity(intent) ;
            }
        });

        Button button2 = (Button) findViewById(R.id.button2) ;
        button2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CameraFragmentMainActivity.class) ;

                startActivity(intent) ;
            }
        });

        Button button3 = (Button) findViewById(R.id.button3) ;
        button3.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FlickrActivity.class) ;

                startActivity(intent);
            }
        });
    }
}
