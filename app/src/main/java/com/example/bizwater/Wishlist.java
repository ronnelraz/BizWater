package com.example.bizwater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.bizwater.Model.m_order;
import com.example.bizwater.Model.m_wishlist;
import com.example.bizwater.adapter.Adapter_order;
import com.example.bizwater.adapter.Adapter_wishlist;
import com.example.bizwater.connection.con_getMyorder;
import com.example.bizwater.connection.con_getwishlist;
import com.example.bizwater.func.Func;
import com.novoda.merlin.Merlin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Wishlist extends AppCompatActivity {

    public Func controller;

    public static RecyclerView recyclerView;
    public static RecyclerView.Adapter adapter;
    public static List<m_wishlist> list;
    @BindView(R.id.loading)
    LottieAnimationView loading;
    @BindView(R.id.search)
    EditText search;
    Handler handler = new Handler();
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_wishlist);
        ButterKnife.bind(this);
        controller = new Func(this);

        list = new ArrayList<>();
        recyclerView = findViewById(R.id.data);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(999999999);

        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter_wishlist(list,getApplicationContext());
        recyclerView.setAdapter(adapter);



        controller.merlin = new Merlin.Builder().withAllCallbacks().build(this);
        //check connected
        controller.merlin.registerConnectable(() -> {
            loadwishlist();
            controller.toast(R.raw.wifi,"Connecting to the server...", Gravity.TOP|Gravity.CENTER,0,50);
        });
        //check diconnected
        controller.merlin.registerDisconnectable(() -> {
            loadwishlist();
            no_connection();
            controller.toast(R.raw.error_con,"No Internet Connection",Gravity.TOP|Gravity.CENTER,0,50);
        });


        //realtime update data
//        handler.postDelayed(runnable = new Runnable() {
//            public void run() {
//                handler.postDelayed(runnable, 3000);
//                loadwishlist();
//            }
//        }, 3000);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                ArrayList<m_wishlist> newList = new ArrayList<>();
                for (m_wishlist sub : list)
                {
                    String name = sub.getId().toLowerCase();
                    if(name.contains(s)){
                        newList.add(sub);
                    }
                    adapter = new Adapter_wishlist(newList, getApplicationContext());
                    recyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    protected void no_connection(){
        loading.setAnimation(R.raw.lose);
        loading.playAnimation();
        loading.loop(true);
    }


    public void loadwishlist(){
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

                            m_wishlist item = new m_wishlist(
                                    object.getString("id"),
                                    object.getString("name"),
                                    object.getString("price"),
                                    object.getString("qty"),
                                    object.getString("total"),
                                    object.getString("img"),
                                    object.getString("stock"),
                                    object.getString("productId")

                            );

                            list.add(item);
                        }

                        adapter = new Adapter_wishlist(list,getApplicationContext());
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
                no_connection();
            };
            con_getwishlist get = new con_getwishlist(controller.USERID(),response,errorListener);
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
        Func.intent(Home.class,view.getContext());
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}