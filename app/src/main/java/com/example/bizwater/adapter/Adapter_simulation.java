package com.example.bizwater.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.example.bizwater.Model.m_simulation;
import com.example.bizwater.R;
import com.example.bizwater.connection.con_product;
import com.example.bizwater.connection.con_sentRequest;
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

public class Adapter_simulation extends RecyclerView.Adapter<Adapter_simulation.ViewHolder> {
    Context mContext;
    List<m_simulation> newsList;


    public Adapter_simulation(List<m_simulation> list, Context context) {
        super();
        this.newsList = list;
        this.mContext = context;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.simulation_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final m_simulation getData = newsList.get(position);
        holder.name.setText(getData.getName());
        holder.img.setImageResource(getData.getImg());
        holder.card.setOnClickListener(v -> {

        });

        holder.card.setOnClickListener(v -> {
            android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(v.getContext());
            View vs = LayoutInflater.from(v.getContext()).inflate(R.layout.description, null);

            TextView partname = vs.findViewById(R.id.partname);
            ImageView partimage= vs.findViewById(R.id.partimage);
            TextView partdesc = vs.findViewById(R.id.partdesc);


            dialog.setView(vs);
            AlertDialog alert = dialog.create();
            partname.setText(getData.getName());
            partimage.setImageResource(getData.getImg());
            partdesc.setText(getData.getDesc());

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
       public ImageView img;
       public MaterialCardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            img= itemView.findViewById(R.id.img);
            card = itemView.findViewById(R.id.card);
        }
    }
}
