package com.example.bestitude.smartsurvey;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class SyncSurvey extends AsyncTask<String, String, String> {
    private static LoggedinActivity liactivity;
    private static SQLiteDatabase ldber;
    private static CheckAuthHandler cauth;
    private static ConnectwithAPI cwapi;

    SyncSurvey(){ }
    SyncSurvey(LoggedinActivity lia, SQLiteDatabase ldb){
        liactivity = lia;
        ldber = ldb;
        cauth = new CheckAuthHandler();
        cwapi = new ConnectwithAPI("http://www.tutorialspole.com/smartsurvey/takesurvey.php","POST");

    }

    @Override
    protected void onPreExecute() {
        //Setup precondition to execute some task
    }

    @Override
    protected String doInBackground(String...arg) {
        //Do some task
        publishProgress ("Processing");
        try{
            Map<String,String> map=new HashMap<String,String>();
            map.put("request", "createtable");
            map.put("emailid", arg[0]);
            map.put("surveyid", arg[1]);
            cwapi.doConnect(map, cauth.getCookiegotten());
            String Response = cwapi.getResponse();
            Log.w("vs fas takesurvey1", "as"+Response);


            if(Response.equals("true")){
                Cursor  cursor = ldber.rawQuery("select * from " + arg[0] + "_" + arg[1],null);
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                //        String name = cursor.getString(cursor.getColumnIndex(countyname));
                        Map<String,String> map1=new HashMap<String,String>();
                        map1.put("request", "insertdata");
                        map1.put("emailid", arg[0]);
                        map1.put("surveyid", arg[1]);
                        map1.put("name", cursor.getString(cursor.getColumnIndex("name")));
                        map1.put("sex", cursor.getString(cursor.getColumnIndex("sex")));
                        map1.put("age", cursor.getString(cursor.getColumnIndex("age")));
                        map1.put("agegroup", cursor.getString(cursor.getColumnIndex("agegroup")));
                        map1.put("alcohol", cursor.getString(cursor.getColumnIndex("alcohol")));
                        map1.put("smooking", cursor.getString(cursor.getColumnIndex("smooking")));
                        map1.put("tobaccochewing", cursor.getString(cursor.getColumnIndex("tobacco_chewing")));
                        map1.put("farming", cursor.getString(cursor.getColumnIndex("farming")));
                        map1.put("pesticideapplicator", cursor.getString(cursor.getColumnIndex("pesticide_applicator")));
                        map1.put("mixingandhandlinofpesticide", cursor.getString(cursor.getColumnIndex("mixing_and_handlin_of_pesticide")));
                        map1.put("workingpesticidesprayedfield", cursor.getString(cursor.getColumnIndex("working_pesticide_sprayed_field")));
                        map1.put("workinginpesticideshop", cursor.getString(cursor.getColumnIndex("working_in_pesticide_shop")));
                        map1.put("useofinsectrepellentsathome", cursor.getString(cursor.getColumnIndex("use_of_insect_repellents_at_home")));
                        map1.put("useofreverseosmosiswaterfordrinking", cursor.getString(cursor.getColumnIndex("use_of_reverse_osmosis_water_for_drinking")));
                        map1.put("diabetes", cursor.getString(cursor.getColumnIndex("diabetes")));
                        map1.put("hypertension", cursor.getString(cursor.getColumnIndex("hypertension")));
                        map1.put("otherdiseases", cursor.getString(cursor.getColumnIndex("other_diseases")));
                        cwapi.doConnect(map1, cauth.getCookiegotten());
                        Response = cwapi.getResponse();
                        Log.w("vs fas ts updatesurvey", "as"+Response);
                        if(Response.equals("true")){
                            Log.w("delete id", Integer.toString(cursor.getInt(cursor.getColumnIndex("id"))));
                            ldber.delete(arg[0] + "_" + arg[1], "id=" + Integer.toString(cursor.getInt(cursor.getColumnIndex("id"))), null);
                        }
                        cursor.moveToNext();
                    }
                }


            }
            return "true";

        }catch(Exception e){
            liactivity.saveException(e);
            e.printStackTrace();
        }

        return "";
    }

    @Override
    protected void onProgressUpdate(String...values) {
        //Update the progress of current task
    }

    @Override
    protected void onPostExecute(String s) {
        //Show the result obtained from doInBackground
        Log.w("vsonpostexecute", s);
        Toast.makeText(liactivity,
                "Data Successfully Synced Online", Toast.LENGTH_SHORT).show();
    }

    public void syncAll(){
        Cursor c = ldber.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                if(c.getString(0).toLowerCase().contains(cauth.getUserEmailid().replaceAll("\\.","_").replaceAll("@","_").toLowerCase())){
                    Log.w("Tablename:", c.getString(0) + Boolean.toString(c.getString(0).toLowerCase().contains(cauth.getUserEmailid().replaceAll("\\.","_").replaceAll("@","_").toLowerCase())));
                    String Email[] = c.getString(0).split("_");
                    String Emailid = "";
                    for (int i=0;i < (Email.length-1); i++){
                        Emailid = Emailid + Email[i] + "_";
                    }
                    //Log.w("texttosend",Emailid.substring(0, Emailid.length()-1)+Email[Email.length - 1]);

                    new SyncSurvey().execute(Emailid.substring(0, Emailid.length()-1), Email[Email.length - 1]);
                }

                //Toast.makeText(activityName.this, "Table Name=> "+c.getString(0), Toast.LENGTH_LONG).show();
                c.moveToNext();
            }
        }
    }

/*    private void setTableName(String Email){
        String emid[] = Email.replaceAll("\\."  ,  "_").split("@");
        currTableName = emid[0] + "_" + emid[1] + "_" + data;
    }*/
}
