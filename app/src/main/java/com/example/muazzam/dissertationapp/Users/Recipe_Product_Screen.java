package com.example.muazzam.dissertationapp.Users;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.muazzam.dissertationapp.Adapter.ProductAdapter;
import com.example.muazzam.dissertationapp.Model.Products;
import com.example.muazzam.dissertationapp.Prevalent.Prevalent;
import com.example.muazzam.dissertationapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class Recipe_Product_Screen extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private String recipe,recipedesc;
    private ArrayList<String> productslist;
    private ArrayList<Products> productList;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ProductAdapter madapter;
    private TextView recipeDescription;
    private String name,desc,cat,id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe__product__screen);

        setupUIViews();

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        retrieveRecipeProducts();

    }

    private void setupUIViews()
    {
        Toolbar toolbar = findViewById(R.id.toolbarRecipesDetails);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_action_arrow_back);

        Bundle getRecipe = getIntent().getExtras();
        if (getRecipe == null)
        {
            return;
        }
        else
        {
            recipe = getRecipe.getString("Recipe");
            recipedesc = getRecipe.getString("Description");
            Toast.makeText(Recipe_Product_Screen.this,recipe,Toast.LENGTH_SHORT).show();
            Toast.makeText(Recipe_Product_Screen.this,recipedesc,Toast.LENGTH_SHORT).show();
        }
        toolbar.setTitle(recipe);

        recipeDescription = findViewById(R.id.tvRecipeDescription);
        recipeDescription.setText(recipedesc);
        productslist = new ArrayList<>();
        productList = new ArrayList<>();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home__screen, menu);
        return true;
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

            case R.id.itshopping_cart:
                Intent intent = new Intent(this,Shopping_Cart_Screen.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void retrieveRecipeProducts()
    {
        DatabaseReference mDb = firebaseDatabase.getReference();
        mDb.child("Recipes").child(recipe).child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    String prod = ds.getKey();
                    productslist.add(prod);
                }
                retrieveProducts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Recipe_Product_Screen.this,"Failure retrieving products from Recipes database",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void retrieveProducts()
    {
        DatabaseReference Db = firebaseDatabase.getReference();
        Db.child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String category = ds.getKey();

                    for(DataSnapshot dSnapshot : dataSnapshot.child(category).getChildren()){
                        String ids = dSnapshot.getKey();

                        if (productslist.contains(ids))
                        {
                            name = String.valueOf(dSnapshot.child("Name").getValue(String.class));
                            desc = String.valueOf(dSnapshot.child("Description").getValue(String.class));
                            cat = String.valueOf(dSnapshot.child("Category").getValue(String.class));
                            id = String.valueOf(dSnapshot.child("ID").getValue(String.class));
                            productList.add(new Products(name,id,cat,desc));
                        }
                    }
                }

                recyclerView = findViewById(R.id.rvRecipeProd);
                recyclerView.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(Recipe_Product_Screen.this);
                madapter = new ProductAdapter(productList);

                madapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent = new Intent(Recipe_Product_Screen.this,Product_Supermarket_Screen.class);
                        startActivity(intent);

                        Products selectedProd = productList.get(position);
                        Prevalent.displayProducts = selectedProd;
                    }
                });
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(madapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Recipe_Product_Screen.this,"Failure Retrieving data from Database",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
