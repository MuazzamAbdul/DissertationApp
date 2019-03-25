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

import com.example.muazzam.dissertationapp.Adapter.AdminProductAdapter2;
import com.example.muazzam.dissertationapp.Model.Products;
import com.example.muazzam.dissertationapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Admin_Edit_Products_Screen extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private RecyclerView recyclerView;
    private AdminProductAdapter2 madapter;
    private RecyclerView.LayoutManager layoutManager;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    private ArrayList<Products> productList;
    private String name,desc,cat,id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__edit__products__screen);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Products");
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        setupUIViews();
        retrieveProducts();
    }

    private void setupUIViews()
    {
        Toolbar toolbar = findViewById(R.id.toolbarProducts);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Products");

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_action_black_arrow_back);

        productList = new ArrayList<>();
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

    private void retrieveProducts()
    {
        DatabaseReference mDb = firebaseDatabase.getReference();
        mDb.child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String category = ds.getKey();

                    for(DataSnapshot dSnapshot : dataSnapshot.child(category).getChildren()) {
                        String ids = dSnapshot.getKey();

                        name = String.valueOf(dSnapshot.child("Name").getValue(String.class));
                        desc = String.valueOf(dSnapshot.child("Description").getValue(String.class));
                        cat = String.valueOf(dSnapshot.child("Category").getValue(String.class));
                        id = String.valueOf(dSnapshot.child("ID").getValue(String.class));
                        productList.add(new Products(name,id,cat,desc));

                    }
                }

                recyclerView = findViewById(R.id.rvProducts);
                recyclerView.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(Admin_Edit_Products_Screen.this);
                madapter = new AdminProductAdapter2(productList);

                madapter.setOnItemClickListener(new AdminProductAdapter2.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        finish();
                        Intent intent = new Intent(Admin_Edit_Products_Screen.this,Admin_Edit_Product2_Screen.class);
                        intent.putExtra("Name", productList.get(position).getName());
                        intent.putExtra("ID",productList.get(position).getID());
                        intent.putExtra("Category" , productList.get(position).getCategory());
                        intent.putExtra("Desc" , productList.get(position).getDescription());
                        startActivity(intent);
                    }
                });
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(madapter);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Admin_Edit_Products_Screen.this,"Failure Retrieving data from Database",Toast.LENGTH_SHORT).show();
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
                madapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
