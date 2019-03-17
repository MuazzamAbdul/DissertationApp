package com.example.muazzam.dissertationapp.Users;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muazzam.dissertationapp.Adapter.PageAdapter;
import com.example.muazzam.dissertationapp.Model.Users;
import com.example.muazzam.dissertationapp.Prevalent.Prevalent;
import com.example.muazzam.dissertationapp.R;
import com.example.muazzam.dissertationapp.Shopping_Cart_Screen;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home_Screen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView usernameText;
    private TextView usernameEmail;
    private DrawerLayout drawer;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private CircleImageView imagePic;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PageAdapter pageAdapter;
    private TabItem tabCat,tabProd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__screen);
        setupUIViews();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        pageAdapter = new PageAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));





        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        usernameText= headerView.findViewById(R.id.tvnavbar_name);
        usernameEmail = headerView.findViewById(R.id.tvnavbar_email);
        imagePic = headerView.findViewById(R.id.reg_Users);


    }

    private void setupUIViews()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Home");

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_action_menu);

        drawer = findViewById(R.id.drawer_layout);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.tabViewPager);
        tabCat = findViewById(R.id.tabCategory);
        tabProd = findViewById(R.id.tabProducts);

    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home__screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;

            case R.id.itshopping_cart:
                Intent intent = new Intent(this,Shopping_Cart_Screen.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.my_account) {

            Intent intent = new Intent(Home_Screen.this, My_Account_Screen.class);
            startActivity(intent);
        }else if (id == R.id.faq) {

            Intent intent = new Intent(Home_Screen.this, FAQ_Screen.class);
            startActivity(intent);
        } else if (id ==R.id.reviews) {

            Intent intent = new Intent(Home_Screen.this, Reviews_Screen.class);
            startActivity(intent);
        } else if (id == R.id.about_us) {

            Intent intent = new Intent(Home_Screen.this, About_Us_Screen.class);
            startActivity(intent);
        } else if (id == R.id.settings) {
            Intent intent = new Intent(Home_Screen.this, Settings_Screen.class);
            startActivity(intent);
        } else if (id == R.id.sign_out) {
            firebaseAuth.signOut();
            finish();
            Intent intent = new Intent(Home_Screen.this, Login_Screen.class);
            startActivity(intent);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference mDb = firebaseDatabase.getReference();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String userAuthKey = user.getUid();
        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child("Users").child(userAuthKey).child("Images").child("Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Picasso.get().load(uri).fit().centerCrop().into(imagePic);
            }
        });

        mDb.child("Users").child(userAuthKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String userEmail = String.valueOf(dataSnapshot.child("Email").getValue());
                String userName = String.valueOf(dataSnapshot.child("Name").getValue());
                String userPhone = String.valueOf(dataSnapshot.child("PhoneNumber").getValue());
                String userAddress = String.valueOf(dataSnapshot.child("Address").getValue());

                Users userData = new Users(userName,userEmail,userAddress,userPhone);
                Prevalent.onlineUser = userData;
                usernameText.setText(Prevalent.onlineUser.getName());
                usernameEmail.setText(Prevalent.onlineUser.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Home_Screen.this,"Failure Retrieving data from Database",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
