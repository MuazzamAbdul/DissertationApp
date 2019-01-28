package com.example.muazzam.dissertationapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.muazzam.dissertationapp.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class My_Account_Screen extends AppCompatActivity {

    private TextView LogoName,name,email,address,phoneNo;
    private Button update,changePass;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CircleImageView imagePic;
    private FirebaseStorage firebaseStorage;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private String userAuthKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__account__screen);
        setupUIViews();

        firebaseStorage = FirebaseStorage.getInstance();
        firebaseAuth =FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        userAuthKey = user.getUid();
        storageReference = firebaseStorage.getReference();
        storageReference.child("Users").child(userAuthKey).child("Images").child("Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
//                Picasso.get().load(uri).fit().centerCrop().into(imagePic);
//                imagePic.setImageURI(uri);
                Glide.with(My_Account_Screen.this).load(uri).into(imagePic);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                storageReference.child("Users").child(userAuthKey).child("Images").child("Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).fit().centerCrop().into(imagePic);
                    }
                });

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },4000);
            }
        });


        LogoName.setText(Prevalent.onlineUser.getName());
        name.setText(Prevalent.onlineUser.getName());
        email.setText(Prevalent.onlineUser.getEmail());
        address.setText(Prevalent.onlineUser.getAddress());
        phoneNo.setText(Prevalent.onlineUser.getPhoneNo());


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(My_Account_Screen.this,UpdateAccount_Screen.class);
                startActivity(intent);
            }
        });

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(My_Account_Screen.this,Change_Pass_Screen.class);
                startActivity(intent);
            }
        });

    }

    private void setupUIViews()
    {
        Toolbar toolbar = findViewById(R.id.toolbarMyAccount);
        setSupportActionBar(toolbar);
        toolbar.setTitle("My Account");

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_action_arrow_back);

        LogoName = findViewById(R.id.tvAccountName);
        name = findViewById(R.id.tvAccountUserName);
        email = findViewById(R.id.tvAccountEmail);
        address = findViewById(R.id.tvAccountAddress);
        phoneNo = findViewById(R.id.tvAccountPhone);
        update = findViewById(R.id.btnUpdate);
        changePass = findViewById(R.id.tvChangePass);
        imagePic = findViewById(R.id.myAccount_pic);
        swipeRefreshLayout = findViewById(R.id.Swipe);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                Intent intent = new Intent(My_Account_Screen.this,Home_Screen.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


}
