package com.example.bizwater;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.bizwater.func.Func;

public class passwordsuccess extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwordsuccess);
    }

    public void backtologin(View view) {
        Func.intent(Login.class, view.getContext());
    }
}