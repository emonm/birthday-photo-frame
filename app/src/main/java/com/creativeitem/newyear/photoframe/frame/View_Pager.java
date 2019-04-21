package com.creativeitem.newyear.photoframe.frame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.creativeitem.newyear.photoframe.R;

public class View_Pager extends Activity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager);


        button = (Button) findViewById(R.id.btn_go);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(View_Pager.this, ChooseFrameActivity.class);
                startActivity(i);
            }
        });


    }

}
