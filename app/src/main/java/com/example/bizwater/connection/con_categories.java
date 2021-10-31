package com.example.bizwater.connection;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class con_categories extends StringRequest {

    public static final String con = config.URL + "get_categories.php";
    private Map<String,String> params;

    public con_categories(Response.Listener<String> Listener, Response.ErrorListener errorListener){
        super(Method.POST,con,Listener,errorListener);
        params = new HashMap<>();
    }

    @Override
    protected Map<String, String> getParams(){
        return params;
    }
}
