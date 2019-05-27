package com.example.muazzam.dissertationapp.Admin;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.muazzam.dissertationapp.Model.Products;
import com.example.muazzam.dissertationapp.R;
import com.example.muazzam.dissertationapp.ViewHolder.DeleteProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Admin_Delete_Product2_Screen extends AppCompatActivity {
    private String category;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__delete__product2__screen);
        setupUIViews();

        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Products").child(category);
        storageReference = firebaseStorage.getReference();


    }

    private void setupUIViews()
    {
        Toolbar toolbar = findViewById(R.id.toolbarDeleteProduct2);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_action_black_arrow_back);

        Bundle getCategory = getIntent().getExtras();
        if (getCategory == null)
        {
            return;
        }
        else
        {
            category = getCategory.getString("Category");
            Toast.makeText(Admin_Delete_Product2_Screen.this,category,Toast.LENGTH_SHORT).show();
        }
        toolbar.setTitle(category);

        recyclerView = findViewById(R.id.rvDeleteProduct);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Products> options  = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(databaseReference,Products.class).build();

        final FirebaseRecyclerAdapter<Products,DeleteProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, DeleteProductViewHolder>(options) {
            @NonNull
            @Override
            public DeleteProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.delete_product_layout,viewGroup,false);
                DeleteProductViewHolder holder = new DeleteProductViewHolder(view);
                return holder;
            }

            @Override
            protected void onBindViewHolder(@NonNull final DeleteProductViewHolder holder, int position, @NonNull final Products model) {

                holder.txtname.setText(model.getName());
                holder.txtdesc.setText(model.getDescription());

                storageReference.child("Products").child(model.getID()).child("Images").child("Product Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(Admin_Delete_Product2_Screen.this).load(uri).into(holder.prodPic);
                    }
                });
                holder.deleteProd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder exit = new AlertDialog.Builder(Admin_Delete_Product2_Screen.this,R.style.AdminDialogAlert);
                        exit.setMessage("Do you want to delete Product?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

//                                        adapter.notifyItemRemoved(position);
                                        deleteProduct(model.getCategory(),model.getID());
                                        deleteSupermarketProduct(model.getID());
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
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }



    private void firebaseSearch(String searchText)
    {

        if (!searchText.isEmpty())
        {
            searchText = searchText.substring(0,1).toUpperCase()+ searchText.substring(1,searchText.length()).toLowerCase();
        }
        FirebaseRecyclerOptions<Products> options  = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(databaseReference.orderByChild("Name").startAt(searchText).endAt(searchText + "\uf8ff"),Products.class).build();

        FirebaseRecyclerAdapter<Products,DeleteProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, DeleteProductViewHolder>(options) {
            @NonNull
            @Override
            public DeleteProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.delete_product_layout,viewGroup,false);
                DeleteProductViewHolder holder = new DeleteProductViewHolder(view);
                return holder;
            }

            @Override
            protected void onBindViewHolder(@NonNull final DeleteProductViewHolder holder, final int position, @NonNull final Products model) {

                holder.txtname.setText(model.getName());
                holder.txtdesc.setText(model.getDescription());

                storageReference.child("Products").child(model.getID()).child("Images").child("Product Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
//                Picasso.get().load(uri).fit().centerCrop().into(imagePic);
//                imagePic.setImageURI(uri);
                        Glide.with(Admin_Delete_Product2_Screen.this).load(uri).into(holder.prodPic);
                    }
                });
                holder.deleteProd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder exit = new AlertDialog.Builder(Admin_Delete_Product2_Screen.this,R.style.AdminDialogAlert);
                        exit.setMessage("Do you want to delete Product?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

//                                        adapter.notifyItemRemoved(position);
                                        deleteProduct(model.getCategory(),model.getID());
                                        deleteSupermarketProduct(model.getID());
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
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_search, menu);
        MenuItem item = menu.findItem(R.id.search);

        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                firebaseSearch(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void deleteProduct(final String category, final String id)
    {
        final DatabaseReference mDb = firebaseDatabase.getReference();


        mDb.child("Products").child(category).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mDb.child("Products").child(category).child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(Admin_Delete_Product2_Screen.this, "Product Deleted!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            // Catch allExceptions
                            Toast.makeText(Admin_Delete_Product2_Screen.this,"Failure deleting product from Products Database",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

                    String prodid = nameID.substring(nameID.indexOf('-') + 1,nameID.length());

                    if(prodid.equals(id))
                    {
                        db.child("Supermarkets_Products").child(nameID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    deletePicFromStorage(id);
                                    Toast.makeText(Admin_Delete_Product2_Screen.this, "Product Deleted!", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    // Catch allExceptions
                                    Toast.makeText(Admin_Delete_Product2_Screen.this, "Failure deleting product from Supermarkets_Products Database", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Admin_Delete_Product2_Screen.this,"Failure deleting product from Products Database",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deletePicFromStorage(String id)
    {
        storageReference.child("Products").child(id).child("Images").child("Product Pic").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(Admin_Delete_Product2_Screen.this,"Picture deleted successfully",Toast.LENGTH_SHORT).show();
            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(Admin_Delete_Product2_Screen.this,"Failure deleting product pic from Storage",Toast.LENGTH_SHORT).show();
//            }
        });
    }
}
