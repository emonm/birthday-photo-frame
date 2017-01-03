package com.creativeitem.newyear.photoframe.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.creativeitem.newyear.photoframe.custom.AnalyticsTrackerClass;

/**
 * Created by SelfCoderLab
 */
public class PhotoEditorApplication extends Application {


    static Context context;
    static boolean isLowMemoryDevice = false;
    static boolean isMiddleMemoryDevice = false;
    static boolean islargeMemoryDevice = false;
    int memoryVolume;
    private static Bitmap swapBitmap;

    Bitmap image;
    private int position = 0;
    private int color = 0xFFFF0000;

    public static final String TAG = PhotoEditorApplication.class
            .getSimpleName();

    private static PhotoEditorApplication mInstance;

    public static synchronized PhotoEditorApplication getInstance() {
        return mInstance;
    }

    public PhotoEditorApplication()
    {
    }

    public static Context getContext()
    {
        return context;
    }

    public static int getResolution()
    {
        return 612;
    }

    public static Bitmap getSwapBitmap()
    {
        return swapBitmap;
    }

    public static boolean isLargeMemoryDevice()
    {
        return islargeMemoryDevice;
    }

    public static boolean isLowMeoryDevice()
    {
        return isLowMemoryDevice;
    }

    public static boolean isMiddleMemoryDevice()
    {
        return isMiddleMemoryDevice;
    }

    public static void setSwapBitmap(Bitmap bitmap)
    {
        if (bitmap == null && swapBitmap != null)
        {
            if (!swapBitmap.isRecycled())
            {
                swapBitmap.recycle();
            }
            swapBitmap = null;
        }
        swapBitmap = bitmap;
    }



    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;


        boolean flag1 = true;
        super.onCreate();
        context = getApplicationContext();

        ActivityManager activitymanager = (ActivityManager)getApplicationContext().getSystemService(ACTIVITY_SERVICE);
        memoryVolume = activitymanager.getMemoryClass();
        boolean flag;
        if (activitymanager.getMemoryClass() <= 32)
        {
            flag = true;
        } else
        {
            flag = false;
        }
        isLowMemoryDevice = flag;
        if (activitymanager.getMemoryClass() > 32 && activitymanager.getMemoryClass() < 64)
        {
            isMiddleMemoryDevice = true;
        }
        if (activitymanager.getMemoryClass() >= 64)
        {
            flag = flag1;
        } else
        {
            flag = false;
        }
        islargeMemoryDevice = flag;
        
        
        
        AnalyticsTrackerClass.initialize(this);
        AnalyticsTrackerClass.getInstance().get(AnalyticsTrackerClass.Target.APP);
    }

    public synchronized Tracker getGoogleAnalyticsTracker() {
        AnalyticsTrackerClass AnalyticsTrackerClass = com.creativeitem.newyear.photoframe.custom.AnalyticsTrackerClass.getInstance();
        return AnalyticsTrackerClass.get(com.creativeitem.newyear.photoframe.custom.AnalyticsTrackerClass.Target.APP);
    }


    public void trackScreenView(String screenName) {
        Tracker t = getGoogleAnalyticsTracker();

        // Set screen name.
        t.setScreenName(screenName);

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());

        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }


    public void trackException(Exception e) {
        if (e != null) {
            Tracker t = getGoogleAnalyticsTracker();

            t.send(new HitBuilders.ExceptionBuilder()
                            .setDescription(
                                    new StandardExceptionParser(this, null)
                                            .getDescription(Thread.currentThread().getName(), e))
                            .setFatal(false)
                            .build()
            );
        }
    }

    public void trackEvent(String category, String action, String label) {
        Tracker t = getGoogleAnalyticsTracker();

        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
    }

}