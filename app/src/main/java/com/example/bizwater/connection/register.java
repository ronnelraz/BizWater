package com.example.bizwater.connection;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class register extends StringRequest {

    public static final String con = config.URL + "registerUser.php";
    private Map<String,String> params;

    public register(String firstname,String lastname,String address,String number,String business,String email,String username,String status,String password,String province,String muni,String brgy,Response.Listener<String> Listener, Response.ErrorListener errorListener){
        super(Method.POST,con,Listener,errorListener);
        params = new HashMap<>();
        params.put("firstname",firstname);
        params.put("lastname",lastname);
        params.put("address",address);
        params.put("mobilenumber",number);
        params.put("business",business);
        params.put("email",email);
        params.put("username",username);
        params.put("status",status);
        params.put("password",password);
        params.put("province",province);
        params.put("muni",muni);
        params.put("brgy",brgy);
    }

    @Override
    protected Map<String, String> getParams(){
        return params;
    }
}
