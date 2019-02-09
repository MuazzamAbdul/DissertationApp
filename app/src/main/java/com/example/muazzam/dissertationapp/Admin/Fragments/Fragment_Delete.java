package com.example.muazzam.dissertationapp.Admin.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.muazzam.dissertationapp.Admin.Admin_Delete_Supermarket_Screen;
import com.example.muazzam.dissertationapp.R;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Delete extends Fragment {


    public Fragment_Delete() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment__delete, container, false);
        CircleImageView deleteProduct = view.findViewById(R.id.Delete_Product);
        CircleImageView deleteSupermarket = view.findViewById(R.id.Delete_Supermarket);

        deleteSupermarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Admin_Delete_Supermarket_Screen.class);
                startActivity(intent);
            }
        });

//        deleteProduct.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(),Admin_Add_Product_Screen.class);
//                startActivity(intent);
//            }
//        });

        return view;
    }

}
