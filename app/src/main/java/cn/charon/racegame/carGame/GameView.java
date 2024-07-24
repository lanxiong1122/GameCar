package cn.charon.racegame.carGame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 游戏动态界面绘制 /2021/10/16
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback{
    carPlay mainCar;//游戏主车
    EnemyCar enemyCar1;//障碍车1号
    EnemyCar2 enemyCar2;//障碍车2号
    EnemyCar3 enemyCar3;//障碍车3号
    Context context;
    SurfaceHolder sh;
    MyThread mt;
    Boom boom;//爆炸效果
    Rect main;//主赛场矩形区域
    Rect enemyRect1;//障碍车1号矩形区域
    Rect enemyRect2;//障碍车2号矩形区域
    Rect enemyRect3;//障碍车3号矩形区域
    gameOverListener listener;
    boolean isContinue=true;
    public void setGameOverListener(gameOverListener listener)
    {
        this.listener=listener;
    }
    public GameView(Context context) {
        super(context);
        initStyle(context);//初始化各自参数
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initStyle(context);//初始化各自参数
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initStyle(context);//初始化各自参数
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initStyle(context);//初始化各自参数
    }

    private void initStyle(Context context) {
        this.context=context;
        DisplayMetrics dm=context.getResources().getDisplayMetrics();
        mainCar = new carPlay(context,dm.widthPixels,dm.heightPixels);
        enemyCar1=new EnemyCar(context,dm.widthPixels,dm.heightPixels);
        enemyCar2=new EnemyCar2(context,dm.widthPixels,dm.heightPixels);
        enemyCar3=new EnemyCar3(context,dm.widthPixels,dm.heightPixels);
        boom =new Boom(context);
        sh=getHolder();
        mt = new MyThread(this);
        sh.addCallback(this);
    }

    Paint ps=new Paint();
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int score=mainCar.getScore();
        canvas.drawColor(Color.WHITE);
        ps.setColor(Color.BLUE);
        ps.setTextSize(70f);
        canvas.drawText("Score:"+score,50,90,ps);
        drawLoad(canvas);
        main=new Rect(mainCar.getX(),mainCar.getY(),mainCar.getX()+mainCar.getBp().getWidth(),mainCar.getY()+mainCar.getBp().getHeight());
        enemyRect1=new Rect(enemyCar1.getX(),enemyCar1.getY(),enemyCar1.getX()+enemyCar1.getBitmap().getWidth(),enemyCar1.getY()+enemyCar1.getBitmap().getHeight());
        enemyRect2=new Rect(enemyCar2.getX(),enemyCar2.getY(),enemyCar2.getX()+enemyCar2.getBitmap().getWidth(),enemyCar2.getY()+enemyCar2.getBitmap().getHeight());
        enemyRect3=new Rect(enemyCar3.getX(),enemyCar3.getY(),enemyCar3.getX()+enemyCar3.getBitmap().getWidth(),enemyCar3.getY()+enemyCar3.getBitmap().getHeight());
        //矩形有相交区域则产生碰撞
        canvas.drawBitmap(mainCar.getBp(),mainCar.getX(),mainCar.getY(),null);
        canvas.drawBitmap(enemyCar1.getBitmap(),enemyCar1.getX(),enemyCar1.getY(),null);
        canvas.drawBitmap(enemyCar2.getBitmap(),enemyCar2.getX(),enemyCar2.getY(),null);
        canvas.drawBitmap(enemyCar3.getBitmap(),enemyCar3.getX(),enemyCar3.getY(),null);
        if(Rect.intersects(main,enemyRect1)||Rect.intersects(main,enemyRect2)||Rect.intersects(main,enemyRect3)) {
            //爆炸效果绘制
            boom.setX(mainCar.getX()+mainCar.getBp().getWidth()-150);
            boom.setY(mainCar.getY()-45);
            canvas.drawBitmap(boom.getBitmap(),boom.getX(),boom.getY(),null);
            mt.isrunning(false);
            //回调监听
            //String timeFormat = "yyyy年MM月dd日";
            String timeFormat = "MMMM dd, yyyy";
            SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(timeFormat);
            String str =  TIME_FORMAT.format(new Date());
            listener.gameOver(str,score);
            //此处游戏结束
            Log.w("gameview_change","gameover:");
        }
        enemyCar1.change();//改变位置
        enemyCar2.change();//改变位置
        enemyCar3.change();//改变位置
        if (enemyCar1.getY()-enemyCar1.getBitmap().getHeight()>=enemyCar1.screeny)
        {
            enemyCar1.initData();
            mainCar.addScore();//得分增加
        }
        if (enemyCar2.getY()>=enemyCar2.screeny)
        {
            enemyCar2.initData();
            mainCar.addScore();//得分增加
        }
        if (enemyCar3.getY()>=enemyCar3.screeny)
        {
            enemyCar3.initData();
            mainCar.addScore();//得分增加
        }

    }

    /**
     * 绘制公路车道
     * @param canvas
     */
    private void drawLoad(Canvas canvas) {
        ps.setColor(Color.BLACK);
        int start_x= enemyCar1.screenx/3;
        int width=10;//车道矩形小块宽度
        int heigh=80;//车道矩形小块高度
        int start_y= enemyCar1.screeny/15;
        for(int i=1;i<=18;i++)
        {
            //绘制第一条线
            Rect rect=new Rect(start_x,start_y*i,start_x+width,start_y*i+heigh);
            //绘制第二条线
            Rect rect1=new Rect(start_x*2,start_y*i,start_x*2+width,start_y*i+heigh);
            canvas.drawRect(rect,ps);
            canvas.drawRect(rect1,ps);
        }
    }

    //视图创建
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        conTinue();
        /*mt.isrunning(true);
        mt.start();*/
        Log.w("gameview_change","surfaceCreated:");
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        Log.w("gameview_change","surfaceChanged:");
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        Log.w("gameview_change","surfaceDestroyed:");
     //   holder.removeCallback(this);
        stop();
    }


    public void moveLeft()
    {
        mainCar.moveLeft();
    }

    public void moveRight()
    {
        mainCar.moveRight();
    }
    public void moveTop()
    {
        mainCar.moveTop();
    }

    public void moveDown()
    {
        mainCar.moveDown();
    }

    public void stop()//停止游戏
    {
        mt.isStop=true;
        mt.isrunning(false);
        //this.setVisibility(View.GONE);
    }

    public void conTinue()//继续游戏
    {
        mt.isStop=false;
        mt.isrunning(true);
        mt.start();
    }

    /**
     * activity外部滑动设置位置
     */
    public void setLocation(float x1,float y1)
    {
       mainCar.setX((int) x1);mainCar.setY((int)y1);
    }
   public interface gameOverListener{
        void gameOver(String data,int score);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
