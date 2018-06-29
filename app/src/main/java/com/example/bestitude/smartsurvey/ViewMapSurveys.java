package com.example.bestitude.smartsurvey;

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

public class ViewMapSurveys extends AsyncTask<String, String, String> {

    private static ConnectwithAPI cwapi;
    public static LoggedinActivity liactivity;
    private static ViewFlipper vf;
    //private static ViewUsersEdit vue;
    private static String cookie = "";
    private static ViewMapUsersSurvyes vmus;

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
            cwapi.doConnect(map, cookiegotton);
            String Response = cwapi.getResponse();
            Log.w("vs fetchallsurveys", "as"+Response);

            return Response;

        } catch (Exception e) {
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
    }

    public void fetchAllSurveys(Boolean IsAdmin, String UserEmailId, String cookiestring){
        cookie = cookiestring;
        new ViewMapSurveys().execute(Boolean.toString(IsAdmin), UserEmailId, cookie);

    }

    private void updateLayoutContent(String Response){

        try {
            if (Response != null) {
                vf =  (ViewFlipper) liactivity.getVf();

                LinearLayout dynamicContent = (LinearLayout) liactivity.getactivityview(R.id.viewmap_surveys_layout);
                dynamicContent.removeAllViews();
                JSONArray jsonArray = new JSONArray(Response);
                if (jsonArray != null && jsonArray.length() > 0) {
                    int N = jsonArray.length(); // total number of textviews to add

                    for (int i = 0; i < jsonArray.length(); i++) {

                        Log.w("view surveys -response", "as" + jsonArray.getJSONObject(i));
                        JSONObject jb = new JSONObject(jsonArray.getJSONObject(i).toString());


// assuming your Wizard content is in content_wizard.xml

                        View wizardView = liactivity.getactivityinflator()
                                .inflate(R.layout.viewmapsurvey_include, dynamicContent, false);

// add the inflated View to the layout
                        dynamicContent.addView(wizardView);
                        //   Log.w("vusers layout id", Integer.toString(dynamicContent.getChildCount()));
                        TextView viewer = (TextView) liactivity.getactivityview(R.id.commontextview1);
                        viewer.setId(10000+i);
                        viewer.setText(Integer.toString(jb.getInt("survey_id"))+"-"+
                                jb.getString("district") + "-" +
                                jb.getString("locality") + "-" +
                                jb.getString("state") + "-" +
                                jb.getString("EndDate"));
                        viewer.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                TextView tv = (TextView) liactivity.getactivityview(v.getId());

                                vf.setDisplayedChild(vf.indexOfChild(liactivity.getactivityview(R.id.mapusers)));
                                vmus.getUsersDetails(tv.getText().toString());
                                //takeSurvey.CheckDBExists(tv.getText().toString());
                                //takeSurvey.configListener(tv.getText().toString());
                                Log.w("click for id",tv.getText().toString());
                            }
                        });
                    }
                }
            }
        }catch(Exception e){
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
