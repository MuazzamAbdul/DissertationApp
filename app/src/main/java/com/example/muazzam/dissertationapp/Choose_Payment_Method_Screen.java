package com.example.muazzam.dissertationapp;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muazzam.dissertationapp.Users.Cat_Prod_Screen;

public class Choose_Payment_Method_Screen extends AppCompatActivity {
    private String price;
    private TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose__payment__method__screen);

        setupUIViews();
    }

    private void setupUIViews()
    {
        total = findViewById(R.id.tvPricePay);
        Toolbar toolbar = findViewById(R.id.toolbarPaymentMethod);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Payment Method");

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_action_arrow_back);

        Bundle getPrice = getIntent().getExtras();
        if (getPrice == null)
        {
            return;
        }
        else
        {
            price = getPrice.getString("Price");
            total.setText(price);
            Toast.makeText(Choose_Payment_Method_Screen.this,price,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                Intent intent = new Intent(Choose_Payment_Method_Screen.this,Shopping_Cart_Screen.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
