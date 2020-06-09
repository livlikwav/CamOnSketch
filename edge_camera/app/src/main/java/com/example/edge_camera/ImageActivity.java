package com.example.edge_camera;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;


public class ImageActivity extends AppCompatActivity {

    private static final String TAG = "AndroidOpenCv";
    private static final int REQ_CODE_SELECT_IMAGE = 100;
    private Bitmap mInputImage;
    private Bitmap mOriginalImage;

    private ImageView mImageView;
    private ImageView mEdgeImageView;
    private boolean mIsOpenCVReady = true;

    public native void detectEdgeJNI(long inputImage, long outputImage, int th1, int th2);

    static {
        System.loadLibrary("opencv_java4");
        System.loadLibrary("native-lib");
    }

    public void detectEdgeUsingJNI() {
        //System.out.println("k1111");
        if (!mIsOpenCVReady) {
            return;
        }
        Mat src = new Mat();
        Utils.bitmapToMat(mInputImage, src);
        //mImageView.setImageBitmap(mOriginalImage);
        Mat edge = new Mat();
        detectEdgeJNI(src.getNativeObjAddr(), edge.getNativeObjAddr(), 100, 150);
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
                pixels[i]=Color.WHITE;
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
        Intent intent3 = new Intent(ImageActivity.this, CameraFragmentMainActivity.class);
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

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_image);
        mImageView = (ImageView) findViewById(R.id.origin_iv);
        mEdgeImageView = (ImageView) findViewById(R.id.edge_iv);

        byte[] byteArray = getIntent().getByteArrayExtra("image");
        mInputImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        //mImageView.setImageBitmap(mInputImage);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasPermissions(PERMISSIONS)) {
                requestPermissions(PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        }

        detectEdgeUsingJNI();




    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (OpenCVLoader.initDebug()) {
            mIsOpenCVReady = true;
        }
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

    public void onDestroy() {
        super.onDestroy();

        mInputImage.recycle();
        if (mInputImage != null) {
            mInputImage = null;
        }
    }

    public void onButtonClicked(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
    }

    // permission
    static final int PERMISSIONS_REQUEST_CODE = 1000;
    String[] PERMISSIONS = {"android.permission.READ_EXTERNAL_STORAGE"};


    private boolean hasPermissions(String[] permissions) {
        int result;
        for (String perms : permissions) {
            result = ContextCompat.checkSelfPermission(this, perms);
            if (result == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
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

    // permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean cameraPermissionAccepted = grantResults[0]
                            == PackageManager.PERMISSION_GRANTED;

                    if (!cameraPermissionAccepted)
                        showDialogForPermission("실행을 위해 권한 허가가 필요합니다.");
                }
                break;
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void showDialogForPermission(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ImageActivity.this);
        builder.setTitle("알림");
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                requestPermissions(PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });
        builder.create().show();
    }


}
