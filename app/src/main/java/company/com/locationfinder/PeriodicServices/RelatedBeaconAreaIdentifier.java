package company.com.locationfinder.PeriodicServices;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
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

    private static final String TAG = "RBAI";
    public static Location relatedAreaAround;

    /////////////////////////////
    private int delay=1000;  // delay to start after calling startTimer in ms
    private int period=2000; // period in ms
    /////////////////////////////

    private Timer timer;
    private TimerTask timerTask;
    private final Handler handler = new Handler();

    SQLiteDatabaseHandler db;

    AudioManager audiomanager;
    Context context;

    private void runPeriodically() {

        HashMap<String, BeaconData.BeaconWithLastSeen> foundBeacons = BeaconData.getFoundBeacons();

        if(foundBeacons.size()!=0) {
            List<Location> allRelatedLocations = db.getAllLocations();
            Location relatedArea = isRelatedArea(foundBeacons, allRelatedLocations);
            if (relatedArea != null) {
                relatedAreaAround=relatedArea;
                handleRelatedArea(relatedArea,foundBeacons);
                Log.d(TAG, "!!!!!!!!! matching with "+relatedArea.getPlace()+"!!!!!!!!!");
            } else {
                relatedAreaAround=null;
                Log.d(TAG, "any related areas were not found yet");
            }
        }else {
            Log.d(TAG, "no beacons here");
        }
    }



    private void handleRelatedArea(Location relatedArea,  HashMap<String, BeaconData.BeaconWithLastSeen> foundBeacons) {


//        try {
//            audiomanager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
//
//        }catch(Exception e){
//            e.printStackTrace();
//        }
        if(isInsideTheReactangle() && isThereALeadingBeacon()){
            Log.d(TAG,"inside the "+ relatedAreaAround.getPlace()+"  and there is a leading beacon");
            ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 200);
            toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP,500);

            changeRingerModeToSilent();

        }else if(isInsideTheReactangle()){
            Log.d(TAG,"inside the "+ relatedAreaAround.getPlace());
            ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 200);
            toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP,100);

            changeRingerModeToNormal();
        }
        else {
            Log.d(TAG,"neer to "+ relatedAreaAround.getPlace()+" but not inside");
            changeRingerModeToNormal();
        }

    }

    private boolean isThereALeadingBeacon() {
        if(BeaconData.getFoundBeacons().size()>3){
            return true;
        }else{
            return false;
        }
    }

    private boolean isInsideTheReactangle(){
        double b1x = relatedAreaAround.getBeacon1x();
        double b1y = relatedAreaAround.getBeacon1y();

        double b2x = relatedAreaAround.getBeacon2x();
        double b2y = relatedAreaAround.getBeacon2y();

        double b3x = relatedAreaAround.getBeacon3x();
        double b3y = relatedAreaAround.getBeacon3y();

        double px = PositionUpdatingService.pointX;
        double py = PositionUpdatingService.pointY;


        //keep b1 and b2 in same y cordinate and b3 above in y cordinate. (b1y=b2y  and b3y>b1y also b1x<=b3x<=b2x)
        if(py>=b1y && py<=b3y && px>=b1x && px<=b2x){
            //p is inside the rectangle
            return true;
        }else {
            return false;
        }

    }


    private Location isRelatedArea(HashMap<String,BeaconData.BeaconWithLastSeen> foundBeacons, List<Location> allRelatedLocations){
        for (Location location:allRelatedLocations
             ) {
            int match=0;
            Log.d(TAG +"found",foundBeacons.keySet().toString());
            Log.d(TAG+"location",location.getBeacon1()+" "+location.getBeacon2()+" "+location.getBeacon3());

            for(String foundkey:foundBeacons.keySet()){
                if (foundkey.equals(location.getBeacon1()) ||
                     foundkey.equals(location.getBeacon2()) ||
                        foundkey.equals(location.getBeacon3())){

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

    AudioManager mAudio;

    private void changeRingerModeToSilent(){
        mAudio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
    }

    private void changeRingerModeToNormal(){
        mAudio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        db=new SQLiteDatabaseHandler(getBaseContext());
        mAudio = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        context = getApplicationContext();
        audiomanager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

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
