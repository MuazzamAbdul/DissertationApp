package com.example.muazzam.dissertationapp.Users;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.muazzam.dissertationapp.Prevalent.Prevalent;
import com.example.muazzam.dissertationapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Add_To_Cart_Screen extends AppCompatActivity {

    private TextView superName,prodName,prodDesc,price;
    private ImageView prodPic,close;
    private ElegantNumberButton qty;
    private Button ATC,orderSuper;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabase;
    private String userAuthKey;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__to__cart__screen);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();

        setupUIViews();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(Add_To_Cart_Screen.this,Product_Supermarket_Screen.class);
                startActivity(intent);
            }
        });

        ATC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder exit = new AlertDialog.Builder(Add_To_Cart_Screen.this,R.style.DialogAlert);
                exit.setMessage("Add product to Shopping Cart?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                addToCart();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = exit.create();
                alert.setTitle("Purchase Product");
                alert.show();


            }
        });

        orderSuper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder exit = new AlertDialog.Builder(Add_To_Cart_Screen.this,R.style.DialogAlert);
                exit.setMessage("Order Product from supermarket?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveOrder();
                                AlertDialog.Builder exit = new AlertDialog.Builder(Add_To_Cart_Screen.this,R.style.DialogAlert);
                                exit.setMessage("Get Direction to supermarket?")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(Add_To_Cart_Screen.this,GetDirection_Screen.class);
                                                intent.putExtra("Name",Prevalent.supermarketProductPrice.getName());
                                                intent.putExtra("ID",Prevalent.supermarketProductPrice.getSuperid().substring(0,Prevalent.supermarketProductPrice.getSuperid().indexOf("-")));
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                                finish();
                                            }
                                        });
                                AlertDialog alert = exit.create();
                                alert.setTitle("Get Direction");
                                alert.show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = exit.create();
                alert.setTitle("Purchase Product");
                alert.show();
            }
        });
    }

    private void setupUIViews()
    {
        superName = findViewById(R.id.tvSupName);
        prodName = findViewById(R.id.tvPnameATC);
        prodDesc = findViewById(R.id.tvPDescATC);
        prodPic = findViewById(R.id.ivProductATC);
        close = findViewById(R.id.ivCloseATC);
        price = findViewById(R.id.tvPriceATC);
        qty = findViewById(R.id.btnQty);
        ATC = findViewById(R.id.btnATC);
        orderSuper = findViewById(R.id.btnOrder);

        prodName.setText(Prevalent.displayProducts.getName());
        prodDesc.setText(Prevalent.displayProducts.getDescription());
        superName.setText(Prevalent.supermarketProductPrice.getName());
        price.setText("Price: Rs " + Prevalent.supermarketProductPrice.getPrice());

        storageReference.child("Products").child(Prevalent.displayProducts.getID()).child("Images").child("Product Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(prodPic);
//                imagePic.setImageURI(uri);
//                Glide.with(Product_Supermarket_Screen.this).load(uri).into(productPic);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(Add_To_Cart_Screen.this,Product_Supermarket_Screen.class);
        startActivity(intent);
    }

    private void addToCart()
    {
        final String saveCurrentTime,saveCurrentDate;
        Calendar date = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd MM yyyy");
        saveCurrentDate = currentDate.format(date.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(date.getTime());

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();
        userAuthKey = user.getUid();


        databaseReference.child("Cart").child(userAuthKey).child("Products").child(Prevalent.supermarketProductPrice.getSuperid())
                .addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                HashMap<String,Object> userDataMap = new HashMap<>();

                userDataMap.put("ID",Prevalent.displayProducts.getID());
                userDataMap.put("Name",prodName.getText().toString());
                userDataMap.put("Date", saveCurrentDate);
                userDataMap.put("Time",saveCurrentTime);
                userDataMap.put("Supermarket",superName.getText().toString());
                userDataMap.put("SupermarketID",Prevalent.supermarketProductPrice.getSuperid().substring(0,Prevalent.supermarketProductPrice.getSuperid().indexOf("-")));
                userDataMap.put("Quantity",qty.getNumber());
                userDataMap.put("Price",Prevalent.supermarketProductPrice.getPrice());

                databaseReference.child("Cart").child(userAuthKey).child("Products").child(Prevalent.supermarketProductPrice.getSuperid())
                        .updateChildren(userDataMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful())
                        {
                            Toast.makeText(Add_To_Cart_Screen.this,"Added to Shopping Cart",Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(Add_To_Cart_Screen.this,"Database Error",Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void saveOrder()
    {
        final String saveCurrentTime,saveCurrentDate;
        Calendar currentDates = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd MM yyyy");
        saveCurrentDate = currentDate.format(currentDates.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(currentDates.getTime());

        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();
        userAuthKey = user.getUid();

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Orders").child(userAuthKey).child(saveCurrentDate + " " + saveCurrentTime).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists())
                {

                    HashMap<String,Object> userDataMap = new HashMap<>();

                    userDataMap.put("Status","Ongoing");
                    userDataMap.put("Total","Rs " + Prevalent.supermarketProductPrice.getPrice());


                    databaseReference.child("Orders").child(userAuthKey).child(saveCurrentDate + " " + saveCurrentTime)
                            .updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(Add_To_Cart_Screen.this,"Status!",Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });

                }
                else
                {
                    Toast.makeText(Add_To_Cart_Screen.this,"Order Already Exists!",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        db.child("Orders").child(userAuthKey).child(saveCurrentDate + " " + saveCurrentTime).child("Products").child(Prevalent.supermarketProductPrice.getSuperid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists())
                {

                    HashMap<String,Object> userDataMap = new HashMap<>();

                    userDataMap.put("ID",Prevalent.displayProducts.getID());
                    userDataMap.put("Name",prodName.getText().toString());
                    userDataMap.put("Date", saveCurrentDate);
                    userDataMap.put("Time",saveCurrentTime);
                    userDataMap.put("Supermarket",superName.getText().toString());
                    userDataMap.put("Quantity",qty.getNumber());
                    userDataMap.put("Price",Prevalent.supermarketProductPrice.getPrice());
//                    userDataMap.put("Status","Not Completed");

                    db.child("Orders").child(userAuthKey).child(saveCurrentDate + " " + saveCurrentTime).child("Products").child(Prevalent.supermarketProductPrice.getSuperid())
                            .updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(Add_To_Cart_Screen.this,"Order Placed! Please collect product from Supermarket",Toast.LENGTH_SHORT).show();
                                        decreaseQuantitySupermarket(Prevalent.supermarketProductPrice.getSuperid(),qty.getNumber());
                                    }

                                }
                            });

                }
                else
                {
                    Toast.makeText(Add_To_Cart_Screen.this,"Order Already Exists!",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void decreaseQuantitySupermarket(final String key, final String quantity)
    {
        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

        databaseRef.child("Supermarkets_Products").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String oldQty = String.valueOf(dataSnapshot.child("Quantity").getValue(String.class));

                int oldQuantity = Integer.parseInt(oldQty);
                int quantityOrdered = Integer.parseInt(quantity);
                int newQuantity = oldQuantity - quantityOrdered;

                String newQty = String.valueOf(newQuantity);


                HashMap<String,Object> userDataMap = new HashMap<>();
                userDataMap.put("Quantity",newQty);

                databaseRef.child("Supermarkets_Products").child(key).updateChildren(userDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(Add_To_Cart_Screen.this,"Quantity Decremented",Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Add_To_Cart_Screen.this,"Failure decrementing products from Supermarkets",Toast.LENGTH_SHORT).show();
            }
        });


    }

}
