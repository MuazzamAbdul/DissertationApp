package com.example.muazzam.dissertationapp.Users;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.muazzam.dissertationapp.Model.ShippingDetails;
import com.example.muazzam.dissertationapp.Prevalent.Prevalent;
import com.example.muazzam.dissertationapp.R;

public class Billing_Info_Screen extends AppCompatActivity {

    private EditText etfname,etlname,etaddr,etcity;
    private String fname,lname,address,city,price;
    private Button proceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing__info__screen);

        setupUIViews();

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateTextFields())
                {
                    ShippingDetails shipping = new ShippingDetails(fname,lname,address,city,price);
                    Prevalent.shippingDetails = shipping;
                    finish();
                    Intent intent = new Intent(Billing_Info_Screen.this,Choose_Payment_Method_Screen.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void setupUIViews()
    {
        Toolbar toolbar = findViewById(R.id.toolbarShippingDetails);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Shipping Details");

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_action_arrow_back);

        etfname = findViewById(R.id.etFirstName);
        etlname = findViewById(R.id.etLastName);
        etaddr = findViewById(R.id.etShipAddress);
        etcity = findViewById(R.id.etCity);
        proceed = findViewById(R.id.btnContinue);

        Bundle getPrice = getIntent().getExtras();
        if (getPrice == null)
        {
            Toast.makeText(Billing_Info_Screen.this,price,Toast.LENGTH_SHORT).show();
        }
        else
        {
            price = getPrice.getString("Price");
            Toast.makeText(Billing_Info_Screen.this,price,Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateTextFields()
    {
        boolean result = false;
        fname = etfname.getText().toString();
        lname = etlname.getText().toString();
        address = etaddr.getText().toString();
        city = etcity.getText().toString();

        if (TextUtils.isEmpty(fname))
        {
            etfname.setError("Please enter First Name");
        }
        else if(TextUtils.isEmpty(lname))
        {
            etlname.setError("Please enter Last Name");
        }
        else if (TextUtils.isEmpty(address))
        {
            etaddr.setError("Please enter Address");
        }
        else if (TextUtils.isEmpty(city))
        {
            etcity.setError("Please enter City");
        }
        else
        {
            result = true;
        }
        return result;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                Intent intent = new Intent(Billing_Info_Screen.this,Shopping_Cart_Screen.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        Intent intent = new Intent(Billing_Info_Screen.this,Shopping_Cart_Screen.class);
        startActivity(intent);
    }
}
