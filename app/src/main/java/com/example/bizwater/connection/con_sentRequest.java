package com.example.bizwater.connection;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class con_sentRequest extends StringRequest {

    public static final String con = config.URL + "sentRequest.php";
    private Map<String,String> params;

    public con_sentRequest(String id,String userid,String problem, Response.Listener<String> Listener, Response.ErrorListener errorListener){
        super(Method.POST,con,Listener,errorListener);
        params = new HashMap<>();
        params.put("id",id);
        params.put("userid",userid);
        params.put("problem",problem);
    }

    @Override
    protected Map<String, String> getParams(){
        return params;
    }
}
