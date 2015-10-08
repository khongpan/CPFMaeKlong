package com.smn.cpfmaeklong;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GraphActivity extends AppCompatActivity {
    Date date = new Date();
    Date chkdate = new Date();
    float oldXAxis = 0f;
    float oldYAxis = 0f;
    float newXAxis = 0f;
    float newYAxis = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        chkdate.setDate(chkdate.getDate() + 1);
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
        String Showday = new SimpleDateFormat("dd-MM-yyyy").format(date);
        GraphView graphView = new LineGraphView(this, Showday);

        setContentView(R.layout.activity_graph);{
            String finalUrl = url + "4096,1505,"+ today;
            obj = new HandleXML(finalUrl);
            obj.fetchXML();
            while (obj.parsingComplete) ;
            float[] ValueY = new float[obj.countRecord];
            for (int i = 0; i < obj.getCountRecord(); i++) {
                String str = obj.getValue();
                ValueY[i] = Float.parseFloat(str);
            }
            GraphView.GraphViewData[] data = new GraphView.GraphViewData[ValueY.length];
            for (int i = 0; i < ValueY.length; i++) {
                data[i] = new GraphView.GraphViewData(i, ValueY[i]);
            }
            GraphViewSeries seriesA = new GraphViewSeries("M", new GraphViewSeries.GraphViewSeriesStyle(Color.RED, 2), data);
            graphView.addSeries(seriesA);
        }
        {
            String finalUrl = url + "4096,1506,"+ today;
            obj = new HandleXML(finalUrl);
            obj.fetchXML();
            while (obj.parsingComplete) ;
            float[] ValueY = new float[obj.countRecord];
            for (int i = 0; i < obj.getCountRecord(); i++) {
                String str = obj.getValue();
                ValueY[i] = Float.parseFloat(str);
            }
            GraphView.GraphViewData[] data = new GraphView.GraphViewData[ValueY.length];
            for (int i = 0; i < ValueY.length; i++) {
                data[i] = new GraphView.GraphViewData(i, ValueY[i]);
            }
            GraphViewSeries seriesA = new GraphViewSeries("DO", new GraphViewSeries.GraphViewSeriesStyle(Color.BLUE, 2), data);
            graphView.addSeries(seriesA);
        }

        graphView.setShowLegend(true);
        LinearLayout layout = (LinearLayout) findViewById(R.id.graph_area);
        graphView.setHorizontalLabels(new String[]{
                "","","","03","","","06","","","09","","","12","","","15","","","18","","","21","","",""
        });
        graphView.setManualYAxisBounds(15, 0);
        graphView.setVerticalLabels(new String[]{"15","","","","","10","","","","","5","","","","","0"});
        graphView.setViewPort(0, 288);
        layout.addView(graphView);
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
                if(oldXAxis<newXAxis){
                    date.setDate(date.getDate() - 1);
                }else if(oldXAxis>newXAxis){
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
}
