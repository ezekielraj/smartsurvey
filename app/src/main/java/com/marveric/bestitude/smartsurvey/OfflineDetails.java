package com.marveric.bestitude.smartsurvey;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
                if(BuildConfig.DEBUG) Log.i("DBname",c.getString(0));
                if((!c.getString(0).equals("android_metadata")) &&
                        (!c.getString(0).equals("Survey")) &&
                        (!c.getString(0).equals("sqlite_sequence"))){

                    Cursor cursor = ldber.rawQuery("select * from " + c.getString(0),null);

                    int j = (cursor.getCount() / 500) + 1;
                    for(int a=0;a<j;a++) {
                        int limit = 500;
                        int offset = a * 500;


                        wizardView = liactivity.getactivityinflator()
                                .inflate(R.layout.offlinetabledetails_add, dynamicContent, false);

                        dynamicContent.addView(wizardView);
                        TextView viewer = (TextView) liactivity.getactivityview(R.id.tablenamevalue);
                        TextView viewer1 = (TextView) liactivity.getactivityview(R.id.tbvaluescount);
                        viewer.setId(View.generateViewId());
                        viewer.setText(c.getString(0) + " "
                                +Integer.toString(limit)
                                +"-"
                                +Integer.toString(offset));
                        viewer1.setId(View.generateViewId());
                        viewer1.setText(Integer.toString(cursor.getCount()));

                        viewer.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                String fulltxt[] = ((TextView) liactivity.getactivityview(v.getId())).getText().toString().split( " ");
                                String tbname = fulltxt[0];
                                String data[] = fulltxt[1].split("-");
                                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                sharingIntent.setType("text/html");
                                if(BuildConfig.DEBUG) Log.i("tablequery","select * from " + tbname + " limit "+data[0]+" offset "+data[1]);

                                Cursor cursor = ldber.rawQuery("select * from " + tbname + " limit "+data[0]+" offset "+data[1], null);
                                String values = "<html><p>"+fulltxt[0]+" "+fulltxt[1]+"</p><br></br><table  width=\"600\" style=\"border:1px solid #333\">";

                                values = values + "<tr>" +
                                        "<td>" + "name," + "</td>" +
                                        "<td>" + "sex," + "</td>" +
                                        "<td>" + "age," + "</td>" +
                                        "<td>" + "bodyphysique," + "</td>" +
                                        "<td>" + "alcohol," + "</td>" +
                                        "<td>" + "smooking," + "</td>" +
                                        "<td>" + "tobacco_chewing," + "</td>" +
                                        "<td>" + "occupation," + "</td>" +
                                        "<td>" + "pesticide_applicator," + "</td>" +
                                        "<td>" + "mixing_and_handlin_of_pesticide," + "</td>" +
                                        "<td>" + "working_pesticide_sprayed_field," + "</td>" +
                                        "<td>" + "working_in_pesticide_shop," + "</td>" +
                                        "<td>" + "use_of_insect_repellents_at_home," + "</td>" +
                                        "<td>" + "no_direct_exposure," + "</td>" +
                                        "<td>" + "use_of_reverse_osmosis_water_for_drinking," + "</td>" +
                                        "<td>" + "diabetes," + "</td>" +
                                        "<td>" + "hypertension," + "</td>" +
                                        "<td>" + "other_diseases," + "</td>" +

                                        "</tr><br></br>";
try {
    String od = "";
    while (cursor.moveToNext()) {
        od = "";
        //for(int i=0;i<375;i++){
        if(cursor.getString(cursor.getColumnIndex("other_diseases"))!=null) {
            od = (new String(Base64.decode(cursor.getString(cursor.getColumnIndex("other_diseases")), Base64.DEFAULT), "UTF-8"));
        }
        values = values + "<tr>" +
                "<td>" + cursor.getString(cursor.getColumnIndex("name")) + ",</td>" +
                "<td>" + cursor.getString(cursor.getColumnIndex("sex")) + ",</td>" +
                "<td>" + cursor.getString(cursor.getColumnIndex("age")) + ",</td>" +
                "<td>" + cursor.getString(cursor.getColumnIndex("bodyphysique")) + ",</td>" +
                "<td>" + cursor.getString(cursor.getColumnIndex("alcohol")) + ",</td>" +
                "<td>" + cursor.getString(cursor.getColumnIndex("smooking")) + ",</td>" +
                "<td>" + cursor.getString(cursor.getColumnIndex("tobacco_chewing")) + ",</td>" +
                "<td>" + cursor.getString(cursor.getColumnIndex("occupation")) + ",</td>" +
                "<td>" + cursor.getString(cursor.getColumnIndex("pesticide_applicator")) + ",</td>" +
                "<td>" + cursor.getString(cursor.getColumnIndex("mixing_and_handlin_of_pesticide")) + ",</td>" +
                "<td>" + cursor.getString(cursor.getColumnIndex("working_pesticide_sprayed_field")) + ",</td>" +
                "<td>" + cursor.getString(cursor.getColumnIndex("working_in_pesticide_shop")) + ",</td>" +
                "<td>" + cursor.getString(cursor.getColumnIndex("use_of_insect_repellents_at_home")) + ",</td>" +
                "<td>" + cursor.getString(cursor.getColumnIndex("no_direct_exposure")) + ",</td>" +
                "<td>" + cursor.getString(cursor.getColumnIndex("use_of_reverse_osmosis_water_for_drinking")) + ",</td>" +
                "<td>" + cursor.getString(cursor.getColumnIndex("diabetes")) + ",</td>" +
                "<td>" + cursor.getString(cursor.getColumnIndex("hypertension")) + ",</td>" +
                "<td>" + od + ",</td>" +

                "</tr><br></br>";
        //}
    }
}catch(Exception e){
    e.printStackTrace();
}
                                values = values + "</table></html>";
                                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(
                                        values));
                                liactivity.startActivity(Intent.createChooser(sharingIntent, "Share using"));

                            }
                            });
                        }
                    }

                c.moveToNext();
            }

        }
    }
}
