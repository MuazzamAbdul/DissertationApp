package com.example.muazzam.dissertationapp.Users;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.example.muazzam.dissertationapp.R;

public class Splash_Screen extends AppCompatActivity {

    Button getstarted;
    TextView splashtxt;
    CardView logo;
    Animation frombottom,fromtop,fadein;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);

        setupUIViews();

        frombottom = AnimationUtils.loadAnimation(this,R.anim.frombottom);
        fromtop = AnimationUtils.loadAnimation(this,R.anim.fromtop);
        fadein = AnimationUtils.loadAnimation(this,R.anim.fade_in);

        logo.setAnimation(fromtop);
        getstarted.setAnimation(frombottom);
        splashtxt.setAnimation(fadein);

        getstarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Splash_Screen.this,Login_Screen.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void setupUIViews()
    {
        getstarted = findViewById(R.id.btnGetStarted);
        logo = findViewById(R.id.splashLogo);
        splashtxt = findViewById(R.id.tvSplash);
    }
}
