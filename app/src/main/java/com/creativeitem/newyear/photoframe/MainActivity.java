package com.creativeitem.newyear.photoframe;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import android.widget.RelativeLayout.LayoutParams;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.creativeitem.newyear.photoframe.app.PhotoEditorApplication;
import com.creativeitem.newyear.photoframe.colorpick.ColorPicker;

import com.creativeitem.newyear.photoframe.custom.BitmapUtil;
import com.creativeitem.newyear.photoframe.custom.StickerBtn;
import com.creativeitem.newyear.photoframe.custom.SystemConfig;
import com.creativeitem.newyear.photoframe.custom.TouchImageView;
import com.creativeitem.newyear.photoframe.frame.ChooseFrameActivity;
import com.creativeitem.newyear.photoframe.model.Sticker;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener{
    ColorPicker colorpickerView;
    PhotoEditorApplication photoEditorApplication;
    private OnColorSelectedListener onColorSelectedListener;

   private SeekBar seekOverlay;
    private RecyclerView recyclerOverlay;
    private ImageView imgOverlay;
    Sticker Sticker;
    int alpha;


    private ImageView imgOverlayView;
    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    private static final String TAG = "Touch";
    // These matrices will be used to move and zoom image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    int mode = NONE;
    Button btnheader;
    // Remember some things for zooming
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
    boolean isSelected_one = true;
    //    RelativeLayout ll2;
    ImageView main_img;
    //    ImageButton gallary;
    public static int imgid=0;
    private Bitmap m_bitmap1;
    private ImageView mIv_1;


    private RelativeLayout frameContainer;
    private Uri imageUri;
    private int containerWidth;
    private Bitmap imgBitmapMain;
    private ImageView bgImage;
    private TouchImageView imgSize;
    float scale = 0;
    float d = 0f;
    float newRot = 0f;
    float[] lastEvent = new float[0];
    private TextView tempTextView;
    int jj = 0;

    private ImageView imgSticker, imgText, imgSave, imgShare, imgFrame, imgGallery;

    private FrameLayout frame;

    private RecyclerView recyclerView;

    private StickersAdapter mAdapter;

    private LinearLayout mRootLayout;

    private boolean isSttickerVisible = false;
    private boolean isOverlayVisible=false;

    private GridView gridView;

    private MyAdapter myAdapter;

    public static StickerBtn sticker_view;

    private Toolbar toolbar;


    private FrameLayout mainFrame;


    String style[] = {"font1.ttf", "font2.ttf", "font3.ttf", "font4.ttf", "font5.ttf", "font6.ttf", "font7.ttf", "font8.ttf"};

   // PhotoEditorApplication photoEditorApplication;
    FrameLayout frotext;
    private EditText et_view;

    private TextView txtHidden;

    Spinner mSpinner_text_style;
    ImageButton mIbtn_color_text;

    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    InterstitialAd mInterstitialAdSAve;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_close) {
            sticker_view.clearAll();
            sticker_view.setVisibility(View.GONE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_close, menu);
        return true;
    }


    public boolean isInternetAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    private void setupBannerAd() {

        AdRequest adRequest = new AdRequest.Builder().build();

        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);


    }

    private void setupInterstialAd() {
        mInterstitialAd = new InterstitialAd(MainActivity.this);
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

    private void setupInterstialAdForSave() {
        mInterstitialAdSAve = new InterstitialAd(MainActivity.this);
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();

        btnheader = (Button) findViewById(R.id.btnheader);
        imgFrame = (ImageView) findViewById(R.id.imgFrame);
        frameContainer= (RelativeLayout) findViewById(R.id.frameContainer);
        frameContainer.setVisibility(View.GONE);
        imgOverlayView= (ImageView) findViewById(R.id.imgOverlayView);
        imgOverlayView.setVisibility(View.GONE);
        recyclerOverlay= (RecyclerView) findViewById(R.id.recyclerOverlay);
        seekOverlay= (SeekBar) findViewById(R.id.seekOverlay);
        seekOverlay.setVisibility(View.GONE);
        recyclerOverlay.setVisibility(View.GONE);


        imgFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity.this, ChooseFrameActivity.class);
                startActivity(i);
            }
        });
        imgGallery= (ImageView) findViewById(R.id.imgGallery);
        imgGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSelected_one) {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, 0);
                } else {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, 1);
                }
            }
        });



        mAdView = (AdView) findViewById(R.id.ad_view);

        if (isInternetAvailable()) {
            btnheader.setVisibility(View.GONE);
            setupBannerAd();

         //   setupInterstialAd();
        } else {
            btnheader.setVisibility(View.VISIBLE);
            mAdView.setVisibility(View.GONE);
        }
        mainFrame = (FrameLayout) findViewById(R.id.mainFrame);
        sticker_view = (StickerBtn) findViewById(R.id.sticker_view);
        sticker_view.setVisibility(View.GONE);
        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setVisibility(View.GONE);
        seekOverlay.setVisibility(View.GONE);
        recyclerOverlay.setVisibility(View.GONE);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setVisibility(View.GONE);
        mRootLayout = (LinearLayout) findViewById(R.id.mRootLayout);
        imgSize = (TouchImageView) findViewById(R.id.imgSize);
        imgSize.setMinZoom(0.5f);
        imgSize.setMaxZoom(1.5f);
        frame = (FrameLayout) findViewById(R.id.frame);
        bgImage = (ImageView) findViewById(R.id.bgImage);
        resizeImage(false, null);
        imgShare = (ImageView) findViewById(R.id.imgShare);






        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                frame.setDrawingCacheEnabled(true);
               Bitmap bitmap;
