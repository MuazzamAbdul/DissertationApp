package com.example.muazzam.dissertationapp.Admin.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.muazzam.dissertationapp.Admin.Admin_Modify_Supermarket_Screen;
import com.example.muazzam.dissertationapp.Admin.Admin_Registered_Users;
import com.example.muazzam.dissertationapp.Admin.Admin_Order_Screen;
import com.example.muazzam.dissertationapp.Admin.Admin_Edit_Products_Screen;
import com.example.muazzam.dissertationapp.R;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Home extends Fragment {


    public Fragment_Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment__home, container, false);
        CircleImageView regUsers = view.findViewById(R.id.reg_Users);
        CircleImageView orders = view.findViewById(R.id.Orders);
        CircleImageView modSuper = view.findViewById(R.id.Supermarkets);
        CircleImageView modProd = view.findViewById(R.id.Products);


        regUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Admin_Registered_Users.class);
                startActivity(intent);
            }
        });

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Admin_Order_Screen.class);
                startActivity(intent);
            }
        });

        modSuper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Admin_Modify_Supermarket_Screen.class);
                startActivity(intent);
            }
        });

        modProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Admin_Edit_Products_Screen.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
