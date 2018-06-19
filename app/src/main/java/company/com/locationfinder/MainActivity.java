package company.com.locationfinder;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import company.com.locationfinder.fragments.BeaconFragment;
import company.com.locationfinder.fragments.GraphFragment;
import company.com.locationfinder.fragments.LocationPointFragment;
import company.com.locationfinder.fragments.SettingsFragment;
import company.com.locationfinder.fragments.dummy.DummyContent;

import static company.com.locationfinder.Constants.SHARED_PREFERENCES;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,

        GraphFragment.OnFragmentInteractionListener,
        LocationPointFragment.OnFragmentInteractionListener,
        BeaconFragment.OnListFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener{

    private static final String TAG ="Main activity";

    private String title;

    public NavigationView navigationView;

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
        ft.replace(R.id.mainFrame, new GraphFragment());
        ft.commit();
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
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    @Override
    public void onFragmentInteraction(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    private void writeSharedPreferences(String key,float value){
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    private void writeSharedPreferences(String key,int value){
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    private float readSharedPreferences_float(String key){
        float defaultValue=0;
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(SHARED_PREFERENCES,Context.MODE_PRIVATE);
        float val=sharedPref.getFloat(key,defaultValue);
        return val;
    }

    private float readSharedPreferences_int(String key){
        int defaultValue=0;
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(SHARED_PREFERENCES,Context.MODE_PRIVATE);
        int val=sharedPref.getInt(key,defaultValue);
        return val;
    }
}