package com.example.muazzam.dissertationapp.Adapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.muazzam.dissertationapp.Model.Products;
import com.example.muazzam.dissertationapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHoolder> {
    private ArrayList<Products> prodList;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;


    public static class ProductViewHoolder extends RecyclerView.ViewHolder{

        public TextView txtname,txtdesc;
        public ImageView prodPic;

        public ProductViewHoolder(@NonNull View itemView) {
            super(itemView);

            txtname = itemView.findViewById(R.id.product_name);
            prodPic = itemView.findViewById(R.id.product_pic);
            txtdesc = itemView.findViewById(R.id.product_desc);
        }
    }

    public ProductAdapter(ArrayList<Products> prodList) {

        this.prodList = prodList;

    }

    @NonNull
    @Override
    public ProductViewHoolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_layout,viewGroup,false);
        ProductViewHoolder holder = new ProductViewHoolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductViewHoolder productViewHoolder, int i) {

        Products currentItem = prodList.get(i);

        productViewHoolder.txtname.setText(currentItem.getName());
        productViewHoolder.txtdesc.setText(currentItem.getDescription());
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        storageReference.child("Products").child(currentItem.getID()).child("Images").child("Product Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(productViewHoolder.prodPic);
//                imagePic.setImageURI(uri);

            }
        });

    }

    @Override
    public int getItemCount() {
        return prodList.size();
    }

}
