package com.example.bizwater;


import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.os.SystemClock;
import android.text.BoringLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.bizwater.func.Func;

import butterknife.ButterKnife;

public class Simulation extends AppCompatActivity {


    Func controller;


    SurfaceView surfaceView;
    CustomViewer customViewer;
    MotionEvent motionEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_simulation);
        ButterKnife.bind(this);
        controller = new Func(this);

        surfaceView = findViewById(R.id.surface);
        customViewer = new CustomViewer();
        customViewer.loadEntity();
        customViewer.setSurfaceView(surfaceView);
        customViewer.loadGlb(this, "grogu", "rr");
        customViewer.loadIndirectLight(this,"venetian_crossroads_2k");

    }





    @Override
    protected void onResume() {
        super.onResume();
        customViewer.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        customViewer.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        customViewer.onDestroy();
    }


    public void back(View view) {
        Func.intent(Home.class,view.getContext());
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}