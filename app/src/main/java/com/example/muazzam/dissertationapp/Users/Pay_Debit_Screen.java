package com.example.muazzam.dissertationapp.Users;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muazzam.dissertationapp.Model.Wallet;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class Pay_Debit_Screen extends AppCompatActivity {

    private TextView amount;
    private EditText etCardName,etCardNo,etCardExp,etCardSec;
    private String cname,cno,cexp,csec;
    private Button pay;
    private String userAuthKey;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser user;
    private ArrayList<String> card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay__debit__screen);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        user = firebaseAuth.getCurrentUser();
        userAuthKey = user.getUid();

        setupUIViews();

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateTextFields())
                {

                    AlertDialog.Builder exit = new AlertDialog.Builder(Pay_Debit_Screen.this,R.style.DialogAlert);
                    exit.setMessage("Confirm Payment?")
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
            }
        });
    }

    private void setupUIViews()
    {
        Toolbar toolbar = findViewById(R.id.toolbarPaymentDebitCard);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Payment");

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_action_arrow_back);

        etCardName = findViewById(R.id.etCardHolder);
        etCardNo = findViewById(R.id.etCardNumber);
        etCardExp = findViewById(R.id.etExpiryDate);
        etCardSec = findViewById(R.id.etSecurityCode);
        amount = findViewById(R.id.tvAmount);
        pay = findViewById(R.id.btnPay);

        card = new ArrayList<>();
        amount.setText(Prevalent.shippingDetails.getPrice());

        card.add("12356789");
    }

    private boolean validateTextFields()
    {
        boolean result = false;
        cname = etCardName.getText().toString();
        cno = etCardNo.getText().toString();
        cexp = etCardExp.getText().toString();
        csec = etCardSec.getText().toString();


        if (TextUtils.isEmpty(cname))
        {
            etCardName.setError("Please enter CardHolder Name");
        }
        else if(TextUtils.isEmpty(cno))
        {
            etCardNo.setError("Please enter Card Number");
        }
        else if (!card.contains(cno))
        {
            Toast.makeText(Pay_Debit_Screen.this,"Card not available",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(cexp))
        {
            etCardExp.setError("Please enter Expiry Date");
        }
        else if (TextUtils.isEmpty(csec))
        {
            etCardSec.setError("Please enter Security Code");
        }
        else if (csec.length() < 4)
        {
            Toast.makeText(Pay_Debit_Screen.this,"Security code not up to standards",Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(Pay_Debit_Screen.this,Choose_Payment_Method_Screen.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(Pay_Debit_Screen.this,Choose_Payment_Method_Screen.class);
        startActivity(intent);
    }

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

                                if (task.isSuccessful())
                                {
                                    Toast.makeText(Pay_Debit_Screen.this,"Shipping Details saved!",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

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
                    Toast.makeText(Pay_Debit_Screen.this,"Cart does not exist!",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

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
                    userDataMap.put("Total",Prevalent.shippingDetails.getPrice());


                    databaseReference.child("Orders").child(userAuthKey).child(saveCurrentDate + " " + saveCurrentTime)
                            .updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(Pay_Debit_Screen.this,"Status!",Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });

                }
                else
                {
                    Toast.makeText(Pay_Debit_Screen.this,"Order Already Exists!",Toast.LENGTH_SHORT).show();
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
//                    userDataMap.put("Status","Not Completed");

                    databaseReference.child("Orders").child(userAuthKey).child(saveCurrentDate + " " + saveCurrentTime).child("Products").child(key)
                            .updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful())
                            {
                                Toast.makeText(Pay_Debit_Screen.this,"Order Placed!",Toast.LENGTH_SHORT).show();
                                deleteCart();
                            }

                        }
                    });

                }
                else
                {
                    Toast.makeText(Pay_Debit_Screen.this,"Order Already Exists!",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

                            Toast.makeText(Pay_Debit_Screen.this,"Cart deleted!",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    Toast.makeText(Pay_Debit_Screen.this,"Cart does not exist!",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(Pay_Debit_Screen.this,"Failure retrieving products from Cart",Toast.LENGTH_SHORT).show();
            }
        });
    }

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
                            Toast.makeText(Pay_Debit_Screen.this,"Quantity Decremented",Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Pay_Debit_Screen.this,"Failure decrementing products from Supermarkets",Toast.LENGTH_SHORT).show();
            }
        });


    }
}
