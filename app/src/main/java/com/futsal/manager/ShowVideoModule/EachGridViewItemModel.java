package com.futsal.manager.ShowVideoModule;

import android.graphics.Bitmap;

/**
 * Created by stories2 on 2017. 5. 27..
 */

public class EachGridViewItemModel {

    String videoName, videoOriginName;
    Bitmap thumbnailImage;
    boolean isItemSelected;

    public void SetVideoName(String videoName) {
        this.videoName = videoName;
    }

    public void SetVideoOriginName(String videoOriginName) {
        this.videoOriginName = videoOriginName;
    }

    public void SetThumnailImage(Bitmap thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    public void SetIsItemSelected(boolean isItemSelected) {
        this.isItemSelected = isItemSelected;
    }

    public String GetVideoName() {
        return videoName;
    }

    public String GetVideoOriginName() {
        return videoOriginName;
    }

    public Bitmap GetThumbnailImage() {
        return thumbnailImage;
    }

    public boolean GetIsItemSelected() {
        return isItemSelected;
    }
}
