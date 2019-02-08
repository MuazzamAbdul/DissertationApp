package com.example.muazzam.dissertationapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muazzam.dissertationapp.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

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
    private ArrayList<String> supName,supId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__add__product__screen2);

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();

        setupUIViews();

        loadingBar2.setTitle("Retrieveing Supermarkets");
        loadingBar2.setMessage("Please wait...");
        loadingBar2.setCanceledOnTouchOutside(false);
        loadingBar2.show();
        setProductDetails();
//        getSupermarkets();
//
//        list = new ArrayList<String>();
//        list.add("Choose Supermarket");
////        getSupermarkets();
//        list.add("Intermart");
//        list.add("SuperU");
//        list.add("Winners");
//        list.add("Jumbo");

        supName = new ArrayList<>();
        supName.add("Choose Supermarket");


        DatabaseReference mDb = firebaseDatabase.getReference();
        mDb.child("Supermarkets").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String location = ds.getKey();

                    Log.d("TAG", location);

                    for(DataSnapshot dSnapshot : dataSnapshot.child(location).getChildren()) {
                        String id = dSnapshot.getKey();
                        supId.add(id);

                        String name = String.valueOf(dSnapshot.child("Supermarket Name").getValue(String.class));
                        supName.add(name);
                    }
                }
                loadingBar2.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Admin_Add_Product_Screen2.this,"Failure Retrieving data from Database",Toast.LENGTH_SHORT).show();
            }
        });


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.my_spinner,supName);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter.notifyDataSetChanged();
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner.setSelection(position);
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

                }
            }
        });

        cancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupUIViews()
    {

        prodId = findViewById(R.id.tvProductId);
        prodName = findViewById(R.id.tvProductName);
        qty = findViewById(R.id.etProductQty);
        price = findViewById(R.id.etProductPrice);
        add2= findViewById(R.id.btnAddProduct);
        cancel2 = findViewById(R.id.btncancel2);
        spinner = findViewById(R.id.spinner);
        loadingBar = new ProgressDialog(this);
        loadingBar2 = new ProgressDialog(this);



        supId = new ArrayList<>();

    }


    private void setProductDetails()
    {
        prodId.setText(Prevalent.products.getId());
        prodName.setText(Prevalent.products.getName());
    }

    private boolean validateTextFields()
    {
        loadingBar.setTitle("Adding Product");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        boolean result = false;
        quantity = qty.getText().toString();
        prices = price.getText().toString();

        if (TextUtils.isEmpty(quantity))
        {
            Toast.makeText(this,"Please enter Quantity",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(prices))
        {
            Toast.makeText(this,"Please enter Price",Toast.LENGTH_SHORT).show();
        }
        else
        {
            result = true;
        }
        return result;
    }


    private void storeProductPic()
    {

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
                loadingBar.dismiss();
                Toast.makeText(Admin_Add_Product_Screen2.this,"Upload successful",Toast.LENGTH_SHORT).show();
                //image successfully Uploaded
            }
        });
    }

    private void getSupermarkets()
    {
        DatabaseReference mDb = firebaseDatabase.getReference();
        mDb.child("Supermarkets").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                supName.add("Choose Supermarket");

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String location = ds.getKey();

                    Log.d("TAG", location);

                    for(DataSnapshot dSnapshot : dataSnapshot.child(location).getChildren()) {
                        String id = dSnapshot.getKey();
                        supId.add(id);

                        String name = String.valueOf(dSnapshot.child("Supermarket Name").getValue(String.class));
                        supName.add(name);
                        Toast.makeText(Admin_Add_Product_Screen2.this,name,Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Admin_Add_Product_Screen2.this,"Failure Retrieving data from Database",Toast.LENGTH_SHORT).show();
            }
        });

//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.my_spinner,supName);
//        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        arrayAdapter.notifyDataSetChanged();
//        spinner.setAdapter(arrayAdapter);
    }
}
