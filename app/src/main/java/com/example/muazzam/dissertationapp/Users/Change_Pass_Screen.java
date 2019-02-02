package com.example.muazzam.dissertationapp.Users;

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

import com.example.muazzam.dissertationapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Change_Pass_Screen extends AppCompatActivity {

    private EditText oldPass,newPass,confirmPass;
    private Button done,cancel;
    private String oldP,newP,confirmP;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__pass__screen);
        setupUIViews();
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validatePass())
                {
                    changePassword();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToMyAccount();
            }
        });
    }


    private void setupUIViews()
    {
        Toolbar toolbar = findViewById(R.id.toolbarChangePass);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Change Password");

        oldPass = findViewById(R.id.etOldPass);
        newPass = findViewById(R.id.etNewPass);
        confirmPass = findViewById(R.id.etConfirmNewPass);
        done = findViewById(R.id.btnDone);
        cancel = findViewById(R.id.btnCancel_Pass);
    }

    private void sendUserToMyAccount()
    {
        finish();
        Intent intent = new Intent(Change_Pass_Screen.this,My_Account_Screen.class);
        startActivity(intent);
    }

    private boolean validatePass()
    {
        boolean result = false;
        oldP = oldPass.getText().toString();
        newP = newPass.getText().toString();
        confirmP = confirmPass.getText().toString();
        if (TextUtils.isEmpty(oldP))
        {
            Toast.makeText(Change_Pass_Screen.this,"Please enter your old address", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(newP))
        {
            Toast.makeText(Change_Pass_Screen.this,"Please enter new Password", Toast.LENGTH_SHORT).show();

        }
        else if (newP.length() < 6)
        {
            Toast.makeText(Change_Pass_Screen.this,"New Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(confirmP))
        {
            Toast.makeText(Change_Pass_Screen.this,"Please confirm new Password", Toast.LENGTH_SHORT).show();
        }
        else if (!(newP.equals(confirmP)))
        {
            Toast.makeText(Change_Pass_Screen.this,"Password do not match", Toast.LENGTH_SHORT).show();
        }
        else
        {
            result = true;
        }

        return result;
    }

    private void changePassword()
    {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseUser.updatePassword(confirmP).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(Change_Pass_Screen.this,"Password Updated", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    Toast.makeText(Change_Pass_Screen.this,"Password Update Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });


//        firebaseUser.updateEmail("muazzamabdul@hotmail.com").addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                {
//                    if (task.isSuccessful())
//                    {
//                        Toast.makeText(Change_Pass_Screen.this,"Email Updated", Toast.LENGTH_SHORT).show();
//                        finish();
//                    }
//                    else
//                    {
//                        Toast.makeText(Change_Pass_Screen.this,"Email Update Failed!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });
    }
}
