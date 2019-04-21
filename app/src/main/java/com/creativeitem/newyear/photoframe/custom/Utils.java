package com.creativeitem.newyear.photoframe.custom;


import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

/**
 * Created by SelfCoderLab
 */
public class Utils {

    private static Point point = null;

    public static Point getDisplayWidthPixels(Context context) {
        if (point != null) {
            return point;
        }
        WindowManager wm = ((Activity)context).getWindowManager();
        point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return point;
    }


    public static double lineSpace(double x1, double y1, double x2, double y2) {
        double lineLength = 0;
        double x, y;
        x = x1 - x2;
        y = y1 - y2;
        lineLength = Math.sqrt(x * x + y * y);
        return lineLength;
    }


    public static PointD getMidpointCoordinate(double x1, double y1, double x2, double y2) {
        PointD midpoint = new PointD();
        midpoint.set((x1 + x2) / 2, (y1 + y2) / 2);
        return midpoint;
    }






}
