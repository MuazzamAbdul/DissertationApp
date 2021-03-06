package com.example.muazzam.dissertationapp.Users;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.muazzam.dissertationapp.Admin.Admin_Home_Screen;
import com.example.muazzam.dissertationapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login_Screen extends AppCompatActivity {

    private EditText useremail;
    private EditText userpassword;
    private Button login;
    private Button signup;
    private Button forgetPass;

    private String email,password;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__screen);
        setupUIViews();
        firebaseAuth = FirebaseAuth.getInstance();



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateTextFields())
                {
                    validateEmailPass(email,password);
                }
            }
        });

        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_Screen.this,Forget_Pass_Screen.class));
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Screen.this,Sign_Up_Screen.class);
                startActivity(intent);
            }
        });
    }

    private void setupUIViews()
    {
        useremail = findViewById(R.id.useremail);
        userpassword = findViewById(R.id.password);
        login = findViewById(R.id.btnLogin);
        signup = findViewById(R.id.btnSignUp);
        forgetPass = findViewById(R.id.btnForgetPass);
        loadingBar = new ProgressDialog(this,R.style.DialogAlert);
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//
//        AlertDialog.Builder exit = new AlertDialog.Builder(Login_Screen.this,R.style.DialogAlert);
//        exit.setMessage("Exit App?")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//        AlertDialog alert = exit.create();
//        alert.setTitle("Exit");
//        alert.show();
//    }

    private boolean validateTextFields()
    {
        boolean result = false;
        email = useremail.getText().toString().trim();
        password= userpassword.getText().toString();
        if (TextUtils.isEmpty(email))
        {
            useremail.setError("Please enter Email");
        }
        else if(TextUtils.isEmpty(password))
        {
            userpassword.setError("Please enter Password");
        }
        else
        {
            result = true;
        }
        return result;
    }

    private void validateEmailPass(final String email, final String password)
    {
        loadingBar.setTitle("Sign In");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        if (email.equals("Admin") && (password.equals("Admin")))
        {
            loadingBar.dismiss();
            finish();
            Toast.makeText(Login_Screen.this,"Admin Login Successful",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Login_Screen.this,Admin_Home_Screen.class);
            startActivity(intent);
        }
        else{
            firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {
                        loadingBar.dismiss();
                        checkEmailVerification();
                    }
                    else
                    {
                        loadingBar.dismiss();
                        Toast.makeText(Login_Screen.this,"Wrong Credentials!",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void checkEmailVerification()
    {
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean verified = firebaseUser.isEmailVerified();
        if (verified)
        {
            Toast.makeText(Login_Screen.this,"Login Successful!",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Login_Screen.this,Home_Screen.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(Login_Screen.this,"Verify your Email!",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }


}
