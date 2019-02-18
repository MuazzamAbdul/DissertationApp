package com.example.muazzam.dissertationapp.Admin;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
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

import com.bumptech.glide.Glide;
import com.example.muazzam.dissertationapp.Model.DisplayUsers;
import com.example.muazzam.dissertationapp.R;
import com.example.muazzam.dissertationapp.ViewHolder.Admin_UsersList;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Admin_Registered_Users extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__registered__users);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        setupUIViews();
    }

    private void setupUIViews()
    {
        Toolbar toolbar = findViewById(R.id.toolbarAddProduct1);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Registered Users");

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_action_black_arrow_back);


        recyclerView = findViewById(R.id.rvUsers);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_search,menu);
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

    private void firebaseSearch(String searchText)
    {
        FirebaseRecyclerOptions<DisplayUsers> options  = new FirebaseRecyclerOptions.Builder<DisplayUsers>()
                .setQuery(databaseReference.orderByChild("Name").startAt(searchText).endAt(searchText + "\uf8ff"),DisplayUsers.class).build();

        FirebaseRecyclerAdapter<DisplayUsers,Admin_UsersList> adapter = new FirebaseRecyclerAdapter<DisplayUsers, Admin_UsersList>(options)
        {

            @NonNull
            @Override
            public Admin_UsersList onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_layout,viewGroup,false);
                Admin_UsersList holder = new Admin_UsersList((view));
                return holder;
            }

            @Override
            protected void onBindViewHolder(@NonNull final Admin_UsersList holder, int position, @NonNull DisplayUsers model) {

                holder.txtname.setText(model.getName());
                holder.txtemail.setText(model.getEmail());
                holder.txtaddress.setText(model.getAddress());
                holder.txtphone.setText(model.getPhoneNumber());
                storageReference.child("Users").child(model.getKey()).child("Images").child("Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
//                Picasso.get().load(uri).fit().centerCrop().into(imagePic);
//                imagePic.setImageURI(uri);
                        Glide.with(Admin_Registered_Users.this).load(uri).into(holder.userpic);
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<DisplayUsers> options  = new FirebaseRecyclerOptions.Builder<DisplayUsers>()
                .setQuery(databaseReference,DisplayUsers.class).build();

        FirebaseRecyclerAdapter<DisplayUsers,Admin_UsersList> adapter = new FirebaseRecyclerAdapter<DisplayUsers, Admin_UsersList>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final Admin_UsersList holder, int position, @NonNull DisplayUsers model) {

                holder.txtname.setText(model.getName());
                holder.txtemail.setText(model.getEmail());
                holder.txtaddress.setText(model.getAddress());
                holder.txtphone.setText(model.getPhoneNumber());
                storageReference.child("Users").child(model.getKey()).child("Images").child("Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
//                Picasso.get().load(uri).fit().centerCrop().into(imagePic);
//                imagePic.setImageURI(uri);
                        Glide.with(Admin_Registered_Users.this).load(uri).into(holder.userpic);
                    }
                });

//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(Admin_Registered_Users.this,Category_Screen.class);
//                        startActivity(intent);
//                    }
//                });
            }

            @NonNull
            @Override
            public Admin_UsersList onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                 View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_layout,viewGroup,false);
                 Admin_UsersList holder = new Admin_UsersList((view));
                 return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


}
