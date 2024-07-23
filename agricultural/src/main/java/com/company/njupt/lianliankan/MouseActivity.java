package com.company.njupt.lianliankan;

import android.Manifest;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.company.njupt.lianliankan.R;

import java.io.IOException;
import java.util.Random;

public class MouseActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tvScore;
    private TextView tvTime;
    private Button btnStart;
    private Button btnStop;
    private ImageView[]imgsViews=new ImageView[12];
    private MyDownTimer downTimer;
    private int[] imgIds={R.id.img11,R.id.img12,R.id.img21,R.id.img22,R.id.img23,
            R.id.img24,R.id.img31,R.id.img32,R.id.img33,R.id.img34,R.id.img41,R.id.img42};
    private SoundPool pool;
    private int sd1,sd2;
    private MusicService.MyMusicBinder binder=null;
    private ServiceConnection conn=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
               binder=(MusicService.MyMusicBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //完全全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mouse_game);
        initViews();
        pool=new SoundPool(2, AudioManager.STREAM_SYSTEM,0);
        sd1=pool.load(MouseActivity.this,R.raw.alert,1);
        sd2=pool.load(MouseActivity.this,R.raw.hit,1);
        int version= Build.VERSION.SDK_INT;
        if(version>=23){
            //进行动态的权限申请
            if (ContextCompat.checkSelfPermission(MouseActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MouseActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
//启动service
        Intent intent=new Intent(MouseActivity.this,MusicService.class);
        bindService(intent,conn,BIND_AUTO_CREATE);
    }

    private void initViews() {
        tvScore = (TextView) findViewById(R.id.tv_score);
        tvTime = (TextView) findViewById(R.id.tv_time);
        for(int i=0;i<imgsViews.length;i++){
            imgsViews[i]=findViewById(imgIds[i]);
            imgsViews[i].setClickable(true);
            imgsViews[i].setOnClickListener(MouseActivity.this);
        }
        btnStop = (Button) findViewById(R.id.btn_stop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameOver();
            }
        });
        btnStart = (Button) findViewById(R.id.btn_start);
        //为开始按钮添加单击事件
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //启动倒计时
                if(downTimer!=null){
                    downTimer.cancel();
                    downTimer=null;
                }
                downTimer=new MyDownTimer(60*1000,1000);
                downTimer.start();
                //启动游戏运行
                gameStart();

            }


        });
    }
    //开始游戏
    private int oldpos=0;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                int pos=(int) (Math.random()*100%12);
                while (oldpos==pos){
                    pos=(int) (Math.random()*100%12);
                }
                imgsViews[pos].setImageResource(R.drawable.hitmouse);
                imgsViews[oldpos].setImageResource(R.drawable.dong);
                oldpos=pos;
                handler.sendEmptyMessageDelayed(1,1000);
            }
        }
    };
    private void gameStart() {
        handler.removeCallbacksAndMessages(null);
        handler.sendEmptyMessage(1);
        for(int i=0;i<imgsViews.length;i++) {
            imgsViews[i].setEnabled(true);
        }
        score=0;
        tvScore.setText("得分"+score);
    }

    private void gameOver() {
        handler.removeCallbacksAndMessages(null);
        if(downTimer!=null){
            downTimer.cancel();
            downTimer=null;
        }
        for(int i=0;i<imgsViews.length;i++) {
            imgsViews[i].setEnabled(false);
        }
    }
    private int score=0;
    @Override
    public void onClick(View view) {
        if(view.getId()==imgsViews[oldpos].getId()){
            score+=3.4f;
            if(score>100)score=100;
            tvScore.setText("得分"+score);
            pool.play(sd2,1,1,1,0,1);
            if(score==12||score==48||score==72){
                Random random = new Random();
                String[] know=this.getResources().getStringArray(R.array.know);
                int index = random.nextInt(know.length);
                String knowledge = know[index];
                //初始化游戏胜利的对话框
                AlertDialog.Builder successDialog = createDialog("小知识", knowledge,
                        R.drawable.success).setNegativeButton("关闭",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                successDialog.show();
            }
        }
    }
    // 创建对话框的工具方法
    private AlertDialog.Builder createDialog(String title, String message, int imageResource) {
        return new AlertDialog.Builder(this).setTitle(title)
                .setMessage(message).setIcon(imageResource).setCancelable(false);
    }

    class MyDownTimer extends CountDownTimer{

        public MyDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            //改变倒计时显示的时间
            //判断activity是否在运行
            if(MouseActivity.this.isFinishing()){
                //取消倒计时
                downTimer.cancel();
            }
            else{
                int hour=(int) (l/1000/3600);//小时
                int minut=(int) (l/1000%3600/60);//分钟
                int second=(int) (l/1000%3600%60);//秒
                SpannableString str = new SpannableString("倒计时："+second);
                if(second<=5){
                    pool.play(sd1,1,1,1,0,1);
                    str.setSpan(new ForegroundColorSpan(Color.BLUE),4,str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    str.setSpan(new RelativeSizeSpan(1.5f),4,str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                tvTime.setText(str);
                if(second==0){
                    handler.removeCallbacksAndMessages(null);
                }
            }

        }

        @Override
        public void onFinish() {
            Toast.makeText(MouseActivity.this, "游戏结束！", Toast.LENGTH_SHORT).show();

        }
    }
    private MediaPlayer mp=new MediaPlayer();
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.musiccontrol,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_play:
                //startService(new Intent(MouseActivity.this,MusicService.class));
                 binder.play();
                break;
            case R.id.menu_stop:
               //stopService(new Intent(MouseActivity.this,MusicService.class));
                binder.stop();
                break;
            case R.id.menu_pause:
                binder.pause();

        }
        return super.onOptionsItemSelected(item);
    }
*/
    @Override
    protected void onPause() {
        super.onPause();
        if(downTimer!=null){
            downTimer.cancel();
            downTimer=null;
        }
        handler.removeCallbacksAndMessages(null);
        if(pool!=null){
            pool.autoPause();
            pool.release();
            pool=null;
        }
        if(mp!=null&&mp.isPlaying()){
            mp.stop();
        }
        if(mp!=null)mp.release();
        mp=null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(conn!=null)
        unbindService(conn);
    }
}
