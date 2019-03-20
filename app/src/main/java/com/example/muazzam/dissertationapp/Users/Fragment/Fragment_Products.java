package com.example.muazzam.dissertationapp.Users.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.muazzam.dissertationapp.Adapter.ProductAdapter;
import com.example.muazzam.dissertationapp.Model.Products;
import com.example.muazzam.dissertationapp.Prevalent.Prevalent;
import com.example.muazzam.dissertationapp.R;
import com.example.muazzam.dissertationapp.Users.Shopping_Cart_Screen;
import com.example.muazzam.dissertationapp.Users.Product_Supermarket_Screen;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Products extends Fragment {


    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private RecyclerView recyclerView;
    private ProductAdapter madapter;
    private RecyclerView.LayoutManager layoutManager;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    private String name,desc,cat,id;

    public Fragment_Products() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Products");
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();



        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_fragment__products, container, false);

        final ArrayList<Products> productList = new ArrayList<>();

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

                recyclerView = view.findViewById(R.id.rvTabProduct);
                recyclerView.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(getContext());
                madapter = new ProductAdapter(productList);

                madapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent = new Intent(getActivity(),Product_Supermarket_Screen.class);
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
                Toast.makeText(getActivity(),"Failure Retrieving data from Database",Toast.LENGTH_SHORT).show();
            }
        });



        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.prod_menu,menu);

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
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.itshopping_cart:
                Intent intent = new Intent(getActivity(),Shopping_Cart_Screen.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
