package com.marveric.bestitude.smartsurvey;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OfflineDetails {
    private static SQLiteDatabase ldber;
    public static LoggedinActivity liactivity;
    public static LDatabaseHelper ldh;
    OfflineDetails() { }

    OfflineDetails(LoggedinActivity lia) {
        liactivity=lia;
        ldh = new LDatabaseHelper(liactivity);
        ldber = ldh.getWritableDatabase();
    }

    public void filDBDetails(){

        LinearLayout dynamicContent = (LinearLayout) liactivity.getactivityview(R.id.offline_table_count_layout);
        dynamicContent.removeAllViews();

        View wizardView = liactivity.getactivityinflator()
                .inflate(R.layout.offlinetabledetailshead_add, dynamicContent, false);

        dynamicContent.addView(wizardView);

        Cursor c = ldber.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                Log.w("DBname",c.getString(0));
                if((!c.getString(0).equals("android_metadata")) &&
                        (!c.getString(0).equals("Survey")) &&
                        (!c.getString(0).equals("sqlite_sequence"))){

                    Cursor cursor = ldber.rawQuery("select * from " + c.getString(0),null);

                    wizardView = liactivity.getactivityinflator()
                            .inflate(R.layout.offlinetabledetails_add, dynamicContent, false);

                    dynamicContent.addView(wizardView);
                    TextView viewer = (TextView) liactivity.getactivityview(R.id.tablenamevalue);
                    TextView viewer1 = (TextView) liactivity.getactivityview(R.id.tbvaluescount);
                    viewer.setText(c.getString(0));
                    viewer1.setText(Integer.toString(cursor.getCount()));


                }
                c.moveToNext();
            }

        }
    }
}