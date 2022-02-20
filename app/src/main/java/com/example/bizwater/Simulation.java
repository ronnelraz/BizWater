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

    public static String[] desc = {
            "PART OF FIRST STAGE WHERE SOURCE WATER PASSES THROUGH. THE FUNCTION OF THIS FILTER IS TO REMOVE THE PARTICLES OR MICRONS BEFORE THE WATER ENTERS THE RAW TANK.",
            "THE TANK HAS ITS LOW LEVEL INDICATOR WHEREIN OPERATOR OF MACHINE SHOULD NOT WORRY, THERE WILL BE NO INSTANCE THAT THE RAW TANK WILL BE EMPTY.",
            "A booster pump is a machine which will increase the pressure of a fluid. They may be used with liquids or gases, but the construction details will vary depending on the fluid.",
            "THESE THREE MULTIMEDIA FILTERS HAVE FUNCTIONS. FIRST IS THE ANTHRACITE, IT REMOVES THE DISCOLORATION OF WATER. THE CARBON REMOVES THE ODOR AND THE THIRD ONE IS THE SOFTENER, IT REMOVES THE HARDNESS AND MAKES THE WATER SOFT.",
            "THIS TANK OR SIMILAR TO BIN IS USED EVERYTIME THE MACHINE UNDERGOES BACKWASHING. THIS IS WHERE INDUSTRIAL SALT IS BEING POURED AS PART OF THE PROCESS.",
            "5 MICRON FILTER-THIS IS SOMEHOW SIMILAR TO 10 MICRON FILTER ALTHOUGH THE TEXTURE OF THIS FILTER IS SMALLER. 1 MICRON FILTER-THIS FILTER HAS THE SMALLEST TEXTURE IN ORDER TO BLOCK THE PARTICLES THAT PASSED THROUGH WITH THE 10 AND 5 MICRON FILTERS.",
            "THIS IS THE HEART OF THE PURIFICATION PROCESS. THIS IS WHERE REJECT AND PURIFIED WATER SEPARATES. THE REJECT WATER COULD BE USED IN HOUSEHOLD.",
            "THIS TANK HAS HIGH AND LOW INDICATORS, WHICH MEANS WATER AUTOMATICALLY FILLS IN, AND STOP ONCE THE TANK IS FULL.",
            "THE PURPOSE OF THESE IS TO ASSURE THAT THE WATER IS TOTALLY CLEAN AND ALL UNECESSARY PARTICLES HAVE BEEN REMOVED. CARBON BLOCKS’ PURPOSE IS TO NEUTRALIZE AND ENHANCE THE TASTE OF THE PRODUCT OR PURIFIED WATER SINCE IT IS MADE OF COCONUT SHELL.",
            "SAFETY OF CONSUMERS IS A MUST. THIS PROCESS IS VERY IMPORTANT. BEFORE DISPENSING, WATER MUST UNDERGO TO THIS IN ORDER TO KILL BACTERIA.",
            "THIS IS THE LAST STEP OF THE PURIFICATION SYSTEM. WATER IS READY TO DISPENSE AND READY TO DRINK BY OUR CONSUMERS."
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
                  name[i],
                  desc[i]
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
//            webview.loadUrl("https://bizwatertechnologies.000webhostapp.com/systemwatech/");
//        }
//        else {
//            webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//            WebSettings webSettings = webview.getSettings();
//            webSettings.setJavaScriptEnabled(true);
//            webSettings.setUseWideViewPort(true);
//            webSettings.setLoadWithOverviewMode(true);
//            webview.loadUrl("https://bizwatertechnologies.000webhostapp.com/systemwatech/");
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