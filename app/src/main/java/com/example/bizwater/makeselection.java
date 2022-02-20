package com.example.bizwater;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.bizwater.func.Func;

public class makeselection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makeselection);
    }

    public void callBackScreenFromMakeSelection(View view) {
        Func.intent(forgetpassword.class,view.getContext());
    }

    public void sms(View view) {
        Func.intent(newcredentials.class,view.getContext());
    }
}