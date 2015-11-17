package com.smn.cpfmaeklong;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.smn.cpfmaeklong.R.layout.activity_column;

public class GridActivity extends AppCompatActivity {
    Date date = new Date();
    Date chkdate = new Date();
    float oldXAxis = 0f;
    float oldYAxis = 0f;
    float newXAxis = 0f;
    float newYAxis = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        chkdate.setDate(chkdate.getDate() + 1);
        ShowGrid();
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

    private void ShowGrid() {
        final ListView lv;
        final TextView dt;
        final EditText keysch;
        final Button sch;
        final int[] index = new int[1];
        String url = "http://203.185.131.92/ws/get.php?appkey=0c5a295bd8c07a081c521369eefa7c64&syslog=POND-CONTROL";
        final HandleXML objs;

        String today = new SimpleDateFormat("yyyy-MM-dd").format(date);

        String finalUrl = url + ",4096," + today + ",NodeDateTime"; //"2015-10-02"
        objs = new HandleXML(finalUrl);
        objs.fetchXML();
        while (objs.parsingComplete) ;

        if(objs.getCountRecord()==0){
            Toast.makeText(this, "No Data in Syslog.", Toast.LENGTH_LONG).show();
        }else {
            final String[] time = new String[objs.getCountRecord()];
            final String[] data = new String[objs.getCountRecord()];

            for (int i = 0; i < objs.getCountRecord(); i++) {
                time[i] = objs.NodeDateTime();
                data[i] = objs.Message();
            }
            keysch = (EditText) findViewById(R.id.tvsch);
            sch = (Button) findViewById(R.id.btsch);
            dt = (TextView) findViewById(R.id.date2);
            lv = (ListView) findViewById(R.id.listView);
            dt.setText(time[0].substring(0, 10));

            final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<>();
            HashMap<String, String> map;

            for (int j = objs.getCountRecord()-1; j >=0; j--) {
                map = new HashMap<>();
                map.put("DATE", time[j].substring(11));
                map.put("DATA", data[j]);
                MyArrList.add(map);
            }
            SimpleAdapter sAdap;
            sAdap = new SimpleAdapter(GridActivity.this, MyArrList, activity_column,
                    new String[]{"DATE", "DATA"}, new int[]{R.id.date, R.id.data});
            lv.setAdapter(sAdap);

            sch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<>();
                    HashMap<String, String> map;
                    for (int k = objs.getCountRecord()-1; k >=0; k--) {
                        index[0] = data[k].indexOf(keysch.getText().toString());
                        if (index[0] >= 0) {
                            map = new HashMap<>();
                            map.put("DATE", time[k].substring(11));
                            map.put("DATA", data[k]);
                            MyArrList.add(map);
                        }
                    }
                    SimpleAdapter sAdap;
                    sAdap = new SimpleAdapter(GridActivity.this, MyArrList, activity_column,
                            new String[]{"DATE", "DATA"}, new int[]{R.id.date, R.id.data});
                    lv.setAdapter(sAdap);
                }
            });
        }
    }

    public boolean onTouchEvent(MotionEvent ev){
        final int actionPeformed = ev.getAction();
        switch(actionPeformed){
            case MotionEvent.ACTION_DOWN:{
                final float x = ev.getX();
                final float y = ev.getY();
                oldXAxis = x;
                oldYAxis = y;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                final float x = ev.getX();
                final float y = ev.getY();
                newXAxis = x;
                newYAxis = y;
                break;
            }
            case MotionEvent.ACTION_UP:{
                if(oldXAxis<newXAxis) {
                    date.setDate(date.getDate() - 1);
                }
                else if(oldXAxis>newXAxis){
                    date.setDate(date.getDate() + 1);
                }
                if(chkdate.equals(date)){
                    date.setDate(date.getDate() - 1);
                }
                ShowGrid();
                break;}
        }
        return false;
    }
}
