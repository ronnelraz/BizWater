package com.example.bizwater.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.daimajia.swipe.SwipeLayout;
import com.example.bizwater.Home;
import com.example.bizwater.Model.m_product;
import com.example.bizwater.Model.m_wishlist;
import com.example.bizwater.Mycart;
import com.example.bizwater.R;
import com.example.bizwater.Wishlist;
import com.example.bizwater.connection.con_addcart;
import com.example.bizwater.connection.con_removeCart;
import com.example.bizwater.connection.con_removeWishlist;
import com.example.bizwater.connection.con_update_wishlist;
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

public class Adapter_wishlist extends RecyclerView.Adapter<Adapter_wishlist.ViewHolder> {
    Context mContext;
    List<m_wishlist> newsList;

    public String getqty = "";
    public String getsubtotal = "";




    public Adapter_wishlist(List<m_wishlist> list, Context context) {
        super();
        this.newsList = list;
        this.mContext = context;



    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final m_wishlist getData = newsList.get(position);
        holder.name.setText("Product Name : "+getData.getName());
        holder.price.setText("Price : "+Func.getInstance(mContext).numberformat(Float.valueOf(getData.getPrice())));
        holder.stock.setText("Stock : "+getData.getStock());
        holder.qty.setValue(Integer.parseInt(getData.getQty()));
        holder.subtotal.setText("Total : "+Func.getInstance(mContext).numberformat(Float.valueOf(getData.getTotal())));

        getqty = getData.getQty();
        getsubtotal = getData.getTotal();

        holder.trash.setOnClickListener(v -> {
            if(getItemCount() == 1){
                removeItemList(v, getData.getId());
                Func.intent(Home.class,v.getContext());
            }else{
                removeItemList(v, getData.getId());
            }


        });

        holder.addtocart.setOnClickListener(v -> {
            String itemId = getData.getProductId();
            String userID = Func.getInstance(v.getContext()).USERID();
            String qty = getqty;
            String total = getsubtotal;


            Response.Listener<String> response = response1 -> {
                try {
                    JSONObject jsonResponse = new JSONObject(response1);
                    boolean success = jsonResponse.getBoolean("success");

                    if(success){
                        DeleteWishlist(v,getData.getId());
                        Func.getInstance(v.getContext()).toast(R.raw.ok,getData.getName() + " added in your cart",Gravity.TOP|Gravity.CENTER,0,50);
//                        Toast.makeText(v.getRootView().getContext(), getData.getName() + " added in your cart", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        getqty = "";
                        getsubtotal = "";
                        Toast.makeText(v.getRootView().getContext(), "Sorry " + getData.getName() + " Already added to your cart", Toast.LENGTH_SHORT).show();

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


        holder.qty.setOnValueChangedListener((view, value) -> {
                int stock = Integer.parseInt(getData.getStock());


                if(value > stock){
//                    Toast.makeText(view.getContext(), "Sorry your order Quantity is more than available Stocks", Toast.LENGTH_SHORT).show();
//                    Func.getInstance(view.getContext()).toast(R.raw.error_con,"Sorry your order Quantity is more than available Stocks",Gravity.TOP|Gravity.CENTER,0,50);
                    Toast.makeText(view.getRootView().getContext(), "Sorry your order Quantity is more than available Stocks", Toast.LENGTH_SHORT).show();
                }
                else{
                    int price =  Integer.parseInt(getData.getPrice());
                    int newTotal = price * value;
                    Response.Listener<String> response = response1 -> {
                        try {
                            JSONObject jsonResponse = new JSONObject(response1);
                            boolean success = jsonResponse.getBoolean("success");

                            if(success){
                                getqty = String.valueOf(value);
                                getsubtotal = String.valueOf(newTotal);
                                holder.subtotal.setText("Total : "+Func.getInstance(mContext).numberformat(Float.valueOf(String.valueOf(newTotal))));
                                Toast.makeText(mContext, "update quantity", Toast.LENGTH_SHORT).show();
                            }
                            else{

                                Func.getInstance(mContext).toast(R.raw.error_con,"Something went wrong. Try again later.", Gravity.TOP|Gravity.CENTER,0,50);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    };
                    Response.ErrorListener errorListener = error -> {

                    };
                    con_update_wishlist get = new con_update_wishlist(getData.getId(),String.valueOf(value),String.valueOf(newTotal),response,errorListener);
                    RequestQueue queue = Volley.newRequestQueue(view.getContext());
                    queue.add(get);
//
                }

        });

        if(getData.getStock().equals("0")){
            holder.addtocart.setEnabled(false);
            holder.qty.setEnabled(false);
            holder.card.setCardBackgroundColor(Color.parseColor("#EF9A9A"));
        }
        else{

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


    private void removeItemList(View v, String id){
        Response.Listener<String> response = response1 -> {
            try {
                JSONObject jsonResponse = new JSONObject(response1);
                boolean success = jsonResponse.getBoolean("success");

                if(success){
                    ((Wishlist)v.getContext()).loadwishlist();
                    Func.getInstance(v.getContext()).toast(R.raw.ok,"Item Removed", Gravity.TOP|Gravity.CENTER,0,50);
                }
                else{
                    ((Wishlist)v.getContext()).loadwishlist();
                    Func.getInstance(v.getContext()).toast(R.raw.error_con,"Something went wrong. Try again later.", Gravity.TOP|Gravity.CENTER,0,50);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
        Response.ErrorListener errorListener = error -> {

        };
        con_removeWishlist get = new con_removeWishlist(id,response,errorListener);
        RequestQueue queue = Volley.newRequestQueue(v.getContext());
        queue.add(get);
    }

    private void DeleteWishlist(View v, String id){
        Response.Listener<String> response = response1 -> {
            try {
                JSONObject jsonResponse = new JSONObject(response1);
                boolean success = jsonResponse.getBoolean("success");

                if(success){
                    ((Wishlist)v.getContext()).loadwishlist();
//                    Func.getInstance(v.getContext()).toast(R.raw.ok,"Order Successfully!", Gravity.TOP|Gravity.CENTER,0,50);
                }
                else{
//                    ((Wishlist)v.getContext()).loadwishlist();
//                    Func.getInstance(v.getContext()).toast(R.raw.error_con,"Something went wrong. Try again later.", Gravity.TOP|Gravity.CENTER,0,50);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
        Response.ErrorListener errorListener = error -> {

        };
        con_removeWishlist get = new con_removeWishlist(id,response,errorListener);
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
        public TextView name,price,stock,subtotal;
        public SwipeLayout swipe;
        public LinearLayout trash;
        public NumberStepper qty;
        public MaterialButton addtocart;
        public CardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.img);
            loading = itemView.findViewById(R.id.loading);
            name = itemView.findViewById(R.id.productname);
            price = itemView.findViewById(R.id.price);
            stock = itemView.findViewById(R.id.stock);
            qty = itemView.findViewById(R.id.qty);
            subtotal = itemView.findViewById(R.id.subtotal);
            swipe = itemView.findViewById(R.id.swipe);
            trash = itemView.findViewById(R.id.cancelitem);
            card = itemView.findViewById(R.id.card);
            addtocart = itemView.findViewById(R.id.addtocart);


        }
    }
}
