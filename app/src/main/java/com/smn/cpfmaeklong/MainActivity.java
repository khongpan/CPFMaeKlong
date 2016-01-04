package com.smn.cpfmaeklong;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button mBtnDoLevel, mBtnOnMotorCount, mBtnRefresh, mBtnUsableMotor;
    TextView tv;
    TextView[] mAerator =new TextView[12+1];
    String url1,url2;
    String[] sMotorUrl= new String[12+1];
    String sAvlMotorUrl;
    private String chk;
    String[] BaseURL = {"http://203.185.131.92/ws/get.php?appkey=0c5a295bd8c07a080b450069e3f2&p=POND-CONTROL",
                        "http://203.185.131.92/ws/get.php?appkey=0c5a295bd8c07a080b450069e3f2&p=POND-CONTROL",
                        "http://203.185.131.92/ws/get.php?appkey=0c5a295bd8c07a080b450069e3f2&p=JRD-19",
                        "http://203.185.131.92/ws/get.php?appkey=0c5a295bd8c07a080b450069e3f2&p=TEST-POND-CONTROL-1",
                        "http://203.185.131.92/ws/get.php?appkey=0c5a295bd8c07a080b450069e3f2&p=TEST-POND-CONTROL-2",
                        "http://203.185.131.92/ws/get.php?appkey=0c5a295bd8c07a080b450069e3f2&p=TEST-POND-CONTROL-3",
                        "http://203.185.131.92/ws/get.php?appkey=0c5a295bd8c07a080b450069e3f2&p=TEST-POND-CONTROL-4",};
    SensorInfoXML xmlDoLevel, xmlOnMotorCount;
    SensorInfoXML[] xmlMotor=new SensorInfoXML[12+1];
    SensorInfoXML xmlUsableMotorCount;

    private String[] strPondsName;
    int SelectedPond;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StartApp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private void StartApp() {
        mBtnDoLevel = (Button) findViewById(R.id.btnDOL);
        mBtnOnMotorCount = (Button) findViewById(R.id.btnOMC);
        mBtnRefresh = (Button) findViewById(R.id.btnRefresh);
        mBtnUsableMotor = (Button) findViewById(R.id.btUsableAerator);
        tv = (TextView) findViewById(R.id.txtDate);
        mAerator[1] = (TextView) findViewById(R.id.btM1);
        mAerator[2] = (TextView) findViewById(R.id.btM2);
        mAerator[3] = (TextView) findViewById(R.id.btM3);
        mAerator[4] = (TextView) findViewById(R.id.btM4);
        mAerator[5] = (TextView) findViewById(R.id.btM5);
        mAerator[6] = (TextView) findViewById(R.id.btM6);
        mAerator[7] = (TextView) findViewById(R.id.btM7);
        mAerator[8] = (TextView) findViewById(R.id.btM8);
        mAerator[9] = (TextView) findViewById(R.id.btM9);
        mAerator[10] = (TextView) findViewById(R.id.btM10);
        mAerator[11] = (TextView) findViewById(R.id.btM11);
        mAerator[12] = (TextView) findViewById(R.id.btM12);

        //ConnectivityManager cManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        //NetworkInfo nInfo = cManager.getActiveNetworkInfo();

        //if(nInfo==null) {
        //   Toast.makeText(this, "No Internet connection! Please connect to the Internet.", Toast.LENGTH_LONG).show();
        //}else
        {
            Spinner spntype = (Spinner) findViewById(R.id.spnPond);
            strPondsName = getResources().getStringArray(R.array.type);
            ArrayAdapter<String> objAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, strPondsName);
            spntype.setAdapter(objAdapter);
            spntype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    int i;

                    url1 = BaseURL[pos] + ",4096,1505";
                    url2 = BaseURL[pos] + ",4096,1506";
                    sAvlMotorUrl = BaseURL[pos] + ",4096,1507";

                    SelectedPond = pos;

                    if (pos == 0 ) {

                        sMotorUrl[1] = BaseURL[pos] + ",4096,1560";
                        sMotorUrl[2] = BaseURL[pos] + ",4096,1564";
                        sMotorUrl[3] = BaseURL[pos] + ",4096,1561";
                        sMotorUrl[4] = BaseURL[pos] + ",4096,1565";
                        sMotorUrl[5] = BaseURL[pos] + ",4096,1562";
                        sMotorUrl[6] = BaseURL[pos] + ",4096,1566";
                        sMotorUrl[7] = BaseURL[pos] + ",4096,1563";
                        sMotorUrl[8] = BaseURL[pos] + ",4096,1567";
                        sMotorUrl[9] = BaseURL[pos] + ",4096,1568";
                        sMotorUrl[10] = BaseURL[pos] + ",4096,1569";
                        sMotorUrl[11] = BaseURL[pos] + ",4096,1570";
                        sMotorUrl[12] = BaseURL[pos] + ",4096,1571";
                    } else if (pos==1) {
                        url1 = BaseURL[pos] + ",8192,100";
                        url2 = BaseURL[pos] + ",4096,1560";
                        for (i = 1; i < 12; i++) sMotorUrl[i] = BaseURL[pos] + ",4096,1560";
                    } else {
                        int ionumber= 1560;

                        for (i=1;i<=12;i++){
                            sMotorUrl[i] = BaseURL[pos] +",4096,"+ String.valueOf(ionumber);
                            ionumber++;
                        }
                    }
                    updateView();

                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    //SelectedPond=0;
                }
            });

            mBtnRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateView();
                }
            });

            mBtnDoLevel.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    launchDailyGraph(v);
                    return true;
                }
            });

            mBtnUsableMotor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    launchAeratorGraph(v);
                }


        });


            mAerator[1].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowMotorState(1);
                }
            });
            mAerator[2].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowMotorState(2);
                }
            });
            mAerator[3].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowMotorState(3);
                }
            });
            mAerator[4].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowMotorState(4);
                }
            });
            mAerator[5].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowMotorState(5);
                }
            });
            mAerator[6].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowMotorState(6);
                }
            });
            mAerator[7].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowMotorState(7);
                }
            });
            mAerator[8].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowMotorState(8);
                }
            });
            mAerator[9].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowMotorState(9);
                }
            });
            mAerator[10].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowMotorState(10);
                }
            });
            mAerator[11].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowMotorState(11);
                }
            });
            mAerator[12].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowMotorState(12);
                }
            });
        }
    }

    public void DisplayMotorStatus(){

        for(int i=1;i<=12;i++) {

            if(xmlMotor[i].getLastValue()==null){
                mAerator[i].setBackgroundColor(Color.WHITE);
            }else if (xmlMotor[i].getLastValue().equals("3")) {
                mAerator[i].setBackgroundColor(Color.argb(255, 0, 150, 0));
            } else if (xmlMotor[i].getLastValue().equals("5")) {
                if (xmlMotor[i].getDetails().equals("must_off"))
                    mAerator[i].setBackgroundColor(Color.argb(64,64,64,0));
                else
                    mAerator[i].setBackgroundColor(Color.BLACK);
            } else if (xmlMotor[i].getLastValue().equals("-")) {
                mAerator[i].setBackgroundColor(Color.WHITE);
            } else {
                mAerator[i].setBackgroundColor(Color.RED);
            }



        }

    }

    public void ShowMotorState(int mNo){

        String[] sStateMeaning = {
            "null","Unknow","Activate","On","Deactivate","Off",
            "OverCurrent","UnderCurrent","InternalErr","CommError","LastState"
        };

        if (xmlMotor[mNo]==null) return;

        if (xmlMotor[mNo].getLastValue().equals("-")) return;

        int state = Integer.parseInt(xmlMotor[mNo].getLastValue());

        Toast.makeText(this,"Motor"+ mNo + " " + sStateMeaning[state] + " S" + state , Toast.LENGTH_SHORT).show();

    }

    void updateView() {

        if (checkInternetConnection()==true) {

            DownloadFromInternet downloader = new DownloadFromInternet();
            downloader.execute();
        }
    }

    public void LaunchGraph(View view) {
        //String EXTRA_MESSAGE = "com.smn.cpfmaeklong.MESSAGE";
        String message = BaseURL[SelectedPond];
        Intent intent = new Intent(this, DailyGraphActivity.class);
        intent.putExtra("BASE_URL", message);
        intent.putExtra("SELECTED_POND", SelectedPond);
        intent.putExtra("GRAPH_GROUP" , "DO_LEVEL");
        startActivity(intent);
    }

    public void LaunchGrid(View view) {
        //String EXTRA_MESSAGE = "com.smn.cpfmaeklong.MESSAGE";
        String message = BaseURL[SelectedPond];
        Intent intent = new Intent(this, SyslogActivity.class);
        intent.putExtra("BASE_URL", message);
        intent.putExtra("SELECTED_POND", SelectedPond);
        startActivity(intent);
    }

    public void launchDailyGraph(View view) {
        String message = BaseURL[SelectedPond];
        String graphGroup = new String("DO_PROBE");

        Intent intent = new Intent(this, DailyGraphActivity.class);
        intent.putExtra("BASE_URL", message);
        intent.putExtra("SELECTED_POND", SelectedPond);
        intent.putExtra("GRAPH_GROUP" , new String("DO_PROBE"));
        startActivity(intent);
    }

    public void launchAeratorGraph(View view) {
        String message = BaseURL[SelectedPond];
        String graphGroup = new String("DO_PROBE");

        Intent intent = new Intent(this, DailyGraphActivity.class);
        intent.putExtra("BASE_URL", message);
        intent.putExtra("SELECTED_POND", SelectedPond);
        intent.putExtra("GRAPH_GROUP" , new String("AERATOR_STATUS"));
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putBoolean("MyBoolean", true);
        savedInstanceState.putDouble("myDouble", 1.9);
        savedInstanceState.putInt("MyInt", 1);
        savedInstanceState.putString("MyString", "Welcome back to Android");
        savedInstanceState.putInt("SelectedPond", SelectedPond);
        // etc.
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        boolean myBoolean = savedInstanceState.getBoolean("MyBoolean");
        double myDouble = savedInstanceState.getDouble("myDouble");
        int myInt = savedInstanceState.getInt("MyInt");
        String myString = savedInstanceState.getString("MyString");
        SelectedPond = savedInstanceState.getInt("SelectedPond");
        if (SelectedPond>=BaseURL.length)
            SelectedPond=0;
    }

    public boolean checkInternetConnection() {

        boolean have_connection = false;

        ConnectivityManager cManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();

        if(nInfo==null) {
            Toast.makeText(this, "No Internet connection! Please connect to the Internet.", Toast.LENGTH_LONG).show();
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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
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

            progressDialog = ProgressDialog.show(MainActivity.this,
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
                xmlDoLevel = new SensorInfoXML(url1);
                xmlDoLevel.fetchXML();
                xmlOnMotorCount = new SensorInfoXML(url2);
                xmlOnMotorCount.fetchXML();

                for (int i = 1; i <= 12; i++) {
                    xmlMotor[i] = new SensorInfoXML(sMotorUrl[i]);
                    xmlMotor[i].fetchXML();
                }

                xmlUsableMotorCount = new SensorInfoXML(sAvlMotorUrl);
                xmlUsableMotorCount.fetchXML();


                while (!xmlDoLevel.isFetchComplete()) {
                    Thread.sleep(1000);
                    publishProgress("" + count++);
                }
                while (!xmlOnMotorCount.isFetchComplete()) ;
                for (int i = 1; i <= 12; i++) {
                    while (!xmlMotor[i].isFetchComplete()) ;
                }
                while (!xmlUsableMotorCount.isFetchComplete()) ;
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
            // Play the music
            //updateSeriesData();
            mBtnDoLevel.setText(xmlDoLevel.getLastValue());
            mBtnOnMotorCount.setText(xmlOnMotorCount.getLastValue());
            tv.setText(xmlDoLevel.getIoDateTime());

            String str = mBtnDoLevel.getText().toString();
            float f = Float.parseFloat(str);
            if (f < 1) {
                f = f * 20;
            }
            str = String.format("%.2f", f);
            mBtnDoLevel.setText(str);

            for (int i = 1; i <= 12; i++) {
                mAerator[i].setText(String.valueOf(i));
            }


            mBtnUsableMotor.setText(xmlUsableMotorCount.getLastValue());

            DisplayMotorStatus();

            unlockScreenOrientation();

        }
    }

}

