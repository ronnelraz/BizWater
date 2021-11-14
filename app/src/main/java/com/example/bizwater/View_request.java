package com.example.bizwater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.example.bizwater.Model.m_mycart;
import com.example.bizwater.Model.m_request_support;
import com.example.bizwater.adapter.Adapter_mycart;
import com.example.bizwater.adapter.Adapter_request_tech;
import com.example.bizwater.connection.con_getmycart;
import com.example.bizwater.connection.con_request_tech;
import com.example.bizwater.func.Func;
import com.novoda.merlin.Merlin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class View_request extends AppCompatActivity {

    Func controller;
    public static RecyclerView recyclerView;
    public static RecyclerView.Adapter adapter;
    public static List<m_request_support> list;
    @BindView(R.id.loading)
    LottieAnimationView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_request);
        ButterKnife.bind(this);
        controller = new Func(this);
        list = new ArrayList<>();
        recyclerView = findViewById(R.id.data);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(999999999);

        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter_request_tech(list,getApplicationContext());
        recyclerView.setAdapter(adapter);


        controller.merlin = new Merlin.Builder().withAllCallbacks().build(this);
        //check connected
        controller.merlin.registerConnectable(() -> {
            loadquest();
            controller.toast(R.raw.wifi,"Connecting to the server...", Gravity.TOP|Gravity.CENTER,0,50);
        });
        //check diconnected
        controller.merlin.registerDisconnectable(() -> {
            loadquest();
            no_connection();
            controller.toast(R.raw.error_con,"No Internet Connection",Gravity.TOP|Gravity.CENTER,0,50);
        });

    }


    protected void no_connection(){
        loading.setAnimation(R.raw.lose);
        loading.playAnimation();
        loading.loop(true);
    }


    public void loadquest(){
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
                            m_request_support item = new m_request_support(
                                    object.getString("id"),
                                    object.getString("name"),
                                    object.getString("img"),
                                    object.getString("problem"),
                                    object.getString("date"),
                                    object.getString("flag")
                            );

                            list.add(item);
                        }

                        adapter = new Adapter_request_tech(list,getApplicationContext());
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
//            controller.toast(R.raw.error_con,result,Gravity.TOP|Gravity.CENTER,0,50);
//                loading.setVisibility(View.GONE);
                no_connection();
            };
            con_request_tech get = new con_request_tech(response,errorListener);
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(get);
        }catch (Exception e){
            Log.d("Error",e.toString());
        }
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

    public void back(View view) {
        Func.intent(TechSupport.class,view.getContext());
    }
}