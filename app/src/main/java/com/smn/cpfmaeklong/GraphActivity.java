package com.smn.cpfmaeklong;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.view.View.OnClickListener;
import android.widget.Toast;


import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


import java.text.SimpleDateFormat;
import java.util.Date;

import static android.widget.Toast.LENGTH_SHORT;

public class GraphActivity extends AppCompatActivity {
    Date date = new Date();
    Date chkdate = new Date();
    float oldXAxis = 0f;
    float oldYAxis = 0f;
    float newXAxis = 0f;
    float newYAxis = 0f;
    Button btNextDay,btPrevDay;

    GraphView graphView;
    LineGraphSeries<DataPoint> seriesA ;
    LineGraphSeries<DataPoint> seriesB ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        //GraphView graphView = (GraphView) findViewById(R.id.graph_area);
        graphView = new GraphView(this);
        LinearLayout layout = (LinearLayout) findViewById(R.id.graph_area);
        layout.addView(graphView);

        seriesA = new LineGraphSeries<DataPoint>();
        seriesB = new LineGraphSeries<DataPoint>();

        btNextDay = (Button) findViewById(R.id.bt_next_day);
        btPrevDay = (Button) findViewById(R.id.bt_prev_day);

        graphView.getLegendRenderer().setVisible(true);
        graphView.getLegendRenderer().setBackgroundColor(Color.WHITE);
        graphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(288);

        // set manual Y bounds
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMinY(0);
        graphView.getViewport().setMaxY(16);

        graphView.getViewport().setScrollable(true);
        graphView.addSeries(seriesB);
        graphView.addSeries(seriesA);


        {
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);
            //staticLabelsFormatter.setHorizontalLabels(new String[] {"","","","03","","","06","","","09","","","12","","","15","","","18","","","21","","",""});
            staticLabelsFormatter.setVerticalLabels(new String[] {"","","","","","5","","","","","10","","","","","15",""});
            graphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        }

        seriesA.setTitle("DO");
        seriesA.setColor(Color.BLUE);
        seriesA.setThickness(2);
        seriesB.setTitle("Aerator");
        seriesB.setColor(Color.RED);
        seriesB.setThickness(2);

        btNextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = "";
                date.setDate(date.getDate() + 1);

                ShowGraph();
            }
        });

        btPrevDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date.setDate(date.getDate() - 1);
                ShowGraph();
            }
        });

        ShowGraph();

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
        String url = "http://203.185.131.92/ws/get.php?appkey=0c5a295bd8c07a080b450069e3f2&p=POND-CONTROL,";
        HandleXML obj;

        String today = new SimpleDateFormat("yyyy-MM-dd").format(date);
        //String Showday = new SimpleDateFormat("dd-MM-yyyy").format(date);
        //GraphView graphView = new LineGraphView(this, Showday);

        //GraphView graphView = new GraphView(this);
        //LineGraphSeries<DataPoint> seriesA = new LineGraphSeries();
        //LineGraphSeries<DataPoint> seriesB = new LineGraphSeries();



        //setContentView(R.layout.activity_graph);
        {
            String finalUrl = url + "4096,1505,"+ today;
            obj = new HandleXML(finalUrl);
            obj.fetchXML();
            while (obj.parsingComplete) ;
            //float[] ValueY = new float[obj.countRecord];
            DataPoint[] values = new DataPoint[obj.countRecord];

            for (int i = 0; i < obj.getCountRecord(); i++) {
                String str = obj.getValue();
                //ValueY[i] = Float.parseFloat(str);
                values[i] = new DataPoint(i,Float.parseFloat(str));

                //seriesA.appendData(new DataPoint(i, ValueY[i]), false, 288 * 2);
            }
            seriesA.resetData(values);
            //GraphView.GraphViewData[] data = new GraphView.GraphViewData[ValueY.length];
            //for (int i = 0; i < ValueY.length; i++) {
            //    data[i] = new GraphView.GraphViewData(i, ValueY[i]);
            //}
            //GraphViewSeries seriesA = new GraphViewSeries("DO", new GraphViewSeries.GraphViewSeriesStyle(Color.RED, 2), data);
                //graphView.addSeries(seriesA);
        }
        {
            String finalUrl = url + "4096,1506,"+ today;
            obj = new HandleXML(finalUrl);
            obj.fetchXML();
            while (obj.parsingComplete) ;
            DataPoint[] values = new DataPoint[obj.countRecord];

            for (int i = 0; i < obj.getCountRecord(); i++) {
                String str = obj.getValue();
                //ValueY[i] = Float.parseFloat(str);
                values[i] = new DataPoint(i,Float.parseFloat(str));

                //seriesB.appendData(new DataPoint(i, ValueY[i]), false, 288 * 2);
            }
            seriesB.resetData(values);
            //GraphView.GraphViewData[] data = new GraphView.GraphViewData[ValueY.length];
            //for (int i = 0; i < ValueY.length; i++) {
            //    data[i] = new GraphView.GraphViewData(i, ValueY[i]);
            //}
            //GraphViewSeries seriesA = new GraphViewSeries("Aerator", new GraphViewSeries.GraphViewSeriesStyle(Color.BLUE, 2), data);
            //graphView.addSeries(seriesB);
        }



/*

*/
/*
        graphView.getLegendRenderer().setVisible(true);
        graphView.getLegendRenderer().setBackgroundColor(Color.WHITE);
        graphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(288);

        // set manual Y bounds
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMinY(0);
        graphView.getViewport().setMaxY(16);
*/

        graphView.getViewport().setScrollable(true);

        //LinearLayout layout = (LinearLayout) findViewById(R.id.graph_area);
        //layout.addView(graphView);
    }



    public void onNextDay(){

        date.setDate(date.getDate() + 1);
        ShowGraph();
    }

    public void onPrevDay(){

        date.setDate(date.getDate() - 1);
        ShowGraph();
    }
 /*
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
                if(chkdate.equals(date)){
                    date.setDate(date.getDate() - 1);
                }
                ShowGraph();
                break;}
        }
        return false;
    }
 */
}
