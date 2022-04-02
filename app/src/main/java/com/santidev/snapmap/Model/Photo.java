package com.santidev.snapmap.Model;

import android.net.Uri;

public class Photo {

    private String mTitle;
    private String mTag1, mTag2, mTag3;
    private Uri mStorageLocation;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getTag1() {
        return mTag1;
    }

    public void setTag1(String tag1) {
        this.mTag1 = tag1;
    }

    public String getTag2() {
        return mTag2;
    }

    public void setTag2(String tag2) {
        this.mTag2 = tag2;
    }

    public String getTag3() {
        return mTag3;
    }

    public void setTag3(String tag3) {
        this.mTag3 = tag3;
    }

    public Uri getStorageLocation() {
        return mStorageLocation;
    }

    public void setStorageLocation(Uri storageLocation) {
        this.mStorageLocation = storageLocation;
    }
}
