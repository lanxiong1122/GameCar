package com.example.test;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;

import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import java.util.Vector;


public class Playground extends SurfaceView implements View.OnTouchListener {
    private static final int COL = 10, ROW = 10;
    private static int WIDTH = 100;
    private static final int BLOCKS = 10;//默认添加的路障数量
    private Dot matrix[][];
    private Dot cat;

    private static Context this_context;

    public Playground(Context context) {
        super(context);
        this_context = context;
        getHolder().addCallback(callback);
        matrix = new Dot[ROW][COL];
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                matrix[i][j] = new Dot(j, i);
            }
        }
        setOnTouchListener(this);
        initGame();
    }
    //因为二维数组坐标与游戏界面坐标相反，为了方便直接封装一个函数
    private Dot getDot(int x, int y) {
        return matrix[y][x];
    }
    //判断是否是边界
    private boolean isAtEdge(Dot d) {
        if (d.getX() * d.getY() == 0 || d.getX() + 1 == COL || d.getY() + 1 == ROW) {
            return true;
        }
        return false;
    }
    //每一个点获取相邻点的方法
    private Dot getNeighbour(Dot d, int dir) {
        switch (dir) {
            case 1:
                return getDot(d.getX() - 1, d.getY());
            case 2:
                if (d.getY() % 2 == 0) {
                    return getDot(d.getX() - 1, d.getY() - 1);
                } else {
                    return getDot(d.getX(), d.getY() - 1);
                }
            case 3:
                if (d.getY() % 2 == 0) {
                    return getDot(d.getX(), d.getY() - 1);
                } else {
                    return getDot(d.getX() + 1, d.getY());
                }
            case 4:
                return getDot(d.getX() + 1, d.getY());
            case 5:
                if (d.getY() % 2 == 0) {
                    return getDot(d.getX(), d.getY() + 1);
                } else {
                    return getDot(d.getX() + 1, d.getY() + 1);
                }
            case 6:
                if (d.getY() % 2 == 0) {
                    return getDot(d.getX() - 1, d.getY() + 1);
                } else {
                    return getDot(d.getX(), d.getY() + 1);
                }
            default:break;
        }
        return null;
    }
    //获取距离的方法
    private int getDistance(Dot d,int dir){
        int distance=0;
        Dot ori = d,next;
        while (true){
            next=getNeighbour(ori,dir);
            if (next.getStatus()==Dot.STATUS_ON){
                return distance*-1;
            }
            if (isAtEdge(next)){
                distance++;
                return distance;
            }
            distance++;
            ori=next;
        }
    }

    /*猫的移动状态改变*/
    private void MoveTo(Dot d){
        d.setStatus(Dot.STATUS_IN);
        getDot(cat.getX(),cat.getY()).setStatus(Dot.STATUS_OFF);
        cat.setXY(d.getX(),d.getY());
    }
    //猫的移动
    private void move(){
        if (isAtEdge(cat)){
            lose();
            return;
        }
        Vector<Dot> avaliable = new Vector<>();
        for (int i = 1; i < 7; i++) {
            Dot n =getNeighbour(cat,i);
            if (n.getStatus()==Dot.STATUS_OFF){
                avaliable.add(n);
            }
        }
        if (avaliable.size()==0){
            win();
        }else {
            MoveTo(avaliable.get(0));
        }
    }

    private void lose(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this_context);
//                设置图标，按钮，内容
        builder.setIcon(R.drawable.lose);
        builder.setTitle("神经猫跑掉了！");
//        builder.setMessage("内容");
        builder.setPositiveButton("再试一次", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(this_context,"再来一次"+which,Toast.LENGTH_SHORT).show();
                this_context.startActivity(new Intent(this_context, Surface_view.class));
            }
        });
        builder.setNegativeButton("返回主菜单", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(this_context,"返回主菜单"+which,Toast.LENGTH_SHORT).show();
                this_context.startActivity(new Intent(this_context,IndexActivity.class));
            }
        });
        //这是内置方法让点击对话框外面的区域时，对话框不消失
        builder.setCancelable(false);
        builder.show();
        Toast.makeText(getContext(),"Lose",Toast.LENGTH_LONG).show();
    }

    private void win(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this_context);
