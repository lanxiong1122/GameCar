package com.example.test;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * 警告：获取每个单元格的逻辑是依托xml布局实现的，请不要修改xml布局！！！
 * 为保障逻辑正常，所有后台处理的行/列下标均从 0/0 开始，
 * 我们约定，在棋盘数组中，0表示单元格可行走，1表示墙不可行走
 */
public class LayoutXml_view extends AppCompatActivity{
    //这个VG指代整个棋盘布局的LinearLayout
    ViewGroup VG;
    int chessboard[][];
    //这两个是移动向量组，根据奇偶行的不同在数组上有不同的移动策略
    final int[][] X1MoveVector={{0,-1},{-1,0},{-1,1},{0,1},{1,1},{1,0}};
    final int[][] X2MoveVector={{0,-1},{-1,-1},{-1,0},{0,1},{1,0},{1,-1}};
    //猫的当前位置
    int CatX;
    int CatY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //隐藏ActionBar
        getSupportActionBar().hide();

        //java默认新建的数组元素为0
        chessboard = new int[9][9];
        VG = findViewById(R.id.presenter_img);
        listenAllView();
        init_chessBoard();
    }
    //点击设置墙后，猫开始移动，并判断是否胜利
    public void CatMove(){
        //判断是否胜利
        switch (isWin(CatX,CatY)){
            case "继续":
                while (true){
                    int direction = (int) (Math.random() * 1000) % 6;
                    if (CatMovetoDirection(CatX,CatY, direction))break;
                }
                break;
            case "失败":
                System.out.println("失败");
                lose();
                break;
            case "成功":
                System.out.println("成功！");
                win();
                break;
        }
    }
    //判断是否胜利-失败-继续
    public String isWin(int x,int y){
        //猫的位置处理地图边线，失败
        if (x==0 || x==8 || y==0 || y==8)return "失败";
        /**
         * 这里注意，判断猫是否被堵住是根据chessboard来判断的
         *                    0 1 1                               1 1 0
         * 如果猫在第2行（x=1) 1 -1 1才是被堵住，但是在第3行（x=2）行 1 -1 1才是被抓住，这里存在奇偶行判断逻辑不一致的问题
         *                    0 1 1                               1 1 0
         */
        //无论是奇还是偶行，被抓住至少需要满足-上-下-左-右都有强，即是上下左右合为4
        if(chessboard[x-1][y]+chessboard[x+1][y]+chessboard[x][y-1]+chessboard[x][y+1]==4){
            if (chessboard[x-1][y+1]==1 && chessboard[x+1][y+1]==1)return "成功";
            if (chessboard[x-1][y-1]==1 && chessboard[x+1][y-1]==1)return "成功";
        }
        return "继续";
    }
    //猫移动的路径，每个点与周围的6个点想通，根据奇偶行的不同，使用不同的移动策略
    public boolean CatMovetoDirection(int x,int y,int direction){
        int toX;
        int toY;
        int[][] useMoveVector;
        //根据奇偶行的不同使用不同的移动策略
        if (x%2==1)useMoveVector = X1MoveVector;
        else useMoveVector = X2MoveVector;

        toX=x+useMoveVector[direction][0];
        toY=y+useMoveVector[direction][1];
        //跳转的目的地是墙，返回false，重新选择路径
        if (chessboard[toX][toY]==1)return false;
        //跳转的目的地可达
        if (chessboard[x][y]!=1) getViewOfRowColumn(x,y).setBackground(getDrawable(R.drawable.q_no));
        getViewOfRowColumn(toX,toY).setBackground(getDrawable((R.drawable.cat)));
        CatX = toX;CatY = toY;
        return true;
    }
    //监听xml布局下所有ImageView
    public void listenAllView(){
        int row_count=9;
        int column_count=9;
        for (int row=0;row<row_count;row++){
            for (int column=0;column<column_count;column++){
                final int finalRow = row;
                final int finalColumn = column;
                //获取坐标[0,0]到[8,8]的所有view并绑定监听
                final View IV =  getViewOfRowColumn(row,column);
                IV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (chessboard[finalRow][finalColumn] != 1){
                            System.out.println("你点击了第"+ (finalRow+1) +"行，第"+(finalColumn +1)+"个img");
                            IV.setBackground(getDrawable(R.drawable.q_yes));
                            chessboard[finalRow][finalColumn]=1;
                            CatMove();
                        }else {
                            Toast.makeText(LayoutXml_view.this,"This plot has already been blocked, please choose again",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }
    //获取对应行，列数的ImageView对象
    public View getViewOfRowColumn(int row,int column){
        View return_view = null;
        //依托xml布局结构，找出每一行的布局组件
        ViewGroup img_parent = (ViewGroup)VG.getChildAt(row);
        //第0行空格在末尾，第1行空格在开头，故要在奇偶行判断
        if (row%2==0){
            //偶行，img_parent的第column个孩子就是目标view
            return_view=img_parent.getChildAt(column);
        }else {
            //奇行，由于有空格的存在，img_parent的第column+1个孩子才是目标view
            return_view=img_parent.getChildAt(column+1);
        }
        return return_view;
    }
    //初始化棋盘
    public void init_chessBoard(){
        final int BLOCKS = 10;//默认添加的路障数量
        //设置猫的初始坐标，以及状态为STATUS_IN
        getViewOfRowColumn(4,4).setBackground(getDrawable(R.drawable.cat));
        CatX=4;CatY=4;
        //游戏初始化时随便生成一定数量的路障
        for (int i = 0; i < BLOCKS;) {
            int x = (int) (Math.random() * 1000) % 9;
            int y = (int) (Math.random() * 1000) % 9;
            if (chessboard[x][y]==0) {
                getViewOfRowColumn(x,y).setBackground(getDrawable(R.drawable.q_yes));
                i++;//将i++放这里，为了使语句要满足条件（避免路障被重复添加）才能进行下一次循环
                System.out.println("Block" + i);
                chessboard[x][y]=1;
            }
        }
    }
    private void lose(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                设置图标，按钮，内容
        builder.setIcon(R.drawable.lose);
        builder.setTitle("The cat ran away!");
        builder.setPositiveButton("do it again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(LayoutXml_view.this,"Once more"+which,Toast.LENGTH_SHORT).show();
                //关闭当前Activity,防止返回时，再次回到该页面
                LayoutXml_view.this.finish();
                startActivity(new Intent(LayoutXml_view.this, LayoutXml_view.class));
            }
        });
        builder.setNegativeButton("Return to the main Page", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(LayoutXml_view.this,"Return to the main Page"+which,Toast.LENGTH_SHORT).show();
                //关闭当前Activity,防止返回时，再次回到该页面
                LayoutXml_view.this.finish();
                startActivity(new Intent(LayoutXml_view.this,IndexActivity.class));
            }
        });
        //这是内置方法让点击对话框外面的区域时，对话框不消失
        builder.setCancelable(false);
        builder.show();
        Toast.makeText(this,"Lose",Toast.LENGTH_LONG).show();
    }
    private void win(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                设置图标，按钮，内容
        builder.setIcon(R.drawable.win);
        builder.setTitle("congratulations! You successfully caught the cat!");
//        builder.setMessage("内容");
        builder.setPositiveButton("Once more", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(LayoutXml_view.this,"Once more"+which,Toast.LENGTH_SHORT).show();
                //关闭当前Activity,防止返回时，再次回到该页面
                LayoutXml_view.this.finish();
                startActivity(new Intent(LayoutXml_view.this, LayoutXml_view.class));
            }
        });
        builder.setNegativeButton("Return to the main Page", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(LayoutXml_view.this,"Return to the main Page"+which,Toast.LENGTH_SHORT).show();
                //关闭当前Activity,防止返回时，再次回到该页面
                LayoutXml_view.this.finish();
                startActivity(new Intent(LayoutXml_view.this,IndexActivity.class));
            }
        });
        //这是内置方法让点击对话框外面的区域时，对话框不消失
        builder.setCancelable(false);
        builder.show();
        Toast.makeText(this,"Win",Toast.LENGTH_LONG).show();
    }
}
