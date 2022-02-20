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
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.bizwater.Model.m_categories;
import com.example.bizwater.Model.m_order;
import com.example.bizwater.Model.m_orderDetails;
import com.example.bizwater.Model.m_product;
import com.example.bizwater.R;
import com.example.bizwater.connection.con_order_details;
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

public class Adapter_order extends RecyclerView.Adapter<Adapter_order.ViewHolder> {
    Context mContext;
    List<m_order> newsList;


    private BottomSheetBehavior bottomSheetBehavior;
    private RecyclerView.Adapter adapter;
    private List<m_orderDetails> list;



    public Adapter_order(List<m_order> list, Context context) {
        super();
        this.newsList = list;
        this.mContext = context;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_order,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final m_order getData = newsList.get(position);
        holder.tra.setText("Transaction No: "+getData.getId());
        holder.date.setText("Order Date: " + getData.getDate());
        if(getData.status.equals("N")){
            holder.status.setImageResource(R.drawable.icons8_data_pending);
        }
        else if(getData.status.equals("Y")){
            holder.status.setImageResource(R.drawable.okok);
        }
        else{
            holder.status.setImageResource(R.drawable.icons8_cancel_subscription);
        }

        holder.card.setOnClickListener(v -> {
            View view = LayoutInflater.from(v.getContext()).inflate(R.layout.bottomsheet_order_details,null);

            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(v.getContext(),R.style.BottomSheetDialog);


            LinearLayout linearLayout = view.findViewById(R.id.root);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
            params.height = getScreenHeight();
            linearLayout.setLayoutParams(params);

            MaterialButton back = view.findViewById(R.id.back);
            back.setOnClickListener(v1 -> {
                list.clear();
                bottomSheetDialog.dismiss();
            });
            LottieAnimationView loading = view.findViewById(R.id.loading);
            RecyclerView rviewbottom = view.findViewById(R.id.data);
            SwipeRefreshLayout swipe = view.findViewById(R.id.swipe);
            TextView tranid = view.findViewById(R.id.transid);
            TextView total = view.findViewById(R.id.total);


            rviewbottom.setHasFixedSize(true);
            rviewbottom.setItemViewCacheSize(999999999);

            list = new ArrayList<>();
            rviewbottom.setLayoutManager(new LinearLayoutManager(v.getContext()));
            adapter = new Adapter_order_details(list,view.getContext());
            rviewbottom.setAdapter(adapter);


            loading.setVisibility(View.VISIBLE);

            swipe.setOnRefreshListener(() -> {
                list.clear();
                loadData(view.getContext(),rviewbottom,getData.getId(),loading,tranid,total);
                swipe.setRefreshing(false);
            });

            swipe.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);



            loadData(view.getContext(),rviewbottom,getData.getId(),loading,tranid,total);

            bottomSheetDialog.setContentView(view);
            bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            bottomSheetDialog.show();

        });

    }

    private void loadData(Context context, RecyclerView rviewbottom, String id,LottieAnimationView loading,TextView transid,TextView total) {
        list.clear();
        adapter.notifyDataSetChanged();
        transid.setText("Transaction No: "+id);
        Response.Listener<String> response = response1 -> {
            try {
                JSONObject jsonResponse = new JSONObject(response1);
                boolean success = jsonResponse.getBoolean("success");
                total.setText("Total Order: ₱"+jsonResponse.getString("Total"));
                JSONArray array = jsonResponse.getJSONArray("data");

                if(success){
                    loading.setVisibility(View.GONE);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);

                        m_orderDetails item = new m_orderDetails(
                                object.getString("id"),
                                object.getString("name"),
                                object.getString("price"),
                                object.getString("img"),
                                object.getString("qty"),
                                object.getString("sub"),
                                object.getString("status")
                        );
                        list.add(item);

                    }
                    adapter = new Adapter_order_details(list,context);
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
        con_order_details get = new con_order_details(id,response,errorListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(get);

    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }


    @Override
    public int getItemCount() {
        return newsList.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder{

       public TextView tra,date;
       public MaterialCardView card;
       public ImageView status;

        public ViewHolder(View itemView) {
            super(itemView);
            tra = itemView.findViewById(R.id.transaction);
            date= itemView.findViewById(R.id.date);
            card = itemView.findViewById(R.id.card);
            status = itemView.findViewById(R.id.status);


        }
    }
}
