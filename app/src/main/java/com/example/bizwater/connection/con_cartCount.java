package com.example.bizwater.connection;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class con_cartCount extends StringRequest {

    public static final String con = config.URL + "cartCount.php";
    private Map<String,String> params;

    public con_cartCount(String id, Response.Listener<String> Listener, Response.ErrorListener errorListener){
        super(Method.POST,con,Listener,errorListener);
        params = new HashMap<>();
        params.put("id",id);
    }

    @Override
    protected Map<String, String> getParams(){
        return params;
    }
}
