package com.example.muazzam.dissertationapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.muazzam.dissertationapp.Interface.ItemClickListener;
import com.example.muazzam.dissertationapp.R;

public class DeleteProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtname,txtdesc;
    public ImageView prodPic,deleteProd;
    public ItemClickListener listener;

    public DeleteProductViewHolder(@NonNull View itemView) {
        super(itemView);

        txtname = itemView.findViewById(R.id.adproduct_name);
        prodPic = itemView.findViewById(R.id.adproduct_pic);
        txtdesc = itemView.findViewById(R.id.adproduct_desc);
        deleteProd = itemView.findViewById(R.id.addelete_prod);

    }

    public void setItemClickListener(ItemClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {

        listener.OnClick(v,getAdapterPosition(),false);

    }
}
