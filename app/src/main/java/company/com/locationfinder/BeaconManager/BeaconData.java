package company.com.locationfinder.BeaconManager;

import android.util.Log;

import org.altbeacon.beacon.Beacon;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class BeaconData {

    private static final String TAG = "BeaconData";
//    private static HashMap<Integer,Beacon> beacons=new HashMap<>();
    private static HashMap<Integer,BeaconWithLastSeen> beaconWithLastSeen = new HashMap<>();
    private static final int BEACON_MAP_CLEANING_INTERVAL=5000;

    public static HashMap<Integer, BeaconWithLastSeen> getFoundBeacons() {
        return beaconWithLastSeen;
    }

    public static void setBeacons(Collection<Beacon> beacons) {
//        BeaconData.beacons.clear();
        removeLastSeenLimitExceededBeacons();
        for (Beacon beacon: beacons
             ) {
            BeaconWithLastSeen beaconwt=new BeaconWithLastSeen(beacon,System.currentTimeMillis());

            BeaconData.beaconWithLastSeen.put(beacon.getId2().toInt(),beaconwt);
        }
        Log.d("BEACON DATA","beacons:"+BeaconData.getFoundBeacons().size());
    }


    public static void showBeaconData(){
        String data="num of beacons:"+ beaconWithLastSeen.size()+"\n";
        TreeMap<Integer,BeaconWithLastSeen> treeMappedBeacons=new TreeMap<>(beaconWithLastSeen);
        for(Map.Entry entry:treeMappedBeacons.entrySet()){
            data+="key: "+entry.getKey()+"  distance:"+((BeaconWithLastSeen)entry.getValue()).getBeacon().getDistance()+"\n";
        }
        Log.i(TAG,data);
    }

    private static void removeLastSeenLimitExceededBeacons(){
        long now=System.currentTimeMillis();
        for (Map.Entry beaconwls: beaconWithLastSeen.entrySet()
             ) {
            long timegap=(now-((BeaconWithLastSeen)beaconwls.getValue()).getTime());
            if (timegap>BEACON_MAP_CLEANING_INTERVAL){
                beaconWithLastSeen.remove(beaconwls.getKey());
            }
        }
    }

    public static class BeaconWithLastSeen{

        public BeaconWithLastSeen(Beacon beacon, long time) {
            this.beacon = beacon;
            this.time = time;
        }

        Beacon beacon;
        long time;

        public Beacon getBeacon() {
            return beacon;
        }

        public long getTime() {
            return time;
        }
    }
}
