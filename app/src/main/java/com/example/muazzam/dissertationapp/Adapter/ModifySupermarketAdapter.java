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

import com.example.muazzam.dissertationapp.Model.Admin_Modify_Supermarket;
import com.example.muazzam.dissertationapp.R;

import java.util.ArrayList;

public class ModifySupermarketAdapter extends RecyclerView.Adapter<ModifySupermarketAdapter.ModifySupermarketViewHolder> implements Filterable {

    private ArrayList<Admin_Modify_Supermarket> msupermarketList;
    private ArrayList<Admin_Modify_Supermarket> msupermarketListFull;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class ModifySupermarketViewHolder extends RecyclerView.ViewHolder{

        public TextView superName,superID,district;
        private ImageView modify;

        public ModifySupermarketViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            superID = itemView.findViewById(R.id.SuperID2);
            superName = itemView.findViewById(R.id.Name2);
            district = itemView.findViewById(R.id.District2);
            modify = itemView.findViewById(R.id.btnModify);

            modify.setOnClickListener(new View.OnClickListener() {
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

    public ModifySupermarketAdapter(ArrayList<Admin_Modify_Supermarket> supermarketList) {
        msupermarketList = supermarketList;
        msupermarketListFull = new ArrayList<>(supermarketList);
    }


    @NonNull
    @Override
    public ModifySupermarketViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.admin_modify_super_layout,viewGroup,false);
        ModifySupermarketViewHolder svh = new ModifySupermarketViewHolder(v,mListener);
        return svh;
    }

    @Override
    public void onBindViewHolder(@NonNull ModifySupermarketViewHolder modifySupermarketViewHolder, int i) {
        Admin_Modify_Supermarket currentItem = msupermarketList.get(i);

        modifySupermarketViewHolder.superID.setText(currentItem.getID());
        modifySupermarketViewHolder.superName.setText(currentItem.getName());
        modifySupermarketViewHolder.district.setText(currentItem.getDistrict());
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
            ArrayList<Admin_Modify_Supermarket> filteredList = new ArrayList<>();

            if ((constraint == null) || (constraint.length() == 0))
            {
                filteredList.addAll(msupermarketListFull);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Admin_Modify_Supermarket item : msupermarketListFull){
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
