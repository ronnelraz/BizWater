package com.example.bizwater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.bizwater.Model.m_categories;
import com.example.bizwater.Model.m_mycart;
import com.example.bizwater.adapter.Adapter_categories;
import com.example.bizwater.adapter.Adapter_mycart;
import com.example.bizwater.connection.con_categories;
import com.example.bizwater.connection.con_getmycart;
import com.example.bizwater.connection.con_orderSubmit;
import com.example.bizwater.func.Func;
import com.google.android.material.button.MaterialButton;
import com.novoda.merlin.Merlin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Mycart extends AppCompatActivity {
    Func controller;
    public static RecyclerView recyclerView;
    public static RecyclerView.Adapter adapter;
    public static List<m_mycart> list;
    @BindView(R.id.loading)
    LottieAnimationView loading;
    private String transactionID = "";
    @BindView(R.id.total)
    TextView Total;

    @BindView(R.id.back)
    MaterialButton back;
    @BindView(R.id.ordernow) MaterialButton ordernow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mycart);
        ButterKnife.bind(this);
        controller = new Func(this);

        list = new ArrayList<>();
        recyclerView = findViewById(R.id.data);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(999999999);

        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter_mycart(list,getApplicationContext());
        recyclerView.setAdapter(adapter);


        controller.merlin = new Merlin.Builder().withAllCallbacks().build(this);
        //check connected
        controller.merlin.registerConnectable(() -> {
            loadCart();
            controller.toast(R.raw.wifi,"Connecting to the server...", Gravity.TOP|Gravity.CENTER,0,50);
        });
        //check diconnected
        controller.merlin.registerDisconnectable(() -> {
            loadCart();
            no_connection();
            controller.toast(R.raw.error_con,"No Internet Connection",Gravity.TOP|Gravity.CENTER,0,50);
        });

        back.setOnClickListener(v ->{
            Func.intent(Home.class,this);
            finish();
        });

        ordernow.setOnClickListener(v -> {
            Response.Listener<String> response = response1 -> {
                try {
                    JSONObject jsonResponse = new JSONObject(response1);
                    boolean success = jsonResponse.getBoolean("success");
                    String msg = jsonResponse.getString("message");

                    if(success){

                        Func.intent(Home.class,v.getContext());
                        finish();
                        controller.toast(R.raw.ok,msg,Gravity.TOP|Gravity.CENTER,0,50);
                    }
                    else{
                        Func.intent(Home.class,v.getContext());
                        finish();
                        controller.toast(R.raw.error_con,msg,Gravity.TOP|Gravity.CENTER,0,50);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            };
            Response.ErrorListener errorListener = error -> {
                String result = controller.Errorvolley(error);
            controller.toast(R.raw.error_con,result,Gravity.TOP|Gravity.CENTER,0,50);
            };
            con_orderSubmit get = new con_orderSubmit(transactionID,controller.USERID(),response,errorListener);
            RequestQueue queue = Volley.newRequestQueue(Mycart.this);
            queue.add(get);
        });
    }


    protected void no_connection(){
        loading.setAnimation(R.raw.lose);
        loading.playAnimation();
        loading.loop(true);
        ordernow.setEnabled(false);
    }


    public void loadCart(){
        try{
            list.clear();
            Response.Listener<String> response = response1 -> {
                try {
                    JSONObject jsonResponse = new JSONObject(response1);
                    boolean success = jsonResponse.getBoolean("success");
                    Total.setText("Total Order: ₱" +jsonResponse.getString("totalOrder"));
                    transactionID = jsonResponse.getString("trasactionID");
                    JSONArray array = jsonResponse.getJSONArray("data");



                    if(success){
                        ordernow.setEnabled(true);
                        loading.setVisibility(View.GONE);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            m_mycart item = new m_mycart(
                                    object.getString("id"),
                                    object.getString("name"),
                                    object.getString("price"),
                                    object.getString("qty"),
                                    object.getString("total"),
                                    object.getString("img")
                            );

                            list.add(item);
                        }

                        adapter = new Adapter_mycart(list,getApplicationContext());
                        recyclerView.setAdapter(adapter);
                    }
                    else{
                        ordernow.setEnabled(false);
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
            con_getmycart get = new con_getmycart(controller.USERID(),response,errorListener);
            RequestQueue queue = Volley.newRequestQueue(Mycart.this);
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
}