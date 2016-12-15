package com.smn.cpfmaeklong;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PondLayoutFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PondLayoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PondLayoutFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    Date date = new Date();
    String mStrSelectedDay = new SimpleDateFormat("yyyy-MM-dd").format(date);

    private String mBaseUrl;
    private int mSelectedPond;
    private String mStrGraphGroup;



    private final Handler mHandler = new Handler();
    //private Runnable mTimer1;
    //private Runnable mTimer2;

    private PointsGraphSeries<DataPoint> mSeries1;

    private final int AERATOR_NUM=20;
    private PointsGraphSeries<DataPoint> mAeratorPos[] = new PointsGraphSeries[AERATOR_NUM];
    private SensorInfoXML mAeratorInfoXml[]= new SensorInfoXML[AERATOR_NUM];

    private GraphView mGraphView;
    private PointsGraphSeries.CustomShape mAeratorShape;

    private SensorDailyDataXML sensorXml1;
    private SensorDailyDataXML sensorXml2;
    private SensorDailyDataXML sensorXml3;


    public PondLayoutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PondLayoutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PondLayoutFragment newInstance(String param1, String param2) {
        PondLayoutFragment fragment = new PondLayoutFragment();
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

        View rootView = inflater.inflate(R.layout.fragment_pond_layout, container, false);

        mGraphView = (GraphView) rootView.findViewById(R.id.pond_layout_graph);

        //setGraphFormat();
        //updateGraph();

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

        //mGraphView.getLegendRenderer().setVisible(true);
        //mGraphView.getLegendRenderer().setBackgroundColor(Color.WHITE);
        //mGraphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        //mGraphView.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);

        mGraphView.getGridLabelRenderer().setGridColor(Color.DKGRAY);

        mGraphView.getViewport().setXAxisBoundsManual(true);
        mGraphView.getViewport().setMinX(0);
        mGraphView.getViewport().setMaxX(100);

        // set manual Y bounds
        mGraphView.getViewport().setYAxisBoundsManual(true);
        mGraphView.getViewport().setMinY(0);
        mGraphView.getViewport().setMaxY(100);

        mGraphView.getGridLabelRenderer().setNumVerticalLabels(0);
        mGraphView.getGridLabelRenderer().setNumHorizontalLabels(0);



        mGraphView.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        mGraphView.getGridLabelRenderer().setVerticalLabelsVisible(false);




        for (int i=0;i<AERATOR_NUM;i++)
        {
            mAeratorPos[i] = new PointsGraphSeries<DataPoint>(new DataPoint[] {new DataPoint(-10,-10)});
            mAeratorPos[i].setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                    int i=0;

                    for (i=0;i<AERATOR_NUM;i++) {
                        if(series==mAeratorPos[i])  break;
                    }

                    ShowMotorState(i);

                    //Toast.makeText(getActivity(), "On Data Point clicked: "+ i +series.getTitle()
                    //       +dataPoint , Toast.LENGTH_SHORT).show();
                }
            });

            mAeratorShape = new PointsGraphSeries.CustomShape() {
                @Override
                public void draw(Canvas canvas, Paint paint, float x, float y, DataPointInterface dataPoint) {


                    DisplayMetrics displaymetrics = new DisplayMetrics();

                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

                    float value = getResources().getDisplayMetrics().density;
                    int dim = (int) (10*value);
                    paint.setStrokeWidth(10);
                    canvas.drawLine(x-dim, y-dim, x+dim, y+dim, paint);
                    canvas.drawLine(x+dim, y-dim, x-dim, y+dim, paint);
                }
            };


            mAeratorPos[i].setCustomShape(mAeratorShape);

            mAeratorPos[i].setSize(24);
            mGraphView.addSeries(mAeratorPos[i]);
        }
    }

    public void updateSeriesData() {
        int relay_state;
        int decision_state;
        int profile_state;
        int mode_state;

        String str_value;
        int x,y;
        int value;
        for(int i=0;i<AERATOR_NUM;i++) {

            mAeratorPos[i].setColor(Color.WHITE);
            mAeratorPos[i].resetData(new DataPoint[] { new DataPoint(-10, -10)});
            mAeratorPos[i].setTitle(mAeratorInfoXml[i].getIoName());


            str_value = mAeratorInfoXml[i].getLastValue();

            if(str_value.equals("-")){

            }else {

                value = (int) Float.parseFloat(str_value);
                relay_state = value%100;
                value/=100;
                decision_state = value%10;
                value/=10;
                profile_state = value%10;
                value/=10;
                mode_state = value %10;

                x=-10;y=-10;
                str_value=mAeratorInfoXml[i].getPosX();
                if(!str_value.equals("-"))
                    x = (int )Float.parseFloat(str_value);
                str_value=mAeratorInfoXml[i].getPosY();
                if(!str_value.equals("-"))
                    y = (int )Float.parseFloat(str_value);
                DataPoint[] aeratorPos =  new DataPoint[] { new DataPoint(x, y)};
                mAeratorPos[i].setColor(Color.WHITE);
                mAeratorPos[i].resetData(aeratorPos);


                if (relay_state == 3) { //relay on
                    mAeratorPos[i].setColor(Color.argb(255, 0, 192, 0));
                    if (profile_state == 1) {// profile MustOn
                        mAeratorPos[i].setColor(Color.argb(255, 0, 112, 0));
                    }
                } else if (relay_state == 5) { //relay off
                    if (profile_state == 2) // profile MustOff
                        mAeratorPos[i].setColor(Color.argb(255, 204, 204, 204));
                    else if (profile_state == 3) // profile Rest
                        mAeratorPos[i].setColor(Color.argb(255, 64, 64, 128));
                    else
                        mAeratorPos[i].setColor(Color.BLACK);
                } else if (str_value.equals("-")) {
                    mAeratorPos[i].setColor(Color.WHITE);
                } else if (relay_state == 10) { //relay manual on
                    mAeratorPos[i].setColor(Color.YELLOW);
                    mAeratorPos[i].setShape(PointsGraphSeries.Shape.POINT);

                } else if (relay_state == 11) { // relay manual off
                    mAeratorPos[i].setColor(Color.BLUE);
                    mAeratorPos[i].setShape(PointsGraphSeries.Shape.TRIANGLE);
                    mAeratorPos[i].setCustomShape(null);

                }else {
                    mAeratorPos[i].setColor(Color.RED);

                }


            }


        }

    }

    void setDate(String date_str) {
        mStrSelectedDay = date_str;
    }

    void setPondUrl(String str) {
        mBaseUrl = str;
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

    public void updateGraph() {

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
            try {
                DataPoint[] dataPoint;
                String finalUrl;
                String finalUrl1,finalUrl2;
                int io_number;
                boolean loading_complete;
                String date_str = str[1];


                io_number=1520;


                for(int i=0;i<AERATOR_NUM;i++) {
                    finalUrl = mBaseUrl + ",4096," + io_number;
                    mAeratorInfoXml[i] = new SensorInfoXML(finalUrl);
                    mAeratorInfoXml[i].fetchXML();
                    io_number++;

                }

                do {
                    loading_complete=true;
                    for(int i=0;i<AERATOR_NUM;i++) {
                        loading_complete = loading_complete && mAeratorInfoXml[i].isFetchComplete();
                    }

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

            if (getActivity()==null) {
                return;
            }

            if (progressDialog!=null) {
                 if(progressDialog.isShowing())
                     progressDialog.dismiss();
            }

            //progressDialog.dismiss();
            if (cancel) {
                unlockScreenOrientation();
                return;
            }

            updateSeriesData();
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

    public void ShowMotorState(int mNo){

        int relay_state;
        int decision_state;
        int profile_state;
        int mode_state;
        int value;
        int state;
        String str_value;

        String[] strRelayState = {
                "Null","Unknow","Activate","On","Deactivate","Off",
                "OverCurrent","UnderCurrent","InternalErr","CommError",
                "ManualOn","ManualOff","mOverCurrent","mUnderCurrent","mInternalError"
        };

        String[] strDecisionState = {
                "On","Off","Defer"
        };

        String[] strProfileState = {
                "OnDemand","MustOn","MustOff","Rest"
        };

        String[] strModeState = {
                "Control","ForceOn","ForceOff","UnCare"
        };

        if (mAeratorInfoXml[mNo]==null) return;

        str_value = mAeratorInfoXml[mNo].getLastValue();

        if (str_value.equals("-")) return;

        value = (int) Float.parseFloat(str_value);
        state = value;
        relay_state = value%100;
        value/=100;
        decision_state = value%10;
        value/=10;
        profile_state = value%10;
        value/=10;
        mode_state = value %10;

        String str_show;

        /*if (SelectedPond==2) {
            str_show = "Motor"+ mNo + " S" + state + " "
                    + strRelayState[relay_state];
        } else*/ {
            str_show = "Index"+ mNo + " S" + state + " m"
                    + strModeState[mode_state] + " p"
                    + strProfileState[profile_state] + " d"
                    + strDecisionState[decision_state] + " r"
                    + strRelayState[relay_state] ;

        }


        Toast.makeText(getActivity() ,str_show,Toast.LENGTH_SHORT).show();

    }
}
