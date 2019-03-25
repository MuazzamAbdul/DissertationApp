package com.example.muazzam.dissertationapp.Admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muazzam.dissertationapp.Prevalent.Prevalent;
import com.example.muazzam.dissertationapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Admin_Edit_ProductSuper_Screen extends AppCompatActivity {

    private CircleImageView prodPic;
    private TextView prodName,superName;
    private EditText qty,etprice;
    private Button done;
    private String superProdId,productName,quantity,price;
    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__edit__product_super__screen);

        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        Bundle getID = getIntent().getExtras();
        if (getID == null)
        {
            return;
        }
        else
        {
            superProdId = getID.getString("ID");
            productName = getID.getString("Name");
        }

        setupUIViews();
        retrieveProductPic();
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateTextFields())
                {
                    AlertDialog.Builder exit = new AlertDialog.Builder(Admin_Edit_ProductSuper_Screen.this,R.style.AdminDialogAlert);
                    exit.setMessage("Do you want to Update Product?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    saveProductDetails();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = exit.create();
                    alert.setTitle("Warning");
                    alert.show();
                    saveProductDetails();
                }
            }
        });


    }

    private void setupUIViews()
    {
        Toolbar toolbar = findViewById(R.id.toolbarEditProduct);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Edit Products");

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_action_black_arrow_back);

        prodPic = findViewById(R.id.EditproductPic);
        prodName = findViewById(R.id.tvEditProductName);
        superName = findViewById(R.id.tvEditSuperName);
        qty = findViewById(R.id.etEditProductQty);
        etprice = findViewById(R.id.etEditProductPrice);
        done = findViewById(R.id.btnEditProduct);

        prodName.setText(productName);
        superName.setText(Prevalent.supermarkets.getName());


    }

    private boolean validateTextFields()
    {
        boolean result = false;

        quantity = qty.getText().toString();
        price = etprice.getText().toString();

        if (TextUtils.isEmpty(quantity))
        {
            qty.setError("Please enter Quantity");
        }
        else if (TextUtils.isEmpty(price))
        {
            etprice.setError("Please enter Price");
        }
        else
        {
            result = true;
        }

        return result;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                Intent intent = new Intent(Admin_Edit_ProductSuper_Screen.this,Admin_Edit_Supermarket_Screen.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        Intent intent = new Intent(Admin_Edit_ProductSuper_Screen.this,Admin_Edit_Supermarket_Screen.class);
        startActivity(intent);
    }

    private void retrieveProductPic()
    {
        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child("Products").child(superProdId).child("Images").child("Product Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Picasso.get().load(uri).fit().centerCrop().into(prodPic);
            }
        });
    }

    private void saveProductDetails()
    {
        final DatabaseReference mDb = firebaseDatabase.getReference();

        mDb.child("Supermarkets_Products").child(Prevalent.supermarkets.getID() + "-" + superProdId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                HashMap<String,Object> userDataMap = new HashMap<>();

                userDataMap.put("Quantity",quantity);
                userDataMap.put("Price",price);

                mDb.child("Supermarkets_Products").child(Prevalent.supermarkets.getID() + "-" + superProdId).updateChildren(userDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful())
                        {
                            finish();
                            Toast.makeText(Admin_Edit_ProductSuper_Screen.this,"Product Updated",Toast.LENGTH_SHORT).show();
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
