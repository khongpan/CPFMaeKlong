package com.smn.cpfmaeklong;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DateNavigateFragment.OnDateNavigateFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DateNavigateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DateNavigateFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    Date mCurrentDate = new Date();
    String mStrCurrentDate;
    Button btnNextDay,btnPrevDay;
    TextView tvCurrentDate;




    private OnDateNavigateFragmentInteractionListener mListener;

    public DateNavigateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DateNavigateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DateNavigateFragment newInstance(String param1, String param2) {
        DateNavigateFragment fragment = new DateNavigateFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_date_navigate, container, false);
        btnNextDay = (Button) view.findViewById(R.id.btn_next_day);
        btnPrevDay = (Button) view.findViewById(R.id.btn_prev_day);
        tvCurrentDate = (TextView) view.findViewById(R.id.tv_current_date);
        //if (mStrCurrentDate==null) {
        ////    mStrCurrentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        //    tvCurrentDate.setText(mStrCurrentDate);
       // }

        btnNextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date today = new Date();
                if (mCurrentDate.before(today)) {
                    mCurrentDate.setDate(mCurrentDate.getDate() + 1);
                }

                mStrCurrentDate = new SimpleDateFormat("yyyy-MM-dd").format(mCurrentDate);
                tvCurrentDate.setText(mStrCurrentDate);
                if (mListener != null) {
                    mListener.onDateNavigateFragmentInteraction(mStrCurrentDate);
                }
            }
        });

        btnPrevDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date today = new Date();
                mCurrentDate.setDate(mCurrentDate.getDate() - 1);
                mStrCurrentDate = new SimpleDateFormat("yyyy-MM-dd").format(mCurrentDate);
                tvCurrentDate.setText(mStrCurrentDate);
                if (mListener != null) {
                    mListener.onDateNavigateFragmentInteraction(mStrCurrentDate);
                }
            }
        });

        return view;


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onDateNavigateFragmentInteraction("");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDateNavigateFragmentInteractionListener) {
            mListener = (OnDateNavigateFragmentInteractionListener) context;
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("STR_CURRENT_DATE", mStrCurrentDate);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState!=null) {
            mStrCurrentDate = savedInstanceState.getString("STR_CURRENT_DATE");
            mCurrentDate = convertStringToDate(mStrCurrentDate);
        }
        if (mStrCurrentDate==null) {
            mStrCurrentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }
        tvCurrentDate.setText(mStrCurrentDate);
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
    public interface OnDateNavigateFragmentInteractionListener {
        // TODO: Update argument type and name
        void onDateNavigateFragmentInteraction(String date);
    }
}
