package com.example.bizwater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.bizwater.adapter.TabAdapter;
import com.example.bizwater.adapter.TabLayoutAdapter;
import com.example.bizwater.func.Func;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class view_orders extends AppCompatActivity {

    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.pager)
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_orders);
        ButterKnife.bind(this);


        tabs.addTab(tabs.newTab().setText("To Pay"));
        tabs.addTab(tabs.newTab().setText("To Ship"));
        tabs.addTab(tabs.newTab().setText("To Received"));
        tabs.addTab(tabs.newTab().setText("Completed"));
        tabs.addTab(tabs.newTab().setText("Canceled"));
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);

        TabAdapter adapter=new TabAdapter(this,getSupportFragmentManager(),tabs.getTabCount());
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }





    public void back(View view) {
        Func.intent(Home.class,view.getContext());
    }
}