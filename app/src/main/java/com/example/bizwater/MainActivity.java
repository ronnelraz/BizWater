package com.example.bizwater;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.bizwater.func.Func;

public class MainActivity extends AppCompatActivity {

    Func controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controller = new Func(this);


        new Handler().postDelayed(() -> {
            controller.intent(Login.class,this); //intent
            finish();
        },5000);
    }
}