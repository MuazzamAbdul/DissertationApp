package com.example.muazzam.dissertationapp.Admin.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.muazzam.dissertationapp.Admin.Admin_Add_Product_Screen;
import com.example.muazzam.dissertationapp.R;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Add extends Fragment {


    public Fragment_Add() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment__add, container, false);
        CircleImageView addProduct = view.findViewById(R.id.Add_Product);

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Admin_Add_Product_Screen.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
