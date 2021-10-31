package com.example.bizwater.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.bizwater.Model.m_categories;
import com.example.bizwater.R;
import com.example.bizwater.connection.config;
import com.example.bizwater.func.Func;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_categories extends RecyclerView.Adapter<Adapter_categories.ViewHolder> {
    Context mContext;
    List<m_categories> newsList;



    public Adapter_categories(List<m_categories> list, Context context) {
        super();
        this.newsList = list;
        this.mContext = context;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final m_categories getData = newsList.get(position);


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
           Func.getInstance(v.getContext()).toast(R.raw.ok,getData.getId(), Gravity.TOP|Gravity.CENTER,0,50);
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
            item= itemView.findViewById(R.id.item);
            card = itemView.findViewById(R.id.card);
            loading = itemView.findViewById(R.id.loading);


        }
    }
}
