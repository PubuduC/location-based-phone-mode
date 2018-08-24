package company.com.locationfinder.PeriodicServices;

import android.app.IntentService;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import org.altbeacon.beacon.Beacon;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import company.com.locationfinder.BeaconManager.BeaconData;
import company.com.locationfinder.Constants;
import company.com.locationfinder.LocationFindingAlgorithm.Coordinate2D;
import company.com.locationfinder.LocationFindingAlgorithm.LocationFinder;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class PositionUpdatingService {



    public static Coordinate2D currentLocation;

    public static float pointX=0;
    public static float pointY=0;


    static String beacon1Major;
    static String beacon2Major;
    static String beacon3Major;
    static HashMap<String,Coordinate2D> selectedBeaconCoordinates;

    static SharedPreferences sharedpreferences;
    /////////////////////////////
    private int delay=1000;  // delay to start after calling startTimer in ms
    private int period=2000; // period in ms
    /////////////////////////////


    private static final String TAG = "PositionUpdatingService";
    private Timer timer;
    private TimerTask timerTask;
    private final Handler handler = new Handler();


    /*
     tasks needed to be run periodically
     */

    public void runPeriodically(){

        BeaconData.showBeaconData();

        HashMap<String, BeaconData.BeaconWithLastSeen> allBeacons = BeaconData.getFoundBeacons();

        updateBeaconKeysAndCoordinates();

        if(selectedBeaconCoordinates!=null && selectedBeaconCoordinates.size()>0) {
            double distanceFormB1 = allBeacons.containsKey(beacon1Major) ? allBeacons.get(beacon1Major).getBeacon().getDistance() : 0;
            double distanceFormB2 = allBeacons.containsKey(beacon2Major) ? allBeacons.get(beacon2Major).getBeacon().getDistance() : 0;
            double distanceFormB3 = allBeacons.containsKey(beacon3Major) ? allBeacons.get(beacon3Major).getBeacon().getDistance() : 0;


            currentLocation = LocationFinder.getLocation(selectedBeaconCoordinates.get(beacon1Major),
                    selectedBeaconCoordinates.get(beacon2Major), selectedBeaconCoordinates.get(beacon3Major),
                    distanceFormB1, distanceFormB2, distanceFormB3
            );

            if (!Double.isNaN(currentLocation.getX())) {
                pointX = currentLocation.getX();
                pointY = currentLocation.getY();
            }

            Log.d(TAG, "point:" + currentLocation.toString());
        }
    }



    public void init(Context context){
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);

        updateBeaconKeysAndCoordinates();

        timer=new Timer();
        initializeTimer();
        Log.d("lus","strted timer");
        startTimer();
    }

    private void initializeTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        runPeriodically();
                    }
                });

            }
        };
    }


    public void startTimer(){
        timer.schedule(timerTask,delay,period);
    }

    public void stopTimerTask() {
        if (timer != null)
            timer.cancel();
        timer = null;
    }

    private static HashMap<String, Coordinate2D> updateBeaconCoordinatesOfSelected3(){

        double b1x=RelatedBeaconAreaIdentifier.relatedAreaAround.getBeacon1x();
        double b1y=RelatedBeaconAreaIdentifier.relatedAreaAround.getBeacon1y();

        double b2x=RelatedBeaconAreaIdentifier.relatedAreaAround.getBeacon2x();
        double b2y=RelatedBeaconAreaIdentifier.relatedAreaAround.getBeacon2y();

        double b3x=RelatedBeaconAreaIdentifier.relatedAreaAround.getBeacon3x();
        double b3y=RelatedBeaconAreaIdentifier.relatedAreaAround.getBeacon3y();

        selectedBeaconCoordinates=new HashMap<>();

        selectedBeaconCoordinates.put(beacon1Major,new Coordinate2D(b1x,b1y));
        selectedBeaconCoordinates.put(beacon2Major,new Coordinate2D(b2x,b2y));
        selectedBeaconCoordinates.put(beacon3Major,new Coordinate2D(b3x,b3y));

        Log.d(TAG,selectedBeaconCoordinates.toString());
        return selectedBeaconCoordinates;
    }


    public static void updateBeaconKeysAndCoordinates(){
        if (RelatedBeaconAreaIdentifier.relatedAreaAround!=null) {
            beacon1Major = RelatedBeaconAreaIdentifier.relatedAreaAround.getBeacon1();
            beacon2Major =  RelatedBeaconAreaIdentifier.relatedAreaAround.getBeacon2();
            beacon3Major =  RelatedBeaconAreaIdentifier.relatedAreaAround.getBeacon3();

            updateBeaconCoordinatesOfSelected3();
        }else {
            selectedBeaconCoordinates=null;
        }
    }


    private static float readSharedPreferences_float(String key){
        float defaultValue=0;

        float val=sharedpreferences.getFloat(key,defaultValue);
        return val;
    }

    private static int readSharedPreferences_int(String key){
        int defaultValue=0;

        int val=sharedpreferences.getInt(key,defaultValue);
        return val;
    }

}
