package com.example.muazzam.dissertationapp.Adapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.muazzam.dissertationapp.Model.Products;
import com.example.muazzam.dissertationapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHoolder> implements Filterable {
    private ArrayList<Products> prodList;
    private ArrayList<Products> prodListFull;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
    public static class ProductViewHoolder extends RecyclerView.ViewHolder{

        public TextView txtname,txtdesc;
        public ImageView prodPic;

        public ProductViewHoolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);

            txtname = itemView.findViewById(R.id.product_name);
            prodPic = itemView.findViewById(R.id.product_pic);
            txtdesc = itemView.findViewById(R.id.product_desc);

            txtname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            txtdesc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            prodPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);
                        }
                    }
                }
            });


        }
    }

    public ProductAdapter(ArrayList<Products> prodList) {

        this.prodList = prodList;
        prodListFull = new ArrayList<>(prodList);

    }

    @NonNull
    @Override
    public ProductViewHoolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_layout,viewGroup,false);
        ProductViewHoolder holder = new ProductViewHoolder(v,mListener);
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


    @Override
    public Filter getFilter() {
        return productFilter;
    }

    private Filter productFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Products> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0)
            {
                filteredList.addAll(prodListFull);
            }
            else
            {
                String filterPattern = constraint.toString().trim();

                for (Products item : prodListFull){
                    if (item.getName().startsWith(filterPattern))
                    {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            prodList.clear();
            prodList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };


}
