package cn.charon.racegame.carGame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import cn.charon.racegame.R;

/**
 * 爆炸效果
 */

public class Boom {
    private Bitmap bitmap;
    private int x=0;
    private int y=0;
    public Boom(Context context)
    {
        bitmap= BitmapFactory.decodeResource(context.getResources(), R.drawable.boom);
    }

    public void setX(int x){this.x=x;}
    public void setY(int y){this.y=y;}

    public Bitmap getBitmap(){return bitmap;}
    public int getX(){return x;}
    public int getY(){
        return y;
    }
}
