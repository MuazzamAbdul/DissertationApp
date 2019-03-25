package com.example.muazzam.dissertationapp.Admin.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.muazzam.dissertationapp.Adapter.OrderCompleted;
import com.example.muazzam.dissertationapp.Admin.Admin_Completed_Products_Screen;
import com.example.muazzam.dissertationapp.Model.CompletedOrder;
import com.example.muazzam.dissertationapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Completed_Order extends Fragment {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference,db;
    private String name;
    private RecyclerView recyclerView;
    private OrderCompleted madapter;
    private RecyclerView.LayoutManager layoutManager;


    public Fragment_Completed_Order() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_fragment__completed__order, container, false);

        final ArrayList<CompletedOrder> completedList = new ArrayList<>();

        databaseReference.child("Orders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String userid = ds.getKey();

                    for(DataSnapshot dSnapshot : dataSnapshot.child(userid).getChildren())
                    {
                        String dateTime = dSnapshot.getKey();
                        String status = String.valueOf(dSnapshot.child("Status").getValue(String.class));


                        if (status.equals("Completed"))
                        {
                            final String total = String.valueOf(dSnapshot.child("Total").getValue(String.class));
                            completedList.add(new CompletedOrder(userid,total,dateTime));

                        }
                    }
                }


                recyclerView = view.findViewById(R.id.rvTabCompleted);
                recyclerView.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(getContext());
                madapter = new OrderCompleted(completedList);

                madapter.setOnItemClickListener(new OrderCompleted.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent = new Intent(getActivity(),Admin_Completed_Products_Screen.class);
                        intent.putExtra("UserID", completedList.get(position).getName());
                        intent.putExtra("DateTime", completedList.get(position).getDateTime());
                        startActivity(intent);
                    }
                });
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(madapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(),"Failure Retrieving Orders from Database",Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.admin_search,menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                madapter.getFilter().filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

}
