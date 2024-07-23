package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Surface_view extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new Playground(this));
    }
}
