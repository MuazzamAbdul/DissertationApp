package com.example.muazzam.dissertationapp;

import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.muazzam.dissertationapp.Adapter.AdminPageAdapter;
import com.example.muazzam.dissertationapp.Adapter.PageAdapter;

public class Admin_Order_Screen extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AdminPageAdapter AdminpageAdapter;
    private TabItem tabOngoing,tabCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__order__screen);

        setupUIViews();

        AdminpageAdapter = new AdminPageAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(AdminpageAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private void setupUIViews()
    {
        Toolbar toolbar = findViewById(R.id.toolbarOrders);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Orders");

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_action_black_arrow_back);

        tabLayout = findViewById(R.id.AdminTabLayout);
        viewPager = findViewById(R.id.AdminTabViewPager);
        tabOngoing = findViewById(R.id.tabOngoing);
        tabCompleted = findViewById(R.id.tabCompleted);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
