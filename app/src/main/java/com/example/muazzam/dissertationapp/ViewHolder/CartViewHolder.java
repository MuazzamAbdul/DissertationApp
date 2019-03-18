package com.example.muazzam.dissertationapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.muazzam.dissertationapp.Interface.ItemClickListener;
import com.example.muazzam.dissertationapp.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView nameCart,priceCart,supermarketCart;
    public ElegantNumberButton qtyCart;
    public ImageView deleteProd;
    public ItemClickListener listener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        nameCart = itemView.findViewById(R.id.prodNameCart);
        priceCart = itemView.findViewById(R.id.prodPriceCart);
        supermarketCart = itemView.findViewById(R.id.SuperCart);
        qtyCart = itemView.findViewById(R.id.btnQtyCart);
        deleteProd = itemView.findViewById(R.id.ivDeleteProdCart);

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
