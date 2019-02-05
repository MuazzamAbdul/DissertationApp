package com.example.muazzam.dissertationapp.Admin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.muazzam.dissertationapp.R;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class Admin_Add_Product_Screen extends AppCompatActivity {

    private CircleImageView productPic;
    private Spinner spinner;
    private EditText prodId,prodName,prodDesc,prodCat,qty,price;
    private Button insertSup,cancel;
    private static final int galleryPic = 123;
    private  Uri imageUri;
    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabase;
    private StorageReference storageReference;
    private String productId, productName,productDesc,productCat,quantity,prices;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__add__product__screen);


        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();

        setupUIViews();

        list = new ArrayList<String>();
        list.add("Choose Supermarket");
//        getSupermarkets();
        list.add("Intermart");
        list.add("SuperU");
        list.add("Winners");
        list.add("Jumbo");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.my_spinner,list);
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
//                    Toast.makeText(Admin_Add_Product_Screen.this,"Okay",Toast.LENGTH_SHORT).show();
                    storeProductPic();
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
        qty = findViewById(R.id.etProductQty);
        price = findViewById(R.id.etProductPrice);
        insertSup= findViewById(R.id.btnInsert);
        cancel = findViewById(R.id.btncancel);
        spinner = findViewById(R.id.spinner);
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
        quantity = qty.getText().toString();
        prices = price.getText().toString();

        if (imageUri== null)
        {
            Toast.makeText(this,"Please select an image",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(productId))
        {
            Toast.makeText(this,"Please enter product ID",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(productName))
        {
            Toast.makeText(this,"Please enter product Name",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(productDesc))
        {
            Toast.makeText(this,"Please enter product Description",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(productCat))
        {
            Toast.makeText(this,"Please enter product Category",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(quantity))
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

        final StorageReference filePath = storageReference.child("Products").child(productId).child("Images").child("Product Pic");
        final UploadTask uploadTask = filePath.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Admin_Add_Product_Screen.this,"Error in storing product pic!",Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(Admin_Add_Product_Screen.this,"Upload successful",Toast.LENGTH_SHORT).show();
                //image successfully Uploaded
            }
        });
    }

    private void getSupermarkets()
    {
        DatabaseReference mDb = firebaseDatabase.getReference();
//        FirebaseUser user = firebaseAuth.getCurrentUser();
//        String userAuthKey = user.getUid();
//        StorageReference storageReference = firebaseStorage.getReference();
//        storageReference.child("Users").child(userAuthKey).child("Images").child("Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//
//                Picasso.get().load(uri).fit().centerCrop().into(imagePic);
//            }
//        });

        mDb.child("Supermarkets").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


//                String userEmail = String.valueOf(dataSnapshot.child("Email").getValue());
//                String userName = String.valueOf(dataSnapshot.child("Name").getValue());
//                String userPhone = String.valueOf(dataSnapshot.child("PhoneNumber").getValue());
//                String userAddress = String.valueOf(dataSnapshot.child("Address").getValue());
//
//                Users userData = new Users(userName,userEmail,userAddress,userPhone);
//                Prevalent.onlineUser = userData;
//                usernameText.setText(Prevalent.onlineUser.getName());
//                usernameEmail.setText(Prevalent.onlineUser.getEmail());

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String name = ds.child("Supermarket Name").getValue(String.class);
//                    Toast.makeText(Admin_Add_Product_Screen.this,name,Toast.LENGTH_SHORT).show();
                    list.add(name);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Admin_Add_Product_Screen.this,"Failure Retrieving data from Database",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
