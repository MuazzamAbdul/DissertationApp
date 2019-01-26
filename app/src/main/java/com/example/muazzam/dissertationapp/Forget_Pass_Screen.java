package com.example.muazzam.dissertationapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class Forget_Pass_Screen extends AppCompatActivity {

    private EditText userEmail;
    private Button resetPass;
    private Button cancel;
    private FirebaseAuth firebaseAuth;
    public static Boolean reset = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget__pass__screen);
        setupUIViews();

        Toolbar toolbar = findViewById(R.id.toolbarPass);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Forget Password");

        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToLogin();
            }
        });


    }

    private void setupUIViews()
    {
        userEmail = findViewById(R.id.etForgetPass);
        resetPass = findViewById(R.id.btnReset_Pass);
        cancel= findViewById(R.id.btnCancel);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void changePassword()
    {
        String email = userEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(Forget_Pass_Screen.this,"Please enter your email address", Toast.LENGTH_SHORT).show();
        }
        else
        {
            reset = true;
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(Forget_Pass_Screen.this,"Password reset email sent", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(Forget_Pass_Screen.this,Login_Screen.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(Forget_Pass_Screen.this,"Invalid Email address!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendUserToLogin()
    {
        finish();
        Intent intent = new Intent(Forget_Pass_Screen.this,Login_Screen.class);
        startActivity(intent);
    }



}
