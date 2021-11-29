package com.example.bizwater;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.opengl.EGLConfig;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Build;
import android.os.Bundle;


import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.example.bizwater.Model.m_categories;
import com.example.bizwater.Model.m_simulation;
import com.example.bizwater.adapter.Adapter_categories;
import com.example.bizwater.adapter.Adapter_simulation;
import com.example.bizwater.func.Func;
import com.novoda.merlin.Merlin;


import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.microedition.khronos.opengles.GL10;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Simulation extends AppCompatActivity {


    Func controller;


    SurfaceView surfaceView;
    CustomViewer customViewer;



    public static RecyclerView recyclerView;
    public static RecyclerView.Adapter adapter;
    public static List<m_simulation> simulation_list = new ArrayList<>();
    public static int[] img = {
            R.drawable.pic1,
            R.drawable.pic2,
            R.drawable.pic3,
            R.drawable.pic4,
            R.drawable.pic5,
            R.drawable.pic6,
            R.drawable.pic7,
            R.drawable.pic8,
            R.drawable.pic9,
            R.drawable.pic10,
            R.drawable.pic11
    };

    public static String[] name = {
            "10 uM Slim",
            "Raw Water Storage Tank",
            "Booster Pump",
            "Multimedia, Carbon and Water Softener",
            "Brine Tank",
            "5uM Slim, 1uM Slim",
            "Membrane",
            "Product Tank",
            "1uM Slim, CTO Slim",
            "UV Lamp Sterilizer",
            "Dispensing Unit"
    };


    @BindView(R.id.webview)
    WebView webview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_simulation);
        ButterKnife.bind(this);
        controller = new Func(this);




      for(int i = 0; i < img.length; i++){
          m_simulation item = new m_simulation(
                 img[i],
                 name[i]
          );
          simulation_list.add(item);
      }

        recyclerView = findViewById(R.id.data);
        recyclerView.setHasFixedSize(false);
        recyclerView.setItemViewCacheSize(999999999);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        adapter = new Adapter_simulation(simulation_list,getApplicationContext());
        recyclerView.setAdapter(adapter);



        try{
            surfaceView = findViewById(R.id.surface);
            customViewer = new CustomViewer();
            customViewer.loadEntity();
            customViewer.setSurfaceView(surfaceView);
            customViewer.loadGlb(this, "grogu", "rr");
            customViewer.loadIndirectLight(this,"venetian_crossroads_2k");
        }catch (Exception e){

        }


//        if (Build.VERSION.SDK_INT >= 19) {
//            webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//            WebSettings webSettings = webview.getSettings();
//            webSettings.setJavaScriptEnabled(true);
//            webSettings.setUseWideViewPort(true);
//            webSettings.setLoadWithOverviewMode(true);
//            webview.loadUrl("http://192.168.1.36/systemwatech/");
//        }
//        else {
//            webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//            WebSettings webSettings = webview.getSettings();
//            webSettings.setJavaScriptEnabled(true);
//            webSettings.setUseWideViewPort(true);
//            webSettings.setLoadWithOverviewMode(true);
//            webview.loadUrl("http://192.168.1.36/systemwatech/");
//        }




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