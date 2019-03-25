package com.example.muazzam.dissertationapp.Admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.muazzam.dissertationapp.Admin.Fragments.Fragment_Add;
import com.example.muazzam.dissertationapp.Admin.Fragments.Fragment_Delete;
import com.example.muazzam.dissertationapp.Admin.Fragments.Fragment_Home;
import com.example.muazzam.dissertationapp.R;
import com.example.muazzam.dissertationapp.Users.Login_Screen;

public class Admin_Home_Screen extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {



    private BottomNavigationView bottomNavigationView;

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
                Fragment_Home home = new Fragment_Home();
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.main_frame,home).commit();
                return true;

            case R.id.navigation_add:
                Fragment_Add add = new Fragment_Add();
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.main_frame,add).commit();
                return true;

            case R.id.navigation_delete:
                Fragment_Delete delete = new Fragment_Delete();
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.main_frame,delete).commit();
                return true;

            case R.id.navigation_sign_out:
                AlertDialog.Builder exit = new AlertDialog.Builder(Admin_Home_Screen.this);
                exit.setMessage("Do you want to Sign Out?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                Intent intent = new Intent(Admin_Home_Screen.this,Login_Screen.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();

                            }
                        });
                AlertDialog alert = exit.create();
                alert.setTitle("Exit");
                alert.show();



        }


        return false;
    }
}
