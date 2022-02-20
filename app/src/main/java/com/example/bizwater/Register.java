package com.example.bizwater;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.bizwater.Model.m_categories;
import com.example.bizwater.adapter.Adapter_categories;
import com.example.bizwater.connection.brgy;
import com.example.bizwater.connection.con_categories;
import com.example.bizwater.connection.muni;
import com.example.bizwater.connection.province;
import com.example.bizwater.connection.register;
import com.example.bizwater.func.Func;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.shuhart.stepview.StepView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

    private int currentStep = 0;
    private int currentStep2 = 0;
    @BindView(R.id.step_view)
    StepView stepView;

    @BindViews({R.id.con1,R.id.con2,R.id.con3,R.id.con4})
    LinearLayout[] container;
    @BindViews({R.id.basic_nextbutton,R.id.contact_previousbutton,R.id.contact_nextbutton,R.id.business_previousbutton,R.id.businerss_nextbutton,R.id.login_previousbutton})
    MaterialButton[] btn_controller;

    @BindViews({R.id.province,R.id.muni,R.id.brgy})
    TextView[] address;


    //spinner
    /*@Searchable Spinner*/
    private List<String> spinner_list = new ArrayList<>();
    private ArrayAdapter spinnerAdapter;
    private EditText spinner_search;
    private ListView spinnerview;
    private LottieAnimationView loading;
    private AlertDialog alert;

    private List<String> list_province_name = new ArrayList<>();
    private List<String> getList_province_id = new ArrayList<>();
    private ArrayAdapter adapter_province;
    private String provinceSubID = "";


    /*City*/
    private List<String> list_city_name = new ArrayList<>();
    private List<String> getList_city_id = new ArrayList<>();
    private ArrayAdapter adapter_city;
    private String citySubID = "";

    /*City*/
    private List<String> list_barangay_name = new ArrayList<>();
    private ArrayAdapter adapter_barangay;

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
            String getContact = input[3].getText().toString();
            String getbusiness = input[4].getText().toString();
            String getEmail = input[5].getText().toString();
            String getUsername = input[6].getText().toString();
            String getStatus = Selectedstatus; //di kasali
            String getpassword = input[7].getText().toString();
            String cpassword = input[8].getText().toString();

            String complete = address[0].getText().toString() + " " + address[1].getText().toString() + " " + address[2].getText().toString();
            String province = address[0].getText().toString();
            String city = address[1].getText().toString();
            String brgy = address[2].getText().toString();

            if(getfname.isEmpty()){
                input[0].requestFocus();
                controller.toast(R.raw.error_con,"Please Enter your First name.",Gravity.TOP|Gravity.CENTER,0,50);
            }
            else if(getlname.isEmpty()){
                input[1].requestFocus();
                controller.toast(R.raw.error_con,"Please Enter your Last name.",Gravity.TOP|Gravity.CENTER,0,50);
            }
