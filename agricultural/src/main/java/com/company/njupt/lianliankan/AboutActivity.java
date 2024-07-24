package com.company.njupt.lianliankan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.company.njupt.lianliankan.openwebview.OpenWebActivity;
import com.company.njupt.lianliankan.openwebview.WebUtilsConfig;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.freeprivacypolicy.com/live/a15a02ec-ebc7-496e-921f-eb0047f2d003";
                WebUtilsConfig config =
                        new WebUtilsConfig()
                                .setTitleBackgroundColor(R.color.colorPrimary)//设置标题栏背景色
                                .setBackText("")//设置返回按钮的文案
                                .setBackBtnRes(R.mipmap.arrow_left_white)//设置返回按钮的图标
                                .setMoreBtnRes(R.mipmap.more_web)//设置更多按钮的图标
                                .setShowBackText(true)//设置是否显示返回按钮的文案
                                .setShowMoreBtn(false)//设置是否显示更多按钮
                                .setShowTitleLine(false)//设置是否显示标题下面的分割线
                                .setShowTitleView(true)//设置是否显示标题栏，网页是全屏的时候可以选择隐藏标题栏
                                .setTitleBackgroundRes(-1)//设置标题栏背景资源
                                .setBackTextColor(-1)//设置返回按钮的文案颜色
                                .setTitleTextColor(-1)//设置标题文字颜色
                                .setStateBarTextColorDark(false)//设置状态栏文字颜色是否是暗色，如果你设置了标题栏背景颜色为白色，这里需要设置true，否则状态栏看不到文案了
                                .setTitleLineColor(R.color.app_title_color);//设置标题栏下面的分割线的颜色
                OpenWebActivity.openWebView(AboutActivity.this, url, config);
            }
        });
    }
}
