package com.example.muazzam.dissertationapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muazzam.dissertationapp.Model.CompletedOrder;
import com.example.muazzam.dissertationapp.Model.Products;
import com.example.muazzam.dissertationapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class OrderCompleted extends RecyclerView.Adapter<OrderCompleted.CompletedViewHolder> implements Filterable {

    private ArrayList<CompletedOrder> completedList;
    private ArrayList<CompletedOrder> completedListFull;
    private OnItemClickListener mListener;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference db;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class CompletedViewHolder extends RecyclerView.ViewHolder
    {
        public TextView txtname,txtprice,date;

        public CompletedViewHolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);

            txtname = itemView.findViewById(R.id.Aduser_name);
            txtprice = itemView.findViewById(R.id.AdtotalPrice);
            date = itemView.findViewById(R.id.AdDateTime);

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

            txtprice.setOnClickListener(new View.OnClickListener() {
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

            date.setOnClickListener(new View.OnClickListener() {
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

    public OrderCompleted(ArrayList<CompletedOrder> completedList) {
        this.completedList = completedList;
        completedListFull = new ArrayList<>(completedList);
    }



    @NonNull
    @Override
    public CompletedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.completed_ordername_layout,viewGroup,false);
        CompletedViewHolder completedViewHolder = new CompletedViewHolder(v,mListener);
        return completedViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CompletedViewHolder completedViewHolder, int i) {

        firebaseDatabase = FirebaseDatabase.getInstance();
        final CompletedOrder currentItem = completedList.get(i);

        db = firebaseDatabase.getReference();
        db.child("Users").child(currentItem.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                {
                   String name = String.valueOf(dataSnapshot.child("Name").getValue(String.class));
                    completedViewHolder.txtname.setText(name);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        completedViewHolder.txtprice.setText(currentItem.getTotal());
        completedViewHolder.date.setText(currentItem.getDateTime());
    }

    @Override
    public int getItemCount() {
        return completedList.size();
    }


    @Override
    public Filter getFilter() {
        return productFilter;
    }

    private Filter productFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<CompletedOrder> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0)
            {
                filteredList.addAll(completedListFull);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (CompletedOrder item : completedListFull){
                    if ((item.getName().toLowerCase().startsWith(filterPattern)) || (item.getDateTime().toLowerCase().startsWith(filterPattern)))
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

            completedList.clear();
            completedList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

}
