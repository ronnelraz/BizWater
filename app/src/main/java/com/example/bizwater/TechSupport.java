package com.example.bizwater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.bizwater.Model.m_categories;
import com.example.bizwater.Model.m_order;
import com.example.bizwater.Model.m_support;
import com.example.bizwater.adapter.Adapter_categories;
import com.example.bizwater.adapter.Adapter_order;
import com.example.bizwater.adapter.Adapter_tech;
import com.example.bizwater.connection.con_categories;
import com.example.bizwater.connection.con_getMyorder;
import com.example.bizwater.connection.con_techSupport;
import com.example.bizwater.func.Func;
import com.novoda.merlin.Merlin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TechSupport extends AppCompatActivity {
    Func controller;

    public static RecyclerView recyclerView;
    public static RecyclerView.Adapter adapter;
    public static List<m_support> list;
    @BindView(R.id.loading)
    LottieAnimationView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tech_support);
        ButterKnife.bind(this);
        controller = new Func(this);

        list = new ArrayList<>();
        recyclerView = findViewById(R.id.data);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(999999999);

        list = new ArrayList<>();
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        adapter = new Adapter_tech(list,getApplicationContext());
        recyclerView.setAdapter(adapter);

        controller.merlin = new Merlin.Builder().withAllCallbacks().build(this);
        //check connected
        controller.merlin.registerConnectable(() -> {
            loadtech();
            controller.toast(R.raw.wifi,"Connecting to the server...", Gravity.TOP|Gravity.CENTER,0,50);
        });
        //check diconnected
        controller.merlin.registerDisconnectable(() -> {
            loadtech();
            no_connection();
            controller.toast(R.raw.error_con,"No Internet Connection",Gravity.TOP|Gravity.CENTER,0,50);
        });
    }

    protected void no_connection(){
        loading.setVisibility(View.VISIBLE);
        loading.setAnimation(R.raw.lose);
        loading.playAnimation();
        loading.loop(true);
    }


    public void loadtech(){
        try{
            list.clear();
            Response.Listener<String> response = response1 -> {
                try {
                    JSONObject jsonResponse = new JSONObject(response1);
                    boolean success = jsonResponse.getBoolean("success");
                    JSONArray array = jsonResponse.getJSONArray("data");



                    if(success){
                        loading.setVisibility(View.GONE);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);

                            m_support item = new m_support(
                                    object.getString("id"),
                                    object.getString("name"),
                                    object.getString("contact"),
                                    object.getString("address"),
                                    object.getString("img"),
                                    object.getString("flag")
                            );

                            list.add(item);
                        }

                        adapter = new Adapter_tech(list,getApplicationContext());
                        recyclerView.setAdapter(adapter);
                    }
                    else{
                        loading.setAnimation(R.raw.not_found);
                        loading.playAnimation();
                        loading.loop(true);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            };
            Response.ErrorListener errorListener = error -> {
                String result = controller.Errorvolley(error);
//                no_connection();
            };
            con_techSupport get = new con_techSupport(response,errorListener);
            RequestQueue queue = Volley.newRequestQueue(TechSupport.this);
            queue.add(get);
        }catch (Exception e){
            Log.d("Error",e.toString());
        }
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public void back(View view) {
        Func.intent(Home.class,this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        controller.merlin.bind();
    }

    @Override
    protected void onPause() {
        controller.merlin.unbind();
        super.onPause();
    }


}