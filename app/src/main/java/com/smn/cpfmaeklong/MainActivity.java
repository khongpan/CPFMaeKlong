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
    Date date = new Date();
    Button bt,bt2,bt3;
    TextView tv,m1,m2,m3,m4,m5,m6,m7,m8,m9,m10,m11,m12;
    String url1,url2,url3,url4,url5,url6,url7,url8,url9,url10,url11,url12,url13,url14;
    private String chk,mname;
    String url = "http://203.185.131.92/ws/get.php?appkey=0c5a295bd8c07a080b450069e3f2&p=POND-CONTROL,";
    HandleXML obj,obj2,obj3,obj4,obj5,obj6,obj7,obj8,obj9,obj10,obj11,obj12,obj13,obj14;

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
        tv = (TextView) findViewById(R.id.txtDate);
        m1 = (TextView) findViewById(R.id.btM1);
        m2 = (TextView) findViewById(R.id.btM2);
        m3 = (TextView) findViewById(R.id.btM3);
        m4 = (TextView) findViewById(R.id.btM4);
        m5 = (TextView) findViewById(R.id.btM5);
        m6 = (TextView) findViewById(R.id.btM6);
        m7 = (TextView) findViewById(R.id.btM7);
        m8 = (TextView) findViewById(R.id.btM8);
        m9 = (TextView) findViewById(R.id.btM9);
        m10 = (TextView) findViewById(R.id.btM10);
        m11 = (TextView) findViewById(R.id.btM11);
        m12 = (TextView) findViewById(R.id.btM12);

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
                        url3 = url + "4096,1560";
                        url4 = url + "4096,1564";
                        url5 = url + "4096,1561";
                        url6 = url + "4096,1565";
                        url7 = url + "4096,1562";
                        url8 = url + "4096,1566";
                        url9 = url + "4096,1563";
                        url10 = url + "4096,1567";
                        url11 = url + "4096,1568";
                        url12 = url + "4096,1569";
                        url13 = url + "4096,1570";
                        url14 = url + "4096,1571";
                    } else if (typeChoosed.equals(strtype[1])) {
                        url1 = url + "8192,100";
                        url2 = url + "8192,99";
                        url3 = url + "8192,99";
                        url4 = url + "8192,99";
                        url5 = url + "8192,99";
                        url6 = url + "8192,99";
                        url7 = url + "8192,99";
                        url8 = url + "8192,99";
                        url9 = url + "8192,99";
                        url10 = url + "8192,99";
                        url11 = url + "8192,99";
                        url12 = url + "8192,99";
                        url13 = url + "8192,99";
                        url14 = url + "8192,99";
                    }

                    obj = new HandleXML(url1);
                    obj.fetchXML();
                    obj2 = new HandleXML(url2);
                    obj2.fetchXML();
                    obj3 = new HandleXML(url3);
                    obj3.fetchXML();
                    obj4 = new HandleXML(url4);
                    obj4.fetchXML();
                    obj5 = new HandleXML(url5);
                    obj5.fetchXML();
                    obj6 = new HandleXML(url6);
                    obj6.fetchXML();
                    obj7 = new HandleXML(url7);
                    obj7.fetchXML();
                    obj8 = new HandleXML(url8);
                    obj8.fetchXML();
                    obj9 = new HandleXML(url9);
                    obj9.fetchXML();
                    obj10 = new HandleXML(url10);
                    obj10.fetchXML();
                    obj11 = new HandleXML(url11);
                    obj11.fetchXML();
                    obj12 = new HandleXML(url12);
                    obj12.fetchXML();
                    obj13 = new HandleXML(url13);
                    obj13.fetchXML();
                    obj14 = new HandleXML(url14);
                    obj14.fetchXML();


                    while (obj.parsingComplete) ;
                    while (obj2.parsingComplete) ;
                    while (obj3.parsingComplete) ;
                    while (obj4.parsingComplete) ;
                    while (obj5.parsingComplete) ;
                    while (obj6.parsingComplete) ;
                    while (obj7.parsingComplete) ;
                    while (obj8.parsingComplete) ;
                    while (obj9.parsingComplete) ;
                    while (obj10.parsingComplete) ;
                    while (obj11.parsingComplete) ;
                    while (obj12.parsingComplete) ;
                    while (obj13.parsingComplete) ;
                    while (obj14.parsingComplete) ;
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

                    m1.setText(obj3.getLastValue());
                    m2.setText(obj4.getLastValue());
                    m3.setText(obj5.getLastValue());
                    m4.setText(obj6.getLastValue());
                    m5.setText(obj7.getLastValue());
                    m6.setText(obj8.getLastValue());
                    m7.setText(obj9.getLastValue());
                    m8.setText(obj10.getLastValue());
                    m9.setText(obj11.getLastValue());
                    m10.setText(obj12.getLastValue());
                    m11.setText(obj13.getLastValue());
                    m12.setText(obj14.getLastValue());

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
                        while (obj.parsingComplete) ;

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

            m1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chk = obj3.getLastValue();
                    mname = "Motor 1";
                    chktoast();
                }
            });
            m2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chk = obj4.getLastValue();
                    mname = "Motor 2";
                    chktoast();
                }
            });
            m3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chk = obj5.getLastValue();
                    mname = "Motor 3";
                    chktoast();
                }
            });
            m4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chk = obj6.getLastValue();
                    mname = "Motor 4";
                    chktoast();
                }
            });
            m5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chk = obj7.getLastValue();
                    mname = "Motor 5";
                    chktoast();
                }
            });
            m6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chk = obj8.getLastValue();
                    mname = "Motor 6";
                    chktoast();
                }
            });
            m7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chk = obj9.getLastValue();
                    mname = "Motor 7";
                    chktoast();
                }
            });
            m8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chk = obj10.getLastValue();
                    mname = "Motor 8";
                    chktoast();
                }
            });
            m9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chk = obj11.getLastValue();
                    mname = "Motor 9";
                    chktoast();
                }
            });
            m10.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chk = obj12.getLastValue();
                    mname = "Motor 10";
                    chktoast();
                }
            });
            m11.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chk = obj13.getLastValue();
                    mname = "Motor 11";
                    chktoast();
                }
            });
            m12.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chk = obj14.getLastValue();
                    mname = "Motor 12";
                    chktoast();
                }
            });
        }
    }

    public void chkmotor(){
        if (m1.getText().equals("3")) {
            m1.setBackgroundColor(Color.argb(255, 0, 150, 0));
            m1.setText("1");
        } else if (m1.getText().equals("5")) {
            m1.setBackgroundColor(Color.BLACK);
            m1.setText("1");
        } else {
            m1.setBackgroundColor(Color.RED);
            m1.setText("1");
        }

        if (m2.getText().equals("3")) {
            m2.setBackgroundColor(Color.argb(255, 0, 150, 0));
            m2.setText("2");
        } else if (m2.getText().equals("5")) {
            m2.setBackgroundColor(Color.BLACK);
            m2.setText("2");
        } else {
            m2.setBackgroundColor(Color.RED);
            m2.setText("2");
        }

        if (m3.getText().equals("3")) {
            m3.setBackgroundColor(Color.argb(255, 0, 150, 0));
            m3.setText("3");
        } else if (m3.getText().equals("5")) {
            m3.setBackgroundColor(Color.BLACK);
            m3.setText("3");
        } else {
            m3.setBackgroundColor(Color.RED);
            m3.setText("3");
        }

        if (m4.getText().equals("3")) {
            m4.setBackgroundColor(Color.argb(255, 0, 150, 0));
            m4.setText("4");
        } else if (m4.getText().equals("5")) {
            m4.setBackgroundColor(Color.BLACK);
            m4.setText("4");
        } else {
            m4.setBackgroundColor(Color.RED);
            m4.setText("4");
        }

        if (m5.getText().equals("3")) {
            m5.setBackgroundColor(Color.argb(255, 0, 150, 0));
            m5.setText("5");
        } else if (m5.getText().equals("5")) {
            m5.setBackgroundColor(Color.BLACK);
            m5.setText("5");
        } else {
            m5.setBackgroundColor(Color.RED);
            m5.setText("5");
        }

        if (m6.getText().equals("3")) {
            m6.setBackgroundColor(Color.argb(255, 0, 150, 0));
            m6.setText("6");
        } else if (m6.getText().equals("5")) {
            m6.setBackgroundColor(Color.BLACK);
            m6.setText("6");
        } else {
            m6.setBackgroundColor(Color.RED);
            m6.setText("6");
        }

        if (m7.getText().equals("3")) {
            m7.setBackgroundColor(Color.argb(255, 0, 150, 0));
            m7.setText("7");
        } else if (m7.getText().equals("5")) {
            m7.setBackgroundColor(Color.BLACK);
            m7.setText("7");
        } else {
            m7.setBackgroundColor(Color.RED);
            m7.setText("7");
        }

        if (m8.getText().equals("3")) {
            m8.setBackgroundColor(Color.argb(255, 0, 150, 0));
            m8.setText("8");
        } else if (m8.getText().equals("5")) {
            m8.setBackgroundColor(Color.BLACK);
            m8.setText("8");
        } else {
            m8.setBackgroundColor(Color.RED);
            m8.setText("8");
        }

        if (m9.getText().equals("3")) {
            m9.setBackgroundColor(Color.argb(255, 0, 150, 0));
            m9.setText("9");
        } else if (m9.getText().equals("5")) {
            m9.setBackgroundColor(Color.BLACK);
            m9.setText("9");
        } else {
            m9.setBackgroundColor(Color.RED);
            m9.setText("9");
        }

        if (m10.getText().equals("3")) {
            m10.setBackgroundColor(Color.argb(255, 0, 150, 0));
            m10.setText("10");
        } else if (m10.getText().equals("5")) {
            m10.setBackgroundColor(Color.BLACK);
            m10.setText("10");
        } else {
            m10.setBackgroundColor(Color.RED);
            m10.setText("10");
        }

        if (m11.getText().equals("3")) {
            m11.setBackgroundColor(Color.argb(255, 0, 150, 0));
            m11.setText("11");
        } else if (m11.getText().equals("5")) {
            m11.setBackgroundColor(Color.BLACK);
            m11.setText("11");
        } else {
            m11.setBackgroundColor(Color.RED);
            m11.setText("11");
        }

        if (m12.getText().equals("3")) {
            m12.setBackgroundColor(Color.argb(255, 0, 150, 0));
            m12.setText("12");
        } else if (m12.getText().equals("5")) {
            m12.setBackgroundColor(Color.BLACK);
            m12.setText("12");
        } else {
            m12.setBackgroundColor(Color.RED);
            m12.setText("12");
        }
    }

    public void chktoast(){
        if(chk == null) {
            Toast.makeText(this,mname +  " State 0 Null", Toast.LENGTH_LONG).show();
        }else if(chk.equals("1")) {
            Toast.makeText(this,mname +  " State 1 Unknown", Toast.LENGTH_LONG).show();
        }else if(chk.equals("2")) {
            Toast.makeText(this,mname +  " State 2 Activate", Toast.LENGTH_LONG).show();
        }else if(chk.equals("3")) {
            Toast.makeText(this,mname +  " State 3 On", Toast.LENGTH_LONG).show();
        }else if(chk.equals("4")) {
            Toast.makeText(this,mname +  " State 4 Deactivate", Toast.LENGTH_LONG).show();
        }else if(chk.equals("5")) {
            Toast.makeText(this,mname +  " State 5 Off", Toast.LENGTH_LONG).show();
        }else if(chk.equals("6")) {
            Toast.makeText(this,mname +  " State 6 OverCurrent", Toast.LENGTH_LONG).show();
        }else if(chk.equals("7")) {
            Toast.makeText(this,mname +  " State 7 UnderCurrent", Toast.LENGTH_LONG).show();
        }else if(chk.equals("8")) {
            Toast.makeText(this,mname +  " State 8 InternalError", Toast.LENGTH_LONG).show();
        }else if(chk.equals("9")) {
            Toast.makeText(this,mname +  " State 9 CommError", Toast.LENGTH_LONG).show();
        }else if(chk.equals("10")) {
            Toast.makeText(this,mname +  " State 10 LastState", Toast.LENGTH_LONG).show();
        }
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
