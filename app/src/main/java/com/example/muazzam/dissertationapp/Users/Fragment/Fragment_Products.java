package com.example.muazzam.dissertationapp.Users.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.muazzam.dissertationapp.Model.Products;
import com.example.muazzam.dissertationapp.R;
import com.example.muazzam.dissertationapp.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Products extends Fragment {


    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    public Fragment_Products() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Products");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment__products, container, false);

        recyclerView = view.findViewById(R.id.rvTabProduct);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options  = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(databaseReference,Products.class).build();

        FirebaseRecyclerAdapter<Products,ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ProductViewHolder holder, int position, @NonNull Products model) {

                holder.txtname.setText(model.getName());
                holder.txtdesc.setText(model.getDescription());
                holder.txtcat.setText(model.getCategory());
//                storageReference.child("Products").child(model.getID()).child("Images").child("Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
////                Picasso.get().load(uri).fit().centerCrop().into(imagePic);
////                imagePic.setImageURI(uri);
//                        Glide.with(Fragment_Products.this).load(uri).into(holder.prodPic);
//                    }
//                });

            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_layout,viewGroup,false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
