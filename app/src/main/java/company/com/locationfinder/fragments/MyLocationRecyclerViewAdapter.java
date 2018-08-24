package company.com.locationfinder.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import company.com.locationfinder.Location;
import company.com.locationfinder.R;
import company.com.locationfinder.fragments.LocationFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Location} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyLocationRecyclerViewAdapter extends RecyclerView.Adapter<MyLocationRecyclerViewAdapter.ViewHolder> {

    private final List<Location> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyLocationRecyclerViewAdapter(List<Location> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_location, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mId.setText(String.valueOf(mValues.get(position).getId()));
        holder.mPlace.setText(String.valueOf(mValues.get(position).getPlace()));
        holder.mBeacon1.setText(String.valueOf(mValues.get(position).getBeacon1()));
        holder.mBeacon2.setText(String.valueOf(mValues.get(position).getBeacon2()));
        holder.mBeacon3.setText(String.valueOf(mValues.get(position).getBeacon3()));

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
        public final TextView mId;
        public final TextView mPlace;
        public final TextView mBeacon1;
        public final TextView mBeacon2;
        public final TextView mBeacon3;

        public Location mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mId = (TextView) view.findViewById(R.id.id);
            mPlace = (TextView) view.findViewById(R.id.place);
            mBeacon1 = (TextView) view.findViewById(R.id.beacon1);
            mBeacon2 = (TextView) view.findViewById(R.id.beacon2);
            mBeacon3 = (TextView) view.findViewById(R.id.beacon3);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mPlace.getText() + "'";
        }
    }
}
