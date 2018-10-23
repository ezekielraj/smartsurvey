package com.marveric.bestitude.smartsurvey;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.ceil;

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
    private static BSyncSurvey bsyncSurvey;
    private static OSyncSurvey osyncSurvey;


    //(Boolean.toString(IsAdmin), UserEmailId, cookie, "initial");
    private static Boolean adminstatus;
    private static String emailstatus;


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
        bsyncSurvey = new BSyncSurvey(liactivity, ldb);
        osyncSurvey = new OSyncSurvey(liactivity, ldb);
    }

    @Override
    protected void onPreExecute() {
        //Setup precondition to execute some task
    }

    @Override
    protected String doInBackground(String...arg) {
        //Do some task

        String IsAdmin = arg[0];
        String UserId = arg[1];
        String cookiegotton = arg[2];
        try {
        if(arg[3].equals("initial")) {
            publishProgress ("Getting Count");
            Map<String, String> mapc = new HashMap<String, String>();
            if (IsAdmin.equals("true")) {
                mapc.put("request", "getalladmincount");
            } else {
                mapc.put("request", "getallusercount");
                mapc.put("userid", UserId);

            }
            mapc.put("username", "admin");
            mapc.put("password", "angelEAR2");
            cwapi.doConnect(mapc, cookiegotton);
            String Responsec = cwapi.getResponse();
            if(BuildConfig.DEBUG) Log.i("vs fetchallsurveys count", "as" + Responsec);
            publishProgress ("Getting Count!!!.Got Total Survey Count");
            return "initial,-_" + Responsec;
        }else {

            Map<String, String> map = new HashMap<String, String>();
            if (IsAdmin.equals("true")) {
                map.put("request", "getalladmin");
            } else {
                map.put("request", "getalluser");
                map.put("userid", UserId);

            }
            if(BuildConfig.DEBUG) Log.i("viewsurvyes", UserId + IsAdmin);
            map.put("limit1",arg[4]);
            map.put("limit2", arg[5]);
            map.put("username", "admin");
            map.put("password", "angelEAR2");
            publishProgress("Request Sent! From "+arg[4]+" to "+arg[5]);
            cwapi.doConnect(map, cookiegotton);
            String Response = cwapi.getResponse();
            publishProgress("Request Sent! From "+arg[4]+" to "+arg[5] + "... Got Response");
            if(BuildConfig.DEBUG) Log.i("vs fetchallsurveys", "as" + Response);
            //publishProgress ("Completed");
            return "result,-_"+ Response;
        }
        } catch (Exception e) {
            liactivity.saveException(e);
            e.printStackTrace();
        }

        return "";
    }

    @Override
    protected void onProgressUpdate(String...values) {
        //Update the progress of current task
        if(liactivity!=null) {
            TextView tv = (TextView) liactivity.getactivityview(R.id.viewsurveystatus);
            tv.setText(values[0]);
        }
        /*
        Snackbar.make(liactivity.getactivityview(R.id.viewflippers), values[0], Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
*/
    }

    @Override
    protected void onPostExecute(String s) {
        //Show the result obtained from doInBackground
        String output[] = s.split(",-_");

        if(output[0].equals("initial")){

            try{
                ldb.execSQL("delete from "+ Surveytablename);

            int cvalue = 0;
            JSONArray jsonArray = new JSONArray(output[1]);
            if (jsonArray != null && jsonArray.length() > 0) {
                int N = jsonArray.length(); // total number of textviews to add

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jb = new JSONObject(jsonArray.getJSONObject(i).toString());
                    cvalue = jb.getInt("count");
                }
            }
            if(cvalue < 10) {
                new ViewSurveys().execute(Boolean.toString(adminstatus), emailstatus, cookie, "noninitial",
                        Integer.toString(0) , Integer.toString(10));
            }else{
                int j = 0;
                for(int i=0;i<ceil(cvalue/10);i++){
                    new ViewSurveys().execute(Boolean.toString(adminstatus), emailstatus, cookie, "noninitial",
                            Integer.toString(j) , Integer.toString(10));
                    j = j+10;
                }
                new ViewSurveys().execute(Boolean.toString(adminstatus), emailstatus, cookie, "noninitial",
                        Integer.toString(j) , Integer.toString(10));

            }

            }catch(Exception e){
                e.printStackTrace();
            }

        }else {
        if(output.length > 1) {
            if (BuildConfig.DEBUG) Log.i("vsonpostexecute", output[1]);
            updateLayoutContent(output[1]);
            updateLocalSurvey(output[1]);
        }
        }
    }




    public void fetchAllSurveys(Boolean IsAdmin, String UserEmailId, String cookiestring){
        LinearLayout dynamicContent = (LinearLayout) liactivity.getactivityview(R.id.view_surveys_layout);
        dynamicContent.removeAllViews();
        View wizardView = liactivity.getactivityinflator()
                .inflate(R.layout.viewsurveystatus_include, dynamicContent, false);
        dynamicContent.addView(wizardView);

        cookie = cookiestring;
        if(liactivity.isOnline()) {
            adminstatus = IsAdmin;
            emailstatus = UserEmailId;

            new ViewSurveys().execute(Boolean.toString(IsAdmin), UserEmailId, cookie, "initial");

        }else{
            liactivity.saveString("checker testing"+UserEmailId + cookiestring);
            if(BuildConfig.DEBUG) Log.i("checker testing",UserEmailId + cookiestring);
            updateLayoutContentoffline();
        }
    }

    private void updateLayoutContentoffline(){
        if(BuildConfig.DEBUG) Log.i("uLCo","Started");
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
            if(BuildConfig.DEBUG) Log.i("vs rowCount", Integer.toString(cursor.getCount()));

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
                    //if(BuildConfig.DEBUG) Log.i("rowinvalid" ,Response);//cursor.getString(cursor.getColumnIndex("District")));

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
        if(BuildConfig.DEBUG) Log.i("uls","started");
        try {
           // ldb.execSQL("delete from "+ Surveytablename);

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

                    if(BuildConfig.DEBUG) Log.i("view surveys -response", "as" + jsonArray.getJSONObject(i));
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
                    if(BuildConfig.DEBUG) Log.i("vs rowCount", Integer.toString(cursor.getCount()));

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

                        if(BuildConfig.DEBUG) Log.i("ulav","completed");



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
                //dynamicContent.removeAllViews();
                JSONArray jsonArray = new JSONArray(Response);
                if (jsonArray != null && jsonArray.length() > 0) {
                    int N = jsonArray.length(); // total number of textviews to add

                    for (int i = 0; i < jsonArray.length(); i++) {

                        if(BuildConfig.DEBUG) Log.i("view surveys -response", "as" + jsonArray.getJSONObject(i));
                        JSONObject jb = new JSONObject(jsonArray.getJSONObject(i).toString());


// assuming your Wizard content is in content_wizard.xml

                        View wizardView = liactivity.getactivityinflator()
                                .inflate(R.layout.viewsurveys_include, dynamicContent, false);

// add the inflated View to the layout
                        dynamicContent.addView(wizardView);
                        //   if(BuildConfig.DEBUG) Log.i("vusers layout id", Integer.toString(dynamicContent.getChildCount()));
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

                            //    liactivity.keepScrollup();
                                liactivity.setTitle("Rural Diabetes - Mass Survey");
                                takeSurvey.CheckDBExists(completetext);
                                takeSurvey.configListener(completetext);
                                if(BuildConfig.DEBUG) Log.i("click for id",completetext);
                                ((EditText) liactivity.getactivityview(R.id.entryname)).requestFocus();//clearFocus();//setFocusedByDefault(true);//.scrollTo(0, 0);//.fullScroll(ScrollView.FOCUS_UP);
                                ((ScrollView) liactivity.getactivityview(R.id.viewsurveyscrollview)).fullScroll(ScrollView.FOCUS_UP);
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

    public void syncAllone(){ osyncSurvey.syncAllone(); }

    public void BulkSync(){
        bsyncSurvey.bulkSyncAll();
    }
}
