package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.blankj.utilcode.util.BarUtils;

import java.util.Timer;
import java.util.TimerTask;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        ImageView iv = findViewById(R.id.loding);
        AnimationDrawable anim = (AnimationDrawable) iv.getBackground();
        anim.start();
        //隐藏ActionBar
        getSupportActionBar().hide();

        //将顶部颜色与背景图片颜色协调一致
        BarUtils.setStatusBarColor(this, Color.parseColor("#010101"));

        //定时器，n秒过后自动跳转到主页面
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //关闭当前Activity,防止返回时，再次回到该页面
                LaunchActivity.this.finish();
                Intent intent = new Intent(LaunchActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        },3*1000);
    }
}
