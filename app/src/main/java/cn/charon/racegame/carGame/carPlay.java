package cn.charon.racegame.carGame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import cn.charon.racegame.R;

/**
 * Created by user on 23-11-2017.
 */

public class carPlay {

    Bitmap bp;//视图
     int g;
     int x,y,score=0;
     int sx,sy;
    boolean b;
    carPlay(Context ct, int screenx, int screeny)
    {
        sx=screenx;sy=screeny;
        bp= BitmapFactory.decodeResource(ct.getResources(), R.drawable.car);
        x=screenx/2-bp.getWidth()/2; //初始正中
        y=screeny-bp.getHeight();
        b=false;
        g=15;

    }
    void moveLeft(){
        if (x>=0)
        {
            x-=50;
        }
    }
    void moveRight(){
        if (x+bp.getWidth()<=sx)
        {
            x+=50;
        }
    }
    void moveTop(){
        if (y>=50)
        {
            y-=50;
        }
    }
    void moveDown(){
        if (y+bp.getHeight()<=sy)
        {
            y+=50;
        }
    }
    void change(){
       /* y=y+g;
        if(b){

            x=x+10;
            y=y-30;
        }

        else{
            x=x+10;
        }

        if(x>=x1){
            x=0;
            score=score+5;
        }*/


    }

    public void addScore() {
        score+=5;
    }
    public Bitmap getBp() {
        return bp;
    }

    public void setBp(Bitmap bp) {
        this.bp = bp;
    }
    public int getScore(){return score;}

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }




}
