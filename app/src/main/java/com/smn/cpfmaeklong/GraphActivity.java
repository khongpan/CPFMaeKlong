package com.smn.cpfmaeklong;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


import java.text.SimpleDateFormat;
import java.util.Date;

public class GraphActivity extends AppCompatActivity {
    Date date = new Date();
    //Date today = new Date();
    float oldXAxis = 0f;
    float oldYAxis = 0f;
    float newXAxis = 0f;
    float newYAxis = 0f;
    Button btNextDay,btPrevDay;
    TextView txtGraphDate;

    GraphView graphView;
    LineGraphSeries<DataPoint> seriesA ;
    LineGraphSeries<DataPoint> seriesB ;
    String BaseURL;
    int SelectedPond;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        Intent intent = getIntent();
        BaseURL = intent.getStringExtra("BASE_URL");
        SelectedPond = intent.getIntExtra("SELECTED_POND", 0);

        //GraphView graphView = (GraphView) findViewById(R.id.graph_area);
        graphView = new GraphView(this);
        LinearLayout layout = (LinearLayout) findViewById(R.id.graph_area);
        layout.addView(graphView);

        seriesA = new LineGraphSeries<DataPoint>();
        seriesB = new LineGraphSeries<DataPoint>();

        btNextDay = (Button) findViewById(R.id.bt_next_day);
        btPrevDay = (Button) findViewById(R.id.bt_prev_day);

        txtGraphDate = (TextView) findViewById(R.id.txt_graph_date);

        graphView.getLegendRenderer().setVisible(true);
        graphView.getLegendRenderer().setBackgroundColor(Color.WHITE);
        graphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        graphView.getGridLabelRenderer().setGridColor(Color.DKGRAY);

        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(288);

        // set manual Y bounds
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMinY(0);
        graphView.getViewport().setMaxY(16);

        graphView.getViewport().setScrollable(true);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);
        staticLabelsFormatter.setHorizontalLabels(new String[]{"", "", "", "03", "", "", "06", "", "", "09", "", "", "12", "", "", "15", "", "", "18", "", "", "21", "", "", ""});
        graphView.getGridLabelRenderer().setHumanRounding(false);
        staticLabelsFormatter.setVerticalLabels(new String[]{"", "", "", "", "", "5", "", "", "", "", "10", "", "", "", "", "15", ""});
        graphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        ShowGraph();

        seriesA.setTitle("DO");
        seriesA.setColor(Color.BLUE);
        seriesA.setThickness(2);
        seriesB.setTitle("Aerator");
        seriesB.setColor(Color.RED);
        seriesB.setThickness(2);

        graphView.addSeries(seriesB);
        graphView.addSeries(seriesA);



        btNextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date today = new Date();
                if (date.before(today)) {
                    date.setDate(date.getDate() + 1);
                }
                ShowGraph();
            }
        });

        btPrevDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date today = new Date();
                date.setDate(date.getDate() - 1);
                ShowGraph();
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_graph, menu);
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

    public void ShowGraph() {
        //String url = "http://203.185.131.92/ws/get.php?appkey=0c5a295bd8c07a080b450069e3f2&p=POND-CONTROL,";
        String url = BaseURL;
        SyslogXML obj;

        String strDay = new SimpleDateFormat("yyyy-MM-dd").format(date);

        txtGraphDate.setText(strDay);

        {
            String finalUrl = url + ",4096,1505,"+ strDay;

            if(SelectedPond==1){
                finalUrl = url + ",8192,100,"+ strDay;
            }
            obj = new SyslogXML(finalUrl);
            obj.fetchXML();
            while (!obj.parsingComplete) ;

            DataPoint[] values = new DataPoint[obj.countRecord];

            for (int i = 0; i < obj.getCountRecord(); i++) {
                String str = obj.getValue();

                values[i] = new DataPoint(i,Float.parseFloat(str));


            }
            seriesA.resetData(values);

        }
        {
            String finalUrl = url + ",4096,1506,"+ strDay;
            if(SelectedPond==1){
                finalUrl = url + ",8192,100,"+ strDay;
            }
            obj = new SyslogXML(finalUrl);
            obj.fetchXML();
            while (!obj.parsingComplete) ;
            DataPoint[] values = new DataPoint[obj.countRecord];

            for (int i = 0; i < obj.getCountRecord(); i++) {
                String str = obj.getValue();
                values[i] = new DataPoint(i,Float.parseFloat(str));
            }
            seriesB.resetData(values);

        }
        graphView.getViewport().setScrollable(true);
    }


    @Override
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

                ShowGraph();
                break;}
        }
        return false;
    }

}
