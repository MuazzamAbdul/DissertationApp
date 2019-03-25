package com.example.muazzam.dissertationapp.Admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.example.muazzam.dissertationapp.Adapter.AdminProductAdapter;
import com.example.muazzam.dissertationapp.Model.Products;
import com.example.muazzam.dissertationapp.Prevalent.Prevalent;
import com.example.muazzam.dissertationapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Admin_Edit_Supermarket_Screen extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private RecyclerView recyclerView;
    private AdminProductAdapter madapter;
    private RecyclerView.LayoutManager layoutManager;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    private ArrayList<String> productIdList;
    private ArrayList<Products> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__edit__supermarket__screen);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Products");
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        setupUIViews();
        retrieveProdID();

    }

    private void setupUIViews()
    {
        Toolbar toolbar = findViewById(R.id.toolbarEdit);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Products");

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_action_black_arrow_back);

        productIdList = new ArrayList<>();
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
                Intent intent = new Intent(Admin_Edit_Supermarket_Screen.this,Admin_Modify_Supermarket_Screen.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        Intent intent = new Intent(Admin_Edit_Supermarket_Screen.this,Admin_Modify_Supermarket_Screen.class);
        startActivity(intent);
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

    private void retrieveProdID()
    {
        DatabaseReference mDb = firebaseDatabase.getReference();

        mDb.child("Supermarkets_Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    String id  = ds.getKey();
                    String superID = id.substring(0,id.indexOf("-"));
                    if (Prevalent.supermarkets.getID().equals(superID))
                    {
                        String prodID = id.substring(id.indexOf("-") + 1, id.length());
                        productIdList.add(prodID);
                    }
                }

                retrieveProdDetails();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void retrieveProdDetails()
    {
        DatabaseReference Db = firebaseDatabase.getReference();

        Db.child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    String cat = ds.getKey();

                    for(DataSnapshot dSnapshot : dataSnapshot.child(cat).getChildren()){

                        String id = dSnapshot.getKey();

                        if (productIdList.contains(id))
                        {
                            String name = String.valueOf(dSnapshot.child("Name").getValue(String.class));
                            String desc = String.valueOf(dSnapshot.child("Description").getValue(String.class));
                            String category = String.valueOf(dSnapshot.child("Category").getValue(String.class));
                            String Proid = String.valueOf(dSnapshot.child("ID").getValue(String.class));
                            productList.add(new Products(name,Proid,category,desc));
                        }
                    }


                }

                recyclerView = findViewById(R.id.rvEditProd);
                recyclerView.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(Admin_Edit_Supermarket_Screen.this);
                madapter = new AdminProductAdapter(productList);

                madapter.setOnItemClickListener(new AdminProductAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        finish();
                        Intent intent = new Intent(Admin_Edit_Supermarket_Screen.this,Admin_Edit_ProductSuper_Screen.class);
                        intent.putExtra("ID",productList.get(position).getID());
                        intent.putExtra("Name",productList.get(position).getName());
                        startActivity(intent);
                    }

                    @Override
                    public void onDeleteClick(final int position) {

                        AlertDialog.Builder exit = new AlertDialog.Builder(Admin_Edit_Supermarket_Screen.this,R.style.AdminDialogAlert);
                        exit.setMessage("Do you want to delete Product?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        deleteSupermarketProduct(productList.get(position).getID());
                                        productList.remove(position);
                                        madapter.notifyItemRemoved(position);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = exit.create();
                        alert.setTitle("Warning");
                        alert.show();



                    }
                });
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(madapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void deleteSupermarketProduct(final String prodId)
    {
        final DatabaseReference db = firebaseDatabase.getReference();

        db.child("Supermarkets_Products").child(Prevalent.supermarkets.getID()+ "-" + prodId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                db.child("Supermarkets_Products").child(Prevalent.supermarkets.getID()+ "-" + prodId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful())
                        {
                            Toast.makeText(Admin_Edit_Supermarket_Screen.this,"Product deleted from " + Prevalent.supermarkets.getName(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
