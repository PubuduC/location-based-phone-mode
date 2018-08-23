package company.com.locationfinder.BeaconManager;

import android.util.Log;

import org.altbeacon.beacon.Beacon;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class BeaconData {

    private static final String TAG = "BeaconData";
    private static HashMap<Integer,Beacon> beacons=new HashMap<>();
    private static HashMap<Integer, BeaconWrapper> beaconWrapers=new HashMap<>();

    public static HashMap<Integer, Beacon> getFoundBeacons() {
        return beacons;
    }

    public static void setBeacons(Collection<Beacon> beacons) {
        BeaconData.beacons.clear();
        for (Beacon beacon: beacons
             ) {
            BeaconData.beacons.put(beacon.getId2().toInt(),beacon);
        }
        Log.d("BEACON DATA","beacons:"+BeaconData.getFoundBeacons().size());
    }

    public static HashMap<Integer, BeaconWrapper> getBeaconWrapers() {
        return beaconWrapers;
    }

    public static void setBeaconWrapers(HashMap<Integer, BeaconWrapper> beaconWrapers) {
        BeaconData.beaconWrapers = beaconWrapers;
    }

    public static void showBeaconData(){
        String data="num of beacons:"+ beacons.size()+"\n";
        TreeMap<Integer,Beacon> treeMappedBeacons=new TreeMap<>(beacons);
        for(Map.Entry entry:treeMappedBeacons.entrySet()){
            data+="key: "+entry.getKey()+"  distance:"+((Beacon)entry.getValue()).getDistance()+"\n";
        }
        Log.i(TAG,data);
    }

    public static void addToHashMap(int i, Beacon beacon) {

        beacons.put(i,beacon);

        // TODO: 6/17/18 check if existing add other attributes to the wraper
        BeaconWrapper wraper = new BeaconWrapper(beacon);
        wraper.setKey(i);
        beaconWrapers.put(i,wraper);

    }
}