//            else if(getaddress.isEmpty()){
//                input[2].requestFocus();
//                controller.toast(R.raw.error_con,"Please enter your Address.",Gravity.TOP|Gravity.CENTER,0,50);
//            }
            else if(getContact.isEmpty()){
                input[3].requestFocus();
                controller.toast(R.raw.error_con,"Please Enter your Mobile Number.",Gravity.TOP|Gravity.CENTER,0,50);
            }
            else if(getbusiness.isEmpty()){
                input[4].requestFocus();
                controller.toast(R.raw.error_con,"Please Enter your Business.",Gravity.TOP|Gravity.CENTER,0,50);
            }
            else if(getEmail.isEmpty()){
                input[5].requestFocus();
                controller.toast(R.raw.error_con,"Please Enter valid email Address",Gravity.TOP|Gravity.CENTER,0,50);
            }
            else if(isEmailValid(getEmail)){
                input[5].requestFocus();
                controller.toast(R.raw.error_con,"Please Enter valid email Address",Gravity.TOP|Gravity.CENTER,0,50);
            }
            else if(getUsername.isEmpty()){
                input[6].requestFocus();
                controller.toast(R.raw.error_con,"Please Enter your Username.",Gravity.TOP|Gravity.CENTER,0,50);
            }
            else if(getpassword.isEmpty() || input[7].length() <=7){
                input[7].requestFocus();
                controller.toast(R.raw.error_con,"Please Enter valid password at least 8 characters",Gravity.TOP|Gravity.CENTER,0,50);
            }
            else if(!cpassword.equals(getpassword)){
                input[8].requestFocus();
                controller.toast(R.raw.error_con,"Password not match.",Gravity.TOP|Gravity.CENTER,0,50);
            }
            else{
                String statusme = getStatus == "New Customer" ? "2" : "1";
                Log.d("registered",getfname +" " +getlname+" " +complete+" " +getContact+" " +getbusiness+" " +getEmail+" " +getUsername+" "+ getStatus+" "+cpassword+" "+province+" "+city+" "+brgy);
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
                register get = new register(getfname,getlname,complete,getContact,getbusiness,getEmail,getUsername,statusme,cpassword,province,city,brgy,response,errorListener);
                RequestQueue queue = Volley.newRequestQueue(v.getContext());
                queue.add(get);
            }
        });



        initButtons();
        SelectProvince();

    }


    protected void SelectProvince(){
        try{
            address[0].setOnClickListener(view -> {
                list_province_name.clear();

                Response.Listener<String> response = response1 -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response1);
                        boolean success = jsonResponse.getBoolean("success");
                        JSONArray array = jsonResponse.getJSONArray("data");



                        if(success){
                            loading.setVisibility(View.GONE);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                list_province_name.add(object.getString("name"));
                                getList_province_id.add(object.getString("subid"));

                            }

                            adapter_province = new ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1,list_province_name);
                            spinnerview.setAdapter(adapter_province);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                };
                Response.ErrorListener errorListener = error -> {
                    String result = controller.Errorvolley(error);
                    loading.setVisibility(View.VISIBLE);
                };
                province get = new province(response,errorListener);
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                queue.add(get);

                AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                View vs = LayoutInflater.from(view.getContext()).inflate(R.layout.custom_spinner, null);
                spinnerview = vs.findViewById(R.id.list_item);
                spinner_search = vs.findViewById(R.id.search);
                loading = vs.findViewById(R.id.loading);

                spinner_search.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int position, int i1, int i2) {
                        adapter_province.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                spinnerview.setOnItemClickListener((adapterView, view1, i, l) -> {
                    address[0].setText(adapter_province.getItem(i).toString());
                    int indexOf = list_province_name.indexOf(adapter_province.getItem(i));
                    SelectCity(getList_province_id.get(indexOf));
                    alert.dismiss();
                    list_city_name.clear();
                    getList_city_id.clear();
                    list_barangay_name.clear();
                    address[2].setText("Select Barangay");
                    address[1].setText("Select Municipality/City");
                });

                dialog.setView(vs);
                alert = dialog.create();
                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alert.setCanceledOnTouchOutside(false);
                alert.setCancelable(true);
                alert.show();
            });
        }catch(Exception e){
            System.out.println("Error");
        }


    }

    protected void SelectCity(String id){
        try{
            address[1].setOnClickListener(view -> {
                list_city_name.clear();

                Response.Listener<String> response = response1 -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response1);
                        boolean success = jsonResponse.getBoolean("success");
                        JSONArray array = jsonResponse.getJSONArray("data");

                        if(success){
                            loading.setVisibility(View.GONE);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                list_city_name.add(object.getString("name"));
                                getList_city_id.add(object.getString("subid"));

                            }

                            adapter_city = new ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1,list_city_name);
                            spinnerview.setAdapter(adapter_city);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                };
                Response.ErrorListener errorListener = error -> {
                    loading.setVisibility(View.VISIBLE);
                };
                muni get = new muni(id,response,errorListener);
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                queue.add(get);

                AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                View vs = LayoutInflater.from(view.getContext()).inflate(R.layout.custom_spinner, null);
                spinnerview = vs.findViewById(R.id.list_item);
                spinner_search = vs.findViewById(R.id.search);
                loading = vs.findViewById(R.id.loading);

                spinner_search.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int position, int i1, int i2) {
                        adapter_city.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                spinnerview.setOnItemClickListener((adapterView, view1, i, l) -> {
                    address[1].setText(adapter_city.getItem(i).toString());
                    int indexOf = list_city_name.indexOf(adapter_city.getItem(i));
                    SelectBrgy(getList_city_id.get(indexOf));
                    alert.dismiss();
                    list_barangay_name.clear();
                    address[2].setText("Select Barangay");
                });






                dialog.setView(vs);
                alert = dialog.create();
                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alert.setCanceledOnTouchOutside(false);
                alert.setCancelable(true);
                alert.show();
            });
        }catch(Exception e){
            System.out.println("Error");
        }
    }

    protected void SelectBrgy(String ID){

        try{
            address[2].setOnClickListener(view -> {
                list_barangay_name.clear();

                Response.Listener<String> response = response1 -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response1);
                        boolean success = jsonResponse.getBoolean("success");
                        JSONArray array = jsonResponse.getJSONArray("data");

                        if(success){
                            loading.setVisibility(View.GONE);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                list_barangay_name.add(object.getString("name"));

                            }

                            adapter_barangay = new ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1,list_barangay_name);
                            spinnerview.setAdapter(adapter_barangay);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                };
                Response.ErrorListener errorListener = error -> {
                    loading.setVisibility(View.VISIBLE);
                };
                brgy get = new brgy(ID,response,errorListener);
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                queue.add(get);

                AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                View vs = LayoutInflater.from(view.getContext()).inflate(R.layout.custom_spinner, null);
                spinnerview = vs.findViewById(R.id.list_item);
                spinner_search = vs.findViewById(R.id.search);
                loading = vs.findViewById(R.id.loading);

                spinner_search.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int position, int i1, int i2) {
                        adapter_barangay.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                spinnerview.setOnItemClickListener((adapterView, view1, i, l) -> {
                    address[2].setText(adapter_barangay.getItem(i).toString());
                    alert.dismiss();
                });

                dialog.setView(vs);
                alert = dialog.create();
                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alert.setCanceledOnTouchOutside(false);
                alert.setCancelable(true);
                alert.show();
            });
        }catch(Exception e){
            System.out.println("Error");
        }

    }


    private void initButtons() {

        btn_controller[0].setOnClickListener(v -> {
            String getfname = input[0].getText().toString();
            String getlname = input[1].getText().toString();
            if(getfname.isEmpty()){
                input[0].requestFocus();
                controller.toast(R.raw.error_con,"Please enter your First name.",Gravity.TOP|Gravity.CENTER,0,50);
            }
            else if(getlname.isEmpty()){
                input[1].requestFocus();
                controller.toast(R.raw.error_con,"Please enter your Last name.",Gravity.TOP|Gravity.CENTER,0,50);
            }
            else{
                container[0].setVisibility(View.GONE);
                container[1].setVisibility(View.VISIBLE);
                step(1);
            }
        });

        //contact
        btn_controller[1].setOnClickListener(v-> {
            container[1].setVisibility(View.GONE);
            container[0].setVisibility(View.VISIBLE);
            step(0);
        });
        btn_controller[2].setOnClickListener(v ->{
            String getEmail = input[5].getText().toString();
            String getContact = input[3].getText().toString();
            if(getEmail.isEmpty()){
                input[5].requestFocus();
                controller.toast(R.raw.error_con,"Please Enter Valid Email Address",Gravity.TOP|Gravity.CENTER,0,50);
            }
            else if(isEmailValid(getEmail)){
                input[5].requestFocus();
                controller.toast(R.raw.error_con,"Please Enter Valid Email Address",Gravity.TOP|Gravity.CENTER,0,50);
            }
            else if(getContact.isEmpty()){
                input[3].requestFocus();
                controller.toast(R.raw.error_con,"Please Enter Your Mobile Number.",Gravity.TOP|Gravity.CENTER,0,50);
            }
            else{
                container[1].setVisibility(View.GONE);
                container[2].setVisibility(View.VISIBLE);
                step(2);
            }
        });

        //business
        btn_controller[3].setOnClickListener(v-> {
            container[2].setVisibility(View.GONE);
            container[1].setVisibility(View.VISIBLE);
            step(1);
        });
        btn_controller[4].setOnClickListener(v ->{
//            String getaddress = input[2].getText().toString();
            String getbusiness = input[4].getText().toString();
            String getprovince = address[0].getText().toString();
            String getCity = address[1].getText().toString();
            String getbrgy = address[2].getText().toString();

            if(getbusiness.isEmpty()){
                input[4].requestFocus();
                controller.toast(R.raw.error_con,"Please Enter your Business.",Gravity.TOP|Gravity.CENTER,0,50);
            }
            else if(getprovince.equals("Select Province")){
                address[0].performClick();
                controller.toast(R.raw.error_con,"Please Select Province",Gravity.TOP|Gravity.CENTER,0,50);
            }
            else if(getCity.equals("Select Municipality/City")){
                address[1].performClick();
                controller.toast(R.raw.error_con,"Please Municipality/City",Gravity.TOP|Gravity.CENTER,0,50);
            }
            else if(getbrgy.equals("Select Barangay")){
                address[2].performClick();
                controller.toast(R.raw.error_con,"Please Select Barangay",Gravity.TOP|Gravity.CENTER,0,50);
            }

            else{
                container[2].setVisibility(View.GONE);
                container[3].setVisibility(View.VISIBLE);
                step(3);
            }

        });

        //login
        btn_controller[5].setOnClickListener(v ->{
            container[3].setVisibility(View.GONE);
            container[2].setVisibility(View.VISIBLE);
            step(2);
        });

    }


    private void step(int i){
        stepView.go(i,true);
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