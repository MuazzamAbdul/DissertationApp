package com.example.muazzam.dissertationapp.Users;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muazzam.dissertationapp.Model.Cart;
import com.example.muazzam.dissertationapp.R;
import com.example.muazzam.dissertationapp.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class Shopping_Cart_Screen extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private String userAuthKey,tot;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private double Total;
    private TextView totAmt,totalFee,deliveryFee;
    private Button delete,checkout;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping__cart__screen);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userAuthKey = user.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Cart").child(userAuthKey).child("Products");

        setupUIVIews();
        retrieveCartProducts();

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder exit = new AlertDialog.Builder(Shopping_Cart_Screen.this,R.style.DialogAlert);
                exit.setMessage("Do you want to delete Cart?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteCart();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = exit.create();
                alert.setTitle("Warning");
                alert.show();
            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(Shopping_Cart_Screen.this,Billing_Info_Screen.class);
                intent.putExtra("Price",totalFee.getText().toString());
                startActivity(intent);

            }
        });

    }

    private void setupUIVIews()
    {

        totAmt = findViewById(R.id.tvTotalPrice);
        delete = findViewById(R.id.btnDeleteAll);
        checkout = findViewById(R.id.btnCheckOut);
        deliveryFee = findViewById(R.id.tvDeliveryFee);
        totalFee = findViewById(R.id.tvTotalFee);
        Toolbar toolbar = findViewById(R.id.toolbarShopping);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Shopping Cart");

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_action_arrow_back);

        recyclerView = findViewById(R.id.rvShoppingCart);
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
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void retrieveCartProducts()
    {
        FirebaseRecyclerOptions<Cart> options  = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(databaseReference,Cart.class).build();

        FirebaseRecyclerAdapter<Cart,CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_layout,viewGroup,false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }

            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {

                holder.nameCart.setText(model.getName());
                holder.priceCart.setText("Price: Rs " + model.getPrice());
                holder.supermarketCart.setText("From: " + model.getSupermarket());
                holder.qtyCart.setText("Quantity: " + model.getQuantity());

                holder.deleteProd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder exit = new AlertDialog.Builder(Shopping_Cart_Screen.this,R.style.DialogAlert);
                        exit.setMessage("Do you want to delete Product?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteProductFromCart(model.getID() +"-" + model.getSupermarket());
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = exit.create();
                        alert.setTitle("Warning");
                        alert.show();

                    }
                });

            }

        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
        calculateAmount();
    }

    private void calculateAmount(){
        Total = 0;
        tot = "0.00";
        final DecimalFormat df = new DecimalFormat(".##");
        final DatabaseReference mDb = firebaseDatabase.getReference();

        mDb.child("Cart").child(userAuthKey).child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                {
                    for (DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        String qty = String.valueOf(ds.child("Quantity").getValue(String.class));
                        String price = String.valueOf(ds.child("Price").getValue(String.class));

                        double amt = Double.parseDouble(qty) * Double.parseDouble(price);


                        Total = Total + amt;

                    }
                     tot = df.format(Total);


                    String fee = deliveryFee.getText().toString();
                    double deliveryFees = Double.parseDouble(fee.substring(fee.indexOf("Rs") + 2,fee.length()));
                    double grandTotal = Total + deliveryFees;
                    totalFee.setText("Rs " + df.format(grandTotal));

                }
                else
                {
                    Toast.makeText(Shopping_Cart_Screen.this,"Cart is empty!",Toast.LENGTH_SHORT).show();
                    deliveryFee.setText("+ Rs 0.00");
                    totalFee.setText("Rs 0.00");
                }
                totAmt.setText("Rs " + tot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(Shopping_Cart_Screen.this,"Failure retrieving products from Cart",Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void deleteCart()
    {
        final DatabaseReference mDb = firebaseDatabase.getReference();

        mDb.child("Cart").child(userAuthKey).child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                {
                    mDb.child("Cart").child(userAuthKey).child("Products").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            calculateAmount();
                            Toast.makeText(Shopping_Cart_Screen.this,"Cart is now empty",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    Toast.makeText(Shopping_Cart_Screen.this,"Cart is empty!",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(Shopping_Cart_Screen.this,"Failure retrieving products from Cart",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteProductFromCart(final String id)
    {
        final DatabaseReference mDb = firebaseDatabase.getReference();

        mDb.child("Cart").child(userAuthKey).child("Products").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                {
                    mDb.child("Cart").child(userAuthKey).child("Products").child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            calculateAmount();
                            Toast.makeText(Shopping_Cart_Screen.this,"Item deleted from cart",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    Toast.makeText(Shopping_Cart_Screen.this,"Cart is empty!",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Shopping_Cart_Screen.this,"Failure retrieving products from Cart",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
