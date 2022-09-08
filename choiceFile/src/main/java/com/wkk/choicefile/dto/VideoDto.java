package com.wkk.choicefile.dto;

import android.graphics.Bitmap;


public class VideoDto {

    private Bitmap bitmap;
    private int time;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}