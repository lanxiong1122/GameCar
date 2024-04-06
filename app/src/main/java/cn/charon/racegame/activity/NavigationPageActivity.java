package cn.charon.racegame.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cn.charon.racegame.R;

public class NavigationPageActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnStartGame;
    private Button btnCheckRanking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_page);
        findView();
        btnCheckRanking.setOnClickListener(this);
        btnStartGame.setOnClickListener(this);
    }

    private void findView() {
        btnStartGame = findViewById(R.id.btn_start_game);
        btnCheckRanking = findViewById(R.id.btn_check_ranking);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.btn_start_game:
                intent = new Intent(this, CarActivity.class);
                break;
            case R.id.btn_check_ranking:
                intent = new Intent(this,RankPageActivity.class);
                break;
            default:
        }
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}