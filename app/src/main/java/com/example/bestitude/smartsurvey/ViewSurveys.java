package com.example.bestitude.smartsurvey;

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
    //private static ViewUsersEdit vue;
    private static String cookie = "";

    ViewSurveys(){ }
    ViewSurveys(LoggedinActivity lia){
        liactivity = lia;
        vf =  (ViewFlipper) liactivity.getVf();
        takeSurvey = new TakeSurvey(liactivity);
        cwapi = new ConnectwithAPI("http://www.tutorialspole.com/smartsurvey/surveyif.php","POST");
        addSurvey = new AddSurvey(liactivity);
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
        new ViewSurveys().execute(Boolean.toString(IsAdmin), UserEmailId, cookie);

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
                        viewer2.setText(jb.getString("locality"));
                        viewer3.setText(jb.getString("state"));
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
}
