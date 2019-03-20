package com.example.muazzam.dissertationapp.Users;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muazzam.dissertationapp.R;

public class Choose_Payment_Method_Screen extends AppCompatActivity {
    private String price;
    private TextView total;
    private CardView debitCredit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose__payment__method__screen);

        setupUIViews();
        debitCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Choose_Payment_Method_Screen.this,Category_Screen.class);
                startActivity(intent);
            }
        });
    }

    private void setupUIViews()
    {
        total = findViewById(R.id.tvPricePay);
        debitCredit = findViewById(R.id.cvPaymentCard);
        Toolbar toolbar = findViewById(R.id.toolbarPaymentMethod);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Payment Method");

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_action_arrow_back);

        Bundle getPrice = getIntent().getExtras();
        if (getPrice == null)
        {
            Toast.makeText(Choose_Payment_Method_Screen.this,price,Toast.LENGTH_SHORT).show();
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
