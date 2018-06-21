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
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import company.com.locationfinder.BeaconManager.BeaconData;
import company.com.locationfinder.Constants;
import company.com.locationfinder.LocationFindingAlgorithm.Coordinate2D;
import company.com.locationfinder.LocationFindingAlgorithm.Util2D;
import company.com.locationfinder.LocationUpdatingService;
import company.com.locationfinder.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocationPointFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocationPointFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationPointFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private View view;
    private TextView textView;
    private SharedPreferences sharedpreferences;

    public LocationPointFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocationPointFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationPointFragment newInstance(String param1, String param2) {
        LocationPointFragment fragment = new LocationPointFragment();
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
        view= inflater.inflate(R.layout.fragment_location_point, container, false);

        textView=(TextView)view.findViewById(R.id.beaconDetails);

        startTimer();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction("LocationPointFragment");
        }
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
                        showData();
                    }
                });
            }
        };

        mTimer1.schedule(mTt1, 1, 1000);
    }

    private void showData()  {

        Coordinate2D beacon_1 = new Coordinate2D(readSharedPreferences_float(Constants.b1_x),
                readSharedPreferences_float(Constants.b1_y));
        int beacon_1_major=readSharedPreferences_int(Constants.b1_major);
        double b1_distance= BeaconData.getBeacons().get(beacon_1_major)==null? 0:BeaconData.getBeacons().get(beacon_1_major).getDistance();

        Coordinate2D beacon_2 = new Coordinate2D(readSharedPreferences_float(Constants.b2_x),
                readSharedPreferences_float(Constants.b2_y));
        int beacon_2_major=readSharedPreferences_int(Constants.b2_major);
        double b2_distance= BeaconData.getBeacons().get(beacon_2_major)==null? 0:BeaconData.getBeacons().get(beacon_2_major).getDistance();

        Coordinate2D beacon_3 = new Coordinate2D(readSharedPreferences_float(Constants.b3_x),
                readSharedPreferences_float(Constants.b3_y));
        int beacon_3_major=readSharedPreferences_int(Constants.b3_major);
        double b3_distance= BeaconData.getBeacons().get(beacon_3_major)==null? 0:BeaconData.getBeacons().get(beacon_3_major).getDistance();

        String data="";

       data+="beacon 1 :  ["+beacon_1.toRoundedString()+"]" + "  d="+ Util2D.round3deci(b1_distance)+"m\n\n";
       data+="beacon 2 :  ["+beacon_2.toRoundedString()+"]" + "  d="+Util2D.round3deci(b2_distance)+"m\n\n";
       data+="beacon 3 :  ["+beacon_3.toRoundedString()+"]" + "  d="+Util2D.round3deci(b3_distance)+"m\n\n";

           data += "point : x=" + LocationUpdatingService.pointX;
           data += "  y=" + LocationUpdatingService.pointY;


        textView.setText(String.valueOf(data));

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
    public void onDestroy() {
        stopTimer();
        super.onDestroy();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String title);
    }
}
