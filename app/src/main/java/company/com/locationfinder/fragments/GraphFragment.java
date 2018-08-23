package company.com.locationfinder.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.ScatterChart;

import java.util.Timer;
import java.util.TimerTask;

import company.com.locationfinder.Constants;
import company.com.locationfinder.Graph.GraphMakerXY;
import company.com.locationfinder.LocationFindingAlgorithm.Coordinate2D;
import company.com.locationfinder.PeriodicServices.LocationUpdatingService;
import company.com.locationfinder.R;


public class GraphFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private SharedPreferences sharedpreferences;

    View view;

    public GraphFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GraphFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GraphFragment newInstance(String param1, String param2) {
        GraphFragment fragment = new GraphFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        sharedpreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_graph, container, false);



        //adding chart
        Coordinate2D beacon_1 = new Coordinate2D(readSharedPreferences_float(Constants.b1_x),
                readSharedPreferences_float(Constants.b1_y));

        Coordinate2D beacon_2 = new Coordinate2D(readSharedPreferences_float(Constants.b2_x),
                readSharedPreferences_float(Constants.b2_y));

        Coordinate2D beacon_3 = new Coordinate2D(readSharedPreferences_float(Constants.b3_x),
                readSharedPreferences_float(Constants.b3_y));

        GraphMakerXY.drawGraph((ScatterChart)view.findViewById(R.id.chart),beacon_1,beacon_2,beacon_3);

        startTimer();

        return view;
    }

    private Timer mTimer1;
    private TimerTask mTt1;
    private Handler mTimerHandler = new Handler();

    private void stopTimer(){
        if(mTimer1 != null){
            mTimer1.cancel();
            mTimer1.purge();
        }
    }

    private void startTimer(){
        mTimer1 = new Timer();
        mTt1 = new TimerTask() {
            public void run() {
                mTimerHandler.post(new Runnable() {
                    public void run(){
                        showPoint();
                    }
                });
            }
        };

        mTimer1.schedule(mTt1, 1, 1000);
    }

    private void showPoint(){


        Coordinate2D beacon_1 = new Coordinate2D(readSharedPreferences_float(Constants.b1_x),
                readSharedPreferences_float(Constants.b1_y));

        Coordinate2D beacon_2 = new Coordinate2D(readSharedPreferences_float(Constants.b2_x),
                readSharedPreferences_float(Constants.b2_y));

        Coordinate2D beacon_3 = new Coordinate2D(readSharedPreferences_float(Constants.b3_x),
                readSharedPreferences_float(Constants.b3_y));

        GraphMakerXY.drawGraph((ScatterChart)view.findViewById(R.id.chart),beacon_1,beacon_2,beacon_3);

        GraphMakerXY.addPoint(new Coordinate2D(LocationUpdatingService.pointX,LocationUpdatingService.pointY));
    }



    private float readSharedPreferences_float(String key){
        float defaultValue=0;

        float val=sharedpreferences.getFloat(key,defaultValue);
        return val;
    }

    private int readSharedPreferences_int(String key){
        int defaultValue=0;

        int val=sharedpreferences.getInt(key,defaultValue);
        return val;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction("GraphFragment");
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String title);
    }
}
