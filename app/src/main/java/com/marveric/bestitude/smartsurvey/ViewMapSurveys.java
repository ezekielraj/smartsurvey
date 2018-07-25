package com.marveric.bestitude.smartsurvey;

import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.ceil;

public class ViewMapSurveys extends AsyncTask<String, String, String> {

    private static ConnectwithAPI cwapi;
    public static LoggedinActivity liactivity;
    private static ViewFlipper vf;
    //private static ViewUsersEdit vue;
    private static String cookie = "";
    private static ViewMapUsersSurvyes vmus;

    private static Boolean adminstatus;
    private static String emailstatus;


    ViewMapSurveys(){ }
    ViewMapSurveys(LoggedinActivity lia){
        liactivity = lia;
        vf =  (ViewFlipper) liactivity.getVf();
        cwapi = new ConnectwithAPI("http://www.tutorialspole.com/smartsurvey/surveyif.php","POST");
        vmus = new ViewMapUsersSurvyes(liactivity);
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
                Log.w("vs fetchallsurveys count", "as" + Responsec);
                return "initial,-_" + Responsec;
            }else {

                Map<String, String> map = new HashMap<String, String>();
                if (IsAdmin.equals("true")) {
                    map.put("request", "getalladmin");
                } else {
                    map.put("request", "getalluser");
                    map.put("userid", UserId);

                }
                Log.w("viewsurvyes", UserId + IsAdmin);
                map.put("limit1",arg[4]);
                map.put("limit2", arg[5]);
                map.put("username", "admin");
                map.put("password", "angelEAR2");
                publishProgress("Request Sent! Waiting for Response");
                cwapi.doConnect(map, cookiegotton);
                String Response = cwapi.getResponse();
                publishProgress("Got Response");
                Log.w("vs fetchallsurveys", "as" + Response);
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
        Snackbar.make(liactivity.getactivityview(R.id.viewflippers), values[0], Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        //Update the progress of current task
    }

    @Override
    protected void onPostExecute(String s) {
        //Show the result obtained from doInBackground
if(!s.equals(null)) {

    String output[] = s.split(",-_");

    if(output[0].equals("initial")){

        try{


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
                new ViewMapSurveys().execute(Boolean.toString(adminstatus), emailstatus, cookie, "noninitial",
                        Integer.toString(0) , Integer.toString(10));
            }else{
                int j = 0;
                for(int i=0;i<ceil(cvalue/10);i++){
                    new ViewMapSurveys().execute(Boolean.toString(adminstatus), emailstatus, cookie, "noninitial",
                            Integer.toString(j) , Integer.toString(10));
                    j = j+10;
                }
                new ViewMapSurveys().execute(Boolean.toString(adminstatus), emailstatus, cookie, "noninitial",
                        Integer.toString(j) , Integer.toString(10));

            }

        }catch(Exception e){
            e.printStackTrace();
        }

    }else {
        if(output.length > 1) {
            Log.w("vsonpostexecute", output[1]);

            updateLayoutContent(output[1]);
        }
    }
    }
    }

    public void fetchAllSurveys(Boolean IsAdmin, String UserEmailId, String cookiestring){
        cookie = cookiestring;
        adminstatus = IsAdmin;
        emailstatus = UserEmailId;
        LinearLayout dynamicContent = (LinearLayout) liactivity.getactivityview(R.id.viewmap_surveys_layout);
        dynamicContent.removeAllViews();

//        new com.marveric.bestitude.smartsurvey.ViewSurveys().execute(Boolean.toString(IsAdmin), UserEmailId, cookie, "initial");

        new ViewMapSurveys().execute(Boolean.toString(IsAdmin), UserEmailId, cookie, "initial");

    }

    private void updateLayoutContent(String Response){

        try {
            if (Response != null) {
                vf =  (ViewFlipper) liactivity.getVf();

                LinearLayout dynamicContent = (LinearLayout) liactivity.getactivityview(R.id.viewmap_surveys_layout);
               // dynamicContent.removeAllViews();
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
                 //       TextView viewer = (TextView) liactivity.getactivityview(R.id.commontextview1);
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
                        viewer5.setText("Map Users");
                        //     viewer.setId(10000+i);
 /*                       viewer.setText(Integer.toString(jb.getInt("survey_id"))+"-"+
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

                              //  TextView tv = (TextView) liactivity.getactivityview(v.getId());

                                vf.setDisplayedChild(vf.indexOfChild(liactivity.getactivityview(R.id.mapusers)));
                                liactivity.keepScrollup();
                                vmus.getUsersDetails(completetext);
                                //takeSurvey.CheckDBExists(tv.getText().toString());
                                //takeSurvey.configListener(tv.getText().toString());
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
        FloatingActionButton fab = (FloatingActionButton) liactivity.getactivityview(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "clicked from view map surveys", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
