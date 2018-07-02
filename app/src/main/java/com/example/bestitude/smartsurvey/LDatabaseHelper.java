package com.example.bestitude.smartsurvey;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LDatabaseHelper extends SQLiteOpenHelper {


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SmartSurvey1.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE Survey ( " +
                    "slno INTEGER," +
                    "District VARCHAR(255)," +
                    "Locality VARCHAR(255)," +
                    "State VARCHAR(255)," +
                    "EndDate VARCHAR(255))";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS Survey";


    public LDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

}
