package com.example.muazzam.dissertationapp.Users;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.muazzam.dissertationapp.Model.Products;
import com.example.muazzam.dissertationapp.Model.Supermarkets;
import com.example.muazzam.dissertationapp.Prevalent.Prevalent;
import com.example.muazzam.dissertationapp.R;
import com.example.muazzam.dissertationapp.ViewHolder.ProductViewHolder;
import com.example.muazzam.dissertationapp.ViewHolder.SupermarketViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Product_Supermarket_Screen extends AppCompatActivity {

    private ImageView close,productPic;
    private TextView pname,pdesc;
    private String location;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference databaseReference;

    private static final int MY_PERMISSIONS_REQUEST_CODE = 1 ;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product__supermarket__screen);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        setupUIViews();
        setUpLocation();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void setupUIViews()
    {
        close = findViewById(R.id.ivClose);
        pname = findViewById(R.id.tvPname);
        pdesc = findViewById(R.id.tvPDesc);
        productPic = findViewById(R.id.ivProduct);

        pname.setText(Prevalent.displayProducts.getName());
        pdesc.setText(Prevalent.displayProducts.getDescription());

        storageReference.child("Products").child(Prevalent.displayProducts.getID()).child("Images").child("Product Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(productPic);
//                imagePic.setImageURI(uri);
//                Glide.with(Product_Supermarket_Screen.this).load(uri).into(productPic);
            }
        });





    }


    private void  setUpLocation(){

        if(android.support.v4.app.ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && android.support.v4.app.ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestRuntimePermisson();
        }
        else
        {
             fetchLocation();
        }
    }

    private void requestRuntimePermisson() {
        android.support.v4.app.ActivityCompat.requestPermissions(this, new String[]
                {
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                },MY_PERMISSIONS_REQUEST_CODE);
    }

    private void fetchLocation() {


        if(android.support.v4.app.ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && android.support.v4.app.ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return ;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object

                            Double latitude = location.getLatitude();
                            Double longitude = location.getLongitude();


                            if ((latitude <= -20.193921 && latitude >=-20.334203)   && (longitude>=57.483448 && longitude <= 57.629066))
                            {
                                retrieveSupermarket("Moka");

                            }

                            if ((latitude <= -20.137545&& latitude >=-20.193921)   && (longitude>=57.471481 && longitude <= 57.552166))
                            {
                                retrieveSupermarket("Port-Louis");
                            }

                            if (((latitude <= -20.203829&& latitude >=-20.396059)   && (longitude>=57.451060 && longitude <= 57.485295)) || ((latitude <= -20.239755&& latitude >=-20.396059)   && (longitude>=57.485295 && longitude <= 57.515951)))
                            {
                                retrieveSupermarket("Plaines Wilhems");
                            }
                        }
                    }
                });

    }

    private void retrieveSupermarket(String cityName) {

        recyclerView = findViewById(R.id.rvSupermarkets);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

                                        Toast.makeText(Product_Supermarket_Screen.this,cityName,Toast.LENGTH_SHORT).show();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Supermarkets").child(cityName);

        FirebaseRecyclerOptions<Supermarkets> options  = new FirebaseRecyclerOptions.Builder<Supermarkets>()
                .setQuery(databaseReference,Supermarkets.class).build();

        FirebaseRecyclerAdapter<Supermarkets,SupermarketViewHolder> adapter = new FirebaseRecyclerAdapter<Supermarkets, SupermarketViewHolder>(options) {
            @NonNull
            @Override
            public SupermarketViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.supermarket_layout,viewGroup,false);
                SupermarketViewHolder holder = new SupermarketViewHolder(view);
                return holder;
            }

            @Override
            protected void onBindViewHolder(@NonNull SupermarketViewHolder holder, int position, @NonNull Supermarkets model) {

                holder.superName.setText(model.getName());
                holder.price.setText(model.getID());

            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_CODE)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                fetchLocation();
            }

        }

    }

}
