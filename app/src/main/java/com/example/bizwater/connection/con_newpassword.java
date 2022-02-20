package com.example.bizwater.connection;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class con_newpassword extends StringRequest {

    public static final String con = config.URL + "newpassword.php";
    private Map<String,String> params;

    public con_newpassword(String email, String password, Response.Listener<String> Listener, Response.ErrorListener errorListener){
        super(Method.POST,con,Listener,errorListener);
        params = new HashMap<>();
        params.put("email",email);
        params.put("password",password);
    }

    @Override
    protected Map<String, String> getParams(){
        return params;
    }
}
