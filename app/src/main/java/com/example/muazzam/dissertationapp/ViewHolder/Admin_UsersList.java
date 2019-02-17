package com.example.muazzam.dissertationapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.muazzam.dissertationapp.Interface.ItemClickListener;
import com.example.muazzam.dissertationapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class Admin_UsersList extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtname,txtemail,txtaddress,txtphone;
    public CircleImageView userpic;
    public ItemClickListener listener;

    public Admin_UsersList(@NonNull View itemView) {
        super(itemView);

        userpic = itemView.findViewById(R.id.User_Pic);
        txtname = itemView.findViewById(R.id.user_name);
        txtemail = itemView.findViewById(R.id.user_email);
        txtaddress = itemView.findViewById(R.id.user_address);
        txtphone = itemView.findViewById(R.id.user_phoneNo);
    }

    public void setItemClickListener(ItemClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {

        listener.OnClick(v,getAdapterPosition(),false);

    }
}
