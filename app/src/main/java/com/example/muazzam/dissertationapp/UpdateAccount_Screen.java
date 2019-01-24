package com.example.muazzam.dissertationapp;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class UpdateAccount_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account__screen);
        setupUIViews();
    }

    private void setupUIViews()
    {
        Toolbar toolbar = findViewById(R.id.toolbarUpdateAcc);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Update Account");

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_action_arrow_back);
    }
}
