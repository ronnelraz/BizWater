package com.example.bizwater;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.bizwater.func.Func;

public class newcredentials extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newcredentials);
    }

    public void goToHomeFromSetNewPassword(View view) {
        Func.intent(makeselection.class,view.getContext());
    }

    public void newcredential_nxtbtn(View view) {
        Func.intent(passwordsuccess.class,view.getContext());
    }
}