/*
                if (sticker_view.getTotalSize() > 0) {
                    bitmap = sticker_view.saveBitmap(frame.getDrawingCache());
                } else {
                    bitmap = frame.getDrawingCache();
                }

                Uri imageUri = getImageUri(MainActivity.this,bitmap);

                frame.setDrawingCacheEnabled(false);
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);

                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                shareIntent.setType("image/jpeg");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(shareIntent);*/

                bitmap = frame.getDrawingCache();
                if (sticker_view.getTotalSize() > 0) {
                    bitmap = sticker_view.saveBitmap(frame.getDrawingCache());
                } else {
                    bitmap = frame.getDrawingCache();
                }

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");

                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "title");
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        values);
                Log.e("URIIIII",uri+"");


                OutputStream outstream;
                try {


                    outstream = getContentResolver().openOutputStream(uri);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                    outstream.close();
                } catch (Exception e) {
                    System.err.println(e.toString());
                }

                share.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(share, "Share Image"));
            }
        });


        imgSave = (ImageView) findViewById(R.id.imgSave);
        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoEditorApplication.getInstance().trackEvent("Event Tracking", "Save button clicked", "Save operation");


                if (isInternetAvailable()) {

                    setupInterstialAdForSave();
                } else {
                    mAdView.setVisibility(View.GONE);
                }

                frame.setDrawingCacheEnabled(true);

                Bitmap bitmap;
                bitmap = frame.getDrawingCache();
                if (sticker_view.getTotalSize() > 0) {
                    bitmap = sticker_view.saveBitmap(frame.getDrawingCache());
                } else {
                    bitmap = frame.getDrawingCache();
                }



                try {
                    File file, f = null;

                    file = new File(Environment.getExternalStorageDirectory() + "/Birthday Photo Frame");
                    if (!file.exists()) {
                        file.mkdirs();
                    }

                    String str = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
                    str = "Square_" + str + ".jpg";
                    f = new File(file.getAbsolutePath() + "/" + str);
                    Log.e("path...", f.getAbsolutePath() + "");

                    FileOutputStream ostream = new FileOutputStream(f);

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);

                    ostream.close();

                    MediaScannerConnection.scanFile(MainActivity.this,
                            new String[]{f.toString()}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> uri=" + uri);

                                }
                            });
                    Snackbar snackbar = Snackbar.make(mRootLayout, "Image Saved Successfully", Snackbar.LENGTH_LONG);
                    snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    snackbar.show();
                    frame.setDrawingCacheEnabled(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

        imgText = (ImageView) findViewById(R.id.imgText);
        imgText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSttickerVisible = false;
                isOverlayVisible=false;
                seekOverlay.setVisibility(View.GONE);
                recyclerOverlay.setVisibility(View.GONE);
                gridView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                final Dialog dialog = new Dialog(MainActivity.this);
                // Include dialog.xml file

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(Color.TRANSPARENT));

                dialog.setContentView(R.layout.text_custom_dialog);
                dialog.setCancelable(false);
                // Set dialog title

                et_view = (EditText) dialog.findViewById(R.id.et_view);

