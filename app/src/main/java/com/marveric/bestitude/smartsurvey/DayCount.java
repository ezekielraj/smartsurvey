package com.marveric.bestitude.smartsurvey;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DayCount {

    public static LoggedinActivity liactivity;
    private static DCDatabaseHelper dcDbHelper;
    private static SQLiteDatabase db;
    private static long todaydateid;
    private static String todaydate;
    private static String tablename;
    DayCount() { }
    DayCount(LoggedinActivity lia) {
        liactivity = lia;
        dcDbHelper = new DCDatabaseHelper(liactivity);
        db = dcDbHelper.getWritableDatabase();
        tablename = "DayCount";
        Date dNow = new Date( );
        SimpleDateFormat ft =
                new SimpleDateFormat ("yyyy-MM-dd");
        todaydate = ft.format(dNow);
        String[] projection = {
                "id",
                "todaydate",
                "onlinecount",
                "offlinecount"
        };
        String selection = "todaydate = ?";
        String[] selectionArgs = { todaydate };

        Cursor cursor = db.query(
                tablename,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        if(cursor.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put("todaydate", todaydate);
            values.put("onlinecount", 0);
            values.put("offlinecount", 0);

            todaydateid = db.insert(tablename, null, values);

        }else{
            while(cursor.moveToNext()) {
                todaydateid = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
            }
        }

    }

    public boolean incrementonlinecount(){

        int onlinecountNum = 0;
        String[] projection = {
                "onlinecount",
        };
        String selection = "id = ?";
        String[] selectionArgs = { Long.toString(todaydateid) };

        Cursor cursor = db.query(
                tablename,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        if(cursor.getCount() != 0) {
            while(cursor.moveToNext()) {
                onlinecountNum = cursor.getInt(cursor.getColumnIndexOrThrow("onlinecount"));
            }
            onlinecountNum++;
            ContentValues values = new ContentValues();
            values.put("onlinecount", onlinecountNum);

            int count = db.update(
                    tablename,
                    values,
                    selection,
                    selectionArgs);
            return true;
        }
        return false;
    }

    public boolean incrementofflinecount(){

        int offlinecountNum = 0;
        String[] projection = {
                "offlinecount",
        };
        String selection = "id = ?";
        String[] selectionArgs = { Long.toString(todaydateid) };

        Cursor cursor = db.query(
                tablename,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        if(cursor.getCount() != 0) {
            while(cursor.moveToNext()) {
                offlinecountNum = cursor.getInt(cursor.getColumnIndexOrThrow("offlinecount"));
            }
            offlinecountNum++;
            ContentValues values = new ContentValues();
            values.put("offlinecount", offlinecountNum);

            int count = db.update(
                    tablename,
                    values,
                    selection,
                    selectionArgs);
            return true;
        }
        return false;
    }

    public void fillDayCount(){
        LinearLayout dynamicContent = (LinearLayout) liactivity.getactivityview(R.id.day_count_layout);

        String[] projection = {
                "id",
                "todaydate",
                "onlinecount",
                "offlinecount"
        };

        Cursor cursor = db.query(
                tablename,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        if(cursor.getCount()!=0){
            while(cursor.moveToNext()) {


                View wizardView = liactivity.getactivityinflator()
                        .inflate(R.layout.daycount_add, dynamicContent, false);

                dynamicContent.addView(wizardView);

                TextView viewer = (TextView) liactivity.getactivityview(R.id.datevalue);
                TextView viewer1 = (TextView) liactivity.getactivityview(R.id.oncid);
                TextView viewer2 = (TextView) liactivity.getactivityview(R.id.offcid);

                viewer.setId(View.generateViewId());
                viewer1.setId(View.generateViewId());
                viewer2.setId(View.generateViewId());

                viewer.setText(cursor.getString(cursor.getColumnIndexOrThrow("todaydate")));
                viewer1.setText(Integer.toString(cursor.getInt(cursor.getColumnIndexOrThrow("onlinecount"))));
                viewer2.setText(Integer.toString(cursor.getInt(cursor.getColumnIndexOrThrow("offlinecount"))));

            }

        }


    }

}
