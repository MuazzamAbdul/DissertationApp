package com.example.muazzam.dissertationapp.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.muazzam.dissertationapp.R;
import com.example.muazzam.dissertationapp.Users.Category_Screen;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Admin_Delete_Supermarket_Screen extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private ArrayList<Admin_Supermarket_List> list;
    private ArrayList<String> idList;
    private ProgressDialog loadingBar;
    private RecyclerView recyclerView;
    private SupermarketListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__delete__supermarket__screen);

        firebaseDatabase = FirebaseDatabase.getInstance();

        list = new ArrayList<>();
        idList = new ArrayList<>();

        loadingBar = new ProgressDialog(this);
        loadingBar.setTitle("Retrieving Supermarkets");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        DatabaseReference mDb = firebaseDatabase.getReference();
        mDb.child("Supermarkets").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String location = ds.getKey();
                    for(DataSnapshot dSnapshot : dataSnapshot.child(location).getChildren()) {
                        String id = dSnapshot.getKey();
//                        supId.add(id);
                        String name = String.valueOf(dSnapshot.child("Supermarket Name").getValue(String.class));
                        list.add(new Admin_Supermarket_List(name + " " + "[ " + id +" ]"));
                        idList.add(id);
                    }
                }

                loadingBar.dismiss();
                buildRecyclerView();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Admin_Delete_Supermarket_Screen.this,"Failure Retrieving data from Database",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void buildRecyclerView()
    {
        recyclerView = findViewById(R.id.RecView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(Admin_Delete_Supermarket_Screen.this);
        adapter = new SupermarketListAdapter(list);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new SupermarketListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(Admin_Delete_Supermarket_Screen.this,Category_Screen.class);
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(int position) {
                list.remove(position);
                adapter.notifyItemRemoved(position);
                deleteSupermarket(position);
            }
        });
    }

    private void deleteSupermarket(final int position)
    {
        Toast.makeText(Admin_Delete_Supermarket_Screen.this,Integer.toString(position),Toast.LENGTH_SHORT).show();
        DatabaseReference mDb = firebaseDatabase.getReference();

        mDb.child("Supermarkets").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String location = ds.getKey();
                    for(DataSnapshot dSnapshot : dataSnapshot.child(location).getChildren()) {
                        String id = dSnapshot.getKey();

                        if (id.equals(idList.get(position)))
                        {
                            dSnapshot.getRef().removeValue();
                            idList.remove(position);
                            Toast.makeText(Admin_Delete_Supermarket_Screen.this,"Delete Successful",Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Admin_Delete_Supermarket_Screen.this,"Failure Retrieving data from Database",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
