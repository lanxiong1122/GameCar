package cn.charon.racegame.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import cn.charon.racegame.R;

public class NavigationPageActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnStartGame;
    private Button btnCheckRanking;
    private FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//无标题
        setContentView(R.layout.activity_navigation_page);
        findView();
        btnCheckRanking.setOnClickListener(this);
        btnStartGame.setOnClickListener(this);
        findViewById(R.id.text_more).setOnClickListener(this);
    }

    private void findView() {
        btnStartGame = findViewById(R.id.btn_start_game);
        btnCheckRanking = findViewById(R.id.btn_check_ranking);
        frameLayout = findViewById(R.id.web_layout);
        frameLayout.setOnTouchListener((view, motionEvent) -> true);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.btn_start_game:
                intent = new Intent(this, CarActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_check_ranking:
                intent = new Intent(this,RankPageActivity.class);
                startActivity(intent);
                break;
            case R.id.text_more:
                showCustomDialog();
                break;
            default:
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void showCustomDialog() {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = getLayoutInflater().inflate(R.layout.dialog_about, null);
            AlertDialog alertDialog = builder.setView(view).setCancelable(true).create();

            TextView textViewTitle = view.findViewById(R.id.textViewTitle);
            textViewTitle.setText("More");

            Button buttonPrivacyPolicy = view.findViewById(R.id.buttonPrivacyPolicy);
            buttonPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NavigationPageActivity.this, WebActivity.class);
                    intent.putExtra("title", "Privacy Policy");
                    intent.putExtra("url", "https://www.freeprivacypolicy.com/live/31179630-926d-41c9-96e3-ed67932629c6");
                    startActivity(intent);
                    alertDialog.dismiss();
                }
            });

            Button buttonAboutUs = view.findViewById(R.id.buttonAboutUs);
            buttonAboutUs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NavigationPageActivity.this, WebActivity.class);
                    intent.putExtra("title", "Our Mission");
                    intent.putExtra("url", "file:///android_asset/about.html");
                    startActivity(intent);
                    alertDialog.dismiss();
                }
            });
        alertDialog.show();
        }
}