package com.example.bizwater.connection;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class con_forgotpassword extends StringRequest {

    public static final String con = config.URL + "forgotpasswordEmail.php";
    private Map<String,String> params;

    public con_forgotpassword(String email,String code, Response.Listener<String> Listener, Response.ErrorListener errorListener){
        super(Method.POST,con,Listener,errorListener);
        params = new HashMap<>();
        params.put("email",email);
        params.put("code",code);
    }

    @Override
    protected Map<String, String> getParams(){
        return params;
    }
}
