package com.creativeitem.newyear.photoframe.frame;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import com.creativeitem.newyear.photoframe.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SelectedImageActivity extends Activity implements OnTouchListener {

    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    private static final String TAG = "Touch";
    // These matrices will be used to move and zoom image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    int mode = NONE;

    // Remember some things for zooming
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
    boolean isSelected_one = true;
//    RelativeLayout ll2;
    ImageView main_img;
//    ImageButton gallary;
    int imgid;
    private Bitmap m_bitmap1;
    private ImageView mIv_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
//        getActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_image);

        Intent i = getIntent();
        imgid = i.getIntExtra("img_id", 0);

        main_img = (ImageView) findViewById(R.id.main_img);

        main_img.setBackgroundResource(imgid);

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

//        gallary.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                if (isSelected_one) {
//                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(i, 0);
//                } else {
//                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(i, 1);
//                }
//
//            }
//        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0:

                if (requestCode == 0 && resultCode == RESULT_OK && null != data) {
                    m_bitmap1 = null;

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaColumns.DATA};

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

        if (view == mIv_1) {
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

        }

        return true;
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


//    // For Back Button
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        // TODO Auto-generated method stub
//        show_alert_back("Exit", "Are you sure you want to exit Editor ?");
//        return super.onKeyDown(keyCode, event);
//
//    }
//
//
//    // For Back Button
//    public void show_alert_back(String title, String msg) {
//
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//                SelectedImageActivity.this);
//
//        // set title
//        alertDialogBuilder.setTitle(title);
//
//        // set dialog message
//        alertDialogBuilder
//                .setMessage(msg)
//                .setCancelable(true)
//                .setIcon(R.drawable.gallary).setNegativeButton("No", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // TODO Auto-generated method stub
//                dialog.cancel();
//
//            }
//        })
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // if this button is clicked, close
//                        // current activity
//                        finish();
//
//
//                    }
//                });
//
//
//        // create alert dialog
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.setCancelable(false);
//        // show it
//        alertDialog.show();
//
//        Button b = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
//        Button b1 = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
//        if (b != null)
//            b.setTextColor(Color.RED);
//        if (b1 != null)
//            b1.setTextColor(Color.RED);
//    }


}
