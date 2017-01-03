package com.creativeitem.newyear.photoframe.frame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.creativeitem.newyear.photoframe.MainActivity;
import com.creativeitem.newyear.photoframe.R;

public class ChooseFrameActivity extends Activity {

    Button btnfooter;
    GridView grid;
    Integer[] Frame_id, Frame_id1;
    Adapter_grid adapter;
    private AdView mAdView;
    InterstitialAd mInterstitialAdSAve;

    private InterstitialAd mInterstitialAd;


    private void setupInterstialAdForSave() {
        mInterstitialAdSAve = new InterstitialAd(ChooseFrameActivity.this);
        mInterstitialAdSAve.setAdUnitId(getResources().getString(R.string.full_screen_ad_unit_id_save));

        AdRequest adRequestFull = new AdRequest.Builder().build();

        mInterstitialAdSAve.loadAd(adRequestFull);
        mInterstitialAdSAve.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                // Full screen advertise will show only after loading complete
                mInterstitialAdSAve.show();
            }
        });
    }


    private void setupInterstialAd() {
        mInterstitialAd = new InterstitialAd(ChooseFrameActivity.this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.full_screen_ad_unit_id));

        AdRequest adRequestFull = new AdRequest.Builder().build();

        mInterstitialAd.loadAd(adRequestFull);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                // Full screen advertise will show only after loading complete
                mInterstitialAd.show();
            }
        });
    }

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
        setContentView(R.layout.activity_choose_frame);
        mAdView = (AdView) findViewById(R.id.ad_view);
        btnfooter = (Button) findViewById(R.id.btnfooter);

        if (isInternetAvailable()) {
            btnfooter.setVisibility(View.GONE);
            setupBannerAd();
        } else {
            mAdView.setVisibility(View.GONE);
            btnfooter.setVisibility(View.VISIBLE);
        }


        Frame_id = new Integer[]{
                R.drawable.fframe1,
                R.drawable.fframe2,
                R.drawable.fframe3,
                R.drawable.fframe4,
                R.drawable.fframe5,
                R.drawable.fframe6,
                R.drawable.fframe7,
                R.drawable.fframe8,
                R.drawable.fframe9,
                R.drawable.fframe10,
                R.drawable.fframe11,
                R.drawable.fframe12,
                R.drawable.fframe13,
                R.drawable.fframe14,
                R.drawable.fframe15,
				R.drawable.fframe16,
                R.drawable.fframe17,
                R.drawable.fframe18,

        };

        Frame_id1 = new Integer[]{
                R.drawable.frame1,
                R.drawable.frame2,
                R.drawable.frame3,
                R.drawable.frame4,
                R.drawable.frame5,
                R.drawable.frame6,
                R.drawable.frame7,
                R.drawable.frame8,
                R.drawable.frame9,
                R.drawable.frame10,
                R.drawable.frame11,
                R.drawable.frame12,
                R.drawable.frame13,
                R.drawable.frame14,
                R.drawable.frame15,
				R.drawable.frame16,
                R.drawable.frame17,
                R.drawable.frame18,

        };


        grid = (GridView) findViewById(R.id.gridView1);
        adapter = new Adapter_grid(getApplicationContext(), Frame_id);
        grid.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        grid.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                MainActivity.imgid = Frame_id1[arg2];



                if (arg2 ==18){

                    if (isInternetAvailable()) {

                        setupInterstialAdForSave();
                    } else {
                        mAdView.setVisibility(View.GONE);
                    }

                }

                if (arg2 ==15){

                    if (isInternetAvailable()) {

                        setupInterstialAdForSave();
                    } else {
                        mAdView.setVisibility(View.GONE);
                    }

                }

                if (arg2 ==10){

                    if (isInternetAvailable()) {

                        setupInterstialAdForSave();
                    } else {
                        mAdView.setVisibility(View.GONE);
                    }

                }



                if (arg2 ==10){

                    if (isInternetAvailable()) {

                        setupInterstialAdForSave();
                    } else {
                        mAdView.setVisibility(View.GONE);
                    }

                }

                if (arg2 ==6){

                    if (isInternetAvailable()) {

                        setupInterstialAdForSave();
                    } else {
                        mAdView.setVisibility(View.GONE);
                    }

                }

                if (arg2 ==3){

                    if (isInternetAvailable()) {

                        setupInterstialAdForSave();
                    } else {
                        mAdView.setVisibility(View.GONE);
                    }

                }

                if (arg2 ==2){

                    if (isInternetAvailable()) {

                        setupInterstialAdForSave();
                    } else {
                        mAdView.setVisibility(View.GONE);
                    }

                }

//                finish();
//                Log.i("You are click at...", "" + Frame_id[arg2]);
                Intent i = new Intent(ChooseFrameActivity.this, MainActivity.class);
//                i.putExtra("img_id", );
                startActivity(i);

            }
        });
    }


}
