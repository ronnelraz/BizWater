package com.example.bizwater.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.example.bizwater.Model.m_product;
import com.example.bizwater.R;
import com.example.bizwater.connection.con_product;
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

public class Adapter_categories extends RecyclerView.Adapter<Adapter_categories.ViewHolder> {
    Context mContext;
    List<m_categories> newsList;


    private BottomSheetBehavior bottomSheetBehavior;
    private RecyclerView.Adapter adapter;
    private List<m_product> list;




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

            View view = LayoutInflater.from(v.getContext()).inflate(R.layout.bottomsheet_items,null);

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
            TextInputEditText search = view.findViewById(R.id.search);
            LottieAnimationView loading = view.findViewById(R.id.loading);
            RecyclerView rviewbottom = view.findViewById(R.id.data);
            SwipeRefreshLayout swipe = view.findViewById(R.id.swipe);


            rviewbottom.setHasFixedSize(true);
            rviewbottom.setItemViewCacheSize(999999999);

            list = new ArrayList<>();
            rviewbottom.setLayoutManager(new GridLayoutManager(view.getContext(),2));
            adapter = new Adapter_product(list,view.getContext());
            rviewbottom.setAdapter(adapter);


            loading.setVisibility(View.VISIBLE);

            swipe.setOnRefreshListener(() -> {
                list.clear();
                loadData(view.getContext(),rviewbottom,getData.getId(),loading);
                swipe.setRefreshing(false);
            });

            swipe.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);

            search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    ArrayList<m_product> newListbottom = new ArrayList<>();
                    for (m_product sub : list)
                    {
                        String name = sub.getName().toLowerCase();
                        if(name.contains(s)){
                            newListbottom.add(sub);
                        }
                        adapter = new Adapter_product(newListbottom,view.getContext());
                        rviewbottom.setAdapter(adapter);

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            loadData(view.getContext(),rviewbottom,getData.getId(),loading);

            bottomSheetDialog.setContentView(view);
            bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            bottomSheetDialog.show();


        });//end

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

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
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
