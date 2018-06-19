package company.com.locationfinder.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import company.com.locationfinder.Constants;
import company.com.locationfinder.MainActivity;
import company.com.locationfinder.R;

import static company.com.locationfinder.Constants.*;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "SettingsFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private View view;
    private SharedPreferences sharedpreferences;
    private SettingsFragment this_=this;

    EditText b1major;
    EditText b1x;
    EditText b1y;
    EditText b2major;
    EditText b2x;
    EditText b2y;
    EditText b3major;
    EditText b3x;
    EditText b3y;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        sharedpreferences=getActivity().getSharedPreferences(SHARED_PREFERENCES,Context.MODE_PRIVATE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_settings, container, false);

        b1major = ((EditText) view.findViewById(R.id.beacon1_major_value));
        b1x = ((EditText) view.findViewById(R.id.beacon1_x_value));
        b1y = ((EditText) view.findViewById(R.id.beacon1_y_value));

        b2major = ((EditText) view.findViewById(R.id.beacon2_major_value));
        b2x = ((EditText) view.findViewById(R.id.beacon2_x_value));
        b2y = ((EditText) view.findViewById(R.id.beacon2_y_value));

        b3major = ((EditText) view.findViewById(R.id.beacon3_major_value));
        b3x = ((EditText) view.findViewById(R.id.beacon3_x_value));
        b3y = ((EditText) view.findViewById(R.id.beacon3_y_value));

        Button btn=((Button)view.findViewById(R.id.save_settings_btn));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateBeaconPositions(view);
            }
        });
        setCurrentSavedPositionsToSettingsView(view);

        return view;
    }

    private class MyTask extends AsyncTask<String, Void, Organization> {

        @Override
        protected Organization doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPostExecute(Organization organization) {
            super.onPostExecute(organization);
            setCurrentSavedPositionsToSettingsView(view);

        }
    }


    private void setCurrentSavedPositionsToSettingsView(View view) {

        if (sharedpreferences.contains(Constants.b1_major)) {
            b1major.setText(readSharedPreferences_int(Constants.b1_major));
        }
        if (sharedpreferences.contains(Constants.b1_x))
            ((EditText)view.findViewById(R.id.beacon1_x_value)).setText(readSharedPreferences_float(Constants.b1_x));

        if (sharedpreferences.contains(Constants.b1_y))
            ((EditText)view.findViewById(R.id.beacon1_y_value)).setText(readSharedPreferences_float(Constants.b1_y));

        if (sharedpreferences.contains(Constants.b2_major))
            ((EditText)view.findViewById(R.id.beacon2_major_value)).setText(readSharedPreferences_int(Constants.b2_major));

        if (sharedpreferences.contains(Constants.b2_x))
            ((EditText)view.findViewById(R.id.beacon2_x_value)).setText(readSharedPreferences_float(Constants.b2_x));

        if (sharedpreferences.contains(Constants.b2_y))
            ((EditText)view.findViewById(R.id.beacon2_y_value)).setText(readSharedPreferences_float(Constants.b2_y));

        if (sharedpreferences.contains(Constants.b3_major))
            ((EditText)view.findViewById(R.id.beacon3_major_value)).setText(readSharedPreferences_int(Constants.b3_major));

        if (sharedpreferences.contains(Constants.b3_x))
            ((EditText)view.findViewById(R.id.beacon3_x_value)).setText(readSharedPreferences_float(Constants.b3_x));

        if (sharedpreferences.contains("b3_y"))
            ((EditText)view.findViewById(R.id.beacon3_y_value)).setText(readSharedPreferences_float("b3_y"));


        Log.d(TAG,"Beacon positions an major keys added to settings fragment from storage");

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
    public void onClick(View view) {
        updateBeaconPositions(view);
    }


    public void updateBeaconPositions(View view) {

        int b1_major;
        if (b1major.getText().toString().equals("")) {
            b1_major = 0;
        } else {
            b1_major = Integer.parseInt(b1major.getText().toString());
        }
        writeSharedPreferences(Constants.b1_major,b1_major);


        float b1_x;
        if (b1x.getText().toString().equals("")) {
            b1_x = 0f;
        } else {
            b1_x = Float.parseFloat(b1x.getText().toString());
        }
        writeSharedPreferences(Constants.b1_x,b1_x);


        float b1_y;
        if (b1y.getText().toString().equals("")) {
            b1_y = 0f;
        } else {
            b1_y = Float.parseFloat(b1y.getText().toString());
        }
        writeSharedPreferences(Constants.b1_y,b1_y);

        int b2_major;
        if (b2major.getText().toString().equals("")) {
            b2_major = 0;
        } else {
            b2_major = Integer.parseInt(b2major.getText().toString());
        }
        writeSharedPreferences(Constants.b2_major,b2_major);


        float b2_x;
        if (b2x.getText().toString().equals("")) {
            b2_x = 0f;
        } else {
            b2_x = Float.parseFloat(b2x.getText().toString());
        }
        writeSharedPreferences(Constants.b2_x,b2_x);


        float b2_y;
        if (b2y.getText().toString().equals("")) {
            b2_y= 0f;
        } else {
            b2_y = Float.parseFloat(b2y.getText().toString());
        }
        writeSharedPreferences(Constants.b2_y,b2_y);

        int b3_major;
        if (b3major.getText().toString().equals("")) {
            b3_major = 0;
        } else {
            b3_major = Integer.parseInt(b3major.getText().toString());
        }
        writeSharedPreferences( Constants.b3_major,b3_major);

        float b3_x;
        if (b3x.getText().toString().equals("")) {
            b3_x = 0f;
        } else {
            b3_x = Float.parseFloat(b3x.getText().toString());
        }
        writeSharedPreferences(Constants.b3_x,b3_x);

        float b3_y;
        if (b3y.getText().toString().equals("")) {
            b3_y = 0f;
        } else {
            b3_y = Float.parseFloat(b3y.getText().toString());
        }
        writeSharedPreferences("b3_y",b3_y);

        Log.d(TAG,"Beacon positions and major keys changed");

        ((MainActivity)getActivity()).navigateToGraph();

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
        void onFragmentInteraction(Uri uri);
    }


    private void writeSharedPreferences(String key,float value){

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    private void writeSharedPreferences(String key,int value){

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }
    private String readSharedPreferences_float(String key){
        float defaultValue=0;

        float val=sharedpreferences.getFloat(key,defaultValue);
        return String.valueOf(val);
    }

    private String readSharedPreferences_int(String key){
        int defaultValue=0;

        int val=sharedpreferences.getInt(key,defaultValue);
        return String.valueOf(val);
    }

//    private String readSharedPreferences_int(String key){
//        int defaultValue=0;
//
//        int val=sharedpreferences.getInt(key,defaultValue);
//        return String.valueOf(val);
//    }
}
