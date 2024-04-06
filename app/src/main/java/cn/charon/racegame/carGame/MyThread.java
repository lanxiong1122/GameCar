package cn.charon.racegame.carGame;

import android.annotation.SuppressLint;
import android.graphics.Canvas;

/**
 *
 */

public class MyThread extends Thread{
    boolean flag=false;
    GameView gameView;
    public boolean isStop=false;

    public MyThread(GameView gv)
    {
        gameView=gv;

    }

    void isrunning(boolean flag)
    {
        this.flag=flag;
    }
    @SuppressLint("WrongCall")
    @Override
    public void run() {
        while (flag){
            Canvas c=null;
            try {
                Thread.sleep(100);
                synchronized (gameView.getHolder()){
                    c = gameView.getHolder().lockCanvas();
                    gameView.onDraw(c);

                }
            } catch (Exception e) {
            }
            finally {
                if (!isStop)
                {
                    gameView.getHolder().unlockCanvasAndPost(c);
                }
            }
        }
    }
}
