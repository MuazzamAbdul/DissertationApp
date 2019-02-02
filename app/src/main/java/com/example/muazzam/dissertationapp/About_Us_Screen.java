package com.example.muazzam.dissertationapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class About_Us_Screen extends AppCompatActivity {

    private TextView email;
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about__us__screen);
        setupUIViews();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
    }

    private void setupUIViews()
    {
        Toolbar toolbar = findViewById(R.id.toolbarAboutUs);
        setSupportActionBar(toolbar);
        toolbar.setTitle("About Us");

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_action_arrow_back);

        email = findViewById(R.id.tvDevEmail);
        send = findViewById(R.id.btnSendEmail);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                Intent intent = new Intent(About_Us_Screen.this,Home_Screen.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendEmail()
    {
        String recipient = "muazzamabdul@gmail.com";

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL,new String[] {recipient});
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent,"Choose an email client"));
        Toast.makeText(About_Us_Screen.this,"Email sent",Toast.LENGTH_SHORT).show();
    }
}
