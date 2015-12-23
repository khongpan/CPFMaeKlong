package com.smn.cpfmaeklong;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DailyGraphActivity extends AppCompatActivity
        implements DailyGraphFragment.OnFragmentInteractionListener{
    private String mBaseUrl;
    private int mSelectedPond;
    private String mGraphSeries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_graph);

        Intent intent = getIntent();
        mBaseUrl = intent.getStringExtra("BASE_URL");
        mSelectedPond = intent.getIntExtra("SELECTED_POND", 0);
        mGraphSeries = intent.getStringExtra("GRAPH_SERIES");

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.frDailyGraph) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            DailyGraphFragment dailyGraphFragment = new DailyGraphFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            dailyGraphFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frDailyGraph, dailyGraphFragment).commit();
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
