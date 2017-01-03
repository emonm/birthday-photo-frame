package com.creativeitem.newyear.photoframe;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.GridView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.creativeitem.newyear.photoframe.mywork.AppConstant;
import com.creativeitem.newyear.photoframe.mywork.GridViewImageAdapter;
import com.creativeitem.newyear.photoframe.mywork.Utils;

import java.util.ArrayList;

/**
 *  Created by SelfCoderLab
 */
public class MyImageViewer extends Activity {


    private Utils utils;
    private ArrayList<String> imagePaths = new ArrayList<String>();
    private GridViewImageAdapter adapter;
    private GridView gridView;
    private int columnWidth;
    private AdView mAdView;


    private void setupBannerAd() {

        AdRequest adRequest = new AdRequest.Builder().build();

        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);


    }


    public boolean isInternetAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public static int dipsToPixels(Context context, float dips) {
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dips, context.getResources()
                        .getDisplayMetrics()));
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_imageview);

        mAdView = (AdView) findViewById(R.id.ad_view);

        if (isInternetAvailable()) {
            setupBannerAd();
        } else {
            mAdView.setVisibility(View.GONE);
        }

        try {
            gridView = (GridView) findViewById(R.id.grid_view);
            utils = new Utils(MyImageViewer.this);
            // Initilizing Grid View
            InitilizeGridLayout();
            // loading all image paths from SD card
            imagePaths = utils.getFilePaths();
            // Gridview adapter
            adapter = new GridViewImageAdapter(MyImageViewer.this, imagePaths, columnWidth);
            // setting grid view adapter
            gridView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void InitilizeGridLayout() {
        Resources r = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                AppConstant.GRID_PADDING, r.getDisplayMetrics());

        columnWidth = (int) ((utils.getScreenWidth() - ((AppConstant.NUM_OF_COLUMNS + 1) * padding)) / AppConstant.NUM_OF_COLUMNS);

        gridView.setNumColumns(AppConstant.NUM_OF_COLUMNS);
        gridView.setColumnWidth(columnWidth);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setPadding((int) padding, (int) padding, (int) padding,
                (int) padding);
        gridView.setHorizontalSpacing((int) padding);
        gridView.setVerticalSpacing((int) padding);
    }

}
