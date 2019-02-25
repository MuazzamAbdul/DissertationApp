package com.example.muazzam.dissertationapp.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.muazzam.dissertationapp.R;
import com.example.muazzam.dissertationapp.Users.Category_Screen;

import de.hdodenhof.circleimageview.CircleImageView;

public class Admin_Delete_Product_Screen extends AppCompatActivity {
    private CircleImageView fruitVeg,dairy,rice,frozenFood,snacks,grains,cannedFood,oil,beverages,personalCare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__delete__product__screen);
        setupUIViews();

         fruitVeg = findViewById(R.id.adFruit_Veg);
         dairy = findViewById(R.id.adDairy);
         rice = findViewById(R.id.adRice);
         frozenFood = findViewById(R.id.adFrozen_Food);
         snacks = findViewById(R.id.adSnacks);
         grains = findViewById(R.id.adGrains);
         cannedFood = findViewById(R.id.adCanned_Food);
         oil = findViewById(R.id.adOil);
         beverages = findViewById(R.id.adBeverages);
         personalCare = findViewById(R.id.adPersonal_Care);

        fruitVeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_Delete_Product_Screen.this,Admin_Delete_Product2_Screen.class);
                intent.putExtra("Category","FruitVeg");
                startActivity(intent);
            }
        });

        dairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_Delete_Product_Screen.this,Admin_Delete_Product2_Screen.class);
                intent.putExtra("Category","Dairy");
                startActivity(intent);
            }
        });

        rice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_Delete_Product_Screen.this,Admin_Delete_Product2_Screen.class);
                intent.putExtra("Category","Rice");
                startActivity(intent);
            }
        });

        frozenFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_Delete_Product_Screen.this,Admin_Delete_Product2_Screen.class);
                intent.putExtra("Category","Frozen");
                startActivity(intent);
            }
        });

        snacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_Delete_Product_Screen.this,Admin_Delete_Product2_Screen.class);
                intent.putExtra("Category","Snacks");
                startActivity(intent);
            }
        });

        grains.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_Delete_Product_Screen.this,Admin_Delete_Product2_Screen.class);
                intent.putExtra("Category","Grains");
                startActivity(intent);
            }
        });

        cannedFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_Delete_Product_Screen.this,Admin_Delete_Product2_Screen.class);
                intent.putExtra("Category","FruitVeg");
                startActivity(intent);
            }
        });

        oil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_Delete_Product_Screen.this,Admin_Delete_Product2_Screen.class);
                intent.putExtra("Category","Oil");
                startActivity(intent);
            }
        });

        beverages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_Delete_Product_Screen.this,Admin_Delete_Product2_Screen.class);
                intent.putExtra("Category","Beverages");
                startActivity(intent);
            }
        });

        personalCare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_Delete_Product_Screen.this,Admin_Delete_Product2_Screen.class);
                intent.putExtra("Category","Personal Care");
                startActivity(intent);
            }
        });
    }

    private void setupUIViews()
    {
        Toolbar toolbar = findViewById(R.id.toolbarDeleteProduct);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Delete Product");

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_action_black_arrow_back);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
//                Intent intent = new Intent(Admin_Registered_Users.this,Home_Screen.class);
//                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
