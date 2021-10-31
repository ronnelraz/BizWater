package com.example.bizwater;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.bizwater.connection.register;
import com.example.bizwater.func.Func;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class Register extends AppCompatActivity {

    @BindView(R.id.status)
    Spinner status;
    List<String> listStatus = new ArrayList<>();
    ArrayAdapter<String> adapter;
    private String Selectedstatus = "Former Customer";

    @BindViews({R.id.firstname,R.id.lastname,R.id.address,R.id.mobilenumber,R.id.business,R.id.email,R.id.username,R.id.password,R.id.cpassword})
    TextInputEditText[] input;
    @BindView(R.id.register)
    MaterialButton register;

    Func controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        controller = new Func(this);

        listStatus.add("Former Customer");
        listStatus.add("New Customer");
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listStatus);
        status.setAdapter(adapter);

        status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String getitem = listStatus.get(i);
                Selectedstatus = getitem;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        register.setOnClickListener(v -> {
            String getfname = input[0].getText().toString();
            String getlname = input[1].getText().toString();
            String getaddress = input[2].getText().toString();
            String getContact = input[3].getText().toString();
            String getbusiness = input[4].getText().toString();
            String getEmail = input[5].getText().toString();
            String getUsername = input[6].getText().toString();
            String getStatus = Selectedstatus; //di kasali
            String getpassword = input[7].getText().toString();
            String cpassword = input[8].getText().toString();

            if(getfname.isEmpty()){
                input[0].requestFocus();
                controller.toast(R.raw.error_con,"Please enter your first name.",Gravity.TOP|Gravity.CENTER,0,50);
            }
            else if(getlname.isEmpty()){
                input[1].requestFocus();
                controller.toast(R.raw.error_con,"Please enter your Last name.",Gravity.TOP|Gravity.CENTER,0,50);
            }
            else if(getaddress.isEmpty()){
                input[2].requestFocus();
                controller.toast(R.raw.error_con,"Please enter your Address.",Gravity.TOP|Gravity.CENTER,0,50);
            }
            else if(getContact.isEmpty()){
                input[3].requestFocus();
                controller.toast(R.raw.error_con,"Please enter your Mobile Number.",Gravity.TOP|Gravity.CENTER,0,50);
            }
            else if(getbusiness.isEmpty()){
                input[4].requestFocus();
                controller.toast(R.raw.error_con,"Please enter your Business.",Gravity.TOP|Gravity.CENTER,0,50);
            }
            else if(getEmail.isEmpty()){
                input[5].requestFocus();
                controller.toast(R.raw.error_con,"Please enter valid email Address",Gravity.TOP|Gravity.CENTER,0,50);
            }
            else if(isEmailValid(getEmail)){
                input[5].requestFocus();
                controller.toast(R.raw.error_con,"Please enter valid email Address",Gravity.TOP|Gravity.CENTER,0,50);
            }
            else if(getUsername.isEmpty()){
                input[6].requestFocus();
                controller.toast(R.raw.error_con,"Please enter your Username.",Gravity.TOP|Gravity.CENTER,0,50);
            }
            else if(getpassword.isEmpty() || input[7].length() <=7){
                input[7].requestFocus();
                controller.toast(R.raw.error_con,"Please enter valid password at least 8 characters",Gravity.TOP|Gravity.CENTER,0,50);
            }
            else if(!cpassword.equals(getpassword)){
                input[8].requestFocus();
                controller.toast(R.raw.error_con,"Password not match.",Gravity.TOP|Gravity.CENTER,0,50);
            }
            else{
                controller.loading("Please wait...");
                Response.Listener<String> response = response1 -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response1);
                        boolean error = jsonResponse.getBoolean("error");
                        String msg = jsonResponse.getString("message");



                        if(error){
                            //registered already
                            controller.toast(R.raw.error_con,msg,Gravity.TOP|Gravity.CENTER,0,50);
                            controller.pDialog.dismiss(); //close modal loading

                        }
                        else{
                            //okay
                            controller.pDialog.dismiss(); //close modal loading
                            controller.toast(R.raw.ok,msg,Gravity.TOP|Gravity.CENTER,0,50);
                           new Handler().postDelayed(() -> {
                               Func.intent(Login.class,v.getContext());
                               finish();
                           },3000);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                };
                Response.ErrorListener errorListener = error -> {
                    String result = controller.Errorvolley(error);
                    controller.toast(R.raw.error_con,result,Gravity.TOP|Gravity.CENTER,0,50);

                };
                register get = new register(getfname,getlname,getaddress,getContact,getbusiness,getEmail,getUsername,getStatus,cpassword,response,errorListener);
                RequestQueue queue = Volley.newRequestQueue(v.getContext());
//                get.setRetryPolicy(new DefaultRetryPolicy(
//                        50000,
//                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(get);
            }
        });



    }


    boolean isEmailValid(String email) {
       String patters = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
       if(!email.matches(patters)) {
           return true;
       }
       else{
           return false;
       }
    }


    public void back(View view) {
        Func.intent(Login.class,view.getContext());
        finish();
    }

    @Override
    public void onBackPressed() {
        Func.intent(Login.class,this);
        finish();
        super.onBackPressed();
    }
}