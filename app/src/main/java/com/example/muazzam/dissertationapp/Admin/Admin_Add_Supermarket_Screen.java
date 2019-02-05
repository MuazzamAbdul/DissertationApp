package com.example.muazzam.dissertationapp.Admin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.muazzam.dissertationapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Admin_Add_Supermarket_Screen extends AppCompatActivity {

    private EditText superId,superName,superLocation;
    private Button addSuper,cancel;
    private String id, name,location;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__add__supermarket__screen);
        firebaseDatabase = FirebaseDatabase.getInstance();

        setupUIViews();

        addSuper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateTextFields())
                {
                    uploadSupermarketData();
                    Toast.makeText(Admin_Add_Supermarket_Screen.this,"Okay",Toast.LENGTH_SHORT).show();

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
        superId = findViewById(R.id.etSupermarketId);
        superName = findViewById(R.id.etSupermarketName);
        superLocation = findViewById(R.id.etSupermarketLoc);
        addSuper= findViewById(R.id.btnAddSup);
        cancel = findViewById(R.id.btncancelSup);
    }

    private boolean validateTextFields()
    {
        boolean result = false;
        id = superId.getText().toString();
        name = superName.getText().toString();
        location = superLocation.getText().toString();


         if (TextUtils.isEmpty(id))
        {
            Toast.makeText(this,"Please enter supermarket ID",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this,"Please enter supermarket Name",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(location))
        {
            Toast.makeText(this,"Please enter supermarket Location",Toast.LENGTH_SHORT).show();
        }
        else
        {
            result = true;
        }
        return result;
    }

    private void uploadSupermarketData()
    {
        final DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.child("Supermarkets").child(location).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                HashMap<String,Object> userDataMap = new HashMap<>();
                userDataMap.put("Supermarket ID",id);
                userDataMap.put("Supermarket Name",name);
                userDataMap.put("Supermarket Location",location);

                databaseReference.child("Supermarkets").child(location).child(id).updateChildren(userDataMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful())
                                {
                                    Toast.makeText(Admin_Add_Supermarket_Screen.this,"Upload Successful!",Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else
                                {
                                    // Catch allExceptions
                                    Toast.makeText(Admin_Add_Supermarket_Screen.this,"Network Error! PLease try again later",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(Admin_Add_Supermarket_Screen.this,"Database Error",Toast.LENGTH_SHORT).show();
            }
        });
    }


}
