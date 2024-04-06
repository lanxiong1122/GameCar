package cn.charon.racegame.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import cn.charon.racegame.R;

public class AdvertisingActivity extends AppCompatActivity {
    private Button jumpBtn;
    private CountDownTimer timer;
    private TextView tvTime;
    private int time = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//无标题
        setContentView(R.layout.activity_main);
        findView();
        initTime();
        jumpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdvertisingActivity.this,NavigationPageActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void initTime() {
        if(timer == null){
            timer = new CountDownTimer(5*1000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    tvTime.setText(String.valueOf(time--+"s"));
                }
                //倒计时完的操作
                @Override
                public void onFinish() {
                    Intent intent = new Intent(AdvertisingActivity.this,NavigationPageActivity.class);
                    startActivity(intent);
                    finish();
                }
            };
        }
        timer.start();
    }

    private void findView() {
        jumpBtn = findViewById(R.id.bt_spread_Jump);
        tvTime = findViewById(R.id.tv_time);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}