//               et_view.setText("" + txtHidden.getText().toString().trim());
                dialog.setTitle("Text Appearance");
                dialog.show();
                mSpinner_text_style = (Spinner) dialog
                        .findViewById(R.id.spinner_text_style);
                mIbtn_color_text = (ImageButton) dialog
                        .findViewById(R.id.ibtn_color_text);

                TextAdapter adapter = new TextAdapter(MainActivity.this,
                        R.layout.spinner_row, style);
                mSpinner_text_style.setAdapter(adapter);

                mSpinner_text_style
                        .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> arg0,
                                                       View arg1, int arg2, long arg3) {
                                // TODO Auto-generated method stub
                                photoEditorApplication.setPosition(arg2);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {
                                // TODO Auto-generated method stub

                            }
                        });

                mIbtn_color_text.setBackgroundColor(photoEditorApplication.getColor());

                photoEditorApplication = new PhotoEditorApplication();

                mIbtn_color_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                       // showColorPickerDialogDemo();

                        int initialColor = photoEditorApplication.getColor();

                        showColorPickerDialogDemo(initialColor,
                                new OnColorSelectedListener() {

                                    @Override
                                    public void onColorSelected(int color) {
                                        // TODO Auto-generated method stub
                                        mIbtn_color_text.setBackgroundColor(color);
                                        photoEditorApplication.setColor(color);

                                    }

                                });

                    }
                });


                Button declineButton = (Button) dialog
                        .findViewById(R.id.btn_cancel);
                // if decline button is clicked, close the custom dialog
                declineButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // Close dialog
                        dialog.dismiss();
                    }
                });

                Button Ok = (Button) dialog.findViewById(R.id.btn_ok);
                // if decline button is clicked, close the custom dialog
                Ok.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // Close dialog
                        txtHidden = null;

                        txtHidden = new TextView(MainActivity.this);

                        final FrameLayout.LayoutParams params =
                                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                                        FrameLayout.LayoutParams.WRAP_CONTENT);
                        txtHidden.setLayoutParams(params);
                        txtHidden.setTextColor(photoEditorApplication.getColor());
                        Typeface face = Typeface.createFromAsset(getAssets(),
                                style[photoEditorApplication.getPosition()]);

                        txtHidden.setTypeface(face);
                        txtHidden.setTextSize(60);

                        String s = et_view.getText().toString().trim();
                        Log.e("et text", s);
                        txtHidden.setText(" " + s + " ");


                        if (txtHidden.getText().toString().trim().length() == 0) {
                            Snackbar snackbar = Snackbar.make(mRootLayout, "Please Enter Text", Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            snackbar.show();
                        } else {


                            // Toast.makeText(MainActivity.this,txtHidden.getText()+" niravvvv",Toast.LENGTH_LONG).show();
                            txtHidden.setVisibility(View.INVISIBLE);
                            txtHidden.setDrawingCacheEnabled(false);
                            mainFrame.addView(txtHidden);


                            new CountDownTimer(1000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {

                                    txtHidden.setDrawingCacheEnabled(true);
                                    txtHidden.buildDrawingCache();
                                    sticker_view.setVisibility(View.VISIBLE);
                                    sticker_view.setWaterMark(txtHidden.getDrawingCache(), null);

                                    dialog.dismiss();
                                }
                            }.start();
                        }


                    }
                });

                // ===========================================


            }
        });


        //imgOverlay= (ImageView) findViewById(R.id.imgOverlay);
       /* imgOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOverlayVisible) {
                    isOverlayVisible = false;
                    seekOverlay.setVisibility(View.GONE);
                    recyclerOverlay.setVisibility(View.GONE);
                    gridView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    isOverlayVisible = true;
                    recyclerOverlay.setVisibility(View.VISIBLE);
                    seekOverlay.setVisibility(View.VISIBLE);
                    gridView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    ArrayList<Sticker> stickerArrayList=new ArrayList<Sticker>();
                    stickerArrayList.add(new Sticker(R.drawable.over0,30));
                    stickerArrayList.add(new Sticker(R.drawable.over1,16));
                    stickerArrayList.add(new Sticker(R.drawable.over2,15));
                    stickerArrayList.add(new Sticker(R.drawable.over3,15));
                    stickerArrayList.add(new Sticker(R.drawable.over4,19));
                    stickerArrayList.add(new Sticker(R.drawable.over5,15));
                    stickerArrayList.add(new Sticker(R.drawable.over6,15));
                    stickerArrayList.add(new Sticker(R.drawable.over7,36));
                    stickerArrayList.add(new Sticker(R.drawable.over8,56));
                    stickerArrayList.add(new Sticker(R.drawable.over9,124));
                    stickerArrayList.add(new Sticker(R.drawable.over10,55));
                    stickerArrayList.add(new Sticker(R.drawable.over11,46));
                    stickerArrayList.add(new Sticker(R.drawable.over12,26));
                    stickerArrayList.add(new Sticker(R.drawable.over13,49));
                    stickerArrayList.add(new Sticker(R.drawable.over14,58));
                    stickerArrayList.add(new Sticker(R.drawable.over15,61));
                    stickerArrayList.add(new Sticker(R.drawable.over16,51));
                    stickerArrayList.add(new Sticker(R.drawable.over17,53));
                    stickerArrayList.add(new Sticker(R.drawable.over18,60));
                    stickerArrayList.add(new Sticker(R.drawable.over19,49));
                    stickerArrayList.add(new Sticker(R.drawable.over20,15));
                    stickerArrayList.add(new Sticker(R.drawable.over21,15));
                    stickerArrayList.add(new Sticker(R.drawable.over22,15));
                    stickerArrayList.add(new Sticker(R.drawable.over23,46));
                    stickerArrayList.add(new Sticker(R.drawable.over24,52));
                    stickerArrayList.add(new Sticker(R.drawable.over25,20));
                    stickerArrayList.add(new Sticker(R.drawable.over26,72));
                    stickerArrayList.add(new Sticker(R.drawable.over27,18));
                    stickerArrayList.add(new Sticker(R.drawable.over28,54));
                    stickerArrayList.add(new Sticker(R.drawable.over29,15));



                    mAdapter = new StickersAdapter(stickerArrayList);
                    LinearLayoutManager layoutManager
                            = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    recyclerOverlay.setLayoutManager(layoutManager);
//                recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerOverlay.setAdapter(mAdapter);

                }
            }
        });*/

        imgSticker = (ImageView) findViewById(R.id.imgSticker);
        imgSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isOverlayVisible=false;
                if (isSttickerVisible) {
                    isSttickerVisible = false;
                    seekOverlay.setVisibility(View.GONE);
                    recyclerOverlay.setVisibility(View.GONE);
                    gridView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    isSttickerVisible = true;
                    seekOverlay.setVisibility(View.GONE);
                    recyclerOverlay.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
//                    ArrayList<Sticker> stickerArrayList=new ArrayList<Sticker>();
//                    stickerArrayList.add(new Sticker(R.drawable.c1,30));
//                    stickerArrayList.add(new Sticker(R.drawable.c2,16));
//                    stickerArrayList.add(new Sticker(R.drawable.c3,15));
//                    stickerArrayList.add(new Sticker(R.drawable.c4,15));
//                    stickerArrayList.add(new Sticker(R.drawable.c5,19));
//                    stickerArrayList.add(new Sticker(R.drawable.c6,15));
//                    stickerArrayList.add(new Sticker(R.drawable.c7,15));
//                    stickerArrayList.add(new Sticker(R.drawable.c8,36));
//                    stickerArrayList.add(new Sticker(R.drawable.c9,56));
//                    stickerArrayList.add(new Sticker(R.drawable.c10,124));
//                    stickerArrayList.add(new Sticker(R.drawable.c11,55));
//                    stickerArrayList.add(new Sticker(R.drawable.c12,46));
//                    stickerArrayList.add(new Sticker(R.drawable.c13,26));
//                    stickerArrayList.add(new Sticker(R.drawable.c14,49));
//                    stickerArrayList.add(new Sticker(R.drawable.c15,58));
//                    stickerArrayList.add(new Sticker(R.drawable.c16,61));
//                    stickerArrayList.add(new Sticker(R.drawable.c17,51));
//                    stickerArrayList.add(new Sticker(R.drawable.c18,53));
//                    stickerArrayList.add(new Sticker(R.drawable.c19,60));
//                    stickerArrayList.add(new Sticker(R.drawable.c20,49));
//                    stickerArrayList.add(new Sticker(R.drawable.c21,15));
//                    stickerArrayList.add(new Sticker(R.drawable.c22,15));
//                    stickerArrayList.add(new Sticker(R.drawable.c23,15));
//                    stickerArrayList.add(new Sticker(R.drawable.c24,46));
//                    stickerArrayList.add(new Sticker(R.drawable.c25,52));
//                    stickerArrayList.add(new Sticker(R.drawable.c26,20));
//                    stickerArrayList.add(new Sticker(R.drawable.c27,72));
//                    stickerArrayList.add(new Sticker(R.drawable.c28,18));
//                    stickerArrayList.add(new Sticker(R.drawable.c29,54));
//                    stickerArrayList.add(new Sticker(R.drawable.c30,15));
//                    stickerArrayList.add(new Sticker(R.drawable.c31,18));
//                    stickerArrayList.add(new Sticker(R.drawable.c32,50));
//
//
//                    mAdapter = new StickersAdapter(stickerArrayList);
//                    LinearLayoutManager layoutManager
//                            = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
//                    recyclerView.setLayoutManager(layoutManager);
////                recyclerView.setItemAnimator(new DefaultItemAnimator());
//                    recyclerView.setAdapter(mAdapter);
                    gridView.setVisibility(View.VISIBLE);

                    myAdapter = new MyAdapter(MainActivity.this, 123, 1);
                    gridView.setAdapter(myAdapter);
                }

            }
        });


        photoEditorApplication = new PhotoEditorApplication();
