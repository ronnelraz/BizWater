package com.example.bizwater.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.bizwater.Home;
import com.example.bizwater.Model.m_orderDetails;
import com.example.bizwater.Model.m_product;
import com.example.bizwater.R;
import com.example.bizwater.connection.con_addcart;
import com.example.bizwater.connection.config;
import com.example.bizwater.func.Func;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Adapter_order_details extends RecyclerView.Adapter<Adapter_order_details.ViewHolder> {
    Context mContext;
    List<m_orderDetails> newsList;




    public Adapter_order_details(List<m_orderDetails> list, Context context) {
        super();
        this.newsList = list;
        this.mContext = context;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_order_details,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final m_orderDetails getData = newsList.get(position);

        holder.productname.setText("Item: " + getData.getName());
        holder.price.setText("Price: ₱" + getData.getPrice());
        holder.qty.setText("Qty: " + getData.getQty() +"x");
        holder.sub.setText("Sub Total: ₱" + getData.getSub());

        if(getData.getStatus().equals("C")){
            holder.card.setBackgroundColor(Color.parseColor("#ECD1CE"));
        }
        else if(getData.getStatus().equals("Y")){
            holder.card.setBackgroundColor(Color.parseColor("#E0F1D2"));
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

    }


    @Override
    public int getItemCount() {
        return newsList.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder{


       public ImageView item;
       public LottieAnimationView loading;
       public TextView productname,price,qty,sub;
       public RelativeLayout card;


        public ViewHolder(View itemView) {
            super(itemView);

            item= itemView.findViewById(R.id.img);
            loading = itemView.findViewById(R.id.loading);
            productname = itemView.findViewById(R.id.productname);
            price = itemView.findViewById(R.id.price);
            qty = itemView.findViewById(R.id.qty);
            sub = itemView.findViewById(R.id.subtotal);
            card = itemView.findViewById(R.id.card);



        }
    }
}
