package com.example.datereminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String EVENT_TABLE = "EVENT_TABLE";
    public static final String ID = "ID";
    public static final String EVENT_NAME = "EVENT_NAME";
    public static final String EVENT_DATE = "EVENT_DATE";
    public static final String EVENT_REMIND = "EVENT_REMIND";

    // Constructor wth hardcoded values instead of passing in parameter, except the context
    public DatabaseHelper(@Nullable Context context) {
        super(context, "event.db", null, 1);
    }

    // Called first time a database is accessed. Code here to create a new database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableStatement = "CREATE TABLE " + EVENT_TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + EVENT_NAME + " TEXT, " + EVENT_DATE + " INTEGER, " + EVENT_REMIND + " INTEGER)";

        sqLiteDatabase.execSQL(createTableStatement);
    }

    // Used when changing versions of the database - upgrade or downgrade
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    // Method for adding rows
    public boolean addOne(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(EVENT_NAME, event.getName());
        cv.put(EVENT_DATE, event.getEventDate());
        cv.put(EVENT_REMIND, event.getWillRemind() ? 1: 0);

        long insert = db.insert(EVENT_TABLE, null, cv);
        if (insert == -1) {
            return false;
        }
        else {
            return true;
        }

    }

    // TODO: Create an editOne method, where user can edit one row on database by passing an object's ID

    public boolean editOne(int id, String eventName, Long eventDate, boolean eventRemind) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        int eventRemindVar = eventRemind ? 1 : 0;


        String query = "UPDATE " + EVENT_TABLE +
                " SET " + EVENT_NAME + " = " + "'" + eventName + "'" + ", "
                + EVENT_DATE + " = " + eventDate + ", "
                + EVENT_REMIND + " = " + eventRemindVar +
                " WHERE " + ID + " = " + id + ";";

        db.execSQL(query);
        db.close();
        return true;
    }

    public boolean deleteOne(Event event) {
        // find event in database, if found delete and return true
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + EVENT_TABLE + " WHERE " + ID + " = " + event.getId();

        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            return true;
        }
        else {
            return false;
        }
    }


    // Method for returning all events in a List
    public List<Event> getAllEvents() {

        List<Event> returnList = new ArrayList<>();
        // get data from the database

        String queryString = "SELECT * FROM " + EVENT_TABLE;

        // Writable database will lock the database since it is used for updating, inserting, deleting. Use readable instead, even though writeable may work
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        // move to first returns boolean
        if (cursor.moveToFirst()) {
            // loop through the results, create new event object for each row. insert into the return list
            do {
                // pass column index as a parameter
                int id = cursor.getInt(0);
                String eventName = cursor.getString(1);
                Long eventDate = cursor.getLong(2);
                boolean eventRemind = cursor.getInt(3) == 1;

                Event event = new Event(id, eventName, eventDate, eventRemind);
                returnList.add(event);
            } while (cursor.moveToNext());

        }
        else {
            // No rows in database
        }
        return returnList;

    }
}
