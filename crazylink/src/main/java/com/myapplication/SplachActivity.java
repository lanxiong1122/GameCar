package com.myapplication;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class SplachActivity extends Activity {
    private LinearLayout dotsLayout;
    private View[] dots = new View[5];
    private int currentDot = 0;
    private ValueAnimator animator;
    private TextView tvShowTime;
    private Handler handler = new Handler();
    private Runnable runnable;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splach);
        dotsLayout = findViewById(R.id.dots_layout);
        tvShowTime = findViewById(R.id.showtime);
        // 更新时间的方法
        updateTime();
        // 开始定时更新时间
        startUpdatingTime();

        for (int i = 0; i < 5; i++) {
            dots[i] = dotsLayout.getChildAt(i);
        }

        startDotsAnimation();

        findViewById(R.id.ic_launcher_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SplachActivity.this, CrazyLinkActivity.class));
            }
        });
        findViewById(R.id.start_go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SplachActivity.this, CrazyLinkActivity.class));
            }
        });
        findViewById(R.id.privacy_policy_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomWebDialog customWebDialog = new CustomWebDialog();
                customWebDialog.show(SplachActivity.this,"file:///android_asset/wys.html");
            }
        });
        findViewById(R.id.about_app_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomWebDialog customWebDialog = new CustomWebDialog();
                customWebDialog.show(SplachActivity.this,"file:///android_asset/wo.html");
            }
        });
    }

    private void startDotsAnimation() {
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }

        animator = ValueAnimator.ofInt(0, 4).setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                animateDot(value);
            }
        });

        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.start();
    }

    private void animateDot(int value) {
        int color = Color.argb(255, 255 - (value * 50), value * 50, 0); // Example color change
        dots[currentDot].setBackgroundColor(color);
        dots[currentDot].setScaleX(1f + (value * 0.1f));
        dots[currentDot].setScaleY(1f + (value * 0.1f));

        currentDot = (currentDot + 1) % dots.length;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (animator.isRunning()){
            animator.cancel();
        }
        if (animator != null){
            animator = null;
        }
        // 停止定时任务
        handler.removeCallbacks(runnable);
    }

        private void updateTime() {
            // 获取当前时间并格式化
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String currentTime = sdf.format(new java.util.Date());
            tvShowTime.setText("localTime："+currentTime);
        }

        private void startUpdatingTime() {
            // 创建一个Runnable对象
            runnable = new Runnable() {
                @Override
                public void run() {
                    // 更新时间
                    updateTime();
                    // 每隔1秒重新执行此runnable
                    handler.postDelayed(this, 1000);
                }
            };

            // 启动定时任务
            handler.post(runnable);
        }


}