//                设置图标，按钮，内容
        builder.setIcon(R.drawable.win);
        builder.setTitle("恭喜你！你成功的抓住了神经猫！");
//        builder.setMessage("内容");
        builder.setPositiveButton("再来一次", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(this_context,"再来一次"+which,Toast.LENGTH_SHORT).show();
                this_context.startActivity(new Intent(this_context, Surface_view.class));
            }
        });
        builder.setNegativeButton("返回主菜单", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(this_context,"返回主菜单"+which,Toast.LENGTH_SHORT).show();
                this_context.startActivity(new Intent(this_context,IndexActivity.class));
            }
        });
        //这是内置方法让点击对话框外面的区域时，对话框不消失
        builder.setCancelable(false);
        builder.show();
        Toast.makeText(getContext(),"Win",Toast.LENGTH_LONG).show();
    }

    //界面绘制
    private void redraw() {
        Canvas c = getHolder().lockCanvas();
        c.drawColor(Color.LTGRAY);//背景颜色
        Paint paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);//打开抗锯齿
        for (int i = 0; i < ROW; i++) {
            int offsett = 0;
            if (i % 2 != 0) {
                offsett = WIDTH / 2;
            }
            for (int j = 0; j < COL; j++) {
                Dot one = getDot(j, i);
                switch (one.getStatus()) {
                    case Dot.STATUS_OFF:
                        paint.setColor(0xFFEEEEEE);
                        break;
                    case Dot.STATUS_ON:
                        paint.setColor(0xFFFFAA00);
                        break;
                    case Dot.STATUS_IN:
                        paint.setColor(0xFFFF0000);
                        break;
                    default:
                        break;
                }
                c.drawOval(new RectF(one.getX() * WIDTH + offsett, one.getY() * WIDTH, (one.getX() + 1) * WIDTH + offsett, (one.getY() + 1) * WIDTH), paint);
            }
        }
        getHolder().unlockCanvasAndPost(c);//将Canvas对象传递进来，取消Canvas锁定，把绘图内容更新到界面上
    }

    SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
            redraw();//界面第一次显示时，将指定内容显示到界面上
        }

        @Override
        public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i1, int i2, int i3) {
            WIDTH = i2 / (COL + 1);//对屏幕进行适配
            redraw();
        }

        @Override
        public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

        }
    };
    //游戏初始化
    private void initGame() {
        //将每一个元素设置成STATUS_OFF（即没有路障）
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                matrix[i][j].setStatus(Dot.STATUS_OFF);
            }
        }
        //设置猫的初始坐标，以及状态为STATUS_IN
        cat = new Dot(4, 5);
        getDot(4, 5).setStatus(Dot.STATUS_IN);
        //游戏初始化时随便生成一定数量的路障
        for (int i = 0; i < BLOCKS; ) {
            int x = (int) (Math.random() * 1000) % COL;
            int y = (int) (Math.random() * 1000) % ROW;
            if (getDot(x, y).getStatus() == Dot.STATUS_OFF) {
                getDot(x, y).setStatus(Dot.STATUS_ON);
                i++;//将i++放这里，为了使语句要满足条件（避免路障被重复添加）才能进行下一次循环
                System.out.println("Block" + i);
            }

        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_UP) {
//            Toast.makeText(getContext(), e.getX() + ":" + e.getY(), Toast.LENGTH_LONG).show();
            int x, y;
            y = (int) (e.getY() / WIDTH);
            if (y % 2 == 0) {
                x = (int) (e.getX() / WIDTH);
            } else {
                x = (int) ((e.getX() - WIDTH / 2) / WIDTH);
            }
            /*鼠标点击到非游戏区域时会数组越界，可以进行条件判断进行数组保护*/
            if (x + 1 > COL || y + 1 > ROW) {
                initGame();
            } else if (getDot(x,y).getStatus()==Dot.STATUS_OFF){
                getDot(x, y).setStatus(Dot.STATUS_ON);
                move();
            }
            redraw();
        }
        return true;
    }
}
