package com.example.bizwater.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.bizwater.Model.m_categories;
import com.example.bizwater.Model.m_mycart;
import com.example.bizwater.Model.m_product;
import com.example.bizwater.R;
import com.example.bizwater.connection.con_product;
import com.example.bizwater.connection.config;
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



    }

    private void loadData(Context context, RecyclerView rviewbottom, String id,LottieAnimationView loading) {
        list.clear();
        adapter.notifyDataSetChanged();
        Response.Listener<String> response = response1 -> {
            try {
                JSONObject jsonResponse = new JSONObject(response1);
                boolean success = jsonResponse.getBoolean("success");
                JSONArray array = jsonResponse.getJSONArray("data");

                if(success){
                    loading.setVisibility(View.GONE);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);

                        m_product item = new m_product(
                                object.getString("id"),
                                object.getString("name"),
                                object.getString("price"),
                                object.getString("qty"),
                                object.getString("des"),
                                object.getString("img"),
                                object.getString("flg")
                        );
                        list.add(item);

                    }
                    adapter = new Adapter_product(list,context);
                    rviewbottom.setAdapter(adapter);
                }
                else{
                    loading.setAnimation(R.raw.not_found);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
        Response.ErrorListener errorListener = error -> {

        };
        con_product get = new con_product(id,response,errorListener);
        RequestQueue queue = Volley.newRequestQueue(context);
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

        public ViewHolder(View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.img);
            loading = itemView.findViewById(R.id.loading);
            name = itemView.findViewById(R.id.productname);
            price = itemView.findViewById(R.id.price);
            qty = itemView.findViewById(R.id.qty);
            subtotal = itemView.findViewById(R.id.subtotal);


        }
    }
}
