package com.example.bizwater.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.daimajia.swipe.SwipeLayout;
import com.example.bizwater.Home;
import com.example.bizwater.Model.m_mycart;
import com.example.bizwater.Model.m_product;
import com.example.bizwater.Model.m_request_support;
import com.example.bizwater.Mycart;
import com.example.bizwater.R;
import com.example.bizwater.View_request;
import com.example.bizwater.connection.con_cancelled_request;
import com.example.bizwater.connection.con_removeCart;
import com.example.bizwater.connection.config;
import com.example.bizwater.fragments.Tab1;
import com.example.bizwater.func.Func;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Adapter_request_tech extends RecyclerView.Adapter<Adapter_request_tech.ViewHolder> {
    Context mContext;
    List<m_request_support> newsList;



    public Adapter_request_tech(List<m_request_support> list, Context context) {
        super();
        this.newsList = list;
        this.mContext = context;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_tech_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final m_request_support getData = newsList.get(position);


        holder.name.setText(getData.getName());
        holder.request.setText("Problem : "+getData.getProblem());
        holder.date.setText("Request Date : " +getData.getDate());

        if(getData.getFlag().equals("P")){
            holder.status.setText("Pending");
            holder.status.setTextColor(Color.parseColor("#e67e22"));
        }
        else if(getData.getFlag().equals("Y")){
            holder.swipe.setSwipeEnabled(false);
            holder.status.setText("Accepted");
            holder.status.setTextColor(Color.parseColor("#27ae60"));
        }
        else{
            holder.swipe.setSwipeEnabled(false);
            holder.status.setText("Cancelled");
            holder.status.setTextColor(Color.parseColor("#e74c3c"));
        }

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

        holder.trash.setOnClickListener(v -> {
            if(getItemCount() == 1){
                removeItemList(v, getData.getId());
                Func.intent(Home.class,v.getContext());
            }else{
                removeItemList(v, getData.getId());
            }


        });



    }


    private void removeItemList(View v, String id){
        Response.Listener<String> response = response1 -> {
            try {
                JSONObject jsonResponse = new JSONObject(response1);
                boolean success = jsonResponse.getBoolean("success");

                if(success){
//                    ((View_request)v.getContext()).loadquest();
                    Tab1.loadquest(v.getContext());
                    Func.getInstance(v.getContext()).toast(R.raw.ok,"Cancelled Request", Gravity.TOP|Gravity.CENTER,0,50);
                }
                else{
//                    ((View_request)v.getContext()).loadquest();
                    Tab1.loadquest(v.getContext());
                    Func.getInstance(v.getContext()).toast(R.raw.error_con,"Something went wrong. Try again later.", Gravity.TOP|Gravity.CENTER,0,50);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
        Response.ErrorListener errorListener = error -> {

        };
        con_cancelled_request get = new con_cancelled_request(id,response,errorListener);
        RequestQueue queue = Volley.newRequestQueue(v.getContext());
        queue.add(get);
    }


    @Override
    public int getItemCount() {
        return newsList.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView item;
        public LottieAnimationView loading;
        public TextView name,request,date,status;
        public SwipeLayout swipe;
        public LinearLayout trash;

        public ViewHolder(View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.img);
            loading = itemView.findViewById(R.id.loading);
            name = itemView.findViewById(R.id.name);
            request = itemView.findViewById(R.id.request);
            date = itemView.findViewById(R.id.date);
            status = itemView.findViewById(R.id.status);
            swipe = itemView.findViewById(R.id.swipe);
            trash = itemView.findViewById(R.id.cancelitem);


        }
    }
}
