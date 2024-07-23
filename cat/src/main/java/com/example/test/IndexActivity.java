package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class IndexActivity extends AppCompatActivity implements View.OnClickListener {

    Button start_SurfaceView,start_XML;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        //隐藏ActionBar
        getSupportActionBar().hide();

        start_SurfaceView=findViewById(R.id.start_SurfaceView);
        start_XML=findViewById(R.id.start_XML);
        start_SurfaceView.setOnClickListener(this);
        start_XML.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        /**
         * 第一个启动SurfaceView的布局神经猫，第二个启动Xml布局的神经猫，两个只能启动一个
         */
        switch (v.getId()){
            case R.id.start_SurfaceView:
                Intent intent = new Intent(this, Surface_view.class);
                startActivity(intent);
                break;
            case R.id.start_XML:
                startActivity(new Intent(this,LayoutXml_view.class));
        }
    }
}
