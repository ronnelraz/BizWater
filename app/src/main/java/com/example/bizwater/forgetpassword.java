package com.example.bizwater;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.bizwater.connection.con_forgotpassword;
import com.example.bizwater.connection.con_login;
import com.example.bizwater.connection.con_newpassword;
import com.example.bizwater.func.Func;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.raycoarana.codeinputview.CodeInputView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class forgetpassword extends AppCompatActivity {

    @BindView(R.id.email)
    TextInputEditText email;

    @BindView(R.id.inputcode)
    CodeInputView inputcode;
    Func controller;

    @BindView(R.id.con1)
    RelativeLayout con1;
    @BindView(R.id.con2)
    LinearLayout con2;

    @BindView(R.id.con3)
    LinearLayout con3;
    @BindView(R.id.countdown)
    TextView countdown;
    @BindView(R.id.resetCode)
    MaterialButton resetCode;
    @BindView(R.id.codesubmit)
    MaterialButton codesubmit;
    @BindView(R.id.forget_password_title)
    TextView title;
    @BindView(R.id.forget_password_description)
    TextView desc;

    @BindViews({R.id.password,R.id.conpassword}) TextInputEditText[] input;

    public String code = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);
        ButterKnife.bind(this);
        controller = new Func(this);
    }

    public void callBackScreenFromForgetPassword(View view) {
        Func.intent(Login.class,view.getContext());
    }

    //code
    public static String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }

    //email validator
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void forgetpassword_nxtbtn(View view) {
//        Func.intent(makeselection.class,view.getContext());
        String getEmail = email.getText().toString();

        if(getEmail.isEmpty()){
            controller.toast(R.raw.error_con,"Please Enter your email address", Gravity.TOP|Gravity.CENTER,0,50);
        }
        else if(!isEmailValid(getEmail)){
            controller.toast(R.raw.error_con,"Invalid Email Address", Gravity.TOP|Gravity.CENTER,0,50);
        }
        else{
            String generateCode = getRandomNumberString();
            code = generateCode;
            controller.loading("Please wait...");
            Response.Listener<String> response = response1 -> {
                try {
                    JSONObject jsonResponse = new JSONObject(response1);
                    boolean error = jsonResponse.getBoolean("success");

                    if(error){
                        controller.pDialog.dismiss(); //close modal loading
                        resetcode3min();
                        inputcode.setEditable(true);
                        inputcode.setShowKeyboard(true);
                        Log.d("code: ", generateCode + " : : " +code);
                        //online function here
                        con1.setVisibility(View.GONE);
                        con2.setVisibility(View.VISIBLE);
                        controller.toast(R.raw.ok,"Email sent", Gravity.TOP|Gravity.CENTER,0,50);
                    }
                    else{
                        controller.toast(R.raw.error_con,"Something went wrong please try again later.", Gravity.TOP|Gravity.CENTER,0,50);
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
            con_forgotpassword get = new con_forgotpassword(getEmail,generateCode,response,errorListener);
            RequestQueue queue = Volley.newRequestQueue(view.getContext());
            get.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(get);

        }
    }

    public void submitcode(View view) {
        codesubmit.setEnabled(false);
        String getcode = code;
        String getInputCode = inputcode.getCode();
        Log.d("code: ", getcode + " : : " +getInputCode);

        if(getInputCode.equals(getcode)){
            controller.toast(R.raw.ok,"ok show form password", Gravity.TOP|Gravity.CENTER,0,50);
            con3.setVisibility(View.VISIBLE);
            con2.setVisibility(View.GONE);
            title.setVisibility(View.GONE);
            desc.setVisibility(View.GONE);
        }
        else{
            controller.toast(R.raw.error_con,"Invalid Code", Gravity.TOP|Gravity.CENTER,0,50);
        }
    }


    //resetcode
    public void resetcode3min(){
        //sasas
        new CountDownTimer(300000, 1000) {

            public void onTick(long millisUntilFinished) {
                countdown.setText("Verfication code time valid" + millisUntilFinished / 1000);
                codesubmit.setVisibility(View.VISIBLE);


            }

            public void onFinish() {
                String generateCode = getRandomNumberString();
                code = generateCode;
                inputcode.setCode("");
                inputcode.setEditable(true);
                inputcode.setShowKeyboard(true);
                resetCode.setVisibility(View.VISIBLE);
                codesubmit.setVisibility(View.GONE);
                codesubmit.setEnabled(true);
            }

        }.start();

    }


    public void resetcode(View view) {
        codesubmit.setEnabled(true);
        resetCode.setVisibility(View.GONE);
        codesubmit.setVisibility(View.VISIBLE);
        String generateCode = getRandomNumberString();
        code = generateCode;
        Log.d("code: ", code );
        resetcode3min();
    }

    public void changepass(View view) {
        String getEmail = email.getText().toString();
        String getpass = input[0].getText().toString();
        String getconpass = input[1].getText().toString();

        if(getpass.isEmpty()){
            controller.toast(R.raw.error_con,"Please Enter your new password", Gravity.TOP|Gravity.CENTER,0,50);
        }
        else if(input[0].length() <= 7){
            controller.toast(R.raw.error_con,"Please Enter your new password at least 8 characters", Gravity.TOP|Gravity.CENTER,0,50);
        }
        else if(!getconpass.equals(getpass)){
            controller.toast(R.raw.error_con,"Your password not match", Gravity.TOP|Gravity.CENTER,0,50);
        }
        else{
            controller.loading("Please wait...");
            Response.Listener<String> response = response1 -> {
                try {
                    JSONObject jsonResponse = new JSONObject(response1);
                    boolean error = jsonResponse.getBoolean("success");

                    if(error){
                        controller.pDialog.dismiss(); //close modal loading
                        controller.toast(R.raw.ok,"changed Password successfully", Gravity.TOP|Gravity.CENTER,0,50);
                        Func.intent(Login.class,view.getContext());
                    }
                    else{
                        controller.toast(R.raw.error_con,"Something went wrong please try again later.", Gravity.TOP|Gravity.CENTER,0,50);
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
            con_newpassword get = new con_newpassword(getEmail,getconpass,response,errorListener);
            RequestQueue queue = Volley.newRequestQueue(view.getContext());
            get.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(get);
        }
    }
}