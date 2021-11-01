package com.example.bizwater.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.daimajia.swipe.SwipeLayout;
import com.example.bizwater.Home;
import com.example.bizwater.Model.m_categories;
import com.example.bizwater.Model.m_mycart;
import com.example.bizwater.Model.m_product;
import com.example.bizwater.Mycart;
import com.example.bizwater.R;
import com.example.bizwater.connection.con_product;
import com.example.bizwater.connection.con_removeCart;
import com.example.bizwater.connection.config;
import com.example.bizwater.func.Func;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Adapter_mycart extends RecyclerView.Adapter<Adapter_mycart.ViewHolder> {
    Context mContext;
    List<m_mycart> newsList;


    private BottomSheetBehavior bottomSheetBehavior;
    private RecyclerView.Adapter adapter;
    private List<m_product> list;




    public Adapter_mycart(List<m_mycart> list, Context context) {
        super();
        this.newsList = list;
        this.mContext = context;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_order,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final m_mycart getData = newsList.get(position);


        holder.name.setText("Item: "+getData.getName());
        holder.price.setText("Price: ₱" +getData.getPrice());
        holder.qty.setText("Qty: "+getData.getQty()+"x");
        holder.subtotal.setText("Sub Total: ₱" +getData.getTotal());
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
                    ((Mycart)v.getContext()).loadCart();
                    Func.getInstance(v.getContext()).toast(R.raw.ok,"Item Removed", Gravity.TOP|Gravity.CENTER,0,50);
                }
                else{
                    ((Mycart)v.getContext()).loadCart();
                    Func.getInstance(v.getContext()).toast(R.raw.error_con,"Something went wrong. Try again later.", Gravity.TOP|Gravity.CENTER,0,50);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
        Response.ErrorListener errorListener = error -> {

        };
        con_removeCart get = new con_removeCart(id,response,errorListener);
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
        public TextView name,price,qty,subtotal;
        public SwipeLayout swipe;
        public LinearLayout trash;

        public ViewHolder(View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.img);
            loading = itemView.findViewById(R.id.loading);
            name = itemView.findViewById(R.id.productname);
            price = itemView.findViewById(R.id.price);
            qty = itemView.findViewById(R.id.qty);
            subtotal = itemView.findViewById(R.id.subtotal);
            swipe = itemView.findViewById(R.id.swipe);
            trash = itemView.findViewById(R.id.cancelitem);


        }
    }
}
