package com.marveric.bestitude.smartsurvey;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class TakeSurvey extends AsyncTask<String, String, String> {

    private static LoggedinActivity liactivity;
    private static CheckAuthHandler cauth;
//    private static TakeSurvey takeSurvey;
    private static UpdateSurvey updateSurvey;
    private static ConnectwithAPI cwapi;
    private static SQLiteDatabase ldber;
    private static String currTableName;
    TakeSurvey(){ }
    TakeSurvey(LoggedinActivity lia, SQLiteDatabase ldb){

        liactivity = lia;

        ldber = ldb;
  //      takeSurvey = new TakeSurvey();
        updateSurvey = new UpdateSurvey(liactivity, ldber);
        cauth =  new CheckAuthHandler();
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
        String request = arg[0];
        String emailId = arg[1];
        String edata[] = emailId.replaceAll("\\."  ,  "_").split("@");
        String surveyid = arg[2];
        try {

            Map<String,String> map=new HashMap<String,String>();
            map.put("request", "createtable");
            map.put("emailid", edata[0] + "_" + edata[1]);
            map.put("surveyid", surveyid);
		map.put("username","admin");
                map.put("password","angelEAR2");
            cwapi.doConnect(map, cauth.getCookiegotten());
            String Response = cwapi.getResponse();
            Log.w("vs fas takesurvey", "as"+Response);

            return Response;

        } catch (Exception e) {
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
    }

    private void setTableName(String Email, String data){
        String emid[] = Email.replaceAll("\\."  ,  "_").split("@");
        currTableName = emid[0] + "_" + emid[1] + "_" + data;
    }

    public void CheckDBExists(String s){
        String data[] = s.split("-");
        setTableName(cauth.getUserEmailid(), data[0]);
        if(liactivity.isOnline()) {
            new TakeSurvey().execute("createtable", cauth.getUserEmailid(), data[0]);
        }//else{
            Log.w("Email", cauth.getUserEmailid()+data[0]);
            checkLocalDBExists(cauth.getUserEmailid(), data[0]);
        //}
    }
    private void checkLocalDBExists(String Email, String data){
        String emid[] = Email.replaceAll("\\."  ,  "_").split("@");

Log.w("text", emid[0]+emid[1]);
        String sce = "CREATE TABLE IF NOT EXISTS " + emid[0] + "_" + emid[1] + "_" + data + " ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name VARCHAR(255)," +
                "sex VARCHAR(255)," +
                "age VARCHAR(255)," +
                "bodyphysique varchar(255)," +
                "alcohol VARCHAR(255)," +
                "smooking VARCHAR(255)," +
                "tobacco_chewing VARCHAR(255)," +
                "occupation varchar(255)," +
                "pesticide_applicator VARCHAR(255)," +
                "mixing_and_handlin_of_pesticide VARCHAR(255)," +
                "working_pesticide_sprayed_field VARCHAR(255)," +
                "working_in_pesticide_shop VARCHAR(255)," +
                "use_of_insect_repellents_at_home VARCHAR(255)," +
                "no_direct_exposure varchar(255)," +
                "use_of_reverse_osmosis_water_for_drinking VARCHAR(255)," +
                "diabetes VARCHAR(255)," +
                "hypertension VARCHAR(255)," +
                "other_diseases VARCHAR(255)," +
                "user_remarks VARCHAR(1024))";

        ldber.execSQL(sce);

/*
        Cursor c = ldber.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                Log.w("Tablename:", c.getString(0));
                //Toast.makeText(activityName.this, "Table Name=> "+c.getString(0), Toast.LENGTH_LONG).show();
                c.moveToNext();
            }
        }*/
    }
    public void configListener(String s){
        final String value = s;
        Button btsaverepeat = (Button) liactivity.getactivityview(R.id.saverepeat);
        btsaverepeat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                if(updateSurvey.ValidateData()){
                        updateSurvey.sendData(value, currTableName);
                        Log.w("takesurvey", "success");

                }
            }
        });
    }
}
