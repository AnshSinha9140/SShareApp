package com.example.shareapp;

import android.net.Uri;

public class ShareModel {
    private String message;
    private String userName;
    private int sender;
    private Uri imageView;

    public ShareModel(String message, String userName, int sender) {
        this.message = message;
        this.userName = userName;
        this.sender = sender;
    }

    public Uri getImageView() {

        return imageView;
    }

    public void setImageView(Uri imageView) {
        this.imageView = imageView;
    }

    public ShareModel(Uri imageView, String userName, int sender){
        this.imageView = imageView;
        this.userName = userName;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }
}
