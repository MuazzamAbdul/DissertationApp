package com.example.muazzam.dissertationapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.muazzam.dissertationapp.Model.DisplaySuperProdPrice;
import com.example.muazzam.dissertationapp.R;

import java.util.ArrayList;

public class SupermarketProductpriceAdapter extends RecyclerView.Adapter<SupermarketProductpriceAdapter.SupermarketProductpriceViewHolder> {

    private ArrayList<DisplaySuperProdPrice> list;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class SupermarketProductpriceViewHolder extends RecyclerView.ViewHolder
    {
        public TextView superName,price;

        public SupermarketProductpriceViewHolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);

            superName = itemView.findViewById(R.id.supermarket_name);
            price = itemView.findViewById(R.id.price);

            superName.setOnClickListener(new View.OnClickListener() {
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

            price.setOnClickListener(new View.OnClickListener() {
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

    public SupermarketProductpriceAdapter(ArrayList<DisplaySuperProdPrice> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public SupermarketProductpriceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.supermarket_layout,viewGroup,false);
        SupermarketProductpriceViewHolder holder = new SupermarketProductpriceViewHolder(v,mListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SupermarketProductpriceViewHolder supermarketProductpriceViewHolder, int i) {
        DisplaySuperProdPrice currentItem = list.get(i);

        supermarketProductpriceViewHolder.superName.setText(currentItem.getName());
        supermarketProductpriceViewHolder.price.setText("Rs " + currentItem.getPrice());
    }

    @Override
    public int getItemCount() {
        return  list.size();
    }
}
