package com.example.muazzam.dissertationapp.Admin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.muazzam.dissertationapp.Model.AdminAddProduct;
import com.example.muazzam.dissertationapp.Prevalent.Prevalent;
import com.example.muazzam.dissertationapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Admin_Add_Product_Screen extends AppCompatActivity {

    private CircleImageView productPic;
    private EditText prodId,prodName,prodDesc,prodCat;
    private Button insertSup,cancel;
    private static final int galleryPic = 123;
    private  Uri imageUri;
    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabase;
    private String productId, productName,productDesc,productCat;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__add__product__screen);


        firebaseDatabase =FirebaseDatabase.getInstance();

        setupUIViews();
        retrieveProductID();

        productPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        insertSup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateTextFields())
                {

                        storeDetails();
                        Intent intent = new Intent(Admin_Add_Product_Screen.this, Admin_Add_Product_Screen2.class);
                        startActivity(intent);

                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupUIViews()
    {
        productPic = findViewById(R.id.add_Product);
        prodId = findViewById(R.id.etProductId);
        prodName = findViewById(R.id.etProductName);
        prodDesc = findViewById(R.id.etProductDesc);
        prodCat = findViewById(R.id.etProductCat);
        insertSup= findViewById(R.id.btnInsert);
        cancel = findViewById(R.id.btncancel);
        list = new ArrayList<>();
    }

    private void openGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent,"Select Image"),galleryPic);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if (requestCode == galleryPic && resultCode == RESULT_OK && data.getData() != null)
        {
            imageUri = data.getData();
            productPic.setImageURI(imageUri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean validateTextFields()
    {
        boolean result = false;
        productId = prodId.getText().toString();
        productName = prodName.getText().toString();
        productDesc = prodDesc.getText().toString();
        productCat = prodCat.getText().toString();

        if (imageUri== null)
        {
            Toast.makeText(this,"Please select an image",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(productId))
        {
            prodId.setError("Please enter product ID");
        }
        else if (TextUtils.isEmpty(productName))
        {
            prodName.setError("Please enter product Name");
        }
        else if (TextUtils.isEmpty(productDesc))
        {
            prodDesc.setError("Please enter product Description");
        }
        else if (TextUtils.isEmpty(productCat))
        {
            prodCat.setError("Please enter product Category");
        }
        else if (list.contains(productId))
        {
            prodId.setError("Product ID already taken");
        }
        else
        {
            result = true;
        }
        return result;
    }

    private void storeDetails(){
        AdminAddProduct adminAdd = new AdminAddProduct(productId,productName,productDesc,productCat,imageUri);
        Prevalent.products = adminAdd;
    }


    private void retrieveProductID()
    {
        DatabaseReference mDb = firebaseDatabase.getReference();
        mDb.child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String category = ds.getKey();


                    for(DataSnapshot dSnapshot : dataSnapshot.child(category).getChildren()) {
                        String id = dSnapshot.getKey();

                        list.add(id);
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }




}