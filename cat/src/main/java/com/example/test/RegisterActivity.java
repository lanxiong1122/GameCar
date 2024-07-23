package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton returnbtn;
    EditText register_account,register_password1,register_password2;
    Button register_registerbtn;
    MyHelper db =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        returnbtn=findViewById(R.id.returnbtn);
        register_account=findViewById(R.id.register_account);
        register_password1=findViewById(R.id.register_password1);
        register_password2=findViewById(R.id.register_password2);
        register_registerbtn=findViewById(R.id.register_registerbtn);

        returnbtn.setOnClickListener(this);
        register_registerbtn.setOnClickListener(this);

        db = new MyHelper(this,this.getFilesDir().toString()+"/sdcard/mysql.db3",null,1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.returnbtn:
                Intent intent1 = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent1);
                break;
            case R.id.register_registerbtn:
                String Account=register_account.getText().toString().trim();
                String Password1=register_password1.getText().toString().trim();
                String Password2=register_password2.getText().toString().trim();
                if (Account.equals("") || Password1.equals("") || Password2.equals("")) {
                    Toast.makeText(this, "输入不能为空！", Toast.LENGTH_SHORT).show();
                }else {
                    if (Password1.equals(Password2)) {
                        String addSql = "insert into mysql(Name,Text) values(?,?);";
                        db.getReadableDatabase().execSQL(addSql, new String[]{Account, Password1});
                        Toast.makeText(this, "注册成功", Toast.LENGTH_LONG).show();
                        //进行页面跳转
                        Intent intent2 = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent2);
                    } else {
                        Toast.makeText(RegisterActivity.this, "前后两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:break;
        }
    }
}
