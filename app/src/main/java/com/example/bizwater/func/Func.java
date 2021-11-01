package com.example.bizwater.func;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.example.bizwater.R;
import com.novoda.merlin.Merlin;

import java.security.PublicKey;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import butterknife.Action;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class Func {

    private static Func app;
    private static Context cont;
    public SweetAlertDialog pDialog;
    public Merlin merlin;

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static final String DATA = "data";
    private static final String ID = "ID";
    private static final String FIRSTNAME = "FNAME";
    private static final String LASTNAME  = "LNAME";
    private static final String ADDRESS = "ADDRESS";
    private static final String CONTACT = "CONTACT";
    private static final String BUSINESS = "BUSINESS";
    private static final String EMAIL = "EMAIL";
    private static final String USERNAME = "USERNAME";
    private static final String LOGIN = "false";



    public Func(Context context){
        cont = context;
    }

    public static synchronized Func getInstance(Context context){
        if(app == null){
            app = new Func(context);
        }
        return app;
    }

    public boolean SETINFO(List<String> data){
        sharedPreferences = cont.getSharedPreferences(DATA,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(ID,data.get(0));
        editor.putString(FIRSTNAME,data.get(1));
        editor.putString(LASTNAME,data.get(2));
        editor.putString(ADDRESS,data.get(3));
        editor.putString(CONTACT,data.get(4));
        editor.putString(BUSINESS,data.get(5));
        editor.putString(EMAIL,data.get(6));
        editor.putString(USERNAME,data.get(7));
        editor.putString(LOGIN,data.get(8));
        editor.apply();
        return true;
    }


    public boolean SETLOGOUT(String logout){
        sharedPreferences = cont.getSharedPreferences(DATA,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(LOGIN,logout);
        editor.apply();
        return true;
    }

    public String numberformat(float number){
//        DecimalFormat formatter = new DecimalFormat("#,###,###");
//        return formatter.format(number);

        NumberFormat formatter = new DecimalFormat("#0.00");
        return formatter.format(number);
    }

    public String LOGINVAL(){
        sharedPreferences = cont.getSharedPreferences(DATA, Context.MODE_PRIVATE);
        return sharedPreferences.getString(LOGIN,"wala");
    }

    public String FULLNAME(){
        sharedPreferences = cont.getSharedPreferences(DATA, Context.MODE_PRIVATE);
        return sharedPreferences.getString(FIRSTNAME,"") + " " + sharedPreferences.getString(LASTNAME,"");
    }

    public String USERID(){
        sharedPreferences = cont.getSharedPreferences(DATA, Context.MODE_PRIVATE);
        return sharedPreferences.getString(ID,"");
    }





    public static void intent(Class<?> activity, Context context){
        Intent i = new Intent(context,activity);
        context.startActivity(i);
    }

    public void loading(String msg){
        pDialog = new SweetAlertDialog(cont, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(msg);
        pDialog.setCancelable(true);
        pDialog.show();
    }


    /*Error response*/
    public String Errorvolley(VolleyError volleyError){
        String message = null;
        if (volleyError instanceof NetworkError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof ServerError) {
            message = "The server could not be found. Please try again after some time!!";
        } else if (volleyError instanceof AuthFailureError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof ParseError) {
            message = "Parsing error! Please try again after some time!!";
        } else if (volleyError instanceof NoConnectionError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof TimeoutError) {
            message = "Connection TimeOut! Please check your internet connection.";
        }
        return message;
    }


    public void toast(int raw,String body,int postion,int x ,int y){
        Toast toast = new Toast(cont);
        View vs = LayoutInflater.from(cont).inflate(R.layout.custom_toast, null);
        LottieAnimationView icon = vs.findViewById(R.id.icon);
        TextView msg = vs.findViewById(R.id.body);

        icon.setAnimation(raw);
        icon.loop(false);
        icon.playAnimation();
        msg.setText(body);
        toast.setGravity(postion, x, y);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(vs);
        toast.show();
    }
}
