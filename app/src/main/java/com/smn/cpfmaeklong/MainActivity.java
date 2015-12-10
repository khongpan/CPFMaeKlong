package com.smn.cpfmaeklong;

import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Button bt,bt2,bt3,btUsableMotor;
    TextView tv;
    TextView[] m=new TextView[12+1];
    String url1,url2;
    String[] sMotorUrl= new String[12+1];
    private String chk;
    String url = "http://203.185.131.92/ws/get.php?appkey=0c5a295bd8c07a080b450069e3f2&p=POND-CONTROL,";
    HandleXML obj,obj2;
    HandleXML[] xmlMotor=new HandleXML[12+1];
    HandleXML xmlUsableMotorCount;

    private String[] strtype;
    private String typeChoosed;

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
        bt = (Button) findViewById(R.id.btnDOL);
        bt2 = (Button) findViewById(R.id.btnOMC);
        bt3 = (Button) findViewById(R.id.btnRefresh);
        btUsableMotor = (Button) findViewById(R.id.btUsableAerator);
        tv = (TextView) findViewById(R.id.txtDate);
        m[1] = (TextView) findViewById(R.id.btM1);
        m[2] = (TextView) findViewById(R.id.btM2);
        m[3] = (TextView) findViewById(R.id.btM3);
        m[4] = (TextView) findViewById(R.id.btM4);
        m[5] = (TextView) findViewById(R.id.btM5);
        m[6] = (TextView) findViewById(R.id.btM6);
        m[7] = (TextView) findViewById(R.id.btM7);
        m[8] = (TextView) findViewById(R.id.btM8);
        m[9]= (TextView) findViewById(R.id.btM9);
        m[10] = (TextView) findViewById(R.id.btM10);
        m[11] = (TextView) findViewById(R.id.btM11);
        m[12] = (TextView) findViewById(R.id.btM12);

        ConnectivityManager cManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();

        if(nInfo==null) {
            Toast.makeText(this, "No Internet connection! Please connect to the Internet.", Toast.LENGTH_LONG).show();
        }else {
            Spinner spntype = (Spinner) findViewById(R.id.spnPond);
            strtype = getResources().getStringArray(R.array.type);
            ArrayAdapter<String> objAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, strtype);
            spntype.setAdapter(objAdapter);
            spntype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                    typeChoosed = strtype[i];
                    if (typeChoosed.equals(strtype[0])) {
                        url1 = url + "4096,1505";
                        url2 = url + "4096,1506";
                        sMotorUrl[1] = url + "4096,1560";
                        sMotorUrl[2] = url + "4096,1564";
                        sMotorUrl[3] = url + "4096,1561";
                        sMotorUrl[4] = url + "4096,1565";
                        sMotorUrl[5] = url + "4096,1562";
                        sMotorUrl[6] = url + "4096,1566";
                        sMotorUrl[7] = url + "4096,1563";
                        sMotorUrl[8] = url + "4096,1567";
                        sMotorUrl[9] = url + "4096,1568";
                        sMotorUrl[10] = url + "4096,1569";
                        sMotorUrl[11] = url + "4096,1570";
                        sMotorUrl[12] = url + "4096,1571";
                    } else if (typeChoosed.equals(strtype[1])) {
                        url1 = url + "8192,100";
                        url2 = url + "8192,99";
                        for(i=1;i<12;i++)  sMotorUrl[i] = url + "8192,99";

                    }

                    obj = new HandleXML(url1);
                    obj.fetchXML();
                    obj2 = new HandleXML(url2);
                    obj2.fetchXML();

                    for (i = 1; i <= 12; i++) {
                        xmlMotor[i] = new HandleXML(sMotorUrl[i]);
                        xmlMotor[i].fetchXML();
                    }

                    xmlUsableMotorCount = new HandleXML(url + "4096,1507");
                    xmlUsableMotorCount.fetchXML();


                    while (!obj.parsingComplete) ;
                    while (!obj2.parsingComplete) ;

                    for (i = 1; i <= 12; i++) {
                        while (!xmlMotor[i].parsingComplete) ;
                    }

                    while (!xmlUsableMotorCount.parsingComplete) ;

                    bt.setText(obj.getLastValue());
                    bt2.setText(obj2.getLastValue());
                    tv.setText(obj.getLastIODateTime());

                    String str = bt.getText().toString();
                    float f = Float.parseFloat(str);
                    if (f < 1) {
                        f = f * 20;
                    }
                    str = String.format("%.2f", f);
                    bt.setText(str);

                    for (i = 1; i <= 12; i++) {
                        m[i].setText(String.valueOf(i));
                    }


                    btUsableMotor.setText(xmlUsableMotorCount.getLastValue());

                    chkmotor();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    typeChoosed = strtype[0];
                }
            });

            bt3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (typeChoosed.equals(strtype[0])) {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    } else {
                        url1 = url + "8192,100";
                        obj = new HandleXML(url1);
                        obj.fetchXML();
                        while (!obj.parsingComplete) ;

                        bt.setText(obj.getLastValue());
                        tv.setText(obj.getLastIODateTime());

                        String str = bt.getText().toString();
                        float f = Float.parseFloat(str);
                        if (f < 1) {
                            f = f * 20;
                        }
                        str = String.format("%.2f", f);
                        bt.setText(str);
                    }
                }
            });


            m[1].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowMotorState(1);
                }
            });
            m[2].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowMotorState(2);
                }
            });
            m[3].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowMotorState(3);
;
                }
            });
            m[4].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowMotorState(4);
                }
            });
            m[5].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowMotorState(5);
                }
            });
            m[6].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowMotorState(6);
                }
            });
            m[7].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowMotorState(7);
                }
            });
            m[8].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowMotorState(8);
                }
            });
            m[9].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowMotorState(9);
                }
            });
            m[10].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowMotorState(10);
                }
            });
            m[11].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowMotorState(11);
                }
            });
            m[12].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowMotorState(12);
                }
            });
        }
    }

    public void chkmotor(){

        for(int i=1;i<=12;i++) {
            if (xmlMotor[i].getLastValue().equals("3")) {
                m[i].setBackgroundColor(Color.argb(255, 0, 150, 0));
            } else if (xmlMotor[i].getLastValue().equals("5")) {
                if (xmlMotor[i].getNote().equals("must_off"))
                    m[i].setBackgroundColor(Color.argb(64,64,64,0));
                else
                    m[i].setBackgroundColor(Color.BLACK);
            } else {
                m[i].setBackgroundColor(Color.RED);
            }

        }

    }

    public void ShowMotorState(int mNo){

        String[] sStateMeaning = {
            "null","Unknow","Activate","On","Deactivate","Off","OverCurrent","UnderCurrent","InternalErr","CommError","LastState"
        };

        int state = Integer.parseInt(xmlMotor[mNo].getLastValue());

        Toast.makeText(this,"Motor"+ mNo + " " + sStateMeaning[state] + " S" + state , Toast.LENGTH_SHORT).show();




    }

    public void LaunchGraph(View view) {
        String EXTRA_MESSAGE = "com.smn.cpfmaeklong.MESSAGE";
        String message = "hello world \r\n";
        Intent intent = new Intent(this, GraphActivity.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void LaunchGrid(View view) {
        String EXTRA_MESSAGE = "com.smn.cpfmaeklong.MESSAGE";
        String message = "hello world \r\n";
        Intent intent = new Intent(this, GridActivity.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}
