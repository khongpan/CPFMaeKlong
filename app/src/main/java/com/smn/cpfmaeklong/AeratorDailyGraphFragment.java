package com.smn.cpfmaeklong;

/**
 * Created by Mink on 12/30/2015.
 */


        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.pm.ActivityInfo;
        import android.content.res.Configuration;
        import android.graphics.Color;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.preference.Preference;
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

        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.Date;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AeratorDailyGraphFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AeratorDailyGraphFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AeratorDailyGraphFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final int MAX_AERATOR = 16;
    private static final int PC_PARA_NUM = 3;

    Date date = new Date();
    String mStrSelectedDay;

    private String mBaseUrl;
    private int mSelectedPond;
    private String mStrGraphGroup;

    private OnFragmentInteractionListener mListener;

    private GraphView mGraphView;
    private GraphView mUpperGraphView;


    private int[] mColorList = {Color.BLUE,Color.MAGENTA,Color.DKGRAY,Color.CYAN,Color.YELLOW,
            Color.GREEN,Color.LTGRAY,Color.BLACK,Color.RED,Color.GRAY};

    private RainbowLineGraphSeries[] mSeries = new RainbowLineGraphSeries[MAX_AERATOR];
    private LineGraphSeries[] mPondControlStatusSeries= new LineGraphSeries[PC_PARA_NUM];
    //private LineGraphSeries mOnAeratorCountSeries;
    //private LineGraphSeries mDoLevelSeries;
    //private LineGraphSeries mAvlAeratorSeries;

    private SensorDailyDataXML[] mAeratorXml = new SensorDailyDataXML[MAX_AERATOR];
    private SensorDailyDataXML[] mPondControlStatusXml = new SensorDailyDataXML[3];
    //private SensorDailyDataXML mOnMotorCountXml;
    //private SensorDailyDataXML mDoLevelXml;
    //private SensorDailyDataXML mAvlAeratorXml;

    private DataPoint[][] dataPointSeries1;
    private DataPoint[][] dataPointSeries2;

    private DataPoint DATA_POINT_NO_DATA[] = {new DataPoint(0,0) };

    int [] mStateColor = new int[] {
            Color.GRAY,
            Color.GRAY,
            Color.GRAY,
            0xFF00AF00,
            Color.GRAY,
            Color.BLACK,
            Color.RED,
            Color.RED,
            Color.GRAY,
            Color.GRAY,
            Color.GRAY
    };





    public AeratorDailyGraphFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AeratorDailyGraphFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AeratorDailyGraphFragment newInstance(String param1, String param2) {
        AeratorDailyGraphFragment fragment = new AeratorDailyGraphFragment();
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
            mStrGraphGroup = getArguments().getString("GRAPH_GROUP");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_aerator_daily_graph, container, false);

        mGraphView = (GraphView) rootView.findViewById(R.id.fr_graph1);
        mUpperGraphView = (GraphView) rootView.findViewById(R.id.fr_graph2);

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

    public void formatPlotArea() {

        mGraphView.setTitle("Aerator Status");

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
        //staticLabelsFormatter.setVerticalLabels(new String[]{"", "", "", "", "", "5", "", "", "", "", "10", "", "", "", "", "15", ""});
        mGraphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        mGraphView.getGridLabelRenderer().setNumVerticalLabels(9);

        // set manual x bounds to have nice steps
        SimpleDateFormat  date_formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date start_time = new Date();
        Date end_time= new Date();
        try {
            start_time = date_formatter.parse(mStrSelectedDay);
            end_time.setTime(start_time.getTime()+1000*60*60*24);
        }    catch (Exception e)
        {
            e.printStackTrace();
        }

        mGraphView.getViewport().setMinX(start_time.getTime());
        mGraphView.getViewport().setMaxX(end_time.getTime());
        mGraphView.getViewport().setXAxisBoundsManual(true);



        for (int i = 0; i < MAX_AERATOR; i++) {
            mSeries[i] = new RainbowLineGraphSeries<DataPoint>(new DataPoint[] {new DataPoint(0,0), new DataPoint (0,0)});
            //mSeries[i].setTitle("series 1");
            mSeries[i].setColor(Color.BLUE);
            mSeries[i].setThickness(7);
            mSeries[i].setOffset(i + 1);
            mSeries[i].setFlatLine(true);
            mSeries[i].setLevelColor(mStateColor);
            mGraphView.addSeries(mSeries[i]);
        }
        //mOnAeratorCountSeries = new LineGraphSeries<DataPoint>(new DataPoint[] {new DataPoint(0,0), new DataPoint (0,0)});
        //mOnAeratorCountSeries.setThickness(5);
        //mOnAeratorCountSeries.setColor(Color.MAGENTA);
        //mGraphView.addSeries(mOnAeratorCountSeries);
    }
    public void formatUpperPlotArea() {

        mUpperGraphView.getGridLabelRenderer().setGridColor(Color.DKGRAY);
        mUpperGraphView.getViewport().setXAxisBoundsManual(true);
        mUpperGraphView.getViewport().setMinX(0);
        mUpperGraphView.getViewport().setMaxX(288);

        // set manual Y bounds
        mUpperGraphView.getViewport().setYAxisBoundsManual(true);
        mUpperGraphView.getViewport().setMinY(0);
        mUpperGraphView.getViewport().setMaxY(16);

        //mGraphView.getViewport().setScrollable(true);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(mUpperGraphView);
        staticLabelsFormatter.setHorizontalLabels(new String[]{"", "", "", "03", "", "", "06", "", "", "09", "", "", "12", "", "", "15", "", "", "18", "", "", "21", "", "", ""});
        //staticLabelsFormatter.setVerticalLabels(new String[]{"", "", "", "", "", "5", "", "", "", "", "10", "", "", "", "", "15", "","","","","20"});
        mUpperGraphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        mUpperGraphView.getGridLabelRenderer().setNumVerticalLabels(9);

        // set manual x bounds to have nice steps
        SimpleDateFormat  date_formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date start_time = new Date();
        Date end_time= new Date();
        try {
            start_time = date_formatter.parse(mStrSelectedDay);
            end_time.setTime(start_time.getTime()+1000*60*60*24);
        }    catch (Exception e)
        {
            e.printStackTrace();
        }

        mUpperGraphView.getViewport().setMinX(start_time.getTime());
        mUpperGraphView.getViewport().setMaxX(end_time.getTime());
        mUpperGraphView.getViewport().setXAxisBoundsManual(true);

        for (int i=0; i<PC_PARA_NUM;i++) {
            mPondControlStatusSeries[i] = new LineGraphSeries<DataPoint>(new DataPoint[] {new DataPoint(0,0)});
            mPondControlStatusSeries[i].setThickness(5);
            mPondControlStatusSeries[i].setColor(mColorList[i]);
            mUpperGraphView.addSeries(mPondControlStatusSeries[i]);
        }

/*

        mDoLevelSeries = new LineGraphSeries<DataPoint>(new DataPoint[] {new DataPoint(0,0), new DataPoint (0,0)});
        mDoLevelSeries.setThickness(5);
        mDoLevelSeries.setColor(Color.BLUE);

        mAvlAeratorSeries = new LineGraphSeries<DataPoint>(new DataPoint[] {new DataPoint(0,0), new DataPoint (0,0)});
        mAvlAeratorSeries.setThickness(5);
        mAvlAeratorSeries.setColor(0xFFB0B000);

        mUpperGraphView.addSeries(mOnAeratorCountSeries);
        mUpperGraphView.addSeries(mAvlAeratorSeries);
        mUpperGraphView.addSeries(mDoLevelSeries);
*/
    }

    public void updateSeriesData() {
        DataPoint[] dataPoint;
        int record_count;

        for(int i=0;i<MAX_AERATOR;i++) {

            record_count = mAeratorXml[i].getCountRecord();

            if (record_count > 0) {
                dataPoint = new DataPoint[record_count];

                for (int ii = 0; ii < record_count; ii++) {
                    DataPoint v = new DataPoint(mAeratorXml[i].getDataTimeStamp(ii), mAeratorXml[i].getDataValue(ii)%10);
                    dataPoint[ii] = v;
                }

            } else {
                dataPoint = new DataPoint[] {new DataPoint(0,0)};
            }
            mSeries[i].resetData(dataPoint);
        }

    }

    void reformatPlotArea()
    {

        // set manual x bounds to have nice steps
        SimpleDateFormat  date_formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date start_time = new Date();
        Date end_time= new Date();
        try {
            start_time = date_formatter.parse(mStrSelectedDay);
            end_time.setTime(start_time.getTime()+1000*60*60*24);
        }    catch (Exception e)
        {
            e.printStackTrace();
        }

        mGraphView.getViewport().setMinX(start_time.getTime());
        mGraphView.getViewport().setMaxX(end_time.getTime());
        mGraphView.getViewport().setXAxisBoundsManual(true);
    }

    void reformatPondControlStatusPlotArea()
    {

        // set manual x bounds to have nice steps
        SimpleDateFormat  date_formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date start_time = new Date();
        Date end_time= new Date();
        try {
            start_time = date_formatter.parse(mStrSelectedDay);
            end_time.setTime(start_time.getTime()+1000*60*60*24);
        }    catch (Exception e)
        {
            e.printStackTrace();
        }

        mUpperGraphView.getViewport().setMinX(start_time.getTime());
        mUpperGraphView.getViewport().setMaxX(end_time.getTime());
        mUpperGraphView.getViewport().setXAxisBoundsManual(true);
    }



    public void updatePondControlStatusGraphSeriesData() {
        DataPoint[] dataPoint;
        int record_count;


        for (int i =0; i<PC_PARA_NUM;i++) {
            record_count = mPondControlStatusXml[i].getCountRecord();
            dataPoint = new DataPoint[record_count];
            for (int ii=0;ii<record_count;ii++) {
                DataPoint v = new DataPoint(mPondControlStatusXml[i].getDataTimeStamp(ii), mPondControlStatusXml[i].getDataValue(ii));
                dataPoint[ii] = v;
            }
            if (record_count>0) {
                mPondControlStatusSeries[i].resetData(dataPoint);
            } else {
                dataPoint = new DataPoint[] {new DataPoint(0,0)};
                mPondControlStatusSeries[i].resetData(dataPoint);
            }
            mPondControlStatusSeries[i].setTitle(mPondControlStatusXml[i].getIoName());
        }



/*

        record_count = mDoLevelXml.getCountRecord();
        dataPoint = new DataPoint[record_count];
        for(int ii=0;ii<record_count;ii++) {
            DataPoint v = new DataPoint(mDoLevelXml.getDataTimeStamp(ii), mDoLevelXml.getDataValue(ii)+0.3);
            dataPoint[ii] = v;
        }
        mDoLevelSeries.resetData(dataPoint);
        mDoLevelSeries.setTitle(mDoLevelXml.getIoName());

        record_count = mAvlAeratorXml.getCountRecord();
        dataPoint = new DataPoint[record_count];
        for(int ii=0;ii<record_count;ii++) {
            DataPoint v = new DataPoint(mAvlAeratorXml.getDataTimeStamp(ii), mAvlAeratorXml.getDataValue(ii)+0.3);
            dataPoint[ii] = v;
        }
        mAvlAeratorSeries.resetData(dataPoint);
        mAvlAeratorSeries.setTitle(mAvlAeratorXml.getIoName());


        record_count = mOnMotorCountXml.getCountRecord();
        dataPoint = new DataPoint[record_count];
        for(int ii=0;ii<record_count;ii++) {
            DataPoint v = new DataPoint(mOnMotorCountXml.getDataTimeStamp(ii), mOnMotorCountXml.getDataValue(ii)+0.3);
            dataPoint[ii] = v;
        }
        mOnAeratorCountSeries.resetData(dataPoint);
        mOnAeratorCountSeries.setTitle(mOnMotorCountXml.getIoName());
*/



        // set manual x bounds to have nice steps
        SimpleDateFormat  date_formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date start_time = new Date();
        Date end_time= new Date();
        try {
            start_time = date_formatter.parse(mStrSelectedDay);
            end_time.setTime(start_time.getTime()+1000*60*60*24);
        }    catch (Exception e)
        {
            e.printStackTrace();
        }

        mUpperGraphView.getViewport().setMinX(start_time.getTime());
        mUpperGraphView.getViewport().setMaxX(end_time.getTime());
        mUpperGraphView.getViewport().setXAxisBoundsManual(true);

        mUpperGraphView.getLegendRenderer().setVisible(true);
        mUpperGraphView.getLegendRenderer().setBackgroundColor(Color.TRANSPARENT);
        mUpperGraphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

    }

    void setDate(String date_str) {
        mStrSelectedDay = date_str;
    }

    void setGraphGroup(String group_str) {
        mStrGraphGroup = group_str;
    }

    public boolean checkInternetConnection() {

        boolean have_connection = false;

        ConnectivityManager cManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();

        if(nInfo==null) {
            Toast.makeText(getActivity() , "No Internet connection! Please connect to the Internet.", Toast.LENGTH_LONG).show();
        } else {
            have_connection=true;
        }
        return have_connection;
    }

    void updateGraph() {

        if (checkInternetConnection()==false) return;

        DownloadFromInternet Downloader = new DownloadFromInternet();
        Downloader.execute("100", mStrSelectedDay);
    }

    private void lockScreenOrientation() {
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    private void unlockScreenOrientation() {
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

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
            //showDialog(progress_bar_type);
            cancel = false;

            progressDialog = ProgressDialog.show(getActivity(),
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
            DataPoint[] dataPoint;
            String finalUrl;
            String io_number_str = str[0];
            String date_str = str[1];
            int io_number;
            boolean loading_complete;

            try {
                if (mSelectedPond==0) {
                    io_number = 1520;
                } else {
                    io_number = 1560;
                }
                io_number=1520;
                for (int i=0;i<MAX_AERATOR;i++) {
                    finalUrl = mBaseUrl + ",4096," + io_number + "," + date_str;
                    io_number++;

                    mAeratorXml[i] = new SensorDailyDataXML(finalUrl);
                    mAeratorXml[i].fetchXML(finalUrl);
                }

                io_number = 1505;
                for (int i=0;i<PC_PARA_NUM;i++)
                {
                    finalUrl = mBaseUrl + ",4096," + io_number + "," + date_str;
                    io_number++;

                    mPondControlStatusXml[i] = new SensorDailyDataXML(finalUrl);
                    mPondControlStatusXml[i].fetchXML(finalUrl);
                }

/*
                finalUrl = mBaseUrl + ",4096," + "1506" + "," + date_str;
                mOnMotorCountXml = new SensorDailyDataXML(finalUrl);
                mOnMotorCountXml.fetchXML(finalUrl);

                finalUrl = mBaseUrl + ",4096," + "1505" + "," + date_str;
                mDoLevelXml = new SensorDailyDataXML(finalUrl);
                mDoLevelXml.fetchXML(finalUrl);

                finalUrl = mBaseUrl + ",4096," + "1507" + "," + date_str;
                mAvlAeratorXml = new SensorDailyDataXML(finalUrl);
                mAvlAeratorXml.fetchXML(finalUrl);
*/
                do {
                    loading_complete=true;
                    for(int i=0;i<MAX_AERATOR;i++) {
                        loading_complete = loading_complete && mAeratorXml[i].isFetchComplete();
                    }
                    for (int i=0;i<3;i++) {
                        loading_complete = loading_complete && mPondControlStatusXml[i].isFetchComplete();
                    }
                    /*
                    loading_complete = loading_complete && mOnMotorCountXml.isFetchComplete();
                    loading_complete = loading_complete && mDoLevelXml.isFetchComplete();
                    loading_complete = loading_complete && mAvlAeratorXml.isFetchComplete();
                    */


                    // Publish the progress which triggers onProgressUpdate method
                    publishProgress("" + count++);
                    if (cancel) break;
                    if (!loading_complete) Thread.sleep(1000);
                } while (!loading_complete);

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        // While Downloading Music File
        @Override
        protected void onProgressUpdate(String... progress) {
            // Set progress percentage
            progressDialog.setMessage("Please wait... " + String.valueOf(progress[0]) + " sec");
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
            // Play the music
            //updateSeriesData();
            //updatePondControlStatusGraphSeriesData();
            PlotDailyData plotter = new PlotDailyData();
            plotter.execute("100", mStrSelectedDay);
            //unlockScreenOrientation();
        }
    }

    // Async Task Class
    class PlotDailyData extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        boolean cancel;

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            //showDialog(progress_bar_type);
            cancel = false;
/*
            progressDialog = ProgressDialog.show(getActivity(),
                    "Processing Data",
                    "Please Wait!");

            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    cancel = true;
                }
            });
*/
            for (int i =0; i<MAX_AERATOR;i++) {

                mSeries[i].resetData(DATA_POINT_NO_DATA);

            }
            for (int i =0; i<PC_PARA_NUM;i++) {

                mPondControlStatusSeries[i].resetData(DATA_POINT_NO_DATA);

            }

            //lockScreenOrientation();

            //Toast.makeText(getActivity(),"Progress Start",Toast.LENGTH_LONG).show();
        }

        // Download xml from Internet
        @Override
        protected String doInBackground(String... str) {
            //updateSeriesData();
            //updateUpperGraphSeriesData();


            dataPointSeries1 = new DataPoint[MAX_AERATOR][];
            dataPointSeries2 = new DataPoint[PC_PARA_NUM][];
            int record_count;

            for(int i=0;i<MAX_AERATOR;i++) {

                record_count = mAeratorXml[i].getCountRecord();

                if (record_count > 0) {
                    dataPointSeries1[i] = new DataPoint[record_count];

                    for (int ii = 0; ii < record_count; ii++) {
                        DataPoint v = new DataPoint(mAeratorXml[i].getDataTimeStamp(ii), mAeratorXml[i].getDataValue(ii)%10);
                        dataPointSeries1[i][ii] = v;
                    }

                } else {
                    dataPointSeries1[i] = new DataPoint[] {new DataPoint(0,0)};
                }
            }

            for (int i =0; i<PC_PARA_NUM;i++) {
                record_count = mPondControlStatusXml[i].getCountRecord();
                dataPointSeries2[i] = new DataPoint[record_count];
                for (int ii=0;ii<record_count;ii++) {
                    DataPoint v = new DataPoint(mPondControlStatusXml[i].getDataTimeStamp(ii), mPondControlStatusXml[i].getDataValue(ii));
                    dataPointSeries2[i][ii] = v;
                }
                if (record_count==0) {
                    dataPointSeries2[i] = new DataPoint[]{new DataPoint(0, 0)};
                }

                mPondControlStatusSeries[i].setTitle(mPondControlStatusXml[i].getIoName());
            }

            return null;
        }

        // While Downloading Music File
        @Override
        protected void onProgressUpdate(String... progress) {
            // Set progress percentage
            progressDialog.setMessage("Please wait... " + String.valueOf(progress[0]) + " sec");
        }

        // Once XML is downloaded
        @Override
        protected void onPostExecute(String file_url) {
            // Dismiss the dialog after the Xml file was downloaded
            //Toast.makeText(getActivity(),"Progress Ended",Toast.LENGTH_LONG).show();



            //updateSeriesData();
            //updatePondControlStatusGraphSeriesData();
            reformatPlotArea();
            reformatPondControlStatusPlotArea();
            for (int i =0; i<MAX_AERATOR;i++) {

                mSeries[i].resetData(dataPointSeries1[i]);

                mSeries[i].setTitle(mAeratorXml[i].getIoName());
            }
            for (int i =0; i<PC_PARA_NUM;i++) {

                mPondControlStatusSeries[i].resetData(dataPointSeries2[i]);

                mPondControlStatusSeries[i].setTitle(mPondControlStatusXml[i].getIoName());
            }

            //progressDialog.dismiss();
            unlockScreenOrientation();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("STR_SELECTED_DAY", mStrSelectedDay);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState!=null) {
            mStrSelectedDay = savedInstanceState.getString("STR_SELECTED_DAY");
        }
        if (mStrSelectedDay==null) {
            mStrSelectedDay = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }
        formatPlotArea();
        formatUpperPlotArea();
        updateGraph();
    }

    public static Date convertStringToDate(String date) {
        SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
        if (date != null) {
            try {
                return FORMATTER.parse(date);
            } catch (ParseException e) {
                // nothing we can do if the input is invalid
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}

