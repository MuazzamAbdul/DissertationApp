package com.example.muazzam.dissertationapp.Users.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.muazzam.dissertationapp.R;
import com.example.muazzam.dissertationapp.Users.Cat_Prod_Screen;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Category extends Fragment {

    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;


    public Fragment_Category() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment__category, container, false);

        CircleImageView fruitVeg = view.findViewById(R.id.Fruit_Veg);
        CircleImageView dairy = view.findViewById(R.id.Dairy);
        CircleImageView rice = view.findViewById(R.id.Rice);
        CircleImageView frozenFood = view.findViewById(R.id.Frozen_Food);
        CircleImageView snacks = view.findViewById(R.id.Snacks);
        CircleImageView grains = view.findViewById(R.id.Grains);
        CircleImageView cannedFood = view.findViewById(R.id.Canned_Food);
        CircleImageView oil = view.findViewById(R.id.Oil);
        CircleImageView beverages = view.findViewById(R.id.Beverages);
        CircleImageView personalCare = view.findViewById(R.id.Personal_Care);


        fruitVeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Cat_Prod_Screen.class);
                intent.putExtra("Category","FruitVeg");
                startActivity(intent);
            }
        });

        dairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Cat_Prod_Screen.class);
                intent.putExtra("Category","Dairy");
                startActivity(intent);
            }
        });

        rice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Cat_Prod_Screen.class);
                intent.putExtra("Category","Rice");
                startActivity(intent);
            }
        });

        frozenFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Cat_Prod_Screen.class);
                intent.putExtra("Category","Frozen");
                startActivity(intent);
            }
        });

        snacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Cat_Prod_Screen.class);
                intent.putExtra("Category","Snacks");
                startActivity(intent);
            }
        });

        grains.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Cat_Prod_Screen.class);
                intent.putExtra("Category","Grains");
                startActivity(intent);
            }
        });

        cannedFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Cat_Prod_Screen.class);
                intent.putExtra("Category","FruitVeg");
                startActivity(intent);
            }
        });

        oil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Cat_Prod_Screen.class);
                intent.putExtra("Category","Oil");
                startActivity(intent);
            }
        });

        beverages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Cat_Prod_Screen.class);
                intent.putExtra("Category","Beverages");
                startActivity(intent);
            }
        });

        personalCare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Cat_Prod_Screen.class);
                intent.putExtra("Category","Personal Care");
                startActivity(intent);
            }
        });



        return view;
    }


}
