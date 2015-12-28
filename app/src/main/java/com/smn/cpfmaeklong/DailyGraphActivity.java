package com.smn.cpfmaeklong;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;


public class DailyGraphActivity extends AppCompatActivity
        implements DailyGraphFragment.OnFragmentInteractionListener,
        DateNavigateFragment.OnDateNavigateFragmentInteractionListener {

    private String mBaseUrl;
    private int mSelectedPond;
    private String mGraphSeries;
    private String mCurrentDate;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
   // private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_graph);

        Intent intent = getIntent();
        mBaseUrl = intent.getStringExtra("BASE_URL");
        mSelectedPond = intent.getIntExtra("SELECTED_POND", 0);
        mGraphSeries = intent.getStringExtra("GRAPH_SERIES");


        // However, if we're being restored from a previous state,
        // then we don't need to do anything and should return or else
        // we could end up with overlapping fragments.
        if (savedInstanceState != null) {
            return;
        }

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.frDailyGraph) != null) {

            // Create a new Fragment to be placed in the activity layout
            DailyGraphFragment dailyGraphFragment = new DailyGraphFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            dailyGraphFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frDailyGraph, dailyGraphFragment).commit();
        }
        if (findViewById(R.id.frameDateNavigate) != null) {

            // Create a new Fragment to be placed in the activity layout
            DateNavigateFragment dateNavigateFragment = new DateNavigateFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            dateNavigateFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                   .add(R.id.frameDateNavigate, dateNavigateFragment).commit();
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onDateNavigateFragmentInteraction(String date) {
        DailyGraphFragment dailyGraphFragment = (DailyGraphFragment)
                getSupportFragmentManager().findFragmentById(R.id.frDailyGraph);

        mCurrentDate=date;

        if (dailyGraphFragment != null) {
            // If article frag is available, we're in two-pane layout...

            // Call a method in the ArticleFragment to update its content
            dailyGraphFragment.setDate(mCurrentDate);
            dailyGraphFragment.updateGraph();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        /*
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "DailyGraph Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.smn.cpfmaeklong/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
        */
    }

    @Override
    public void onStop() {
        super.onStop();
        /*

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "DailyGraph Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.smn.cpfmaeklong/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
        */
    }
}
