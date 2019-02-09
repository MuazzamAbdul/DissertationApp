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

import java.util.ArrayList;
import java.util.HashMap;

public class Admin_Add_Supermarket_Screen extends AppCompatActivity {

    private EditText superId,superName,superLocation;
    private Button addSuper,cancel;
    private String id, name,location;
    private FirebaseDatabase firebaseDatabase;
    private ArrayList<String> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__add__supermarket__screen);
        firebaseDatabase = FirebaseDatabase.getInstance();

        setupUIViews();
        retrieveSupermarketID();

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
        list = new ArrayList<>();
    }

    private boolean validateTextFields()
    {
        boolean result = false;
        id = superId.getText().toString();
        name = superName.getText().toString();
        location = superLocation.getText().toString();


         if (TextUtils.isEmpty(id))
        {
            superId.setError("Please enter supermarket ID");
        }
        else if (TextUtils.isEmpty(name))
        {
            superName.setError("Please enter supermarket Name");
        }
        else if (TextUtils.isEmpty(location))
        {
            superLocation.setError("Please enter supermarket Location");
        }
         else if (list.contains(id))
         {
             superId.setError("Supermarket ID already taken");
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
    private void retrieveSupermarketID()
    {
        DatabaseReference mDb = firebaseDatabase.getReference();
        mDb.child("Supermarkets").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String location = ds.getKey();


                    for(DataSnapshot dSnapshot : dataSnapshot.child(location).getChildren()) {
                        String id = dSnapshot.getKey();

                        list.add(id);
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}
