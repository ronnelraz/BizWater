package com.example.bizwater;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.bizwater.Model.m_categories;
import com.example.bizwater.adapter.Adapter_categories;
import com.example.bizwater.connection.con_categories;
import com.example.bizwater.func.Func;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.novoda.merlin.Merlin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class Home extends AppCompatActivity {

    Func controller;

    @BindView(R.id.fullname)
    TextView fullname;

    @BindView(R.id.menu)
    MaterialButton menu;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nvView)
    NavigationView navigationView;


    public static RecyclerView recyclerView;
    public static RecyclerView.Adapter adapter;
    public static List<m_categories> list;
    @BindView(R.id.loading)
    LottieAnimationView loading;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;
    @BindView(R.id.search)
    EditText search;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        controller = new Func(this);
        fullname.setText(controller.FULLNAME());
        list = new ArrayList<>();
        recyclerView = findViewById(R.id.data);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(999999999);

        list = new ArrayList<>();
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        adapter = new Adapter_categories(list,getApplicationContext());
        recyclerView.setAdapter(adapter);



        controller.merlin = new Merlin.Builder().withAllCallbacks().build(this);
        //check connected
        controller.merlin.registerConnectable(() -> {
            loadCategories();
            controller.toast(R.raw.wifi,"Connecting to the server...",Gravity.TOP|Gravity.CENTER,0,50);
        });
        //check diconnected
        controller.merlin.registerDisconnectable(() -> {
            loadCategories();
            no_connection();
            controller.toast(R.raw.error_con,"No Internet Connection",Gravity.TOP|Gravity.CENTER,0,50);
        });


        menu.setOnClickListener(v -> {
            drawerLayout.openDrawer(Gravity.LEFT,true);
            MenuItem();
        });

        swipe.setOnRefreshListener(() -> {
            list.clear();
            onLoadComplete();
        });

        swipe.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                ArrayList<m_categories> newList = new ArrayList<>();
                for (m_categories sub : list)
                {
                    String name = sub.getName().toLowerCase();
                    if(name.contains(s)){
                        newList.add(sub);
                        loading.setVisibility(View.GONE);
                    }
                    else{

                        loading.setVisibility(View.VISIBLE);
                        loading.setAnimation(R.raw.not_found);
                        loading.playAnimation();
                        loading.loop(true);
                    }
                    adapter = new Adapter_categories(newList, getApplicationContext());
                    recyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    public void onLoadComplete(){
        loadCategories();
        swipe.setRefreshing(false);
    }


    private void MenuItem() {
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){

                case R.id.Logout:
                    new SweetAlertDialog(Home.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure?")
                            .setContentText("You want to logout your account?")
                            .setConfirmText("Yes")
                            .setConfirmClickListener(sDialog -> {
                                sDialog.dismissWithAnimation();
                                controller.SETLOGOUT("wala");
                                Func.intent(Login.class,getApplicationContext());
                                finish();
                            })
                            .setCancelButton("No", sDialog -> sDialog.dismissWithAnimation())
                            .show();
                    break;
            }

            return true;
        });
    }


    protected void no_connection(){
        loading.setAnimation(R.raw.lose);
        loading.playAnimation();
        loading.loop(true);
    }


    public void loadCategories(){
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

                            m_categories item = new m_categories(
                                    object.getString("id"),
                                    object.getString("name"),
                                    object.getString("img")
                            );

                            list.add(item);
                        }

                        adapter = new Adapter_categories(list,getApplicationContext());
                        recyclerView.setAdapter(adapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            };
            Response.ErrorListener errorListener = error -> {
                String result = controller.Errorvolley(error);
//            controller.toast(R.raw.error_con,result,Gravity.TOP|Gravity.CENTER,0,50);
                loading.setVisibility(View.VISIBLE);
            };
            con_categories get = new con_categories(response,errorListener);
            RequestQueue queue = Volley.newRequestQueue(Home.this);
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