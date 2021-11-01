package com.example.bizwater.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.bizwater.Home;
import com.example.bizwater.Model.m_product;
import com.example.bizwater.Model.m_support;
import com.example.bizwater.R;
import com.example.bizwater.connection.con_addcart;
import com.example.bizwater.connection.con_login;
import com.example.bizwater.connection.con_sentRequest;
import com.example.bizwater.connection.config;
import com.example.bizwater.func.Func;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Adapter_tech extends RecyclerView.Adapter<Adapter_tech.ViewHolder> {
    Context mContext;
    List<m_support> newsList;

    List<String> checkRequest = new ArrayList<>();




    public Adapter_tech(List<m_support> list, Context context) {
        super();
        this.newsList = list;
        this.mContext = context;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_support,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final m_support getData = newsList.get(position);

        holder.name.setText(getData.getName());
        Picasso.get().load(config.IMGURL + getData.img).into(holder.item, new Callback() {
            @Override
            public void onSuccess() {
                holder.loading.setVisibility(View.GONE);
                Picasso.get().load(config.IMGURL + getData.img).into(holder.item);
            }

            @Override
            public void onError(Exception e) {
                holder.loading.setVisibility(View.VISIBLE);
                holder.loading.setAnimation(R.raw.error_con);
            }
        });
        holder.card.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
            View vs = LayoutInflater.from(v.getContext()).inflate(R.layout.modal_request, null);

            ImageView img = vs.findViewById(R.id.img);
            LottieAnimationView loading = vs.findViewById(R.id.loading);
            TextView name = vs.findViewById(R.id.name);
            name.setText(getData.getName());
            CheckBox leak = vs.findViewById(R.id.leak);
            CheckBox low = vs.findViewById(R.id.low);
            EditText other = vs.findViewById(R.id.other);
            MaterialButton request = vs.findViewById(R.id.request);

            leak.setOnCheckedChangeListener((compoundButton, b) -> {
                if(b){
                    checkRequest.add(leak.getText().toString());
                }
                else{
                    checkRequest.remove(0);
                }
            });

            low.setOnCheckedChangeListener((compoundButton, b) -> {
                if(b){
                    checkRequest.add(low.getText().toString());
                }
                else{
                    checkRequest.remove(1);
                }
            });

            Picasso.get().load(config.IMGURL + getData.img).into(img, new Callback() {
                @Override
                public void onSuccess() {
                    loading.setVisibility(View.GONE);
                    Picasso.get().load(config.IMGURL + getData.img).into(img);
                }

                @Override
                public void onError(Exception e) {
                    loading.setVisibility(View.VISIBLE);
                    loading.setAnimation(R.raw.error_con);
                }
            });

            dialog.setView(vs);
            AlertDialog alert = dialog.create();

            request.setOnClickListener(v1 -> {
                String getID = getData.getId();
                String userID = Func.getInstance(v1.getContext()).USERID();
                String requestProblem = checkRequest != null && checkRequest.isEmpty() ? other.getText().toString() : android.text.TextUtils.join(", ", checkRequest) + ", " + other.getText().toString();


                if(requestProblem.isEmpty()){
                    Func.getInstance(v1.getContext()).toast(R.raw.error_con,"Please Select Problem",Gravity.TOP|Gravity.CENTER,0,50);
                }
                else{
                    Func.getInstance(v1.getContext()).loading("Please wait...");
                    Response.Listener<String> response = response1 -> {
                        try {
                            JSONObject jsonResponse = new JSONObject(response1);
                            boolean error = jsonResponse.getBoolean("success");
                            String msg = jsonResponse.getString("message");

                            if(error){

                                Func.getInstance(v1.getContext()).pDialog.dismiss(); //close modal loading
                                Func.getInstance(v1.getContext()).toast(R.raw.ok,msg,Gravity.TOP|Gravity.CENTER,0,50);
                                alert.dismiss();
                            }
                            else{

                                 Func.getInstance(v1.getContext()).toast(R.raw.error_con,msg,Gravity.TOP|Gravity.CENTER,0,50);
                                 Func.getInstance(v1.getContext()).pDialog.dismiss(); //close modal loadingay
                                 alert.dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    };
                    Response.ErrorListener errorListener = error -> {
                        String result = Func.getInstance(v1.getContext()).Errorvolley(error);
                        Func.getInstance(v1.getContext()).pDialog.dismiss();
                        Func.getInstance(v1.getContext()).toast(R.raw.error_con,result,Gravity.TOP|Gravity.CENTER,0,50);
                        alert.dismiss();
                    };
                    con_sentRequest get = new con_sentRequest(getID,userID,requestProblem,response,errorListener);
                    RequestQueue queue = Volley.newRequestQueue(v1.getContext());
                    queue.add(get);
                }

            });

            alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alert.setCanceledOnTouchOutside(false);
            alert.setCancelable(true);
            alert.show();
        });

    }


    @Override
    public int getItemCount() {
        return newsList.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder{

       public TextView name;
       public ImageView item;
       public MaterialCardView card;
       public LottieAnimationView loading;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            item= itemView.findViewById(R.id.img);
            card = itemView.findViewById(R.id.card);
            loading = itemView.findViewById(R.id.loading);

        }
    }
}
