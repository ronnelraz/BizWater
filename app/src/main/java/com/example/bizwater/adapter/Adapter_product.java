package com.example.bizwater.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.bizwater.Home;
import com.example.bizwater.Model.m_product;
import com.example.bizwater.R;
import com.example.bizwater.connection.con_addcart;
import com.example.bizwater.connection.con_wishlist;
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

import ezy.ui.view.NumberStepper;

public class Adapter_product extends RecyclerView.Adapter<Adapter_product.ViewHolder> {
    Context mContext;
    List<m_product> newsList;




    public Adapter_product(List<m_product> list, Context context) {
        super();
        this.newsList = list;
        this.mContext = context;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final m_product getData = newsList.get(position);
        AtomicReference<String> totalOrder = new AtomicReference<>("");


        holder.name.setText(getData.getName());
        holder.price.setText("₱" + Func.getInstance(mContext).numberformat(Float.parseFloat(getData.getPrice())));




        if(getData.getQty().equals("0")){
            holder.addtocart.setEnabled(false);
            holder.addtocart.setVisibility(View.GONE);
            holder.wishlist.setVisibility(View.VISIBLE);
            holder.card.setCardBackgroundColor(Color.parseColor("#EF9A9A"));
            holder.qty.setActivated(false);
            holder.qty.setEnabled(false);
            holder.qty.setValue(0);
            holder.stock.setText("Out of Stock");
        }
        else{
            holder.wishlist.setVisibility(View.GONE);
            holder.stock.setText("Stock : " + getData.getQty());
        }
        totalOrder.set(getData.getPrice());
       holder.qty.setOnValueChangedListener(new NumberStepper.OnValueChangedListener() {
           @Override
           public void onValueChanged(NumberStepper view, int value) {
               int stock = Integer.valueOf(getData.getQty());

               if(value > stock){
                   holder.addtocart.setEnabled(false);
//                Func.getInstance(view.getContext()).toast(R.raw.error_con,"Sorry your order Quantity is more than available Stocks", Gravity.TOP|Gravity.CENTER,0,50);
               }
               else{

                   totalOrder.set(String.valueOf(Float.parseFloat(getData.getPrice()) * value));
                   holder.addtocart.setEnabled(true);
               }
           }
       });

        holder.card.setOnClickListener(v -> {

        });


        holder.wishlist.setOnClickListener(v -> {
            String itemId = getData.getId();
            String userID = Func.getInstance(v.getContext()).USERID();
            String qty = String.valueOf(holder.qty.getValue());
            String total = totalOrder.get();


            Response.Listener<String> response = response1 -> {
                try {
                    JSONObject jsonResponse = new JSONObject(response1);
                    boolean success = jsonResponse.getBoolean("success");

                    if(success){
//                        ((Home)v.getContext()).cartCount();
                        totalOrder.set("");
                        Func.getInstance(v.getContext()).toast(R.raw.ok,getData.getName() + " added in your wishlist",Gravity.TOP|Gravity.CENTER,0,50);
                    }
                    else{
                        totalOrder.set("");
                        Func.getInstance(v.getContext()).toast(R.raw.error_con,  "Sorry " + getData.getName() + " Already added to your wishlist",Gravity.TOP|Gravity.CENTER,0,50);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            };
            Response.ErrorListener errorListener = error -> {

            };
            con_wishlist get = new con_wishlist(itemId,userID,qty,total,response,errorListener);
            RequestQueue queue = Volley.newRequestQueue(v.getContext());
            queue.add(get);
        });

        holder.addtocart.setOnClickListener(v -> {
            String itemId = getData.getId();
            String userID = Func.getInstance(v.getContext()).USERID();
            String qty = String.valueOf(holder.qty.getValue());
            String total = totalOrder.get();


            Response.Listener<String> response = response1 -> {
                try {
                    JSONObject jsonResponse = new JSONObject(response1);
                    boolean success = jsonResponse.getBoolean("success");

                    if(success){
                        ((Home)v.getContext()).cartCount();
                        totalOrder.set("");
                        Func.getInstance(v.getContext()).toast(R.raw.ok,getData.getName() + " added in your cart",Gravity.TOP|Gravity.CENTER,0,50);
                    }
                    else{
                        totalOrder.set("");
                        Func.getInstance(v.getContext()).toast(R.raw.error_con,  "Sorry " + getData.getName() + " Already added to your cart",Gravity.TOP|Gravity.CENTER,0,50);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            };
            Response.ErrorListener errorListener = error -> {

            };
            con_addcart get = new con_addcart(itemId,userID,qty,total,response,errorListener);
            RequestQueue queue = Volley.newRequestQueue(v.getContext());
            queue.add(get);
        });

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

       public TextView name,price,stock;
       public ImageView item;
       public MaterialCardView card;
       public MaterialButton addtocart,wishlist;
       public LottieAnimationView loading;
       public NumberStepper qty;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.productname);
            price = itemView.findViewById(R.id.productprice);
            stock = itemView.findViewById(R.id.productstock);
            wishlist = itemView.findViewById(R.id.wishlist);
            item= itemView.findViewById(R.id.image);
            card = itemView.findViewById(R.id.card);
            addtocart = itemView.findViewById(R.id.addtocart);
            loading = itemView.findViewById(R.id.loading);
            qty = itemView.findViewById(R.id.qty);


        }
    }
}
