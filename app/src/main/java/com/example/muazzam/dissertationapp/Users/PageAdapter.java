package com.example.muazzam.dissertationapp.Users;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.muazzam.dissertationapp.Users.Fragment.Fragment_Category;
import com.example.muazzam.dissertationapp.Users.Fragment.Fragment_Products;

public class PageAdapter extends FragmentPagerAdapter {

    private int numOfTabs;

    public PageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i)
        {
            case 0:
                return new Fragment_Category();

            case 1:
                return new Fragment_Products();

             default:
              return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
