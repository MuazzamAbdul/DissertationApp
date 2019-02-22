package com.example.muazzam.dissertationapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.muazzam.dissertationapp.Model.Admin_Supermarket_List;
import com.example.muazzam.dissertationapp.R;

import java.util.ArrayList;

public class SupermarketListAdapter extends RecyclerView.Adapter<SupermarketListAdapter.SupermarketListViewHolder> implements Filterable {
    private ArrayList<Admin_Supermarket_List> msupermarketList;
    private ArrayList<Admin_Supermarket_List> msupermarketListFull;
    private OnItemClickListener mListener;



    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
    public static class SupermarketListViewHolder extends RecyclerView.ViewHolder{

        public TextView name;
        public ImageView delete;

        public SupermarketListViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            name = itemView.findViewById(R.id.supName);
            delete = itemView.findViewById(R.id.deleteImage);

            name.setOnClickListener(new View.OnClickListener() {
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

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    public SupermarketListAdapter(ArrayList<Admin_Supermarket_List> supermarketList)
    {
        msupermarketList = supermarketList;
        msupermarketListFull = new ArrayList<>(supermarketList);
    }

    @NonNull
    @Override
    public SupermarketListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
         View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.supermarket_list,viewGroup,false);
         SupermarketListViewHolder svh = new SupermarketListViewHolder(v,mListener);
         return svh;
    }

    @Override
    public void onBindViewHolder(@NonNull SupermarketListViewHolder supermarketListViewHolder, int i) {
        Admin_Supermarket_List currentItem = msupermarketList.get(i);

        supermarketListViewHolder.name.setText(currentItem.getName());
    }

    @Override
    public int getItemCount() {
        return msupermarketList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Admin_Supermarket_List> filteredList = new ArrayList<>();

            if ((constraint == null) || (constraint.length() == 0))
            {
                filteredList.addAll(msupermarketListFull);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Admin_Supermarket_List item : msupermarketListFull){
                    if (item.getName().toLowerCase().startsWith(filterPattern))
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

            msupermarketList.clear();
            msupermarketList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };
}
