package com.example.muazzam.dissertationapp.Users;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muazzam.dissertationapp.Model.Users;
import com.example.muazzam.dissertationapp.Prevalent.Prevalent;
import com.example.muazzam.dissertationapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateAccount_Screen extends AppCompatActivity {

    private CircleImageView profilepic;
    private Button done,choose;
    private EditText uptName,uptAddress,uptPhoneNo;
    private TextView uptEmail;
    private String name,email,address,phone;
    private String userAuthKey;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private FirebaseUser user;
    private StorageReference storageReference;
    private static final int galleryPic = 123;
    private Uri imageUri;
    private boolean changePic = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account__screen);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        user = firebaseAuth.getCurrentUser();
        userAuthKey = user.getUid();
        storageReference = FirebaseStorage.getInstance().getReference();
        setupUIViews();
        setTextDetails();



        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateTextField())
                {
                    if (changePic)
                    {
                        storeProfilePic();
                    }
                    uploadData();
                    setUpdateToUsers();
                    finish();
                }
            }
        });

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });



    }

    private void setupUIViews()
    {
        Toolbar toolbar = findViewById(R.id.toolbarUpdateAcc);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Update Account");

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_action_arrow_back);

        profilepic = findViewById(R.id.update_Profile_Pic);
        choose = findViewById(R.id.tvChoosePhoto);
        uptName = findViewById(R.id.etUptName);
        uptEmail = findViewById(R.id.etUptEmail);
        uptPhoneNo = findViewById(R.id.etUptPhone);
        uptAddress = findViewById(R.id.etUptAddress);
        done = findViewById(R.id.btnDone);

    }

    private void setTextDetails()
    {
        uptName.setText(Prevalent.onlineUser.getName());
        uptEmail.setText(Prevalent.onlineUser.getEmail());
        uptAddress.setText(Prevalent.onlineUser.getAddress());
        uptPhoneNo.setText(Prevalent.onlineUser.getPhoneNo());
        storageReference.child("Users").child(userAuthKey).child("Images").child("Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri)
            {
                Picasso.get().load(uri).fit().centerCrop().into(profilepic);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                Intent intent = new Intent(UpdateAccount_Screen.this,My_Account_Screen.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean validateTextField()
    {
        boolean result = false;
        name = uptName.getText().toString();
        email = uptEmail.getText().toString();
        address = uptAddress.getText().toString();
        phone = uptPhoneNo.getText().toString();

        if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this,"Please enter Name",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Please enter Email",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(address))
        {
            Toast.makeText(this,"Please enter Address",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this,"Please enter Phone Number",Toast.LENGTH_SHORT).show();
        }
        else
        {
            result = true;
        }
        return result;
    }

    private void uploadData()
    {
        final DatabaseReference databaseReference = firebaseDatabase.getReference();
//        user = firebaseAuth.getCurrentUser();
//        userAuthKey = user.getUid();
//        user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//
//                if (!task.isSuccessful()) {
//                    Toast.makeText(UpdateAccount_Screen.this,"UpdateEmail unsuccessful",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        databaseReference.child("Users").child(userAuthKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    HashMap<String,Object> userDataMap = new HashMap<>();
                    userDataMap.put("Name",name);
                    userDataMap.put("Email",email);
                    userDataMap.put("Address",address);
                    userDataMap.put("PhoneNumber",phone);

                    databaseReference.child("Users").child(userAuthKey).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(UpdateAccount_Screen.this,"Update Successful!",Toast.LENGTH_SHORT).show();
                                        finish();
                                        Intent intent = new Intent(UpdateAccount_Screen.this,My_Account_Screen.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        // Catch allExceptions
                                        Toast.makeText(UpdateAccount_Screen.this,"Network Error! PLease try again later",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(UpdateAccount_Screen.this,"Database Error",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpdateToUsers()
    {
        Users userData = new Users(name,email,address,phone);
        Prevalent.onlineUser = userData;
    }

    private void openGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent,"Select Image"),galleryPic);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if (requestCode == galleryPic && resultCode == RESULT_OK && data.getData() != null)
        {
            imageUri = data.getData();
            profilepic.setImageURI(imageUri);
            changePic = true;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void storeProfilePic()
    {

        final StorageReference filePath = storageReference.child("Users").child(userAuthKey).child("Images").child("Profile Pic");
        final UploadTask uploadTask = filePath.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.getMessage();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

//                Toast.makeText(UpdateAccount_Screen.this,"Upload successful",Toast.LENGTH_SHORT).show();
                //image successfully Uploaded
            }
        });
    }
}
