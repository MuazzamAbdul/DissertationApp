package com.example.muazzam.dissertationapp.Users;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muazzam.dissertationapp.Adapter.SupermarketProductpriceAdapter;
import com.example.muazzam.dissertationapp.Model.DisplaySuperProdPrice;
import com.example.muazzam.dissertationapp.Prevalent.Prevalent;
import com.example.muazzam.dissertationapp.R;
import com.example.muazzam.dissertationapp.Shopping_Cart_Screen;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Product_Supermarket_Screen extends AppCompatActivity {

    private ImageView productPic;
    private TextView pname,pdesc;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    private RecyclerView recyclerView;
    private SupermarketProductpriceAdapter madapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private ArrayList<String> supID;
    private ArrayList<DisplaySuperProdPrice> supProdList;
    private Double currentLat,currentLong;


    private static final int MY_PERMISSIONS_REQUEST_CODE = 1 ;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product__supermarket__screen);

        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        storageReference = firebaseStorage.getReference();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        setupUIViews();
        setUpLocation();


    }

    private void setupUIViews()
    {
        pname = findViewById(R.id.tvPname);
        pdesc = findViewById(R.id.tvPDesc);
        productPic = findViewById(R.id.ivProduct);
        supID = new ArrayList<>();
        supProdList = new ArrayList<>();

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

        Toolbar toolbar = findViewById(R.id.toolbarSupermarkets);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Supermarkets");

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_action_arrow_back);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
//                Intent intent = new Intent(FAQ_Screen.this,Home_Screen.class);
//                startActivity(intent);
                return true;

            case R.id.shopping_cart:
                Intent intent = new Intent(Product_Supermarket_Screen.this,Shopping_Cart_Screen.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
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

                            currentLat = location.getLatitude();
                            currentLong = location.getLongitude();


                            if ((currentLat <= -20.193921 && currentLat >=-20.334203)   && (currentLong>=57.483448 && currentLong <= 57.629066))
                            {
                                Toast.makeText(Product_Supermarket_Screen.this,"Moka",Toast.LENGTH_SHORT).show();
                                retrieveSupermarket("Moka");
                                retrieveSupermarketProductList();

                            }

                            if ((currentLat <= -20.137545&& currentLat >=-20.193921)   && (currentLong>=57.471481 && currentLong <= 57.552166))
                            {
                                retrieveSupermarket("Port-Louis");
                                retrieveSupermarketProductList();
                            }

                            if (((currentLat <= -20.203829&& currentLat >=-20.396059)   && (currentLong>=57.451060 && currentLong <= 57.485295)) || ((currentLat <= -20.239755&& currentLat >=-20.396059)   && (currentLong>=57.485295 && currentLong <= 57.515951)))
                            {
                                retrieveSupermarket("Plaines Wilhems");
                                retrieveSupermarketProductList();
                            }
                        }
                    }
                });

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
    private void retrieveSupermarket(String cityName)
    {
        final DatabaseReference mDb = firebaseDatabase.getReference();

        mDb.child("Supermarkets").child(cityName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String id = ds.getKey();


                    String name = String.valueOf(ds.child("Name").getValue(String.class));
                    String superLat = String.valueOf(ds.child("Latitude").getValue(String.class));
                    String superLong = String.valueOf(ds.child("Longitude").getValue(String.class));
                    String distance = calculateDistance(superLat,superLong);
                    supID.add(name + "/" + id + "|" + distance);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Product_Supermarket_Screen.this,"Failure retrieving data from Supermarkets Database",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String calculateDistance(String superLat,String superLong)
    {
        Double lat = Double.parseDouble(superLat);
        Double longi = Double.parseDouble(superLong);

        int R = 6371; // km (Earth radius)
        double dLat = (lat-currentLat)* (Math.PI/180) ;
        double dLon = (longi-currentLong)* (Math.PI/180);


        currentLat = currentLat * (Math.PI/180);
        lat = lat * (Math.PI/180);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(currentLat) * Math.cos(lat);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double d = R * c;

        final DecimalFormat df = new DecimalFormat(".##");

        String distance = df.format(d);

        return distance;
    }

    private void retrieveSupermarketProductList()
    {
        DatabaseReference mDb = firebaseDatabase.getReference();

        mDb.child("Supermarkets_Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String nameID = ds.getKey();

                    for (String supernameID : supID)
                    {
                        String superID = supernameID.substring(supernameID.indexOf('/')+1,supernameID.indexOf("|"));
                        String id = superID + "-" + Prevalent.displayProducts.getID();
                        if (nameID.equals(id))
                        {
                            String superName = supernameID.substring(0,supernameID.indexOf('/'));
                            String superDistance = supernameID.substring(supernameID.indexOf('|') + 1, supernameID.length());
                            String price = String.valueOf(ds.child("Price").getValue(String.class));
                            supProdList.add(new DisplaySuperProdPrice(superName,price,superDistance));
                        }

                    }
                }

                recyclerView = findViewById(R.id.rvSupermarkets);
                recyclerView.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(Product_Supermarket_Screen.this);
                madapter = new SupermarketProductpriceAdapter(supProdList);

                madapter.setOnItemClickListener(new SupermarketProductpriceAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {

                        finish();
                        Intent intent = new Intent(Product_Supermarket_Screen.this,Add_To_Cart_Screen.class);
                        startActivity(intent);

                        DisplaySuperProdPrice prodSuper = supProdList.get(position);
                        Prevalent.supermarketProductPrice = prodSuper;
                    }
                });
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(madapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Product_Supermarket_Screen.this,"Failure retrieving Price",Toast.LENGTH_SHORT).show();
            }
        });
    }


}
