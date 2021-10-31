package com.example.bizwater;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.bizwater.connection.con_login;
import com.example.bizwater.connection.register;
import com.example.bizwater.func.Func;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class Login extends AppCompatActivity {

    Func controller;
    @BindView(R.id.login)
    MaterialButton login;
    @BindViews({R.id.username,R.id.password})
    TextInputEditText[] input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        controller = new Func(this);

//        Toast.makeText(this, controller.LOGINVAL(), Toast.LENGTH_SHORT).show();
        boolean autologin = controller.LOGINVAL().equals("wala") ? false : true;
        if(autologin){
            Func.intent(Home.class,this);
            finish();
        }


        login.setOnClickListener(v -> {
            String getusername = input[0].getText().toString();
            String getPassword = input[1].getText().toString();

            if(getusername.isEmpty()){
                input[0].requestFocus();
                controller.toast(R.raw.error_con,"Please Enter your username.",Gravity.TOP|Gravity.CENTER,0,50);
            }
            else if(getPassword.isEmpty()){
                input[1].requestFocus();
                controller.toast(R.raw.error_con,"Please Enter your Password.",Gravity.TOP|Gravity.CENTER,0,50);
            }
            else{
                controller.loading("Please wait...");
                Response.Listener<String> response = response1 -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response1);
                        boolean error = jsonResponse.getBoolean("error");
                        String id = jsonResponse.getString("id");
                        String fname = jsonResponse.getString("firstname");
                        String lname = jsonResponse.getString("lastname");
                        String address = jsonResponse.getString("address");
                        String contact = jsonResponse.getString("mobilenumber");
                        String business = jsonResponse.getString("business");
                        String email = jsonResponse.getString("email");
                        String username = jsonResponse.getString("username");
                        String msg = jsonResponse.getString("message");

                        if(error){

                            //registered already
                            new Handler().postDelayed(() -> {
                                controller.toast(R.raw.error_con,msg,Gravity.TOP|Gravity.CENTER,0,50);
                                controller.pDialog.dismiss(); //close modal loading
                            },5000);


                        }
                        else{

                            //okay
                            controller.pDialog.dismiss(); //close modal loading
                            controller.toast(R.raw.ok,msg,Gravity.TOP|Gravity.CENTER,0,50);
                            controller.SETINFO(Arrays.asList(id,fname,lname,address,contact,business,email,username,"login"));

                            new Handler().postDelayed(() -> {
                                Func.intent(Home.class,v.getContext());
                                finish();
                            },3000);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                };
                Response.ErrorListener errorListener = error -> {
                    String result = controller.Errorvolley(error);
                    controller.pDialog.dismiss();
                    controller.toast(R.raw.error_con,result,Gravity.TOP|Gravity.CENTER,0,50);

                };
                con_login get = new con_login(getusername,getPassword,response,errorListener);
                RequestQueue queue = Volley.newRequestQueue(v.getContext());
                get.setRetryPolicy(new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(get);
            }

        });
    }

    public void register(View view) {
        Func.intent(Register.class,view.getContext());
    }
}