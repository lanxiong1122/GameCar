package cn.charon.racegame.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

import cn.charon.racegame.R;
import cn.charon.racegame.carGame.GameView;
import cn.charon.racegame.music.VolumsPlayer;
import cn.charon.racegame.music.musicPlayer;

public class CarActivity extends AppCompatActivity implements View.OnClickListener, GameView.gameOverListener {

    public static int direction = 0;//点击方向标志 0左 1右 2上 3下
    SharedPreferences sp;//内置数据库保存历史分数
    public static List<Map<String, String>> scoreList = new ArrayList<>();
    Handler handler = new Handler();
    //用于标记道路滚动步数
    int roadState = 0;
    private GameView carSurface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//无标题
        //视图在onresume周期中绘制
    }

    /**
     * 初始化视图组件
     */
    private void initView() {
        //获取本地数据库
        sp = getSharedPreferences("HistoryInfo", 0x0000);
        carSurface = findViewById(R.id.car_surface);
        carSurface.setGameOverListener(this);
        //carSurface.setZOrderOnTop(true);
        Button btn_left = findViewById(R.id.left);
        Button btn_right = findViewById(R.id.right);
        Button btn_top = findViewById(R.id.top);
        Button btn_down = findViewById(R.id.down);
        btn_left.setOnClickListener(this);
        btn_right.setOnClickListener(this);
        btn_top.setOnClickListener(this);
        btn_down.setOnClickListener(this);
        carSurface.setOnTouchListener(new CarSlideClick());
        VolumsPlayer.init(this);//初始化音效池
        musicPlayer.init(this);//初始化背景音乐
        musicPlayer.play();//播放
        initHistoryScore();//分数排名初始化
    }

    /**
     * 初始化本地分数列表 本类存有最新排名分数
     */
    private void initHistoryScore() {
        //这里用到了gson 把json格式转化为map数据 成功获取到之前保存的分数
        Gson gson=new Gson();
        String strJson=sp.getString("scoreHistory","");
        Log.w("json_return" ,strJson);
        if(!strJson.equals(""))
        {
            scoreList=gson.fromJson(
                    strJson,new TypeToken<List<Map<String,String>>>() {
                    }.getType());
        }
    }
    /**
     * 游戏结束提示
     */
    private void gameOverTip(String data, int score) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(CarActivity.this);
        dialog.setMessage("game over! This game scores " + score + "point");
        dialog.setCancelable(false);
        dialog.setNeutralButton("Return to the mainPage", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dialog.setPositiveButton("Save Score", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveScore(data, score);
            }
        });
        dialog.show();
    }

    /**
     * 游戏返回提示
     */
    private void gameBackTip() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(CarActivity.this);
        dialog.setMessage("Are you sure you want to exit this game? After exiting, game records will not be saved");
        dialog.setCancelable(false);
        dialog.setNeutralButton("Continue playing", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //carSurface.conTinue();
            }
        });
        dialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                carSurface.setVisibility(View.GONE);
            }
        });
        dialog.show();
    }

    /**
     * 保存分数
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void saveScore(String data, int score) {
        Map<String, String> score1 = new HashMap<>();
        score1.put("time", data);
        score1.put("score", score + "");
        scoreList.add(score1);
        //获取编辑权
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        //将list转换为json数据 并排序操作
        Collections.sort(scoreList, new Comparator<Map<String, String>>() {
            @Override
            public int compare(Map<String, String> o1, Map<String, String> o2) {
                return o2.get("score").compareTo(o1.get("score"));
            }
        });
        //java8Stream对List包含的map中的key进行去重 限制sdk版本 >=26
        scoreList = scoreList.stream().collect(
                Collectors.collectingAndThen(Collectors.toCollection(
                        () -> new TreeSet<>(Comparator.comparing(m -> m.get("score")))), ArrayList::new));
        //排序去重后加入value排名
        for (int i = 0; i < scoreList.size(); i++) {
            scoreList.get(i).put("ranking",i+"");
        }
        String strJson = gson.toJson(scoreList);
        editor.putString("scoreHistory", strJson);
        editor.apply();
        Toast.makeText(this
                , "Score recorded", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onPause() {
        Log.w("activity_status", "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w("activity_status", "onStop");
        VolumsPlayer.ReleaseIt();//释放音效资源
        musicPlayer.ReleaseIt();//释放音乐资源
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w("activity_status", "onDestroy");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //home切换时重绘界面 修复因游戏中途退出应用再返回的异常
        setContentView(R.layout.activity_car);
        initView();
        Log.w("activity_status", "onResume");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.w("activity_status", "onSaveInstanceState");
    }

    @Override
    public void onBackPressed() {
        gameBackTip();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left:
                carSurface.moveLeft();
                break;
            case R.id.right:
                carSurface.moveRight();
                break;
            case R.id.top:
                carSurface.moveTop();
                break;
            case R.id.down:
                carSurface.moveDown();
                break;
            default:
        }
    }

    @Override
    public void gameOver(String data, int score) {
        //此处测试发现surfaceview线程未切换 使用handler更新UI
        handler.post(new Runnable() {
            @Override
            public void run() {
                VolumsPlayer.playit(R.raw.gang1);//碰撞音效
                gameOverTip(data, score);
            }
        });
    }


    class CarSlideClick implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                carSurface.setLocation(event.getX()-40,event.getY()-100);
            }
            return true;
        }
    }


}