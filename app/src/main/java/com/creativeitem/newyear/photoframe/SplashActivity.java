package com.creativeitem.newyear.photoframe;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.creativeitem.newyear.photoframe.app.HomeFragmentViewPagerAdapter;
import com.creativeitem.newyear.photoframe.circleindicator.CirclePageIndicator;
import com.creativeitem.newyear.photoframe.custom.ClickableViewPager;
import com.creativeitem.newyear.photoframe.frame.ChooseFrameActivity;
import com.creativeitem.newyear.photoframe.model.Slides;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SplashActivity extends AppCompatActivity {
    private ClickableViewPager mViewPager;
    private Button btnfooter;
    private ImageView imgGallery;
    private ImageButton imgCamera, imgShare, imgRate, imgmywork, imgMore;
    // For Open camera and gallery
    protected static final int REQUEST_CAMERA = 1;
    protected static final int SELECT_FILE = 2;
    private Uri selectedImageUri;
    String mCurrentPhotoPath;
    private AdView mAdView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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
        setContentView(R.layout.activity_splash_chnges);

        btnfooter = (Button) findViewById(R.id.btnfooter);

        imgShare = (ImageButton) findViewById(R.id.imgShare);
        imgRate = (ImageButton) findViewById(R.id.imgRate);


        imgRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.selfcoderlab.birthday.photoframe")));

            }
        });

        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                // Add data to the intent, the receiving app will decide
                // what to do with it.
                share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
                share.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.selfcoderlab.birthday.photoframe");

                startActivity(Intent.createChooser(share, "Share link!"));
            }
        });


        mAdView = (AdView) findViewById(R.id.ad_view);

        mViewPager = (ClickableViewPager) findViewById(R.id.mainFragmentViewPager);

        mViewPager.setOnItemClickListener(new ClickableViewPager.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object


                if (position == 0) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.selfcoderlab.birthday.photoframe")));

                }
                if (position == 1) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.selfcoderlab.birthday.photoframe")));

                }
                if (position == 2) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.selfcoderlab.birthday.photoframe")));

                }
                if (position == 3) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.selfcoderlab.birthday.photoframe")));

                }
                if (position == 4) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.selfcoderlab.birthday.photoframe")));

                }
                if (position == 5) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.selfcoderlab.birthday.photoframe")));

                }

           /*     try {

                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=" + "selfcoderlab")));
                } catch (android.content.ActivityNotFoundException anfe) {

                }*/
            }
        });

        ArrayList<Slides> slides = new ArrayList<>();
        slides.add(new Slides("", R.drawable.slider1));
        slides.add(new Slides("", R.drawable.slider2));
        slides.add(new Slides("", R.drawable.slider3));
        slides.add(new Slides("", R.drawable.slider4));
        slides.add(new Slides("", R.drawable.slider5));
        slides.add(new Slides("", R.drawable.slider6));
        HomeFragmentViewPagerAdapter adapter = new HomeFragmentViewPagerAdapter(
                SplashActivity.this, slides);
        mViewPager.setAdapter(adapter);

        CirclePageIndicator circleIndicator = (CirclePageIndicator)
                findViewById(R.id.mainFragmentViewPagerBullets);
        circleIndicator.setRadius(dipsToPixels(SplashActivity.this, 6));

        circleIndicator.setViewPager(mViewPager);


        if (isInternetAvailable()) {
            btnfooter.setVisibility(View.GONE);
            setupBannerAd();
        } else {
            mAdView.setVisibility(View.GONE);
            btnfooter.setVisibility(View.VISIBLE);
        }

        //  imgCamera = (ImageView) findViewById(R.id.imgCamera);
        imgGallery = (ImageView) findViewById(R.id.imgGallery);
        imgmywork = (ImageButton) findViewById(R.id.imgmywork);
        imgMore = (ImageButton) findViewById(R.id.imgMore);
       /* imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dispatchTakePictureIntent();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CAMERA);

            }
        });
*/
        imgmywork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this, MyImageViewer.class);
                startActivity(intent);

            }
        });


        imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=" + "selfcoderlab")));
                } catch (ActivityNotFoundException anfe) {

                }

            }
        });


        imgGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(SplashActivity.this, ChooseFrameActivity.class);
                startActivity(i);

//                Intent  view = new Intent("android.intent.action.PICK", android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                view.setType("image/*");
//                startActivityForResult(view, SELECT_FILE);
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == SELECT_FILE) {


                selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    try {
                        intent.putExtra("uri", getRealPathFromURI(selectedImageUri));
                        intent.putExtra("realPath", true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        intent.putExtra("uri", selectedImageUri);
                        intent.putExtra("realPath", false);

                    }
                    startActivity(intent);
                } else {
                    Log.e("Error", "Error wile fetching image from gallery");
                }

            }
            if (requestCode == REQUEST_CAMERA) {

//                Bundle extras = data.getExtras();
//                Bitmap imageBitmap = (Bitmap) extras.get("data");
//                imgCamera.setImageBitmap(imageBitmap);

                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                imgCamera.setImageBitmap(thumbnail);
                selectedImageUri = Uri.fromFile(destination);

//                selectedImageUri =data.getData();
                if (selectedImageUri != null) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    try {
                        intent.putExtra("uri", selectedImageUri);
                        intent.putExtra("realPath", false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                } else {
                    Log.e("Error", "Error wile fetching image from gallery");
                }
            }

        }

    }


    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        MainActivity.imgid = 0;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Splash Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
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
                "Splash Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.selfcoderlab.birthday.photoframe/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
