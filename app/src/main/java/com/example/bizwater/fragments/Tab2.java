package com.example.bizwater.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.bizwater.Model.m_request_support;
import com.example.bizwater.R;
import com.example.bizwater.adapter.Adapter_request_tech;
import com.example.bizwater.connection.con_cancel_request;
import com.example.bizwater.connection.con_request_tech;
import com.example.bizwater.func.Func;
import com.novoda.merlin.Merlin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Tab2 extends Fragment {


    public static Func controller;
    public static RecyclerView recyclerView;
    public static RecyclerView.Adapter adapter;
    public static List<m_request_support> list;
    public static LottieAnimationView loading;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.tab3_cancelled,parent,false);
        ButterKnife.bind(this,view);
        controller = new Func(view.getContext());
        loading = view.findViewById(R.id.loading);

        list = new ArrayList<>();
        recyclerView = view.findViewById(R.id.data);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(999999999);

        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new Adapter_request_tech(list,getActivity());
        recyclerView.setAdapter(adapter);
        loadquest(getActivity());
        return view;

    }


    protected static void no_connection(){
        loading.setAnimation(R.raw.lose);
        loading.playAnimation();
        loading.loop(true);
    }


    public static void loadquest(Context context){
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

                        adapter = new Adapter_request_tech(list,context);
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
            con_cancel_request get = new con_cancel_request(controller.USERID(),response,errorListener);
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(get);
        }catch (Exception e){
            Log.d("Error",e.toString());
        }
    }


}