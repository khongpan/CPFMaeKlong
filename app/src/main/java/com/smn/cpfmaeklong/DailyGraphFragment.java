package com.smn.cpfmaeklong;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DailyGraphFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DailyGraphFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailyGraphFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Date date = new Date();
    String mStrSelectedDay = new SimpleDateFormat("yyyy-MM-dd").format(date);

    private String mBaseUrl;
    private int mSelectedPond;
    private String mGraphSeries;

    private OnFragmentInteractionListener mListener;

    private final Handler mHandler = new Handler();
    private Runnable mTimer1;
    private Runnable mTimer2;

    private LineGraphSeries<DataPoint> mSeries1;
    private LineGraphSeries<DataPoint> mSeries2;
    private GraphView mGraphView;


    private SensorDailyDataXML sensorXml1;
    private SensorDailyDataXML sensorXml2;


    public DailyGraphFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DailyGraphFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DailyGraphFragment newInstance(String param1, String param2) {
        DailyGraphFragment fragment = new DailyGraphFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
            mBaseUrl = getArguments().getString("BASE_URL");
            mSelectedPond = getArguments().getInt("SELECTED_POND", 0);
            mGraphSeries = getArguments().getString("GRAPH_SERIES");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_daily_graph, container, false);
        String ioName="DO";

        //GraphView graph = (GraphView) rootView.findViewById(R.id.graph);

        mGraphView = (GraphView) rootView.findViewById(R.id.graph);

       //mSeries1 = new LineGraphSeries<DataPoint>(generateData());
        //downloadDailyData("100", strDay, ioName);

        setGraphFormat();
        updateGraph();

        // Inflate the layout for this fragment
        return rootView;


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void setGraphFormat() {
        mGraphView.getLegendRenderer().setVisible(true);
        mGraphView.getLegendRenderer().setBackgroundColor(Color.WHITE);
        mGraphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        mGraphView.getGridLabelRenderer().setGridColor(Color.DKGRAY);

        mGraphView.getViewport().setXAxisBoundsManual(true);
        mGraphView.getViewport().setMinX(0);
        mGraphView.getViewport().setMaxX(288);

        // set manual Y bounds
        mGraphView.getViewport().setYAxisBoundsManual(true);
        mGraphView.getViewport().setMinY(0);
        mGraphView.getViewport().setMaxY(16);

        mGraphView.getViewport().setScrollable(true);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(mGraphView);
        staticLabelsFormatter.setHorizontalLabels(new String[]{"", "", "", "03", "", "", "06", "", "", "09", "", "", "12", "", "", "15", "", "", "18", "", "", "21", "", "", ""});
        staticLabelsFormatter.setVerticalLabels(new String[]{"", "", "", "", "", "5", "", "", "", "", "10", "", "", "", "", "15", ""});
        mGraphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        mSeries1 = new LineGraphSeries<DataPoint>();
        mSeries2 = new LineGraphSeries<DataPoint>();

        mSeries1.setTitle("series 1");
        mSeries1.setColor(Color.BLUE);
        mSeries1.setThickness(2);

        mSeries2.setTitle("series 2");
        mSeries2.setColor(Color.RED);
        mSeries2.setThickness(2);

        mGraphView.addSeries(mSeries1);
        mGraphView.addSeries(mSeries2);
    }

    public void updateSeriesData() {
        {

            DataPoint[] dataPoint;

            dataPoint = new DataPoint[sensorXml1.getCountRecord()];

            for (int i=0; i < sensorXml1.getCountRecord(); i++) {
                DataPoint v = new DataPoint(i, sensorXml1.getDataValue(i));
                dataPoint[i] = v;
            }
            mSeries1.resetData(dataPoint);

        }

        {

            DataPoint[] dataPoint;

            dataPoint = new DataPoint[sensorXml2.getCountRecord()];

            for (int i=0; i <sensorXml2.getCountRecord(); i++) {
                DataPoint v = new DataPoint(i, sensorXml2.getDataValue(i));
                dataPoint[i] = v;
            }
            mSeries2.resetData(dataPoint);

        }

        mSeries1.setTitle(sensorXml1.getIoName());
        mSeries2.setTitle(sensorXml2.getIoName());

    }

    void setDate(String date_str) {
        mStrSelectedDay = date_str;
    }

    void updateGraph() {

        DownloadFromInternet Downloader = new DownloadFromInternet();
        Downloader.execute("100", mStrSelectedDay);
    }


    // Async Task Class
    class DownloadFromInternet extends AsyncTask<String, String, String> {

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            //showDialog(progress_bar_type);
        }

        // Download xml from Internet
        @Override
        protected String doInBackground(String... str) {
            int count=1;
            try {
                DataPoint[] dataPoint;
                String finalUrl;
                String io_number = str[0];
                String date_str = str[1];

                finalUrl = mBaseUrl+",4096,"+io_number+","+date_str;
                sensorXml1 = new SensorDailyDataXML(finalUrl);
                sensorXml1.fetchXML(finalUrl);


                finalUrl = mBaseUrl+",4096,"+"101"+","+date_str;
                sensorXml2 = new SensorDailyDataXML(finalUrl);
                sensorXml2.fetchXML(finalUrl);

                while (!sensorXml1.isFetchComplete()){
                    while(!sensorXml2.isFetchComplete());

                    // Publish the progress which triggers onProgressUpdate method
                    //publishProgress(""+count++);


                }

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        // While Downloading Music File
        protected void onProgressUpdate(String... progress) {
            // Set progress percentage
            //prgDialog.setProgress(Integer.parseInt(progress[0]));
        }

        // Once XML is downloaded
        @Override
        protected void onPostExecute(String file_url) {
            // Dismiss the dialog after the Music file was downloaded
            //dismissDialog(progress_bar_type);
            //Toast.makeText(getApplicationContext(), "Download complete", Toast.LENGTH_LONG).show();
            // Play the music
            updateSeriesData();

        }
    }
}
