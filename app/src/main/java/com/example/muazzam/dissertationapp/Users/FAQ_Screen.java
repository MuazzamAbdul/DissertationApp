package com.example.muazzam.dissertationapp.Users;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.example.muazzam.dissertationapp.R;

/**
 * Class containing FAQs.
 */
public class FAQ_Screen extends AppCompatActivity {

    /**
     * Create activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq__screen);

        setupUIViews();
    }

    /**
     * Create UI Views.
     */
    private void setupUIViews()
    {
        Toolbar toolbar = findViewById(R.id.toolbarFaq);
        setSupportActionBar(toolbar);
        toolbar.setTitle("FAQ");

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_action_arrow_back);
    }

    /**
     * Return to previous activity when back arrow is pressed.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
