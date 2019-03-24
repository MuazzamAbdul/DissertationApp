package com.example.muazzam.dissertationapp;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import com.example.muazzam.dissertationapp.Adapter.OrderOngoing;
import com.example.muazzam.dissertationapp.Model.CompletedOrder;
import com.example.muazzam.dissertationapp.Prevalent.Prevalent;
import com.example.muazzam.dissertationapp.Users.Add_To_Cart_Screen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Ongoing_Order extends Fragment {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference,db;
    private String name;
    private RecyclerView recyclerView;
    private OrderOngoing madapter;
    private RecyclerView.LayoutManager layoutManager;


    public Fragment_Ongoing_Order() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        final ArrayList<CompletedOrder> completedList = new ArrayList<>();
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_fragment__ongoing__order, container, false);


        databaseReference.child("Orders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String userid = ds.getKey();

                    for(DataSnapshot dSnapshot : dataSnapshot.child(userid).getChildren())
                    {
                        String dateTime = dSnapshot.getKey();
                        String status = String.valueOf(dSnapshot.child("Status").getValue(String.class));


                        if (status.equals("Ongoing"))
                        {
                            final String total = String.valueOf(dSnapshot.child("Total").getValue(String.class));
                            completedList.add(new CompletedOrder(userid,total,dateTime));

                        }
                    }
                }


                recyclerView = view.findViewById(R.id.rvTabOngoing);
                recyclerView.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(getContext());
                madapter = new OrderOngoing(completedList);

                madapter.setOnItemClickListener(new OrderOngoing.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent = new Intent(getActivity(),Admin_Completed_Products_Screen.class);
                        intent.putExtra("UserID", completedList.get(position).getName());
                        intent.putExtra("DateTime", completedList.get(position).getDateTime());
                        startActivity(intent);
                    }

                    @Override
                    public void onCompleteClick(final int position) {

                        AlertDialog.Builder exit = new AlertDialog.Builder(getActivity());
                        exit.setMessage("Order Completed??")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        completeOrder(completedList.get(position).getName(),completedList.get(position).getDateTime());
                                        completedList.remove(position);
                                        madapter.notifyItemRemoved(position);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = exit.create();
                        alert.setTitle("Order");
                        alert.show();



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

    private void completeOrder(final String userId, final String dateTime)
    {
        databaseReference.child("Orders").child(userId).child(dateTime).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String,Object> userDataMap = new HashMap<>();

                userDataMap.put("Status","Completed");

                databaseReference.child("Orders").child(userId).child(dateTime).updateChildren(userDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(getActivity(),"Status Changed",Toast.LENGTH_SHORT).show();
                        }



                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
