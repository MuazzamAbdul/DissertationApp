package com.example.muazzam.dissertationapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class Rules_Reg_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules__reg__screen);

        Toolbar toolbar = findViewById(R.id.toolbarRulesReg);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Rules & Regulations");
    }
}
