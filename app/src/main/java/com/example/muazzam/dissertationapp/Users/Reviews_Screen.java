package com.example.muazzam.dissertationapp.Users;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.muazzam.dissertationapp.Prevalent.Prevalent;
import com.example.muazzam.dissertationapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Reviews_Screen extends AppCompatActivity {

    private RatingBar ratingBar;
    private EditText leaveMessage;
    private Button btnSubmitMes;
    private String message,rate;
    private FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews__screen);
        setupUIViews();
        firebaseDatabase = FirebaseDatabase.getInstance();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser)
            {
                rate = String.valueOf(rating);
            }
        });

        btnSubmitMes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateTextField())
                {
                    storeReview();
                }
            }
        });
    }


    private void setupUIViews()
    {
        Toolbar toolbar = findViewById(R.id.toolbarReviews);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Reviews");

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_action_arrow_back);

        ratingBar = findViewById(R.id.ratingBar);
        leaveMessage = findViewById(R.id.etSubmit);
        btnSubmitMes = findViewById(R.id.btnSubmit);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                Intent intent = new Intent(Reviews_Screen.this,Home_Screen.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean validateTextField()
    {
        boolean result = false;
        message = leaveMessage.getText().toString();
        if (TextUtils.isEmpty(message))
        {
            leaveMessage.setError("Please leave a message");
        }
        else
        {
            result = true;
        }
        return result;
    }

    private void storeReview()
    {
        final DatabaseReference databaseReference = firebaseDatabase.getReference();
        final String name = Prevalent.onlineUser.getName();

        databaseReference.child("Reviews").child(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                HashMap<String,Object> userDataMap = new HashMap<>();
                userDataMap.put("Name",name);
                userDataMap.put("Rating",rate);
                userDataMap.put("Review",message);

                databaseReference.child("Reviews").child(name).updateChildren(userDataMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful())
                                {
                                    Toast.makeText(Reviews_Screen.this,"Thank you for your review",Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    // Catch allExceptions
                                    Toast.makeText(Reviews_Screen.this,"Network Error! PLease try again later",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(Reviews_Screen.this,"Database Error",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
