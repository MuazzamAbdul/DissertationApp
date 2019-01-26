package com.example.muazzam.dissertationapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Sign_Up_Screen extends AppCompatActivity {

    private EditText userName,userEmail,userPassword,userconfirmPass,userAddress,userPhoneNo;
    private Button btncreateAcc;
    private ProgressDialog loadingBar;
    private FirebaseAuth  firebaseAuth;

    private String name,email,password,confirmPass,address,phneNo,downloadImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up__screen);
        setupUIViews();
        firebaseAuth = FirebaseAuth.getInstance();
        downloadImageUrl = "https://firebasestorage.googleapis.com/v0/b/dissertationapp-9b413.appspot.com/o/Users%2FDefault%2FImages%2FDefault_Profile_Pic.png?alt=media&token=e524965d-92fa-4e5b-ae19-bf99a73eeea4";
        btncreateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateTextFields())
                {
                    CreateAccount();
                }
            }
        });

    }

    private void setupUIViews()
    {
        userName = findViewById(R.id.etName);
        userEmail = findViewById(R.id.etEmail);
        userPassword = findViewById(R.id.etPassword);
        userconfirmPass = findViewById(R.id.etConfirmPass);
        userAddress = findViewById(R.id.etAddress);
        userPhoneNo = findViewById(R.id.etPhneNo);
        btncreateAcc = findViewById(R.id.btncreateacc);
        loadingBar = new ProgressDialog(this);
    }

    private boolean validateTextFields()
    {
        boolean result = false;
        name = userName.getText().toString();
        email = userEmail.getText().toString().trim();
        password = userPassword.getText().toString();
        confirmPass = userconfirmPass.getText().toString();
        address = userAddress.getText().toString();
        phneNo = userPhoneNo.getText().toString().trim();

        if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this,"Please enter Name",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Please enter Email",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Please enter Password",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(confirmPass))
        {
            Toast.makeText(this,"Please confirm Password",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(address))
        {
            Toast.makeText(this,"Please enter Address",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phneNo))
        {
            Toast.makeText(this,"Please enter Phone Number",Toast.LENGTH_SHORT).show();
        }
        else if(!(password.equals(confirmPass)))
        {
            Toast.makeText(this,"Password do not match!",Toast.LENGTH_SHORT).show();
        }
        else if (password.length() < 6 )
        {
            Toast.makeText(this,"Password length must be greater that 5!!",Toast.LENGTH_SHORT).show();
        }
        else{
            result = true;
        }
        return  result;
    }
//
    private void CreateAccount()
    {
        loadingBar.setTitle("Creating Account");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {

                    sendEmailVerification();

                    //Delete these part to enable sendEmailVerification
//                    loadingBar.dismiss();
//                    saveUserData();
////                    sendUserData();
//                    firebaseAuth.signOut();
//                    finish();
//                    Intent intent = new Intent(Sign_Up_Screen.this,Login_Screen.class);
//                    startActivity(intent);
                    ///////
                }
                else
                {
                    //catch all exceptions
                    loadingBar.dismiss();
                    Toast.makeText(Sign_Up_Screen.this,"Registration failed",Toast.LENGTH_SHORT).show();
                }

            }

        });
    }

   private void sendEmailVerification()    {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful())
                    {
                        saveUserData();
                    }
                    else
                    {
                        loadingBar.dismiss();
                        //Catch all exceptions
                        Toast.makeText(Sign_Up_Screen.this,"Verification mail has not been sent!",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void saveUserData()
    {
        final DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();
        final String useriD= firebaseAuth.getUid();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(useriD).exists()))
                {
                    HashMap<String,Object> userDataMap = new HashMap<>();
                    userDataMap.put("Name",name);
                    userDataMap.put("Email",email);
                    userDataMap.put("Address",address);
                    userDataMap.put("PhoneNumber",phneNo);
                    userDataMap.put("Image Url",downloadImageUrl);

                    RootRef.child("Users").child(useriD).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful())
                            {
                                loadingBar.dismiss();
                                Toast.makeText(Sign_Up_Screen.this,"Registration Successful, Verification mail sent!",Toast.LENGTH_SHORT).show();
                                firebaseAuth.signOut();
                                finish();
                                Intent intent = new Intent(Sign_Up_Screen.this,Login_Screen.class);
                                startActivity(intent);
                            }
                            else
                            {
                                // Catch allExceptions
                                Toast.makeText(Sign_Up_Screen.this,"Network Error! PLease try again later",Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(Sign_Up_Screen.this,"This email " + email + " already exists!!",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}
