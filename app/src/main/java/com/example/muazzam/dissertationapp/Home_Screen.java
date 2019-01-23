package com.example.muazzam.dissertationapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.muazzam.dissertationapp.Prevalent.Prevalent;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home_Screen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView im;
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Home");
        im = findViewById(R.id.ivFruitVeg);


        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Screen.this,Category_Screen.class);
                startActivity(intent);

            }
        });

        drawer = findViewById(R.id.drawer_layout);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_action_menu);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView usernameText = headerView.findViewById(R.id.tvnavbar_name);
        TextView usernameEmail = headerView.findViewById(R.id.tvnavbar_email);
        CircleImageView profileImage = headerView.findViewById(R.id.image_profile_pic);

        usernameText.setText(Prevalent.onlineUser.getName());
        usernameEmail.setText(Prevalent.onlineUser.getEmail());


    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home__screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;

            case R.id.itshopping_cart:
                Intent intent = new Intent(this,Category_Screen.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.my_account) {
            Intent intent = new Intent(Home_Screen.this, My_Account_Screen.class);
            startActivity(intent);
        }else if (id == R.id.rules_reg) {
            Intent intent = new Intent(Home_Screen.this, Rules_Reg_Screen.class);
            startActivity(intent);
        } else if (id == R.id.contact_us) {
            Intent intent = new Intent(Home_Screen.this, My_Account_Screen.class);
            startActivity(intent);
        } else if (id == R.id.settings) {
            Intent intent = new Intent(Home_Screen.this, My_Account_Screen.class);
            startActivity(intent);
        } else if (id == R.id.sign_out) {
            finish();
            Intent intent = new Intent(Home_Screen.this, Login_Screen.class);
            startActivity(intent);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