//        mTv_text = new AutoResizeTextView(getApplicationContext());
//        frotext = (FrameLayout) findViewById(R.id.framecontainer);
//
//        frotext.addView(mTv_text);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();





        mIv_1 = (ImageView) findViewById(R.id.iv_1);
//
//        ll2 = (RelativeLayout) findViewById(R.id.ll2);
//
//        gallary = (ImageButton) findViewById(R.id.btn_gallary);

        mIv_1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 0);
            }
        });

    }

    private void showColorPickerDialogDemo(int initialColor2,
    final OnColorSelectedListener onColorSelectedListener) {

        this.onColorSelectedListener = onColorSelectedListener;
        colorpickerView = new ColorPicker(MainActivity.this);
        int initialColor = photoEditorApplication.getColor();
        final Dialog d = new Dialog(MainActivity.this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        colorpickerView.setColor(initialColor2);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        d.setContentView(R.layout.cutom_color_dialog);
        RelativeLayout rl = (RelativeLayout) d.findViewById(R.id.color_Layout);
        rl.addView(colorpickerView, layoutParams);
        Button ok_btn = (Button) d.findViewById(R.id.color_ok);
        Button cancel_btn = (Button) d.findViewById(R.id.color_cancel);

        ok_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                int selectedColor = colorpickerView.getColor();
                onColorSelectedListener.onColorSelected(selectedColor);
                d.dismiss();
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                d.dismiss();
            }
        });
        d.setCancelable(false);
        d.show();

    }

    public interface OnColorSelectedListener {
        public void onColorSelected(int color);

    }


    @Override
    protected void onResume() {
        super.onResume();

if(imgid !=0){
    frameContainer.setVisibility(View.VISIBLE);
    imgSize.setVisibility(View.GONE);
    main_img = (ImageView) findViewById(R.id.main_img);
    main_img.setBackgroundResource(imgid);
}
         else {
    frameContainer.setVisibility(View.GONE);
    imgSize.setVisibility(View.VISIBLE);

         }


    }

    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
       String path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, "title", "image/jpeg");
        return Uri.parse(path);
    }


  /*  public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
      //  String path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, "Title", null);\
          String path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, "Title", null);

        return Uri.parse(path);
    }
*/
    private void resizeImage(boolean normalView, Bitmap bMapRotate) {


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        containerWidth = metrics.widthPixels;
        if (SystemConfig.getImageQuality() > containerWidth) {
            jj = SystemConfig.getImageQuality();
        } else {
            jj = containerWidth;
        }

        if(normalView){
            imageUri=getImageUri(MainActivity.this,bMapRotate);
        } else {
            boolean isRealPath = getIntent().getBooleanExtra("realPath", false);
            Log.e("is_real_path", isRealPath + "");
            String bundleValue = getIntent().getStringExtra("uri");


            if (isRealPath) {
                imageUri = Uri.fromFile(new File(bundleValue));
            } else {
                imageUri = getIntent().getParcelableExtra("uri");
            }


        }

        try {
            imgBitmapMain = BitmapUtil.sampledBitmapFromStream(getContentResolver().openInputStream(imageUri), getContentResolver().openInputStream(imageUri), jj, jj);
            Log.e("is_real_path", imgBitmapMain + "");
            try {
                ExifInterface exif = new ExifInterface(imageUri.getPath());
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                Log.d("EXIF", "Exif: " + orientation);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                }
                Log.e("this", "inside this");
                imgBitmapMain = Bitmap.createBitmap(imgBitmapMain, 0, 0, imgBitmapMain.getWidth(), imgBitmapMain.getHeight(), matrix, true); // rotating bitmap

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("this no no", "inside this");
            }
            imgSize.setImageBitmap(imgBitmapMain);
            bgImage.setImageBitmap(imgBitmapMain);
            BitmapDrawable drawable = (BitmapDrawable) bgImage.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            Bitmap blurred = fastblur(bitmap, 1, 100);
            //second parametre is radius
            bgImage.setImageBitmap(blurred);


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("this no no no", "inside this");
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "title", "image/jpeg");

        return Uri.parse(path);
    }

    public Bitmap fastblur(Bitmap sentBitmap, float scale, int radius) {

        int width = Math.round(sentBitmap.getWidth() * scale);
        int height = Math.round(sentBitmap.getHeight() * scale);
        sentBitmap = Bitmap.createScaledBitmap(sentBitmap, width, height, false);

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.selfcoderlab.birthday.photoframe/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.selfcoderlab.birthday.photoframe/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    public class StickersAdapter extends RecyclerView.Adapter<StickersAdapter.MyViewHolder> {

        private List<Sticker> StickersList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageView imgSticker;

            public MyViewHolder(View view) {
                super(view);
                imgSticker = (ImageView) view.findViewById(R.id.imgSticker);

            }
        }


        public StickersAdapter(List<Sticker> StickersList) {
            this.StickersList = StickersList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sticker_category_item_view, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            Sticker = StickersList.get(position);

            Glide.with(MainActivity.this).load(Sticker.stickerId).into(holder.imgSticker);
//            holder.imgSticker.setText(Sticker.getTitle());
            holder.imgSticker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Sticker = StickersList.get(position);
                    imgOverlayView.setVisibility(View.VISIBLE);
                    Glide.with(MainActivity.this).load(Sticker.stickerId).into(imgOverlayView);
                    initShapeAlphaEffect();
                }
            });
        }

        @Override
        public int getItemCount() {
            return StickersList.size();
        }
    }


    private void initShapeAlphaEffect() {

        seekOverlay.setMax(255);
        seekOverlay.setKeyProgressIncrement(1);
        seekOverlay.setProgress(127);
        alpha = seekOverlay.getProgress();
        imgOverlayView.setAlpha(alpha);
        seekOverlay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                alpha = seekOverlay.getProgress();
                imgOverlayView.setAlpha(alpha);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }



    public class TextAdapter extends ArrayAdapter<String> {

        public TextAdapter(Context context, int textViewResourceId,
                           String[] objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.spinner_row, parent, false);

            TextView label = (TextView) row.findViewById(R.id.textView1);
            label.setText("Add Text");

            Typeface face = Typeface.createFromAsset(getAssets(),
                    style[position]);

            label.setTypeface(face);

            return row;
        }

    }

    private class MyAdapter extends BaseAdapter {

        Context context;
        int al;
        int stId;
        LayoutInflater inflater;
        int correctPosition;

        MyAdapter(Context context, int al, int stickerId) {

            this.context = context;
            inflater = LayoutInflater.from(this.context);
            this.al = al;
            stId = stickerId;
        }

        @Override
        public int getCount() {
            return al;
        }

        @Override
        public Object getItem(int position) {
            return al;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;

            if (convertView == null) {

                convertView = inflater.inflate(R.layout.sticker_category_item_view, parent, false);

                viewHolder = new ViewHolder();

                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imgSticker);


                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
//            viewHolder.imageView.setImageBitmap(getBitmapFromAsset("brands/"+al.get(position).bId+"/"+al.get(position).bImage));

            Glide.with(MainActivity.this).load("file:///android_asset/" + stId + "/" + (position) + ".png")
                    .into(viewHolder.imageView);

            Log.e("path", "file:///android_asset/" + stId + "/" + (++correctPosition) + ".png" + "");

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO main logic
                    gridView.setVisibility(View.GONE);
                    seekOverlay.setVisibility(View.GONE);
                    recyclerOverlay.setVisibility(View.GONE);
                    correctPosition = position;
                    sticker_view.setVisibility(View.VISIBLE);
                    sticker_view.setWaterMark(getBitmapFromAsset(MainActivity.this, "" + stId + "/" + (position) + ".png"), null);
                }
            });

            return convertView;
        }


        private class ViewHolder {

            ImageView imageView;

        }

    }


    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            // handle exception
        }

        return bitmap;
    }


    /*public void showColorPickerDialogDemo() {

        int initialColor = photoEditorApplication.getColor();

        ColorPickerDialog colorPickerDialog = new ColorPickerDialog(this,
                initialColor, new ColorPickerDialog.OnColorSelectedListener() {

            @Override
            public void onColorSelected(int color) {
                mIbtn_color_text.setBackgroundColor(color);
                photoEditorApplication.setColor(color);

            }

        });
        colorPickerDialog.show();*/

   // }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0:

                if (requestCode == 0 && resultCode == RESULT_OK && null != data) {
                    m_bitmap1 = null;

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.MediaColumns.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    System.err.println("Image Path =====>" + picturePath);
                    m_bitmap1 = BitmapFactory.decodeFile(picturePath);

                    Matrix mat = new Matrix();

                    Bitmap bMapRotate = Bitmap.createBitmap(m_bitmap1, 0, 0,
                            m_bitmap1.getWidth(), m_bitmap1.getHeight(), mat, true);
                    mIv_1.setImageBitmap(bMapRotate);
                    mIv_1.setOnTouchListener(this);
                    if(imgid ==0) {
                        resizeImage(true, bMapRotate);
                    }
                }
                break;


            case 11:

                if (requestCode == 11) {
                    File f = new File(Environment.getExternalStorageDirectory()
                            .toString());
                    for (File temp : f.listFiles()) {
                        if (temp.getName().equals("temp.jpg")) {
                            f = temp;
                            break;
                        }
                    }
                    try {
                        BitmapFactory.Options btmapOptions = new BitmapFactory.Options();

                        Bitmap bm1 = BitmapFactory.decodeFile(f.getAbsolutePath(),
                                btmapOptions);

                        Matrix mat = new Matrix();
                        Bitmap bMapRotate = Bitmap.createBitmap(bm1, 0, 0,
                                bm1.getWidth(), bm1.getHeight(), mat, true);
                        mIv_1.setImageBitmap(bMapRotate);
                        mIv_1.setOnTouchListener(this);
                        if(imgid ==0) {
                            resizeImage(true, bMapRotate);
                        }
                        String path = Environment
                                .getExternalStorageDirectory()
                                + File.separator
                                + "Phoenix" + File.separator + "default";
                        f.delete();
                        OutputStream fOut = null;
                        File file = new File(path, String.valueOf(System
                                .currentTimeMillis()) + ".jpg");
                        try {
                            fOut = new FileOutputStream(file);
                            bm1.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                            fOut.flush();
                            fOut.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                }
                break;

        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ImageView view = (ImageView) v;
        view.setScaleType(ImageView.ScaleType.MATRIX);

        dumpEvent(event);
        /*if (view == mIv_1) {
            isSelected_one = true;
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    savedMatrix.set(matrix);

                    start.set(event.getX(), event.getY());
                    Log.d(TAG, "mode=DRAG");
                    mode = DRAG;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = spacing(event);
                    Log.d(TAG, "oldDist=" + oldDist);
                    if (oldDist > 10f) {
                        savedMatrix.set(matrix);
                        midPoint(mid, event);
                        mode = ZOOM;
                        Log.d(TAG, "mode=ZOOM");
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    mode = NONE;
                    Log.d(TAG, "mode=NONE");
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mode == DRAG) {
                        // ...
                        matrix.set(savedMatrix);
                        matrix.postTranslate(event.getX() - start.x, event.getY()
                                - start.y);

                    } else if (mode == ZOOM) {
                        float newDist = spacing(event);
                        Log.d(TAG, "newDist=" + newDist);
                        if (newDist > 10f) {
                            matrix.set(savedMatrix);
                            float scale = newDist / oldDist;
                            matrix.postScale(scale, scale, mid.x, mid.y);
                        }
                    }
                    break;
            }

            view.setImageMatrix(matrix);

        }*/

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: //first finger down only
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                Log.d(TAG, "mode=DRAG" );
                mode = DRAG;
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                }
                lastEvent = new float[4];
                lastEvent[0] = event.getX(0);
                lastEvent[1] = event.getX(1);
                lastEvent[2] = event.getY(0);
                lastEvent[3] = event.getY(1);


                  d = rotation(event);
                break;

            case MotionEvent.ACTION_UP: //first finger lifted
            case MotionEvent.ACTION_POINTER_UP: //second finger lifted
                mode = NONE;
                Log.d(TAG, "mode=NONE" );
                break;


            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    // ...
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY()
                            - start.y);
                } else if (mode == ZOOM && event.getPointerCount() == 2) {
                    float newDist = spacing(event);
                    matrix.set(savedMatrix);
                    if (newDist > 10f) {
                        scale = newDist / oldDist;
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                    if (lastEvent != null) {
                        newRot = rotation(event);

                        float r = newRot - d;
                        matrix.postRotate(r, view.getMeasuredWidth() / 2,
                                view.getMeasuredHeight() / 2);
                    }
                }
                break;

        }
        // Perform the transformation
        view.setImageMatrix(matrix);


        return true;
    }

    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);

        return (float) Math.toDegrees(radians);
    }

    private void dumpEvent(MotionEvent event) {
        String names[] = { "DOWN" , "UP" , "MOVE" , "CANCEL" , "OUTSIDE" ,
                "POINTER_DOWN" , "POINTER_UP" , "7?" , "8?" , "9?" };
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_" ).append(names[actionCode]);
        if (actionCode == MotionEvent.ACTION_POINTER_DOWN
                || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid " ).append(
                    action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")" );
        }

        sb.append("[" );

        for (int i = 0; i < event.getPointerCount(); i++) {
            sb.append("#" ).append(i);
            sb.append("(pid " ).append(event.getPointerId(i));
            sb.append(")=" ).append((int) event.getX(i));
            sb.append("," ).append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())

                sb.append(";" );
        }

        sb.append("]" );
        Log.d(TAG, sb.toString());

    }

    /**
     * Determine the space between the first two fingers
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }


}
