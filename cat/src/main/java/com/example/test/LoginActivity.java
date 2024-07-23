package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton loginbtn;
    EditText account,password;
    Button login_registerbtn;
    MyHelper db =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //隐藏ActionBar
        getSupportActionBar().hide();

        login_registerbtn=findViewById(R.id.login_registerbtn);
        account=findViewById(R.id.account);
        password=findViewById(R.id.password);
        loginbtn=findViewById(R.id.loginbtn);

        loginbtn.setOnClickListener(this);
        login_registerbtn.setOnClickListener(this);

        db = new MyHelper(this,this.getFilesDir().toString()+"/sdcard/mysql.db3",null,1);
        System.out.println(this.getFilesDir().toString()+"/sdcard/mysql.db3");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_registerbtn:
                Intent intent1=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent1);
                break;
            case R.id.loginbtn:
                String Account = account.getText().toString().trim();
                String Password = password.getText().toString().trim();
                if (Account.equals("") || Password.equals("")) {
                    Toast.makeText(this, "输入不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    String searchName = "select * from mysql where name="+Account+" and text="+Password;
                    Cursor cursor = db.getWritableDatabase().rawQuery(searchName, null);
                    if (cursor.getCount() == 1){
                        cursor.moveToNext();
                        String myaccount = cursor.getString(1);
                        String mypassword = cursor.getString(2);
                        Log.i("[查询结果]", "账号:" + myaccount + "密码:" + mypassword);
                        Toast.makeText(this, "登录成功，请查看日志", Toast.LENGTH_LONG).show();
                        Intent intent2 = new Intent(LoginActivity.this,IndexActivity.class);
                        startActivity(intent2);
                    }else {
                        Toast.makeText(this, "登录失败，请查看日志", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            default:break;
        }
    }
}
