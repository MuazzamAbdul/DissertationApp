package com.example.muazzam.dissertationapp;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.muazzam.dissertationapp.Model.Cart;
import com.example.muazzam.dissertationapp.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Admin_Completed_Products_Screen extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private String userId,date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed__products__screen);





        Bundle getUserID = getIntent().getExtras();
        if (getUserID == null)
        {
            return;
        }
        else
        {
            userId = getUserID.getString("UserID");
            date = getUserID.getString("DateTime");
        }

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Orders").child(userId).child(date).child("Products");
        setupUIViews();
        retrieveCartProducts();
    }

    private void setupUIViews()
    {
        Toolbar toolbar = findViewById(R.id.toolbarCompletedProduct);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Products");

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_action_black_arrow_back);


        recyclerView = findViewById(R.id.rvCompletedProd);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void retrieveCartProducts()
    {
        FirebaseRecyclerOptions<Cart> options  = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(databaseReference,Cart.class).build();

        FirebaseRecyclerAdapter<Cart,CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.admin_cart_layout,viewGroup,false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }

            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {

                holder.nameCart.setText(model.getName());
                holder.priceCart.setText("Price: Rs " + model.getPrice());
                holder.supermarketCart.setText("From: " + model.getSupermarket());
                holder.qtyCart.setText("Quantity: " + model.getQuantity());
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
