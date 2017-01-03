package com.creativeitem.newyear.photoframe.custom;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.creativeitem.newyear.photoframe.app.PhotoEditorApplication;


/**
 *  Created by SelfCoderLab
 */
public class SystemConfig {

    public static int getImageQuality()
    {
        int imgQuality=0;
        String imageQuality="High";
        if(imageQuality.equalsIgnoreCase("high")){

            if(PhotoEditorApplication.isLowMeoryDevice()) {

                imgQuality= 800;

            } else {
                imgQuality= !PhotoEditorApplication.isLargeMemoryDevice() ? 960 : 1280;
            }
        }

return imgQuality;
    }


    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth)
    {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }
}
