package com.example.muazzam.dissertationapp.Admin;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class Admin_Edit_Product2_Screen extends AppCompatActivity {
    private String name,id,category,desc;
    private CircleImageView prodPic;
    private TextView prodID;
    private EditText prodName,prodDesc;
    private static final int galleryPic = 123;
    private Uri imageUri;
    private Button done;
    private StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase;
    private String  productName,productDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__edit__product2__screen);

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
        Bundle getID = getIntent().getExtras();
        if (getID == null)
        {
            return;
        }
        else
        {
            id = getID.getString("ID");
            name = getID.getString("Name");
            category = getID.getString("Category");
            desc = getID.getString("Desc");
        }

        setupUIViews();


        prodPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateTextFields())
                {
                    storeProductPic();
                    storeProductDetails();
                }
            }
        });

    }

    private void setupUIViews()
    {
        Toolbar toolbar = findViewById(R.id.toolbarProducts2);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Edit Product");

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_action_black_arrow_back);

        prodPic = findViewById(R.id.add_Product2);
        prodID = findViewById(R.id.tvProdID);
        prodName = findViewById(R.id.etEProductName);
        prodDesc = findViewById(R.id.etEProductDesc);
        done = findViewById(R.id.btnProdDone);

        storageReference.child("Products").child(id).child("Images").child("Product Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri)
            {
                imageUri = uri;
                Picasso.get().load(uri).fit().centerCrop().into(prodPic);
            }
        });

        prodID.setText(id);
        prodName.setText(name);
        prodDesc.setText(desc);

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
            prodPic.setImageURI(imageUri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean validateTextFields()
    {
        boolean result = false;
        productName = prodName.getText().toString();
        productDesc = prodDesc.getText().toString();


        if (imageUri== null)
        {
            Toast.makeText(this,"Please select an image",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(productName))
        {
            prodName.setError("Please enter product Name");
        }
        else if (TextUtils.isEmpty(productDesc))
        {
            prodDesc.setError("Please enter product Description");
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
                Intent intent = new Intent(Admin_Edit_Product2_Screen.this,Admin_Edit_Products_Screen.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(Admin_Edit_Product2_Screen.this,Admin_Edit_Products_Screen.class);
        startActivity(intent);
    }

    private void storeProductPic()
    {

        final StorageReference filePath = storageReference.child("Products").child(id).child("Images").child("Product Pic");
        final UploadTask uploadTask = filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(Admin_Edit_Product2_Screen.this,"Error in storing product pic!",Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Admin_Edit_Product2_Screen.this,"Upload successful",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void storeProductDetails()
    {
        final DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.child("Products").child(category).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                HashMap<String,Object> userDataMap = new HashMap<>();

                userDataMap.put("Name",productName);
                userDataMap.put("Description",productDesc);


                databaseReference.child("Products").child(category).child(id).updateChildren(userDataMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful())
                                {
                                    finish();
                                    Toast.makeText(Admin_Edit_Product2_Screen.this,"Product Updated!",Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    // Catch allExceptions
                                    Toast.makeText(Admin_Edit_Product2_Screen.this,"Network Error! PLease try again later",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(Admin_Edit_Product2_Screen.this,"Database Error",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
