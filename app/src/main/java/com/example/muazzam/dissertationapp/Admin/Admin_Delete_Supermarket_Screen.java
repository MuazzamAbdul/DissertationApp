package com.example.muazzam.dissertationapp.Admin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.muazzam.dissertationapp.Adapter.SupermarketListAdapter;
import com.example.muazzam.dissertationapp.Model.Admin_Supermarket_List;
import com.example.muazzam.dissertationapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
        setupUIViews();
        retrieveSupermarkets();
    }

    private void setupUIViews()
    {

        Toolbar toolbar = findViewById(R.id.toolbarDeleteSupermarket);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Delete Supermarket");

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_action_black_arrow_back);

        loadingBar = new ProgressDialog(this);
        loadingBar.setTitle("Retrieving Supermarkets");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();



        list = new ArrayList<>();
        idList = new ArrayList<>();

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
//                Intent intent = new Intent(Admin_Registered_Users.this,Home_Screen.class);
//                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
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
//                Intent intent = new Intent(Admin_Delete_Supermarket_Screen.this,Category_Screen.class);
//                startActivity(intent);
//                Admin_Supermarket_List nameId  = list.get(position);
//                String id = nameId.getName();
//                Toast.makeText(Admin_Delete_Supermarket_Screen.this,id,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteClick(final int position) {

                AlertDialog.Builder exit = new AlertDialog.Builder(Admin_Delete_Supermarket_Screen.this,R.style.AdminDialogAlert);
                exit.setMessage("Do you want to delete supermarket?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Admin_Supermarket_List nameId  = list.get(position);
                                String superId = idList.get(position);

                                list.remove(nameId);
                                adapter.notifyItemRemoved(position);
                                deleteSupermarket(nameId);
                                deleteSupermarketProduct(superId);
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
    }

    private void deleteSupermarket(final Admin_Supermarket_List nameId)
    {
        final DatabaseReference mDb = firebaseDatabase.getReference();
        final String ids = nameId.getName();

        mDb.child("Supermarkets").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String location = ds.getKey();
                    for(DataSnapshot dSnapshot : dataSnapshot.child(location).getChildren()) {
                        String id = dSnapshot.getKey();

                        if (ids.contains(id))
                        {
                            mDb.child("Supermarkets").child(location).child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        buildRecyclerView();
                                        Toast.makeText(Admin_Delete_Supermarket_Screen.this, "Supermarket Deleted!", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        // Catch allExceptions
                                        Toast.makeText(Admin_Delete_Supermarket_Screen.this,"Failure deleting supermarket from Database",Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });


                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Admin_Delete_Supermarket_Screen.this,"Failure deleting data from Supermarkets Database",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteSupermarketProduct(final String id)
    {
        final DatabaseReference db = firebaseDatabase.getReference();

        db.child("Supermarkets_Products").addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    String nameID = ds.getKey();
                    String superID = nameID.substring(0,nameID.indexOf('-'));
                    if(superID.equals(id))
                    {
//
                        db.child("Supermarkets_Products").child(nameID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    Toast.makeText(Admin_Delete_Supermarket_Screen.this, "Supermarket Deleted!", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    // Catch allExceptions
                                    Toast.makeText(Admin_Delete_Supermarket_Screen.this,"Failure deleting supermarket from Supermarkets_Products Database",Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Admin_Delete_Supermarket_Screen.this,"Failure deleting data from Database",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
