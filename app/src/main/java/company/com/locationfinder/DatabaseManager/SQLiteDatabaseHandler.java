package company.com.locationfinder.DatabaseManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import company.com.locationfinder.Location;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper{


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "LocationsDB";
    private static final String TABLE_NAME = "Locations";

    private static final String ID = "id";
    private static final String PLACE = "place";

    private static final String BEACON1_MAJOR = "beacon1";
    private static final String BEACON1_X = "beacon1x";
    private static final String BEACON1_Y = "beacon1y";

    private static final String BEACON2_MAJOR = "beacon2";
    private static final String BEACON2_X = "beacon2x";
    private static final String BEACON2_Y = "beacon2y";

    private static final String BEACON3_MAJOR = "beacon3";
    private static final String BEACON3_X = "beacon3x";
    private static final String BEACON3_Y = "beacon3y";

    private static final String CHANGING_MODE = "mode";

    private static final String[] COLUMNS = { ID, PLACE,
            BEACON1_MAJOR, BEACON1_X, BEACON1_Y,
            BEACON2_MAJOR, BEACON2_X, BEACON2_Y,
            BEACON3_MAJOR, BEACON3_X, BEACON3_Y,
            CHANGING_MODE
    };

    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable="CREATE TABLE "+TABLE_NAME+" ( "+
                ID+" INTEGER PRIMARY KEY, "+
                PLACE+" TEXT, "+
                BEACON1_MAJOR+" TEXT, "+ BEACON1_X+" REAL, "+ BEACON1_Y+" REAL, " +
                BEACON2_MAJOR+" TEXT, "+ BEACON2_X+" REAL, "+ BEACON2_Y+" REAL, "+
                BEACON3_MAJOR+" TEXT, "+ BEACON3_X+" REAL, "+ BEACON3_Y+" REAL, "+
                CHANGING_MODE+" TEXT )";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }

    public void addLocation(Location location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID, location.getId());
        values.put(PLACE, location.getPlace());

        values.put(BEACON1_MAJOR, location.getBeacon1());
        values.put(BEACON1_X, location.getBeacon1x());
        values.put(BEACON1_Y, location.getBeacon1y());

        values.put(BEACON2_MAJOR, location.getBeacon2());
        values.put(BEACON2_X, location.getBeacon2x());
        values.put(BEACON2_Y, location.getBeacon2y());

        values.put(BEACON3_MAJOR, location.getBeacon3());
        values.put(BEACON3_X, location.getBeacon3x());
        values.put(BEACON3_Y, location.getBeacon3y());

        values.put(CHANGING_MODE, location.getMode());

        db.insert(TABLE_NAME,null, values);
        db.close();
    }

    public int updateLocation(Location location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID, location.getId());
        values.put(PLACE, location.getPlace());

        values.put(BEACON1_MAJOR, location.getBeacon1());
        values.put(BEACON1_X, location.getBeacon1x());
        values.put(BEACON1_Y, location.getBeacon1y());

        values.put(BEACON2_MAJOR, location.getBeacon2());
        values.put(BEACON2_X, location.getBeacon2x());
        values.put(BEACON2_Y, location.getBeacon2y());

        values.put(BEACON3_MAJOR, location.getBeacon3());
        values.put(BEACON3_X, location.getBeacon3x());
        values.put(BEACON3_Y, location.getBeacon3y());

        values.put(CHANGING_MODE, location.getMode());

        int i = db.update(TABLE_NAME,values,ID+" = ?", new String[] { String.valueOf(location.getId()) });

        db.close();

        return i;
    }

    public List<Location> getAllLocations() {

        List<Location> locations = new ArrayList<>();
        String query = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Location location = null;

        if (cursor.moveToFirst()) {
            do {
                location = new Location();

                location.setId(Integer.parseInt(cursor.getString(0)));
                location.setPlace(cursor.getString(1));

                location.setBeacon1(cursor.getString(2));
                location.setBeacon1x(Float.parseFloat(cursor.getString(3)));
                location.setBeacon1y(Float.parseFloat(cursor.getString(4)));

                location.setBeacon2(cursor.getString(5));
                location.setBeacon2x(Float.parseFloat(cursor.getString(6)));
                location.setBeacon2y(Float.parseFloat(cursor.getString(7)));

                location.setBeacon3(cursor.getString(8));
                location.setBeacon3x(Float.parseFloat(cursor.getString(9)));
                location.setBeacon3y(Float.parseFloat(cursor.getString(10)));

                location.setMode(cursor.getString(11));


                locations.add(location);
            } while (cursor.moveToNext());
        }

        return locations;
    }

    public Location getLocation(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, // a. table
                COLUMNS, // b. column names
                ID+" = ?", // c. selections
                new String[] { String.valueOf(id) }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        if (cursor != null)
            cursor.moveToFirst();

        Location location = new Location();

        location.setId(Integer.parseInt(cursor.getString(0)));
        location.setPlace(cursor.getString(1));

        location.setBeacon1(cursor.getString(2));
        location.setBeacon1x(Float.parseFloat(cursor.getString(3)));
        location.setBeacon1y(Float.parseFloat(cursor.getString(4)));

        location.setBeacon2(cursor.getString(5));
        location.setBeacon2x(Float.parseFloat(cursor.getString(6)));
        location.setBeacon2y(Float.parseFloat(cursor.getString(7)));

        location.setBeacon3(cursor.getString(8));
        location.setBeacon3x(Float.parseFloat(cursor.getString(9)));
        location.setBeacon3y(Float.parseFloat(cursor.getString(10)));

        location.setMode(cursor.getString(11));


        return location;
    }



}
