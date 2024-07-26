package com.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

public class CustomWebDialog {

    private Dialog dialog;
    private WebView webView;
    private TextView btnClose;

    public void show(Context context, String url) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_webview);

        webView = dialog.findViewById(R.id.webView);
        btnClose = dialog.findViewById(R.id.btnClose);

        // 设置WebView属性
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);

        // 设置关闭按钮点击事件
        btnClose.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
