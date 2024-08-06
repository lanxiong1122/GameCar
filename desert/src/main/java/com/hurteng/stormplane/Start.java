package com.hurteng.stormplane;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import com.hurteng.stormplane.myplane.R;

public class Start extends Activity {
    TextView ourw;
    TextView goOur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 假设这是你的Activity或Fragment中的onCreate或onViewCreated方法
// 获取TextView
        TextView textView = findViewById(R.id.go);
        ourw = findViewById(R.id.our);
        goOur = findViewById(R.id.our_info);

// 创建一个放大缩小动画
        ObjectAnimator scaleAnimator = ObjectAnimator.ofFloat(textView, "scaleX", 1f, 1.2f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(textView, "scaleY", 1f, 1.2f);

// 设置动画时长
        scaleAnimator.setDuration(500);
        scaleYAnimator.setDuration(500);

// 设置循环播放
        scaleAnimator.setRepeatCount(ValueAnimator.INFINITE);
        scaleYAnimator.setRepeatCount(ValueAnimator.INFINITE);

// 设置重复模式
        scaleAnimator.setRepeatMode(ValueAnimator.REVERSE);
        scaleYAnimator.setRepeatMode(ValueAnimator.REVERSE);

// 启动动画
        scaleAnimator.start();
        scaleYAnimator.start();

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Start.this,MainActivity.class));
                finish();
            }
        });

        goOur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ourw.setVisibility(ourw.getVisibility() == View.GONE?View.VISIBLE:View.GONE);
                goOur.setText(ourw.getVisibility() == View.GONE?"Our Info":"Close");
            }
        });
        goOur.setText(ourw.getVisibility() == View.GONE?"Our Info":"Close");

        ourw.setMovementMethod(LinkMovementMethod.getInstance());
        // 读取assets目录下的ourinfo.txt文件
        try {
            InputStream inputStream = getAssets().open("ourinfo.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder content = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }

            reader.close();
            inputStream.close();

            ourw.setText(content.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
