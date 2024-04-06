package cn.charon.racegame.carGame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.Random;

import cn.charon.racegame.R;

/**
 * 障碍物：敌机1号
 */

public class EnemyCar {

    private Bitmap bitmap;
    private int x;
    private int y;

    int screenx;
    int screeny;
    private int loadNumber=3;//赛道数
    public EnemyCar(Context context, int screenx, int screeny){
        bitmap= BitmapFactory.decodeResource(context.getResources(), R.mipmap.enemytwo);
        this.screenx=screenx;this.screeny=screeny;
        initData();
    }
    public void initData()
    {
        Random r=new Random();
        int ranNumber=r.nextInt(3)+1;//[0,3)
        x=screenx/loadNumber*ranNumber-(screenx/loadNumber)/2-bitmap.getWidth()/2;//产生赛道随机位置
        Log.w("random",ranNumber+"---"+x);
        y= 0;
    }

    public void change()
    {
        y=y+50;
    }

    public Bitmap getBitmap(){ return bitmap;}
    public int getX(){return x;}
    public int getY(){return y;}



}
