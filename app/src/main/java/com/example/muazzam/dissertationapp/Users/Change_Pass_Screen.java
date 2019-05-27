package com.example.muazzam.dissertationapp.Users;

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

/**
 * Class to change password.
 */
public class Change_Pass_Screen extends AppCompatActivity {

    private EditText oldPass,newPass,confirmPass;
    private Button done,cancel;
    private String oldP,newP,confirmP;
    private FirebaseUser firebaseUser;

    /**
     * Create activity.
     * Change password when done button is pressed.
     * @param savedInstanceState
     */
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

    /**
     * Create UI Views.
     */
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

    /**
     * Finish activity.
     */
    private void sendUserToMyAccount()
    {
        finish();
    }

    /**
     * Validate text boxes.
     * @return
     */
    private boolean validatePass()
    {
        boolean result = false;
        oldP = oldPass.getText().toString();
        newP = newPass.getText().toString();
        confirmP = confirmPass.getText().toString();
        if (TextUtils.isEmpty(oldP))
        {
            oldPass.setError("Please enter your old Password");
        }
        else if(TextUtils.isEmpty(newP))
        {
            newPass.setError("Please enter new Password");
        }
        else if (newP.length() < 6)
        {
            newPass.setError("New Password must be at least 6 characters");
        }
        else if (TextUtils.isEmpty(confirmP))
        {
            confirmPass.setError("Please confirm new Password");
        }
        else if (!(newP.equals(confirmP)))
        {
            newPass.setError("Passwords do not match");
            confirmPass.setError("Passwords do not match");
        }
        else
        {
            result = true;
        }
        return result;
    }

    /**
     * Change password with new password in database.
     */
    private void changePassword()
    {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseUser.updatePassword(confirmP).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(Change_Pass_Screen.this,"Password Successfully Updated", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    Toast.makeText(Change_Pass_Screen.this,"Password Update Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
