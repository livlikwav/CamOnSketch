package com.example.edge_camera;

public class CvModel {
    private String imageURI;

    CvModel(String imageURI){
        this.imageURI = imageURI;
    }

    public String getImageURI() {
        return imageURI;
    }
}
