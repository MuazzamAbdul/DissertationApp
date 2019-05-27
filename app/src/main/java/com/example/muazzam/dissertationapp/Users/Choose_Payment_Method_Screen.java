package com.example.muazzam.dissertationapp.Users;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.muazzam.dissertationapp.Prevalent.Prevalent;
import com.example.muazzam.dissertationapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Class to chose payment method.
 */
public class Choose_Payment_Method_Screen extends AppCompatActivity {
    private String price;
    private TextView total;
    private CardView debitCredit,cashOnDelivery;
    private String userAuthKey;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser user;

    /**
     * Create activity containing all modes of payment.
     * Redirect to specific activity when each mode of payment is selected.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose__payment__method__screen);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        user = firebaseAuth.getCurrentUser();
        userAuthKey = user.getUid();

        setupUIViews();
        debitCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(Choose_Payment_Method_Screen.this,Pay_Debit_Screen.class);
                startActivity(intent);
            }
        });

        cashOnDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder exit = new AlertDialog.Builder(Choose_Payment_Method_Screen.this,R.style.DialogAlert);
                exit.setMessage("Pay By Cash??")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveShippingDetails();
                                retrieveProductFromCart();
                                finish();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = exit.create();
                alert.setTitle("Place Order");
                alert.show();
            }
        });

    }

    /**
     * Create UI Views.
     */
    private void setupUIViews()
    {
        total = findViewById(R.id.tvPricePay);
        debitCredit = findViewById(R.id.cvPaymentCard);
        cashOnDelivery = findViewById(R.id.cvCashOnDel);

        Toolbar toolbar = findViewById(R.id.toolbarPaymentMethod);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Payment Method");

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_action_arrow_back);

