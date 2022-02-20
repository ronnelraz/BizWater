package com.example.bizwater;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.bizwater.func.Func;

import butterknife.BindView;
import butterknife.ButterKnife;

public class profile extends AppCompatActivity {
    Func controller;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.firstname)
    TextView full_name;
    @BindView(R.id.emailaddress)
    TextView emailaddress;
    @BindView(R.id.mobilenumber)
    TextView mobilenumber;
    @BindView(R.id.password)
    TextView password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        controller = new Func(this);
        name.setText(controller.FULLNAME());
        username.setText(controller.USERNAME());
        full_name.setText(controller.FULLNAME());
        emailaddress.setText(controller.EMAIL());
        mobilenumber.setText(controller.MOBILENUMBER());
        password.setText(controller.PASSWORD());


    }

    public void back(View view) {

        Func.intent(Home.class,view.getContext());
    }

    public void updateinfo(View view) {

    }
}