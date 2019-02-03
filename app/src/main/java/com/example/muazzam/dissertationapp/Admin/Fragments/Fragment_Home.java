package com.example.muazzam.dissertationapp.Admin.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.muazzam.dissertationapp.R;
import com.example.muazzam.dissertationapp.Users.Category_Screen;

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
        View v = inflater.inflate(R.layout.fragment_fragment__home, container, false);
        CircleImageView user = v.findViewById(R.id.reg_Users);

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Category_Screen.class);
                startActivity(intent);
            }
        });

        return v;
    }

}
