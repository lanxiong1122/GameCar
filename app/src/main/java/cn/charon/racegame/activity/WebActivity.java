package cn.charon.racegame.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.TextView;

import cn.charon.racegame.R;

public class WebActivity extends AppCompatActivity {
    TextView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//无标题
        setContentView(R.layout.activity_web);
        findViewById(R.id.imageViewBackArrow).setOnClickListener(view -> finish());
        titleView = findViewById(R.id.textViewTitle);
        WebView webView = findViewById(R.id.webView);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String url = intent.getStringExtra("url");

        // 使用接收到的数据
        if (title != null) {
            // 设置标题
            titleView.setText(title);
        }
        if (url != null) {
            // 加载网页
            webView.loadUrl(url);
        }
    }
}