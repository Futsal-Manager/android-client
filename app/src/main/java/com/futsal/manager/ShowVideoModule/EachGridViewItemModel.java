package com.futsal.manager.ShowVideoModule;

import android.graphics.drawable.Drawable;

/**
 * Created by stories2 on 2017. 5. 27..
 */

public class EachGridViewItemModel {

    String videoName;
    Drawable thumbnailImage;

    public void SetVideoName(String videoName) {
        this.videoName = videoName;
    }

    public void SetThumnailImage(Drawable thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    public String GetVideoName() {
        return videoName;
    }

    public Drawable GetThumbnailImage() {
        return thumbnailImage;
    }
}
