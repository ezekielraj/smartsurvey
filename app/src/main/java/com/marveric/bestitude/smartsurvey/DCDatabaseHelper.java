package com.marveric.bestitude.smartsurvey;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DCDatabaseHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SmartSurvey2.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS DayCount ( " +
                    "id INTEGER PRIMARY KEY," +
                    "todaydate VARCHAR(30)," +
                    "onlinecount INT(10)," +
                    "offlinecount INT(10)," +
                    "synccount INT(10))";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS DayCount";

    public DCDatabaseHelper(Context context) {
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
