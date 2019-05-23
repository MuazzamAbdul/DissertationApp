package com.example.muazzam.dissertationapp.Users;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.muazzam.dissertationapp.Model.Products;
import com.example.muazzam.dissertationapp.Prevalent.Prevalent;
import com.example.muazzam.dissertationapp.R;
import com.example.muazzam.dissertationapp.SupermarketMap_Screen;
import com.example.muazzam.dissertationapp.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Cat_Prod_Screen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private TextView usernameText;
    private TextView usernameEmail;
    private DrawerLayout drawer;
    private CircleImageView imagePic;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private String category;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Uri prodPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat__prod__screen);
        setupUIViews();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Products").child(category);
        storageReference = firebaseStorage.getReference();

    }

    private void setupUIViews()
    {
        Toolbar toolbar = findViewById(R.id.toolbarCat);
        setSupportActionBar(toolbar);


        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_action_menu);

        drawer = findViewById(R.id.drawer_layout);

        Bundle getCategory = getIntent().getExtras();
        if (getCategory == null)
        {
            return;
        }
        else
        {
            category = getCategory.getString("Category");
            Toast.makeText(Cat_Prod_Screen.this,category,Toast.LENGTH_SHORT).show();
        }
        toolbar.setTitle(category);

        recyclerView = findViewById(R.id.rvCatProd);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        NavigationView navigationView =  findViewById(R.id.nav_view1);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        usernameText= headerView.findViewById(R.id.tvnavbar_name);
        usernameEmail = headerView.findViewById(R.id.tvnavbar_email);
        imagePic = headerView.findViewById(R.id.reg_Users);
        getUserData();


        FirebaseRecyclerOptions<Products> options  = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(databaseReference,Products.class).build();

        FirebaseRecyclerAdapter<Products,ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_layout,viewGroup,false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }

            @Override
            protected void onBindViewHolder(@NonNull final ProductViewHolder holder, int position, @NonNull final Products model) {

                holder.txtname.setText(model.getName());
                holder.txtdesc.setText(model.getDescription());

                storageReference.child("Products").child(model.getID()).child("Images").child("Product Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        prodPicture = uri;
//                Picasso.get().load(uri).fit().centerCrop().into(imagePic);
//                imagePic.setImageURI(uri);
                        Glide.with(Cat_Prod_Screen.this).load(uri).into(holder.prodPic);
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(Cat_Prod_Screen.this,Product_Supermarket_Screen.class);
                        startActivity(intent);
                        intent.putExtra("Picture",prodPicture);
                        Products selectedProd = new Products(model.getName(),model.getID(),model.getCategory(),model.getDescription());
                        Prevalent.displayProducts = selectedProd;
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cat_prod_menu, menu);
        MenuItem item = menu.findItem(R.id.app_bar_search);

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
                drawer.openDrawer(GravityCompat.START);
                return true;

            case R.id.shopping_cart:
                Intent intent = new Intent(this,Shopping_Cart_Screen.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.my_account) {

            Intent intent = new Intent(Cat_Prod_Screen.this, My_Account_Screen.class);
            startActivity(intent);
        }else if (id == R.id.faq) {

            Intent intent = new Intent(Cat_Prod_Screen.this, FAQ_Screen.class);
            startActivity(intent);
        } else if (id ==R.id.reviews) {

            Intent intent = new Intent(Cat_Prod_Screen.this, Reviews_Screen.class);
            startActivity(intent);
        } else if (id == R.id.about_us) {

            Intent intent = new Intent(Cat_Prod_Screen.this, About_Us_Screen.class);
            startActivity(intent);
        } else if (id == R.id.supermarket_map) {
            Intent intent = new Intent(Cat_Prod_Screen.this, SupermarketMap_Screen.class);
            startActivity(intent);
        } else if (id == R.id.recipes) {
            Intent intent = new Intent(Cat_Prod_Screen.this, Settings_Screen.class);
            startActivity(intent);
        } else if (id == R.id.sign_out) {
            firebaseAuth.signOut();
            finish();
            Intent intent = new Intent(Cat_Prod_Screen.this, Login_Screen.class);
            startActivity(intent);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getUserData()
    {
        DatabaseReference mDb = firebaseDatabase.getReference();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String userAuthKey = user.getUid();
        mDb.child("Users").child(userAuthKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String userEmail = String.valueOf(dataSnapshot.child("Email").getValue());
                String userName = String.valueOf(dataSnapshot.child("Name").getValue());
                String userPhone = String.valueOf(dataSnapshot.child("PhoneNumber").getValue());
                String userAddress = String.valueOf(dataSnapshot.child("Address").getValue());

                usernameText.setText(userName);
                usernameEmail.setText(userEmail);
//                Users userData = new Users(userName,userEmail,userAddress,userPhone);
//                Prevalent.onlineUser = userData;
//                usernameText.setText(Prevalent.onlineUser.getName());
//                usernameEmail.setText(Prevalent.onlineUser.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Cat_Prod_Screen.this,"Failure Retrieving data from Database",Toast.LENGTH_SHORT).show();
            }
        });

//                usernameText.setText(Prevalent.onlineUser.getName());
//                usernameEmail.setText(Prevalent.onlineUser.getEmail());

        StorageReference storageReference = firebaseStorage.getReference();

        storageReference.child("Users").child(userAuthKey).child("Images").child("Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Picasso.get().load(uri).fit().centerCrop().into(imagePic);
            }
        });
    }

    private void firebaseSearch(String searchText)
    {

        if (!searchText.isEmpty())
        {
            searchText = searchText.substring(0,1).toUpperCase()+ searchText.substring(1,searchText.length()).toLowerCase();
        }
        FirebaseRecyclerOptions<Products> options  = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(databaseReference.orderByChild("Name").startAt(searchText).endAt(searchText + "\uf8ff"),Products.class).build();

        FirebaseRecyclerAdapter<Products,ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_layout,viewGroup,false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }

            @Override
            protected void onBindViewHolder(@NonNull final ProductViewHolder holder, int position, @NonNull final Products model) {

                holder.txtname.setText(model.getName());
                holder.txtdesc.setText(model.getDescription());

                storageReference.child("Products").child(model.getID()).child("Images").child("Product Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
//                Picasso.get().load(uri).fit().centerCrop().into(imagePic);
//                imagePic.setImageURI(uri);
                        Glide.with(Cat_Prod_Screen.this).load(uri).into(holder.prodPic);
                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(Cat_Prod_Screen.this,Product_Supermarket_Screen.class);
                        startActivity(intent);
                        Products selectedProd = new Products(model.getName(),model.getID(),model.getCategory(),model.getDescription());
                        Prevalent.displayProducts = selectedProd;
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}