        total.setText(Prevalent.shippingDetails.getPrice());
    }

    /**
     * Return to previous activity when back arrow is pressed.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                Intent intent = new Intent(Choose_Payment_Method_Screen.this,Billing_Info_Screen.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Return to previous activity when back button on phone is pressed.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        Intent intent = new Intent(Choose_Payment_Method_Screen.this,Billing_Info_Screen.class);
        startActivity(intent);
    }

    /**
     * Save shipping details into Shipping Details database.
     */
    private void saveShippingDetails()
    {
        final String saveCurrentTime,saveCurrentDate;
        Calendar currentDates = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd MM yyyy");
        saveCurrentDate = currentDate.format(currentDates.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(currentDates.getTime());

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("ShippingDetails").child(userAuthKey).child(saveCurrentDate + " " + saveCurrentTime).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                HashMap<String,Object> userDataMap = new HashMap<>();

                userDataMap.put("Name",Prevalent.shippingDetails.getFname() + " " + Prevalent.shippingDetails.getLname());
                userDataMap.put("Address",Prevalent.shippingDetails.getAddress()+ ", " + Prevalent.shippingDetails.getCity());

                databaseReference.child("ShippingDetails").child(userAuthKey).child(saveCurrentDate + " " + saveCurrentTime).updateChildren(userDataMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (!task.isSuccessful())
                                {
                                    Toast.makeText(Choose_Payment_Method_Screen.this,"Error saving shipping details",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Retrieve product details for products found in Cart.
     */
    private void retrieveProductFromCart()
    {
        DatabaseReference mDb = firebaseDatabase.getReference();

        mDb.child("Cart").child(userAuthKey).child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String key = ds.getKey();

                        String date = String.valueOf(ds.child("Date").getValue(String.class));
                        String id = String.valueOf(ds.child("ID").getValue(String.class));
                        String name = String.valueOf(ds.child("Name").getValue(String.class));
                        String price = String.valueOf(ds.child("Price").getValue(String.class));
                        String qty = String.valueOf(ds.child("Quantity").getValue(String.class));
                        String supermarket = String.valueOf(ds.child("Supermarket").getValue(String.class));
                        String time = String.valueOf(ds.child("Time").getValue(String.class));

                        saveOrders(key,date,id,name,price,qty,supermarket,time);
                        decreaseQuantitySupermarket(key,qty);
                    }
                }
                else
                {
                    Toast.makeText(Choose_Payment_Method_Screen.this,"Cart does not exist!",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     *
     * @param key
     * @param date
     * @param id
     * @param name
     * @param price
     * @param qty
     * @param supermarket
     * @param time
     *
     * Save products found in Cart database into Order database when payment is confirmed.
     */
    private void saveOrders(final String key, final String date, final String id, final String name, final String price, final String qty, final String supermarket, final String time)
    {
        final String saveCurrentTime,saveCurrentDate;
        Calendar currentDates = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd MM yyyy");
        saveCurrentDate = currentDate.format(currentDates.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(currentDates.getTime());

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Orders").child(userAuthKey).child(saveCurrentDate + " " + saveCurrentTime).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists())
                {
                    HashMap<String,Object> userDataMap = new HashMap<>();

                    userDataMap.put("Status","Ongoing");
                    userDataMap.put("Total","Rs " + Prevalent.shippingDetails.getPrice());

                    databaseReference.child("Orders").child(userAuthKey).child(saveCurrentDate + " " + saveCurrentTime)
                            .updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (!task.isSuccessful())
                                    {
                                        Toast.makeText(Choose_Payment_Method_Screen.this,"Error saving orders in database!",Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(Choose_Payment_Method_Screen.this,"Order Already Exists!",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child("Orders").child(userAuthKey).child(saveCurrentDate + " " + saveCurrentTime).child("Products").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists())
                {

                    HashMap<String,Object> userDataMap = new HashMap<>();

                    userDataMap.put("ID",id);
                    userDataMap.put("Name",name);
                    userDataMap.put("Date", date);
                    userDataMap.put("Time",time);
                    userDataMap.put("Supermarket",supermarket);
                    userDataMap.put("Quantity",qty);
                    userDataMap.put("Price",price);

                    databaseReference.child("Orders").child(userAuthKey).child(saveCurrentDate + " " + saveCurrentTime).child("Products").child(key)
                            .updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(Choose_Payment_Method_Screen.this,"Order Placed! Thank you for your purchase.",Toast.LENGTH_SHORT).show();
                                        deleteCart();
                                    }

                                }
                            });

                }
                else
                {
                    Toast.makeText(Choose_Payment_Method_Screen.this,"Order Already Exists!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Delete cart once payment has been confirmed.
     */
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

//                            Toast.makeText(Choose_Payment_Method_Screen.this,"Cart deleted!",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
//                    Toast.makeText(Choose_Payment_Method_Screen.this,"Cart does not exist!",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(Choose_Payment_Method_Screen.this,"Failure retrieving products from Cart",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * @param key
     * @param qty
     *
     * Decrease quantity of product in supermarket.
     */
    private void decreaseQuantitySupermarket(final String key, final String qty)
    {
        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

        databaseRef.child("Supermarkets_Products").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String oldQty = String.valueOf(dataSnapshot.child("Quantity").getValue(String.class));

                int oldQuantity = Integer.parseInt(oldQty);
                int quantityOrdered = Integer.parseInt(qty);
                int newQuantity = oldQuantity - quantityOrdered;
                String newQty = String.valueOf(newQuantity);

                HashMap<String,Object> userDataMap = new HashMap<>();
                userDataMap.put("Quantity",newQty);

                databaseRef.child("Supermarkets_Products").child(key).updateChildren(userDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
//                            Toast.makeText(Choose_Payment_Method_Screen.this,"Quantity Decremented",Toast.LENGTH_SHORT).show();
                        }
                        else if (!task.isSuccessful())
                        {
                            Toast.makeText(Choose_Payment_Method_Screen.this,"Error in decreasing quantity in supermarket database",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Choose_Payment_Method_Screen.this,"Failure decrementing products from Supermarkets",Toast.LENGTH_SHORT).show();
            }
        });


    }

}
