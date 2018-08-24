package company.com.locationfinder;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.altbeacon.beacon.Beacon;
import org.json.JSONObject;

import company.com.locationfinder.BeaconManager.BeaconData;
import company.com.locationfinder.BeaconManager.BeaconService;
import company.com.locationfinder.DatabaseManager.SQLiteDatabaseHandler;
import company.com.locationfinder.PeriodicServices.PositionUpdatingService;
import company.com.locationfinder.PeriodicServices.RelatedBeaconAreaIdentifier;
import company.com.locationfinder.fragments.BeaconFragment;
import company.com.locationfinder.fragments.GraphFragment;
import company.com.locationfinder.fragments.LocationAdderFragment;
import company.com.locationfinder.fragments.LocationFragment;
import company.com.locationfinder.fragments.LocationPointFragment;
import company.com.locationfinder.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity
        implements

        NavigationView.OnNavigationItemSelectedListener,

        GraphFragment.OnFragmentInteractionListener,
        LocationPointFragment.OnFragmentInteractionListener,
        BeaconFragment.OnListFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener,
        LocationAdderFragment.OnFragmentInteractionListener,
        LocationFragment.OnListFragmentInteractionListener

        {


    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    private static final String TAG ="Main activity";



    private String title;

    public NavigationView navigationView;


    private SQLiteDatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //NOTE:  Checks first item in the navigation drawer initially
        navigationView.setCheckedItem(R.id.graph);

        //NOTE:  Open fragment1 initially.
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, new LocationAdderFragment());
        ft.commit();



        getPermission();

        bluetoothStartup();

        startBeaconScanningService();

        startLocationUpdatingService();

        startRelatedBeaconAreaIdentifier();

        Intent intentex=getIntent();

        db=new SQLiteDatabaseHandler(this);

        if (intentex.getExtras()!=null){
            String data=intentex.getExtras().getString("apidata");
            saveLocationDataWithMode(data);
        }

    }

    private final static int REQUEST_ENABLE_BT = 1;

    private void bluetoothStartup(){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    private void saveLocationDataWithMode(String data){
        try {

            JSONObject obj = new JSONObject(data);

            Location location=new Location(
                    obj.getInt("id"),
                    obj.getString("place"),
                    obj.getJSONObject("beacon1").getString("uuid"),
                    obj.getJSONObject("beacon1").getDouble("x"),
                    obj.getJSONObject("beacon1").getDouble("y"),
                    obj.getJSONObject("beacon2").getString("uuid"),
                    obj.getJSONObject("beacon2").getDouble("x"),
                    obj.getJSONObject("beacon2").getDouble("y"),
                    obj.getJSONObject("beacon3").getString("uuid"),
                    obj.getJSONObject("beacon3").getDouble("x"),
                    obj.getJSONObject("beacon3").getDouble("y"),
                    "silent"
            );

            Log.d("Location obj",location.toString());

            db.addLocation(location);

        } catch (Throwable t) {

            t.printStackTrace();

        }


    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        setupBeaconScanner();
//    }

    public void startBeaconScanningService() {
//        Intent i = new Intent(this, BeaconScannerService.class);
//        startService(i);
//        setupBeaconScanner();
        startService(new Intent(this, BeaconService.class));

    }

    public void startLocationUpdatingService() {
        new PositionUpdatingService().init(this);
    }

    public void startRelatedBeaconAreaIdentifier(){
        startService(new Intent(this, RelatedBeaconAreaIdentifier.class));
//        new RelatedBeaconAreaIdentifier().init(this);
    }

    public void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check 
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    //                    @Override 
                    @TargetApi(Build.VERSION_CODES.M)
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();
            }
            NotificationManager notificationManager =
                    (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            if(!notificationManager.isNotificationPolicyAccessGranted()) {

                Intent intent = new Intent(
                        android.provider.Settings
                                .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

                startActivity(intent);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }
                    });
                    builder.show();
                }
                return;
            }
        }
    }




    public void navigateToGraph(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, new GraphFragment());
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;

        if (id == R.id.graph) {
            fragment = new GraphFragment();

        } else if (id == R.id.location_point) {
            fragment=new LocationPointFragment();

        } else if (id == R.id.beacon_setup) {
            fragment = new SettingsFragment();

        } else if (id == R.id.check_beacons) {
            fragment = new BeaconFragment();

        } else if (id == R.id.addlocation){
            fragment = new LocationAdderFragment();

        } else if (id == R.id.locations){
            fragment =new LocationFragment();
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    private void writeSharedPreferences(String key,float value){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    private void writeSharedPreferences(String key,int value){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    private float readSharedPreferences_float(String key){
        float defaultValue=0;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        float val=sharedPref.getFloat(key,defaultValue);
        return val;
    }

    private float readSharedPreferences_int(String key){
        int defaultValue=0;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        int val=sharedPref.getInt(key,defaultValue);
        return val;
    }


    ///////////Beacon scanning/////////////
//    private BeaconManager beaconManager;
//    private static final String TAG_BEACON_SCAN ="Beacon Scanner";
//
//    private void setupBeaconScanner(){
//        beaconManager = BeaconManager.getInstanceForApplication(this);
////
//        beaconManager.getBeaconParsers().add(new BeaconParser()
//                .setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT));
////
//        beaconManager.getBeaconParsers().add(new BeaconParser()
//                .setBeaconLayout(BeaconParser.EDDYSTONE_URL_LAYOUT));
//
//        beaconManager.getBeaconParsers().add(new BeaconParser()
//                .setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
//
//
//
//
//        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
//        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
//        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));
//
//        beaconManager.bind(this);
//    }
//
//    @Override
//    public void onBeaconServiceConnect() {
////        Identifier.parse("B9407F30-F5F8-466E-AFF9-25556B57FE6D")
//        final Region region = new Region("myBeaons",null, null, null);
//
//        beaconManager.setMonitorNotifier(new MonitorNotifier() {
//            @Override
//            public void didEnterRegion(Region region) {
//                try {
//                    Log.d(TAG_BEACON_SCAN, "didEnterRegion");
//                    beaconManager.startRangingBeaconsInRegion(region);
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void didExitRegion(Region region) {
//                try {
//                    Log.d(TAG_BEACON_SCAN, "didExitRegion");
//                    beaconManager.stopRangingBeaconsInRegion(region);
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void didDetermineStateForRegion(int i, Region region) {
//
//            }
//        });
////
//        final HashMap<Integer,Beacon> beaconsmap=new HashMap<>();
//
//        beaconManager.setRangeNotifier(new RangeNotifier() {
//            @Override
//            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
//                for(Beacon beacon : beacons) {
//                    Log.d(TAG_BEACON_SCAN, "distance: " + beacon.getDistance() + " id:" + beacon.getId1() + "/" + beacon.getId2() + "/" + beacon.getId3());
////                    beaconsmap.put(beacon.getId2().toInt(),beacon);
////                    BeaconData.addToHashMap(beacon.getId2().toInt(),beacon);
//                }
//                BeaconData.setBeacons(beacons);
//            }
//        });
//
//        try {
//            beaconManager.startMonitoringBeaconsInRegion(region);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (beaconManager!=null){
//            if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(true);
//        }
//    }
//
    @Override
    protected void onResume() {
        super.onResume();
//        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(false);
//        else beaconManager.bind(this);
        startService(new Intent(this, BeaconService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        beaconManager.unbind(this);
    }

            @Override
            public void onListFragmentInteraction(BeaconData.BeaconWithLastSeen item) {

            }

            @Override
            public void onListFragmentInteraction(Location item) {


            }
        }