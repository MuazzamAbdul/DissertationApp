package com.example.muazzam.dissertationapp.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.muazzam.dissertationapp.Fragment_Completed_Order;
import com.example.muazzam.dissertationapp.Fragment_Ongoing_Order;

public class AdminPageAdapter extends FragmentPagerAdapter {

    private int numOfTabs;

    public AdminPageAdapter(FragmentManager fm , int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i)
        {
            case 0:
                return new Fragment_Ongoing_Order();

            case 1:
                return new Fragment_Completed_Order();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
