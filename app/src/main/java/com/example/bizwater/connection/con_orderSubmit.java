package com.example.bizwater.connection;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class con_orderSubmit extends StringRequest {

    public static final String con = config.URL + "orderSubmit.php";
    private Map<String,String> params;

    public con_orderSubmit(String id,String userID,String method,String address, Response.Listener<String> Listener, Response.ErrorListener errorListener){
        super(Method.POST,con,Listener,errorListener);
        params = new HashMap<>();
        params.put("id",id);
        params.put("userid",userID);
        params.put("method",method);
        params.put("address",address);
    }

    @Override
    protected Map<String, String> getParams(){
        return params;
    }
}
