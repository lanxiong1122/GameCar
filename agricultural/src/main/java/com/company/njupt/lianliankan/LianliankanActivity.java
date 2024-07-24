package com.company.njupt.lianliankan;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class LianliankanActivity extends AppCompatActivity {
    //游戏难度 1简单 2普通 3困难
    private int level = 1;
    //背景音乐
    private boolean bgm = true;
    //提示音
    private boolean beep = true;
    //震动
    private boolean vibrate = true;

    private Button startBtn;
    private Button levelBtn;
    private Button bgmBtn;
    private Button beepBtn;
    private Button vibrateBtn;
    private Button rankBtn;
    private Button exitBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lianliankan);
        startBtn = findViewById(R.id.startBtn);
        levelBtn = findViewById(R.id.levelBtn);
        bgmBtn = findViewById(R.id.bgmBtn);
        beepBtn = findViewById(R.id.beepBtn);
        vibrateBtn = findViewById(R.id.vibrateBtn);
        rankBtn = findViewById(R.id.rankBtn);
        exitBtn = findViewById(R.id.exitBtn);
        //设置背景音乐
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.bg);
        mp.setLooping(true);
        mp.start();
        //若无排行榜数据文件 则创建
        try {
            File file = new File(getFilesDir().getPath() + "//myrank.txt");
            if(!file.exists()){
                /*
                Toast.makeText(LianliankanActivity.this,
                        "无排行榜文件",
                        Toast.LENGTH_SHORT).show();
                */
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //为按钮绑定点击函数
        startBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LianliankanActivity.this, GameActivity.class);  //从LianliankanActivity跳转到GameActivity
                //放入数据
                intent.putExtra("level", level);
                intent.putExtra("bgm", bgm);
                intent.putExtra("beep", beep);
                intent.putExtra("vibrate", vibrate);
                startActivity(intent);  //开始跳转
            }
        });
        levelBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String str = (String) levelBtn.getText();
                if (str.equals("Difficulty: Simple"))
                {
                    str = "Difficulty: Ordinary";
                    level = 2;
                }
                else if (str.equals("Difficulty: Difficult"))
                {
                    str = "Difficulty: Simple";
                    level = 1;
                }
                else if (str.equals("Difficulty: Ordinary"))
                {
                    str = "Difficulty: Difficult";
                    level = 3;
                }
                levelBtn.setText(str);
            }
        });
        bgmBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String str = (String) bgmBtn.getText();
                if (str.equals("Music: Open"))
                {
                    str = "Music: Off";
                    bgm = false;
                    mp.pause();
                }
                else if (str.equals("Music: Off"))
                {
                    str = "Music: Open";
                    bgm = true;
                    mp.start();
                }
                bgmBtn.setText(str);
            }
        });
        beepBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String str = (String) beepBtn.getText();
                if (str.equals("Sound prompt: Open"))
                {
                    str = "Sound prompt: Close";
                    beep = false;
                }
                else if (str.equals("Sound prompt: Close"))
                {
                    str = "Sound prompt: Open";
                    beep = true;
                }
                beepBtn.setText(str);
            }
        });
        vibrateBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String str = (String) vibrateBtn.getText();
                if (str.equals("Vibration: Open"))
                {
                    str = "Vibration: Off";
                    vibrate = false;
                }
                else if (str.equals("Vibration: Off"))
                {
                    str = "Vibration: Open";
                    vibrate = true;
                }
                vibrateBtn.setText(str);
            }
        });
        rankBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LianliankanActivity.this, RankActivity.class);  //从MainActivity跳转到RankActivity
                startActivity(intent);
            }
        });
        exitBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*Intent intent = new Intent();
                intent.setClass(LianliankanActivity.this, LeadActivity.class);  //从MainActivity跳转到RankActivity
                startActivity(intent);
                mp.pause();*/
                finish();
            }
        });
        findViewById(R.id.aboutUs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LianliankanActivity.this, com.company.njupt.lianliankan.AboutActivity.class);
                startActivity(intent);
            }
        });
    }
}