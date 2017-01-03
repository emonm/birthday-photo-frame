package com.creativeitem.newyear.photoframe;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);
        new CountDownTimer(2500,1000){

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                Intent oi=new Intent(Splash.this,SplashActivity.class);
                startActivity(oi);
                finish();
            }
        }.start();
    }
}
