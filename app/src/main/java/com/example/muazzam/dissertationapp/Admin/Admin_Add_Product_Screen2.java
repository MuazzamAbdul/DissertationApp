package com.example.muazzam.dissertationapp.Admin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muazzam.dissertationapp.Prevalent.Prevalent;
import com.example.muazzam.dissertationapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Admin_Add_Product_Screen2 extends AppCompatActivity {

    private TextView prodId,prodName;
    private Spinner spinner;
    private EditText qty,price;
    private Button add2,cancel2;
    private List<String> list;
    private StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase;
    private String quantity,prices;
    private ProgressDialog loadingBar,loadingBar2;
//    private ArrayList<String> supName,supId;
    private int supermarketPos;
    private CircleImageView prodPic;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__add__product__screen2);

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();

        setupUIViews();
        setProductDetails();


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.my_spinner,Prevalent.adminSupermarkets.getSupName());
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter.notifyDataSetChanged();
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner.setSelection(position);
                supermarketPos = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        add2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateTextFields())
                {
                        storeProductPic();
                        storeProductDetails();
                        storeProductIntoSupermarkets();

                }
            }
        });

        cancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder exit = new AlertDialog.Builder(Admin_Add_Product_Screen2.this);
                exit.setMessage("Do you want to add another product?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                Intent intent = new Intent(Admin_Add_Product_Screen2.this,Admin_Add_Product_Screen.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();

                            }
                        });
                AlertDialog alert = exit.create();
                alert.setTitle("Warning");
                alert.show();
            }
        });
    }

    private void setupUIViews()
    {

        Toolbar toolbar = findViewById(R.id.toolbarAddProduct2);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Add Product");

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_action_black_arrow_back);

        prodId = findViewById(R.id.tvProductId);
        prodName = findViewById(R.id.tvProductName);
        qty = findViewById(R.id.etProductQty);
        price = findViewById(R.id.etProductPrice);
        add2= findViewById(R.id.btnAddProduct);
        cancel2 = findViewById(R.id.btncancel2);
        spinner = findViewById(R.id.spinner);
        loadingBar = new ProgressDialog(this);
        loadingBar2 = new ProgressDialog(this);
        prodPic = findViewById(R.id.productPic);
        Picasso.get().load(Prevalent.products.getImageUri()).fit().centerCrop().into(prodPic);
        //        Glide.with(My_Account_Screen.this).load(uri).into(imagePic);

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

    private void setProductDetails()
    {
        prodId.setText(Prevalent.products.getId());
        prodName.setText(Prevalent.products.getName());
    }

    private boolean validateTextFields()
    {
        boolean result = false;
        quantity = qty.getText().toString();
        prices = price.getText().toString();

        if (TextUtils.isEmpty(quantity))
        {
            qty.setError("Please enter Quantity");
        }
        else if (TextUtils.isEmpty(prices))
        {
            price.setError("Please enter Price");
        }
        else if (supermarketPos == 0)
        {
            Toast.makeText(this,"Please select Supermarket",Toast.LENGTH_SHORT).show();
        }
        else
        {
            result = true;
        }
        return result;
    }

    private void storeProductPic()
    {
        loadingBar.setTitle("Adding Product");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        final StorageReference filePath = storageReference.child("Products").child(Prevalent.products.getId()).child("Images").child("Product Pic");
        final UploadTask uploadTask = filePath.putFile(Prevalent.products.getImageUri());

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Admin_Add_Product_Screen2.this,"Error in storing product pic!",Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Admin_Add_Product_Screen2.this,"Upload successful",Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
//                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
//                if(urlTask.isSuccessful())
//                {
//                    Uri downloadUrl = urlTask.getResult();
//
//                    Toast.makeText(Admin_Add_Product_Screen2.this,downloadUrl.toString(),Toast.LENGTH_SHORT).show();
//                }

                //image successfully Uploaded
            }
        });
    }

    private void storeProductDetails()
    {
        final DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.child("Products").child(Prevalent.products.getCategory()).child(Prevalent.products.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                HashMap<String,Object> userDataMap = new HashMap<>();
                userDataMap.put("ID",Prevalent.products.getId());
                userDataMap.put("Name",Prevalent.products.getName());
                userDataMap.put("Description",Prevalent.products.getDesc());
                userDataMap.put("Category",Prevalent.products.getCategory());

                databaseReference.child("Products").child(Prevalent.products.getCategory()).child(Prevalent.products.getId()).updateChildren(userDataMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful())
                                {
                                    loadingBar.dismiss();
                                    Toast.makeText(Admin_Add_Product_Screen2.this,"Update Successful!",Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    // Catch allExceptions
                                    Toast.makeText(Admin_Add_Product_Screen2.this,"Network Error! PLease try again later",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(Admin_Add_Product_Screen2.this,"Database Error",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void storeProductIntoSupermarkets()
    {
        final String key = Prevalent.adminSupermarkets.getSupId().get(supermarketPos-1)+"-"+Prevalent.products.getId();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.child("Supermarkets_Products").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                HashMap<String,Object> userDataMap = new HashMap<>();
                userDataMap.put("Quantity",quantity);
                userDataMap.put("Price",prices);

                databaseReference.child("Supermarkets_Products").child(key).updateChildren(userDataMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful())
                                {
                                    loadingBar.dismiss();
                                    Toast.makeText(Admin_Add_Product_Screen2.this,"Update Successful!",Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    // Catch allExceptions
                                    Toast.makeText(Admin_Add_Product_Screen2.this,"Network Error! PLease try again later",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(Admin_Add_Product_Screen2.this,"Database Error",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
