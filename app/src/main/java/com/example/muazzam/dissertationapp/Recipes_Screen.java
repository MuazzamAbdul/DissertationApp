package com.example.muazzam.dissertationapp;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class Recipes_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes__screen);
        setupUIViews();
    }

    private void setupUIViews()
    {
        Toolbar toolbar = findViewById(R.id.toolbarRecipes);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Recipes");

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_action_arrow_back);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
//                Intent intent = new Intent(Reviews_Screen.this,Home_Screen.class);
//                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
