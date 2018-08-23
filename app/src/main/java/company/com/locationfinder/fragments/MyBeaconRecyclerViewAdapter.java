package company.com.locationfinder.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;

import company.com.locationfinder.BeaconManager.BeaconWrapper;
import company.com.locationfinder.LocationFindingAlgorithm.Util2D;
import company.com.locationfinder.R;
import company.com.locationfinder.fragments.BeaconFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link BeaconWrapper} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyBeaconRecyclerViewAdapter extends RecyclerView.Adapter<MyBeaconRecyclerViewAdapter.ViewHolder> {

    private final List<Beacon> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyBeaconRecyclerViewAdapter(List<Beacon> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_beacon, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.beacon_major.setText(String.valueOf(mValues.get(position).getId2()));
        holder.beacon_minor.setText(String.valueOf(mValues.get(position).getId3()));
        holder.beacon_uid.setText(String.valueOf(mValues.get(position).getId1()));

        holder.beacon_distance.setText(String.valueOf(Util2D.round3deci(mValues.get(position).getDistance())));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView beacon_major;
        public final TextView beacon_distance;
        public final TextView beacon_minor;
        public final TextView beacon_uid;


        public Beacon mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            beacon_major = (TextView) view.findViewById(R.id.beacon_major);
            beacon_distance = (TextView) view.findViewById(R.id.distance);
            beacon_minor = (TextView) view.findViewById(R.id.minor);
            beacon_uid = (TextView) view.findViewById(R.id.uid);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + beacon_distance.getText() + "'";
        }
    }
}
