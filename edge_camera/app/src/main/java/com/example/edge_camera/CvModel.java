package com.example.edge_camera;

public class CvModel {
    private String imageURI;
    private String id;

    CvModel(String imageURI, String id){
        this.imageURI = imageURI;
        this.id = id;

    }

    public String getImageURI() {
        return imageURI;
    }
    public String getId() {
        return id;
    }
}
