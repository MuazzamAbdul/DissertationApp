package com.example.muazzam.dissertationapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.muazzam.dissertationapp.Prevalent.Prevalent;

public class My_Account_Screen extends AppCompatActivity {

    private TextView LogoName,name,email,address,phoneNo;
    private Button update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__account__screen);

        setupUIViews();

        LogoName.setText(Prevalent.onlineUser.getName());
        name.setText(Prevalent.onlineUser.getName());
        email.setText(Prevalent.onlineUser.getEmail());
        address.setText(Prevalent.onlineUser.getAddress());
        phoneNo.setText(Prevalent.onlineUser.getPhoneNo());


    }

    private void setupUIViews()
    {
        Toolbar toolbar = findViewById(R.id.toolbarMyAccount);
        setSupportActionBar(toolbar);
        toolbar.setTitle("My Account");

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_action_arrow_back);

        LogoName = findViewById(R.id.tvAccountName);
        name = findViewById(R.id.tvAccountUserName);
        email = findViewById(R.id.tvAccountEmail);
        address = findViewById(R.id.tvAccountAddress);
        phoneNo = findViewById(R.id.tvAccountPhone);
        update = findViewById(R.id.btnUpdate);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(My_Account_Screen.this,UpdateAccount_Screen.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                Intent intent = new Intent(My_Account_Screen.this,Home_Screen.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


}
