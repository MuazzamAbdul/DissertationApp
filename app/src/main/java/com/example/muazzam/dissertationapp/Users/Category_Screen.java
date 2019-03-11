package com.example.muazzam.dissertationapp.Users;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.muazzam.dissertationapp.Model.Supermarkets;
import com.example.muazzam.dissertationapp.R;
import com.example.muazzam.dissertationapp.ViewHolder.SupermarketViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Category_Screen extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category__screen);

//        Toast.makeText(Category_Screen.this,Prevalent.displayProducts.getID(),Toast.LENGTH_SHORT).show();
//        Toast.makeText(Category_Screen.this,Prevalent.displayProducts.getCategory(),Toast.LENGTH_SHORT).show();
//        Toast.makeText(Category_Screen.this,Prevalent.displayProducts.getDescription(),Toast.LENGTH_SHORT).show();
//        Toast.makeText(Category_Screen.this,Prevalent.displayProducts.getName(),Toast.LENGTH_SHORT).show();



        recyclerView = findViewById(R.id.Recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Supermarkets").child("Moka");

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
}
