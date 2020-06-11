package com.example.edge_camera;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA_PERMISSIONS = 931;
    private static final int REQ_CODE_SELECT_IMAGE = 100;
    private Bitmap mInputImage;
    private Bitmap mOriginalImage;
    private Mat src;
    private Mat edge;
    private boolean mIsOpenCVReady = true;

    public native void detectEdgeJNI1(long inputImage, long outputImage, int th1, int th2);
    static {
        System.loadLibrary("opencv_java4");
        System.loadLibrary("native-lib");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d("OpenCV", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            final String[] permissions = {
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE};

            final List<String> permissionsToRequest = new ArrayList<>();
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsToRequest.add(permission);
                }
            }
            if (!permissionsToRequest.isEmpty()) {
                ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[permissionsToRequest.size()]), REQUEST_CAMERA_PERMISSIONS);
            } else {}//addCamera();
        } else {
            //addCamera();
        }

        Button button3 = (Button) findViewById(R.id.button3) ;
        button3.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FlickrActivity.class) ;

                startActivity(intent);
            }
        });
    }

    public void onButtonClicked(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    String path = getImagePathFromURI(data.getData());
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;
                    mOriginalImage = BitmapFactory.decodeFile(path, options);
                    mInputImage = BitmapFactory.decodeFile(path, options);
                    if (mInputImage != null) {
                        detectEdgeUsingJNI();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public String getImagePathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            int idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String imgPath = cursor.getString(idx);
            cursor.close();
            return imgPath;
        }
    }

    public void detectEdgeUsingJNI() {
        //System.out.println("k1111");
        if (!mIsOpenCVReady) {
            return;
        }
//        Mat src = new Mat();
        Utils.bitmapToMat(mInputImage, src);
        //mImageView.setImageBitmap(mOriginalImage);
//        Mat edge = new Mat();
        detectEdgeJNI1(src.getNativeObjAddr(), edge.getNativeObjAddr(), 100, 150);
        Utils.matToBitmap(edge, mInputImage);

        int sW = mInputImage.getWidth();
        int sH = mInputImage.getHeight();
        int size = sW*sH;
        int[] pixels = new int[size];

        mInputImage.getPixels(pixels,0,sW,0,0,sW,sH);
        System.out.println("22222");
        for(int i=0; i<pixels.length; i++){
            int color = pixels[i];

            int r = (color>>16) & 0xFF; //red /65536
            int g = (color>>8) & 0xFF; //green /256
            int b = (color) & 0xFF; //blue
            int y;

            //하얀색 엣지
            if(r==255 && g==255 && b==255){
                pixels[i]= Color.WHITE;
            }
            else{
                pixels[i]=Color.TRANSPARENT;
            }
        }
        System.out.println("3333");
        Bitmap edgeImage  = Bitmap.createBitmap(pixels, 0, sW, sW, sH, Bitmap.Config.ARGB_8888);
        //mEdgeImageView.setImageBitmap(edgeImage);
        //mEdgeImageView.setImageBitmap(b);
        //ColorFilter filter = new PorterDuffColorFilter(Color.YELLOW, PorterDuff.Mode.OVERLAY);
        //mEdgeImageView.setColorFilter(filter); //색깔 입히기
        //mEdgeImageView.setAlpha(0.2f); //투명도 조절
        System.out.println("444");
        Intent intent3 = new Intent(MainActivity.this, CameraFragmentMainActivity.class);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        edgeImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        intent3.putExtra("edgeimage",byteArray);
        startActivity(intent3);

//        float scale = (float) (1024/(float)edgeImage.getWidth());
//        int image_w = (int) (edgeImage.getWidth() * scale);
//        int image_h = (int) (edgeImage.getHeight() * scale);
//        Bitmap resize = Bitmap.createScaledBitmap(edgeImage, image_w, image_h, true);
//        resize.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//        byte[] byteArray2 = stream.toByteArray();
//
//        intent3.putExtra("edgeimage", byteArray2);
//
//        startActivity(intent3);
    }





    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("OpenCV", "OpenCV loaded successfully");
                    src=new Mat();
                    edge = new Mat();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };
}
