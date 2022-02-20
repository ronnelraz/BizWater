package com.example.bizwater.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.bizwater.fragments.Tab1;
import com.example.bizwater.fragments.Tab2;
import com.example.bizwater.fragments.Tab3;
import com.example.bizwater.fragments.Tabfive;
import com.example.bizwater.fragments.Tabfour;
import com.example.bizwater.fragments.Tabone;
import com.example.bizwater.fragments.Tabthree;
import com.example.bizwater.fragments.Tabtwo;

import org.jetbrains.annotations.NotNull;

public class TabAdapter extends FragmentPagerAdapter {
    Context mContext;
    int mTotalTabs;
    public TabAdapter(Context context , FragmentManager fragmentManager , int totalTabs) {
        super(fragmentManager);
        mContext = context;
        mTotalTabs = totalTabs;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Tabone();
            case 1:
                return new Tabtwo();
            case 2:
                return new Tabthree();
            case 3:
                return new Tabfour();
            case 4:
                return new Tabfive();
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 0;
    }
}
