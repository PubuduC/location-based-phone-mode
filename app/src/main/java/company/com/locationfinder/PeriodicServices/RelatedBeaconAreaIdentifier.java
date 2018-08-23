package company.com.locationfinder.PeriodicServices;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import company.com.locationfinder.BeaconManager.BeaconData;
import company.com.locationfinder.DatabaseManager.SQLiteDatabaseHandler;
import company.com.locationfinder.Location;

public class RelatedBeaconAreaIdentifier extends Service {

    private static final String TAG = "RelatedBAreaIdentifier";

    /////////////////////////////
    private int delay=1000;  // delay to start after calling startTimer in ms
    private int period=2000; // period in ms
    /////////////////////////////

    private Timer timer;
    private TimerTask timerTask;
    private final Handler handler = new Handler();

    SQLiteDatabaseHandler db;


    private void runPeriodically() {

        HashMap<Integer, BeaconData.BeaconWithLastSeen> foundBeacons = BeaconData.getFoundBeacons();

        if(foundBeacons!=null) {
            List<Location> allRelatedLocations = db.getAllLocations();
            Location relatedArea = isRelatedArea(foundBeacons, allRelatedLocations);
            if (relatedArea != null) {
                handleRelatedArea(relatedArea);
            } else {
                Log.d(TAG, "any related areas were not found yet");
            }
        }else {
            Log.d(TAG, "no beacons here");
        }
    }

    private void handleRelatedArea(Location relatedArea) {

    }


    private Location isRelatedArea(HashMap<Integer,BeaconData.BeaconWithLastSeen> foundBeacons, List<Location> allRelatedLocations){
        for (Location location:allRelatedLocations
             ) {
            int match=0;
            for(int foundkey:foundBeacons.keySet()){
                if (Integer.toString(foundkey).equals(location.getBeacon1()) ||
                        Integer.toString(foundkey).equals(location.getBeacon2()) ||
                        Integer.toString(foundkey).equals(location.getBeacon3())){

                    match+=1;
                }
            }
            Log.d(TAG,"matching beacons for "+location.getPlace()+" : "+match);
            if(match==3){
                return location;
            }
        }
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        db=new SQLiteDatabaseHandler(getBaseContext());
        init();
    }

    public void init(){

        timer=new Timer();
        initializeTimer();
        Log.d(TAG,"strted timer");
        startTimer();
    }

    public void startTimer(){
        timer.schedule(timerTask,delay,period);
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

    public void stopTimerTask() {
        if (timer != null)
            timer.cancel();
        timer = null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
