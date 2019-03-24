package com.example.muazzam.dissertationapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.muazzam.dissertationapp.Model.CompletedOrder;
import com.example.muazzam.dissertationapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderOngoing extends RecyclerView.Adapter<OrderOngoing.OngoingViewHolder> implements Filterable {

    private ArrayList<CompletedOrder> ongoingList;
    private ArrayList<CompletedOrder> ongoingListFull;
    private OnItemClickListener mListener;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference db;



    public interface OnItemClickListener{
        void onItemClick(int position);
        void onCompleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class OngoingViewHolder extends RecyclerView.ViewHolder{

        public TextView txtname,txtprice,date;
        private Button complete;

        public OngoingViewHolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);

            txtname = itemView.findViewById(R.id.AdOnuser_name);
            txtprice = itemView.findViewById(R.id.AdOntotalPrice);
            date = itemView.findViewById(R.id.AdOnDateTime);
            complete = itemView.findViewById(R.id.btnAdComplete);

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

            complete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            listener.onCompleteClick(position);
                        }
                    }
                }
            });

        }
    }

    public OrderOngoing(ArrayList<CompletedOrder> ongoingList) {
        this.ongoingList = ongoingList;
        ongoingListFull = new ArrayList<>(ongoingList);
    }

    @NonNull
    @Override
    public OngoingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ongoing_order_layout,viewGroup,false);
        OngoingViewHolder holder = new OngoingViewHolder(v,mListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final OngoingViewHolder ongoingViewholder, int i) {

        firebaseDatabase = FirebaseDatabase.getInstance();
        final CompletedOrder currentItem = ongoingList.get(i);

        db = firebaseDatabase.getReference();
        db.child("Users").child(currentItem.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                {
                    String name = String.valueOf(dataSnapshot.child("Name").getValue(String.class));
                    ongoingViewholder.txtname.setText(name);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ongoingViewholder.txtprice.setText(currentItem.getTotal());
        ongoingViewholder.date.setText(currentItem.getDateTime());

    }

    @Override
    public int getItemCount() {
        return ongoingList.size();
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
                filteredList.addAll(ongoingListFull);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (CompletedOrder item : ongoingListFull){
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

            ongoingList.clear();
            ongoingList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };
}
