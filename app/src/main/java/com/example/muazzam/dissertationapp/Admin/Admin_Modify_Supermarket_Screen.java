package com.example.muazzam.dissertationapp.Admin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.muazzam.dissertationapp.Adapter.ModifySupermarketAdapter;
import com.example.muazzam.dissertationapp.Model.Admin_Modify_Supermarket;
import com.example.muazzam.dissertationapp.Model.Supermarkets;
import com.example.muazzam.dissertationapp.Prevalent.Prevalent;
import com.example.muazzam.dissertationapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Admin_Modify_Supermarket_Screen extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private ArrayList<Admin_Modify_Supermarket> superList;
    private RecyclerView recyclerView;
    private ModifySupermarketAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__modify__supermarket__screen);

        firebaseDatabase = FirebaseDatabase.getInstance();

        setupUIViews();
        retrieveSupermarkets();
    }

    private void setupUIViews()
    {
        Toolbar toolbar = findViewById(R.id.toolbarModifySupermarkets);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Supermarkets");

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_action_black_arrow_back);

        superList = new ArrayList<>();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void retrieveSupermarkets()
    {
        DatabaseReference mDb = firebaseDatabase.getReference();
        mDb.child("Supermarkets").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String location = ds.getKey();
                    for(DataSnapshot dSnapshot : dataSnapshot.child(location).getChildren()) {
                        String id = dSnapshot.getKey();
//
                        String name = String.valueOf(dSnapshot.child("Name").getValue(String.class));
                        superList.add(new Admin_Modify_Supermarket(name,id,location));
                    }
                }

                recyclerView = findViewById(R.id.rvModifySupermarkets);
                recyclerView.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(Admin_Modify_Supermarket_Screen.this);
                adapter = new ModifySupermarketAdapter(superList);

                adapter.setOnItemClickListener(new ModifySupermarketAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        finish();
                        Intent intent = new Intent(Admin_Modify_Supermarket_Screen.this,Admin_Edit_Supermarket_Screen.class);
                        startActivity(intent);

                        Supermarkets prodSuper =new Supermarkets(superList.get(position).getID(),superList.get(position).getName(),superList.get(position).getDistrict());
                        Prevalent.supermarkets = prodSuper;
                    }
                });

                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Admin_Modify_Supermarket_Screen.this,"Failure Retrieving data from Database",Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_search,menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
