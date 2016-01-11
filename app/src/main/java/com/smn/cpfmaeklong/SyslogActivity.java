package com.smn.cpfmaeklong;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.smn.cpfmaeklong.R.layout.activity_column;

public class SyslogActivity extends AppCompatActivity {
    ListView lv;
    TextView dt;
    EditText keysch;
    Button sch, btBack, btNext;

    SyslogXML objs;
    ArrayList<HashMap<String, String>> MyArrList = new ArrayList<>();
    HashMap<String, String> map;



    Date date = new Date();
    Date chkdate = new Date();
    float oldXAxis = 0f;
    float oldYAxis = 0f;
    float newXAxis = 0f;
    float newYAxis = 0f;
    String BaseURL;
    int SelectedPond;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syslog);

        Intent intent = getIntent();
        BaseURL = intent.getStringExtra("BASE_URL");
        SelectedPond = intent.getIntExtra("SELECTED_POND", 0);

        chkdate.setDate(chkdate.getDate() + 1);

        keysch = (EditText) findViewById(R.id.tvsch);
        sch = (Button) findViewById(R.id.btsch);
        btBack = (Button) findViewById(R.id.btBack);
        btNext = (Button) findViewById(R.id.btNext);
        dt = (TextView) findViewById(R.id.date2);
        lv = (ListView) findViewById(R.id.listView);


        sch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterTextSet();
            }
        });

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date.setDate(date.getDate() - 1);
                //ShowGrid();
                updateSyslog();
            }
        });

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date.setDate(date.getDate() + 1);
                if (chkdate.equals(date)) {
                    date.setDate(date.getDate() - 1);
                }
                //
                updateSyslog();
            }
        });

        updateSyslog();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_grid, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateSyslog() {

        if (checkInternetConnection()==false) return;
        DownloadFromInternet Downloader = new DownloadFromInternet();
        Downloader.execute();
    }
    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Grid Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.smn.cpfmaeklong/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Grid Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.smn.cpfmaeklong/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    void filterTextSet() {
        final String[] time = new String[objs.getCountRecord()];
        final String[] data = new String[objs.getCountRecord()];
        int[] index = new int[1];

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<>();
        HashMap<String, String> map;
        for (int k = objs.getCountRecord() - 1; k >= 0; k--) {
            index[0] = data[k].indexOf(keysch.getText().toString());
            if (index[0] >= 0) {
                map = new HashMap<>();
                map.put("DATE", time[k].substring(11));
                map.put("DATA", data[k]);
                MyArrList.add(map);
            }
        }
        SimpleAdapter sAdap;
        sAdap = new SimpleAdapter(SyslogActivity.this, MyArrList, activity_column,
                new String[]{"DATE", "DATA"}, new int[]{R.id.date, R.id.data});
        lv.setAdapter(sAdap);

    }

    public boolean checkInternetConnection() {

        boolean have_connection = false;

        ConnectivityManager cManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();

        if(nInfo==null) {
            Toast.makeText(this , "No Internet connection! Please connect to the Internet.", Toast.LENGTH_LONG).show();
        } else {
            have_connection=true;
        }
        return have_connection;
    }

    private void lockScreenOrientation() {
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    private void unlockScreenOrientation() {
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    // Async Task Class
    class DownloadFromInternet extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        boolean cancel;

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method

            cancel = false;

            progressDialog = ProgressDialog.show(SyslogActivity.this,
                    "Downloading Data",
                    "Please Wait!");

            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    cancel = true;
                }
            });
            lockScreenOrientation();
            //Toast.makeText(getActivity(),"Progress Start",Toast.LENGTH_LONG).show();
        }

        // Download xml from Internet
        @Override
        protected String doInBackground(String... str) {
            int count=1;
            try {
                String url = BaseURL.replace("p=","syslog=");
                //SyslogXML objs;

                String today = new SimpleDateFormat("yyyy-MM-dd").format(date);
                String finalUrl = url + ",4096," + today + ",NodeDateTime"; //"2015-10-02"
                objs = new SyslogXML(finalUrl);
                objs.fetchXML();
                while (!objs.parsingComplete) {
                    Thread.sleep(1000);
                    publishProgress("" + count++);
                    if (cancel) break;
                }
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        // While Downloading Music File
        @Override
        protected void onProgressUpdate(String... progress) {
            // Set progress percentage
            progressDialog.setMessage("Please wait... "+String.valueOf(progress[0])+" sec");
        }

        // Once XML is downloaded
        @Override
        protected void onPostExecute(String file_url) {
            // Dismiss the dialog after the Xml file was downloaded
            //Toast.makeText(getActivity(),"Progress Ended",Toast.LENGTH_LONG).show();

            progressDialog.dismiss();

            if (cancel) {
                unlockScreenOrientation();
                return;
            }
            if (objs.getCountRecord() == 0) {
                Toast.makeText(SyslogActivity.this, "No Data in Syslog.", Toast.LENGTH_LONG).show();
            } else {
                final String[] time = new String[objs.getCountRecord()];
                final String[] data = new String[objs.getCountRecord()];

                for (int i = 0; i < objs.getCountRecord(); i++) {
                    time[i] = objs.NodeDateTime();
                    data[i] = objs.Message();
                }
                dt.setText(time[0].substring(0, 10));

                ArrayList<HashMap<String, String>> MyArrList = new ArrayList<>();
                HashMap<String, String> map;

                for (int j = objs.getCountRecord() - 1; j >= 0; j--) {
                    map = new HashMap<>();
                    map.put("DATE", time[j].substring(11));
                    map.put("DATA", data[j]);
                    MyArrList.add(map);
                }
                SimpleAdapter sAdap;
                sAdap = new SimpleAdapter(SyslogActivity.this, MyArrList, activity_column,
                        new String[]{"DATE", "DATA"}, new int[]{R.id.date, R.id.data});
                lv.setAdapter(sAdap);


            }
            unlockScreenOrientation();
        }
    }


}
