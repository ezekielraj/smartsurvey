package com.marveric.bestitude.smartsurvey;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class OSyncSurvey extends AsyncTask<String, String, String> {
    private static LoggedinActivity liactivity;
    private static SQLiteDatabase ldber;
    private static CheckAuthHandler cauth;
    private static ConnectwithAPI cwapi;
    private static DayCount dc;
    private static ArrayList<AsyncTask> ss;
    private static int tablecount = 1;

    OSyncSurvey(){ }
    OSyncSurvey(LoggedinActivity lia, SQLiteDatabase ldb){
        liactivity = lia;
        ldber = ldb;
        dc = new DayCount(liactivity);
        cauth = new CheckAuthHandler();
        cwapi = new ConnectwithAPI("http://www.tutorialspole.com/smartsurvey/takesurvey.php","POST");
        //dc.incrementonlinecount()
        ss = new ArrayList<AsyncTask>();
    }

    @Override
    protected void onPreExecute() {
        //Setup precondition to execute some task
    }

    @Override
    protected String doInBackground(String...arg) {
        if(isCancelled()){ return "false"; }
        int count = 0;
        int maxentries = 3;
        publishProgress("<br><br> Checking whether "+ arg[0] + "_" + arg[1]+" table exists online ");
        Map<String,String> map=new HashMap<String,String>();
        map.put("request", "createtable");
        map.put("emailid", arg[0]);
        map.put("surveyid", arg[1]);
        map.put("username","admin");
        map.put("password","angelEAR2");
        String Response = "";
        count = 0;
        while(true) {

            try {

                if(count > 0){
                    if(isCancelled()){ return "false"; }
                    publishProgress("<br><br><b>Waiting for 1 Minute!!Due to network traffic</b><br>");
                    Thread.sleep(60000);
                }
                if(isCancelled()){ return "false"; }
                cwapi.doConnect(map, cauth.getCookiegotten());

                Response = cwapi.getResponse();
                break;
            } catch (Exception e) {
                //...
                e.printStackTrace();
                count++;
            }
        }
        if(isCancelled()){ return "false"; }
        if(BuildConfig.DEBUG) Log.i("vs fas takesurvey1", "as"+Response);

        if(Response.equals("true")){
            publishProgress("<br>Table Exists online");
        }

        if(Response.equals("true")){
            if(isCancelled()){ return "false"; }
            publishProgress("<br><br>");
            publishProgress("<b>Started to sync tablename: "+ arg[0] + "_" + arg[1]+"</b>");
//            Cursor  cursor = ldber.rawQuery("select * from " + arg[0] + "_" + arg[1] + " ORDER BY SYNC_STATUS ASC",null);
            Cursor  cursor = ldber.rawQuery("select * from " + arg[0] + "_" + arg[1] + " where SYNC_STATUS = ?", new String[]{"0"});
            publishProgress("<br> Selected All Entries<br> Count:"+Integer.toString(cursor.getCount())+" <br>" );
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    if(isCancelled()){ return "false"; }
                    //        String name = cursor.getString(cursor.getColumnIndex(countyname));
                    Map<String,String> map1=new HashMap<String,String>();
                    map1.put("request", "insertdata");
                    map1.put("checker", "checkdata");
                    map1.put("emailid", arg[0]);
                    map1.put("surveyid", arg[1]);
                    map1.put("name", cursor.getString(cursor.getColumnIndex("name")));
                    map1.put("sex", cursor.getString(cursor.getColumnIndex("sex")));
                    map1.put("age", cursor.getString(cursor.getColumnIndex("age")));
                    map1.put("bodyphysique", cursor.getString(cursor.getColumnIndex("bodyphysique")));
                    map1.put("alcohol", cursor.getString(cursor.getColumnIndex("alcohol")));
                    map1.put("smooking", cursor.getString(cursor.getColumnIndex("smooking")));
                    map1.put("tobaccochewing", cursor.getString(cursor.getColumnIndex("tobacco_chewing")));
                    map1.put("occupation", cursor.getString(cursor.getColumnIndex("occupation")));
                    map1.put("pesticideapplicator", cursor.getString(cursor.getColumnIndex("pesticide_applicator")));
                    map1.put("mixingandhandlinofpesticide", cursor.getString(cursor.getColumnIndex("mixing_and_handlin_of_pesticide")));
                    map1.put("workingpesticidesprayedfield", cursor.getString(cursor.getColumnIndex("working_pesticide_sprayed_field")));
                    map1.put("workinginpesticideshop", cursor.getString(cursor.getColumnIndex("working_in_pesticide_shop")));
                    map1.put("useofinsectrepellentsathome", cursor.getString(cursor.getColumnIndex("use_of_insect_repellents_at_home")));
                    map1.put("nodirectexposure", cursor.getString(cursor.getColumnIndex("no_direct_exposure")));
                    map1.put("useofreverseosmosiswaterfordrinking", cursor.getString(cursor.getColumnIndex("use_of_reverse_osmosis_water_for_drinking")));
                    map1.put("diabetes", cursor.getString(cursor.getColumnIndex("diabetes")));
                    map1.put("hypertension", cursor.getString(cursor.getColumnIndex("hypertension")));
                    map1.put("otherdiseases", cursor.getString(cursor.getColumnIndex("other_diseases")));
                    map1.put("username","admin");
                    map1.put("password","angelEAR2");

                    if(BuildConfig.DEBUG) Log.e("data123"+Integer.toString(tablecount), map1.toString());
                    count = 0;
                    while(true) {
                        try {

                            if(count > 0){
                                if(isCancelled()){ return "false"; }
                                publishProgress("<br><br><b>Waiting for 1 Minute!!Due to network traffic</b><br>");
                                Thread.sleep(60000);
                            }
                            if(isCancelled()){ return "false"; }
                            cwapi.doConnect(map1, cauth.getCookiegotten());
                            Response = cwapi.getResponse();
                            break;
                        } catch (Exception e) {
                            e.printStackTrace();
                            count++;
                        }
                    }
                    if(BuildConfig.DEBUG) Log.i("vs fas ts updatesurvey", "as"+Response);
                    if(Response.equals("true")){
                        if(BuildConfig.DEBUG) Log.i("delete id", Integer.toString(cursor.getInt(cursor.getColumnIndex("id"))));
                        //ldber.delete(arg[0] + "_" + arg[1], "id=" + Integer.toString(cursor.getInt(cursor.getColumnIndex("id"))), null);
                        dc.incrementsynccount();
                        publishProgress("<br> Entry Inserted and sync count incremented");
                        publishProgress("<br> For Reference Name: "+cursor.getString(cursor.getColumnIndex("name")));
                    }else{
                        publishProgress("<br> Entry Already Inserted");
                        publishProgress("<br> For Reference Name: "+cursor.getString(cursor.getColumnIndex("name")));
                    }
                    String selection1 = "id = ?";
                    String[] selectionArgs1 = { cursor.getString(cursor.getColumnIndex("id")) };

                    ContentValues values1 = new ContentValues();
                    values1.put("SYNC_STATUS", "1");

                    ldber.update(
                            arg[0] + "_" + arg[1],
                            values1,
                            selection1,
                            selectionArgs1);
                    cursor.moveToNext();
                    if(isCancelled()){ return "false"; }
                }
                tablecount++;
                publishProgress("<br><br>");
                publishProgress("<b> All Entries Synced for tablename: "+ arg[0] + "_" + arg[1]+"</b>");
                publishProgress("<br>");
            }


        }
        return "true";
    }

    @Override
    protected void onProgressUpdate(String...values) {
        //Update the progress of current task
        TextView stv = (TextView) liactivity.getactivityview(R.id.syncalllayoutstatus);

        stv.append(Html.fromHtml(values[0]));
        // stv = null;
//        delete stv;
    }

    @Override
    protected void onPostExecute(String s) {
        //Show the result obtained from doInBackground
        if(BuildConfig.DEBUG) Log.i("vsonpostexecute", s);
        Toast toast = Toast.makeText(liactivity,
                "Data Successfully Synced Online", Toast.LENGTH_SHORT);
        ViewGroup group = (ViewGroup) toast.getView();
        TextView messageTextView = (TextView) group.getChildAt(0);
        messageTextView.setTextSize(25);
        if(s.equals("true")) {
            toast.show();
            try {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(liactivity.getApplicationContext(), notification);
                r.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public void syncAllone(){
        tablecount = 1;
        if(ss.size() > 0){
            for (AsyncTask sss : ss) {
                if(sss.getStatus() == Status.PENDING || sss.getStatus() == Status.RUNNING) {
                    sss.cancel(true);
                }
//                sss.remove();//    ss = null;
            }
            ss.removeAll(ss);
        }


        TextView stv = (TextView) liactivity.getactivityview(R.id.syncalllayoutstatus);
        Cursor c = ldber.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        stv.setText("Status Text:");
        stv.append("\nSelected All local TAbles");
        ArrayList<String> Tablename = new ArrayList<String>();

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
//                Log.w(Integer.toString(c.getCount()), Boolean.toString(c.isAfterLast()));
                if(c.getString(0).toLowerCase().contains(cauth.getUserEmailid().replaceAll("\\.","_").replaceAll("@","_").toLowerCase())) {
                    Cursor ac = ldber.rawQuery("select * from " + c.getString(0), null);
                    int deleteStateColumnIndex = ac.getColumnIndex("SYNC_STATUS");
                    if (deleteStateColumnIndex < 0) {
                        // missing_column not there - add it
                        ldber.execSQL("ALTER TABLE " + c.getString(0) + " ADD COLUMN SYNC_STATUS int default 0;");
                    }
                    ac = ldber.rawQuery("select * from " + c.getString(0) + " where SYNC_STATUS = ?", new String[]{"0"});
                    if(ac.getCount() > 0){
                        Tablename.add(0,c.getString(0));
                    }else{
                        //Tablename.add(c.getString(0));
                    }
                }
                c.moveToNext();
            }
        }

        //if (c.moveToFirst()) {
        //  while (!c.isAfterLast()) {
        //        ldber.rawQuery();
        //    }
        //}
        for (String s : Tablename) {
            if (s.toLowerCase().contains(cauth.getUserEmailid().replaceAll("\\.", "_").replaceAll("@", "_").toLowerCase())) {
                String Email[] = s.split("_");
                String Emailid = "";
                for (int i = 0; i < (Email.length - 1); i++) {
                    Emailid = Emailid + Email[i] + "_";
                }
                stv.append("\n\nThread Started for Tablename: " + s);

             /*   if(ss != null){
                while(true){
                    Log.w("status", ss.getStatus().toString()+s);
                    if(ss.getStatus() == Status.FINISHED){
                        ss = null;
                        break;
                    }
                }}
*/
                //              if(ss == null) {
                //ss =
                ss.add(new SyncSurvey().execute(Emailid.substring(0, Emailid.length() - 1), Email[Email.length - 1]));
                //            }
            }
        }
    }

/*    private void setTableName(String Email){
        String emid[] = Email.replaceAll("\\."  ,  "_").split("@");
        currTableName = emid[0] + "_" + emid[1] + "_" + data;
    }*/
}
