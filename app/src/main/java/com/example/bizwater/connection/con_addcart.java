package com.example.bizwater.connection;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class con_addcart extends StringRequest {

    public static final String con = config.URL + "addTocart.php";
    private Map<String,String> params;

    public con_addcart(String id,String userid,String qty,String total, Response.Listener<String> Listener, Response.ErrorListener errorListener){
        super(Method.POST,con,Listener,errorListener);
        params = new HashMap<>();
        params.put("id",id);
        params.put("userid",userid);
        params.put("qty",qty);
        params.put("total",total);
    }

    @Override
    protected Map<String, String> getParams(){
        return params;
    }
}
