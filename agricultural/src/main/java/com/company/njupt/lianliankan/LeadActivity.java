package com.company.njupt.lianliankan;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LeadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead);
        findViews();
    }
    private Button lianliankan;
    private Button hitmouse;
    private Button about;
    private void findViews(){
        lianliankan=(Button) findViewById(R.id.lianliankan_btn);
        hitmouse=(Button) findViewById(R.id.hitmouse_btn);
        about=(Button) findViewById(R.id.about_btn);
        lianliankan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(LeadActivity.this, com.company.njupt.lianliankan.LianliankanActivity.class);
                startActivity(intent);
            }
        });
        hitmouse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(LeadActivity.this, com.company.njupt.lianliankan.MouseActivity.class);
                startActivity(intent);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(LeadActivity.this, com.company.njupt.lianliankan.AboutActivity.class);
                startActivity(intent);
            }
        });
    }
}
