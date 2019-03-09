package com.example.muazzam.dissertationapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.muazzam.dissertationapp.Interface.ItemClickListener;
import com.example.muazzam.dissertationapp.R;

public class SupermarketViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView superName,price;
    public ItemClickListener listener;

    public SupermarketViewHolder(@NonNull View itemView) {
        super(itemView);

        superName = itemView.findViewById(R.id.supermarket_name);
        price = itemView.findViewById(R.id.price);
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
