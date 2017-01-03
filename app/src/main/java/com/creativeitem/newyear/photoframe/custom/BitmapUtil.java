package com.creativeitem.newyear.photoframe.custom;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

/**
 *  Created by SelfCoderLab
 */
public class BitmapUtil {

    public static Bitmap sampledBitmapFromStream(InputStream inputstream, InputStream inputstream1, int i, int j)
    {
        android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputstream, null, options);
        options.inSampleSize = calculateInSampleSize(options, i, j);
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        return BitmapFactory.decodeStream(inputstream1, null, options);
    }

    public static int calculateInSampleSize(android.graphics.BitmapFactory.Options options, int i, int j)
    {
        label0:
        {
            int i1 = options.outHeight;
            int l = options.outWidth;
            int k = 1;
            if (i1 > j || l > i)
            {
                float f = (float)i1 / (float)j;
                f = (float)l / (float)i;
                k = Math.round((float) i1 / (float) j);
                i = Math.round((float) l / (float) i);
                if (k <= i)
                {
                    break label0;
                }
            }
            return k;
        }
        return i;
    }

    public static float exifOrientationToDegrees(int i)
    {
        if (i == 6)
        {
            return 90F;
        }
        if (i == 3)
        {
            return 180F;
        }
        return i != 8 ? 0.0F : 270F;
    }


}
