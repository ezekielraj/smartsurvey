package com.marveric.bestitude.smartsurvey;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewSurveys extends AsyncTask<String, String, String> {


    private static ConnectwithAPI cwapi;
    public static LoggedinActivity liactivity;
    private static ViewFlipper vf;
    private static TakeSurvey takeSurvey;
    private static AddSurvey addSurvey;
    private static LDatabaseHelper mlDbHelper;
    //private static ViewUsersEdit vue;
    private static String cookie = "";
    private static SQLiteDatabase ldb;
    private final static String Surveytablename = "Survey";
    private static SyncSurvey syncSurvey;
    ViewSurveys(){ }
    ViewSurveys(LoggedinActivity lia){
        liactivity = lia;

        vf =  (ViewFlipper) liactivity.getVf();

        cwapi = new ConnectwithAPI("http://www.tutorialspole.com/smartsurvey/surveyif.php","POST");
        addSurvey = new AddSurvey(liactivity);
        mlDbHelper = new LDatabaseHelper(liactivity);
        ldb = mlDbHelper.getWritableDatabase();
        takeSurvey = new TakeSurvey(liactivity, ldb);
        syncSurvey = new SyncSurvey(liactivity, ldb);
    }

    @Override
    protected void onPreExecute() {
        //Setup precondition to execute some task
    }

    @Override
    protected String doInBackground(String...arg) {
        //Do some task
        publishProgress ("Processing");
        String IsAdmin = arg[0];
        String UserId = arg[1];
        String cookiegotton = arg[2];
        try {

            Map<String,String> map=new HashMap<String,String>();
            if(IsAdmin.equals("true")) {
                map.put("request", "getalladmin");
            }else{
                map.put("request", "getalluser");
                map.put("userid", UserId);

            }
            Log.w("viewsurvyes", UserId+IsAdmin);
		map.put("username","admin");
                map.put("password","angelEAR2");
            cwapi.doConnect(map, cookiegotton);
            String Response = cwapi.getResponse();
            Log.w("vs fetchallsurveys", "as"+Response);

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
        updateLayoutContent(s);
        updateLocalSurvey(s);
    }




    public void fetchAllSurveys(Boolean IsAdmin, String UserEmailId, String cookiestring){
        cookie = cookiestring;
        if(liactivity.isOnline()) {
            new ViewSurveys().execute(Boolean.toString(IsAdmin), UserEmailId, cookie);
        }else{
            liactivity.saveString("checker testing"+UserEmailId + cookiestring);
            Log.w("checker testing",UserEmailId + cookiestring);
            updateLayoutContentoffline();
        }
    }

    private void updateLayoutContentoffline(){
        Log.w("uLCo","Started");
        try{
            String[] projection = {
                    "slno",
                    "District",
                    "Locality",
                    "State",
                    "EndDate"
            };

            String sortOrder =
                    "slno DESC";

            Cursor cursor = ldb.query(
                    Surveytablename,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    null,              // The columns for the WHERE clause
                    null,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    null//sortOrder               // The sort order
            );
            Log.w("vs rowCount", Integer.toString(cursor.getCount()));

            if(cursor.getCount() != 0){
                String Response = "[";
                while(cursor.moveToNext()) {
                Response = Response + "{\"survey_id\":\"" +
                        cursor.getString(cursor.getColumnIndexOrThrow("slno")) +
                        "\",\"district\":\"" +
                        cursor.getString(cursor.getColumnIndexOrThrow("District")) +
                        "\",\"village\":\"" +
                        cursor.getString(cursor.getColumnIndexOrThrow("Locality")) +
                        "\",\"subdistrict\":\"" +
                        cursor.getString(cursor.getColumnIndexOrThrow("State")) +
                        "\",\"EndDate\":\"" +
                        cursor.getString(cursor.getColumnIndexOrThrow("EndDate")) +
                        "\"},";
                    //ValidUser = cursor.getInt(cursor.getColumnIndexOrThrow("IsValid"));
                    //AdminUser = cursor.getInt(cursor.getColumnIndexOrThrow("IsAdmin"));
                    //Log.w("rowinvalid" ,Response);//cursor.getString(cursor.getColumnIndex("District")));

                }
                Response = Response.substring(0, Response.length() - 1);
                Response = Response + "]";
                updateLayoutContent(Response);
            }
        }catch(Exception e)
        {
            liactivity.saveException(e);
            e.printStackTrace();
        }
    }

    private void updateLocalSurvey(String Response){
        Log.w("uls","started");
        try {
            ldb.execSQL("delete from "+ Surveytablename);

            String[] projection = {
                    "slno",
                    "District",
                    "Locality",
                    "State",
                    "EndDate"
            };
            // Filter results WHERE "title" = 'My Title'
            String selection = "slno = ?";


            JSONArray jsonArray = new JSONArray(Response);
            if (jsonArray != null && jsonArray.length() > 0) {
                int N = jsonArray.length(); // total number of textviews to add

                for (int i = 0; i < jsonArray.length(); i++) {

                    Log.w("view surveys -response", "as" + jsonArray.getJSONObject(i));
                    JSONObject jb = new JSONObject(jsonArray.getJSONObject(i).toString());
                    String[] selectionArgs = { Integer.toString(jb.getInt("survey_id")) };

                    String sortOrder =
                            "slno DESC";

                    Cursor cursor = ldb.query(
                            Surveytablename,   // The table to query
                            projection,             // The array of columns to return (pass null to get all)
                            selection,              // The columns for the WHERE clause
                            selectionArgs,          // The values for the WHERE clause
                            null,                   // don't group the rows
                            null,                   // don't filter by row groups
                            sortOrder               // The sort order
                    );
                    Log.w("vs rowCount", Integer.toString(cursor.getCount()));

                    if(cursor.getCount() == 0) {
                        ContentValues values = new ContentValues();
                        values.put("slno", Integer.toString(jb.getInt("survey_id")));
                        values.put("District", jb.getString("district"));
                        values.put("Locality", jb.getString("village"));
                        values.put("State", jb.getString("subdistrict"));
                        values.put("EndDate", jb.getString("EndDate"));

                        long newRowId = ldb.insert(Surveytablename, null, values);

                    }else {

                        ContentValues values = new ContentValues();
                        values.put("District", jb.getString("district"));
                        values.put("Locality", jb.getString("village"));
                        values.put("State", jb.getString("subdistrict"));
                        values.put("EndDate", jb.getString("EndDate"));

                        int count = ldb.update(
                                Surveytablename,
                                values,
                                selection,
                                selectionArgs);

                        Log.w("ulav","completed");



                    }
                }
            }

        }catch(Exception e){
            liactivity.saveException(e);
            e.printStackTrace();
        }




    }

    private void updateLayoutContent(String Response){

        try {
            if (Response != null) {
                vf =  (ViewFlipper) liactivity.getVf();

                LinearLayout dynamicContent = (LinearLayout) liactivity.getactivityview(R.id.view_surveys_layout);
                dynamicContent.removeAllViews();
                JSONArray jsonArray = new JSONArray(Response);
                if (jsonArray != null && jsonArray.length() > 0) {
                    int N = jsonArray.length(); // total number of textviews to add

                    for (int i = 0; i < jsonArray.length(); i++) {

                        Log.w("view surveys -response", "as" + jsonArray.getJSONObject(i));
                        JSONObject jb = new JSONObject(jsonArray.getJSONObject(i).toString());


// assuming your Wizard content is in content_wizard.xml

                        View wizardView = liactivity.getactivityinflator()
                                .inflate(R.layout.viewsurveys_include, dynamicContent, false);

// add the inflated View to the layout
                        dynamicContent.addView(wizardView);
                        //   Log.w("vusers layout id", Integer.toString(dynamicContent.getChildCount()));
              //          TextView viewer = (TextView) liactivity.getactivityview(R.id.commontextview);
                        TextView viewer = (TextView) liactivity.getactivityview(R.id.surveyidtext);
                        TextView viewer1 = (TextView) liactivity.getactivityview(R.id.districtidtext);
                        TextView viewer2 = (TextView) liactivity.getactivityview(R.id.localityidtext);
                        TextView viewer3 = (TextView) liactivity.getactivityview(R.id.stateidtext);
                        TextView viewer4 = (TextView) liactivity.getactivityview(R.id.enddateidtext);
                        TextView viewer5 = (TextView) liactivity.getactivityview(R.id.submitbuttonidtext);
                        //int surveyidvid = ;
                        viewer.setId(View.generateViewId());
                        viewer1.setId(View.generateViewId());
                        viewer2.setId(View.generateViewId());
                        viewer3.setId(View.generateViewId());
                        viewer4.setId(View.generateViewId());
                        viewer5.setId(View.generateViewId());
/*                        viewer.setText(Integer.toString(jb.getInt("survey_id"))+"-"+
                                jb.getString("district") + "-" +
                                jb.getString("locality") + "-" +
                                jb.getString("state") + "-" +
                                jb.getString("EndDate"));
  */
                        viewer.setText(Integer.toString(jb.getInt("survey_id")));
                        viewer1.setText(jb.getString("district"));
                        viewer2.setText(jb.getString("village"));
                        viewer3.setText(jb.getString("subdistrict"));
                        viewer4.setText(jb.getString("EndDate"));


                        viewer5.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                               String completetext = "";

                                TextView tv = (TextView) liactivity.getactivityview((v.getId() - 5));
                                TextView tv1 = (TextView) liactivity.getactivityview((v.getId() - 4));
                                TextView tv2 = (TextView) liactivity.getactivityview((v.getId() - 3));
                                TextView tv3 = (TextView) liactivity.getactivityview((v.getId() - 2));
                                TextView tv4 = (TextView) liactivity.getactivityview((v.getId() - 1));

                                completetext = tv.getText().toString() + "-" +
                                        tv1.getText().toString() + "-" +
                                        tv2.getText().toString() + "-" +
                                        tv3.getText().toString() + "-" +
                                        tv4.getText().toString();

                                vf.setDisplayedChild(vf.indexOfChild(liactivity.getactivityview(R.id.vstake)));
                                liactivity.keepScrollup();
                                liactivity.setTitle("Rural Diabetes - Mass Survey");
                                takeSurvey.CheckDBExists(completetext);
                                takeSurvey.configListener(completetext);
                                Log.w("click for id",completetext);
                            }
                        });
                    }
                }
            }
        }catch(Exception e){
            liactivity.saveException(e);
            e.printStackTrace();
        }

    }


    public void updateFloatingbutton(){
        vf =  (ViewFlipper) liactivity.getVf();
        FloatingActionButton fab = (FloatingActionButton) liactivity.getactivityview(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vf.setDisplayedChild(vf.indexOfChild(liactivity.getactivityview(R.id.vsadd)));
                liactivity.keepScrollup();
                addSurvey.configListener();
            }
        });
    }
    public static String getCookie(){
        return cookie;
    }

    public void syncAll(){
        syncSurvey.syncAll();
    }
}
