package com.example.muazzam.dissertationapp.Admin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.muazzam.dissertationapp.Admin.Fragments.Fragment_Add;
import com.example.muazzam.dissertationapp.Admin.Fragments.Fragment_Delete;
import com.example.muazzam.dissertationapp.Admin.Fragments.Fragment_Home;
import com.example.muazzam.dissertationapp.R;

public class Admin_Home_Screen extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {



    private BottomNavigationView bottomNavigationView;
    Fragment_Home home = new Fragment_Home();
    Fragment_Add add = new Fragment_Add();
    Fragment_Delete delete = new Fragment_Delete();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__home__screen);


        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId())
        {
            case R.id.navigation_home:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.main_frame,home).commit();
                return true;

            case R.id.navigation_add:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.main_frame,add).commit();
                return true;

            case R.id.navigation_delete:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.main_frame,delete).commit();
                return true;
        }
        return false;
    }
}
