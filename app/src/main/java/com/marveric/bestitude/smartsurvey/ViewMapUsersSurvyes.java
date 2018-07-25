package com.marveric.bestitude.smartsurvey;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class ViewMapUsersSurvyes extends AsyncTask<String, String, String> {

    private static LoggedinActivity liactivity;
    private static CheckAuthHandler cauth;
    private static ConnectwithAPI cwapi;
    private static ViewFlipper vf;
    private static vmusMapSubmit vmusms;
    private static String sid;
    private static Vector<View> suid=new Vector<View>();
    ViewMapUsersSurvyes(){ }
    ViewMapUsersSurvyes(LoggedinActivity lia){
        liactivity = lia;
        vmusms = new vmusMapSubmit(liactivity);
        vf =  (ViewFlipper) liactivity.getVf();
        cauth = new CheckAuthHandler();
        cwapi = new ConnectwithAPI("http://www.tutorialspole.com/smartsurvey/surveyif.php","POST");
    }



    @Override
    protected void onPreExecute() {
        //Setup precondition to execute some task
    }

    @Override
    protected String doInBackground(String...arg) {
        //Do some task
        publishProgress ("Processing");
        String surveyid = arg[0];
        Log.w("getud00", surveyid);
        sid = surveyid;
        try{
            Map<String,String> map=new HashMap<String,String>();
            map.put("request","getvalidsurveyusers");
            map.put("surveyid", surveyid);
		map.put("username","admin");
                map.put("password","angelEAR2");
            cwapi.doConnect(map, cauth.getCookiegotten());
            String Response = cwapi.getResponse();
            if(Response != "false"){
                return Response;
            }
            Log.w("vmus fetchallsurveys", "as"+Response);

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
        updateDataCheckbox(s);
    }

    private void updateDataCheckbox(String Response){
        try {
            suid.clear();
            if (Response != null) {
                vf =  (ViewFlipper) liactivity.getVf();

                LinearLayout dynamicContent = (LinearLayout) liactivity.getactivityview(R.id.viewmap_users_surveys_layout);
                dynamicContent.removeAllViews();
                JSONArray jsonArray = new JSONArray(Response);
                if (jsonArray != null && jsonArray.length() > 0) {
                    int N = jsonArray.length(); // total number of textviews to add

                    for (int i = 0; i < jsonArray.length(); i++) {

                        Log.w("vmu surveys -response", "as" + jsonArray.getJSONObject(i));
                        JSONObject jb = new JSONObject(jsonArray.getJSONObject(i).toString());


// assuming your Wizard content is in content_wizard.xml

                        View wizardView = liactivity.getactivityinflator()
                                .inflate(R.layout.viewmapuserssurvey_include, dynamicContent, false);

// add the inflated View to the layout
                        dynamicContent.addView(wizardView);
                        //   Log.w("vusers layout id", Integer.toString(dynamicContent.getChildCount()));
                        CheckBox viewer = (CheckBox) liactivity.getactivityview(R.id.commoncheckbox);
                        viewer.setId(View.generateViewId());
                        viewer.setText(jb.getString("Email"));

                        if(jb.get("surveyid").toString() != "null"){
                            viewer.setChecked(true);
                        }

                        viewer.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v){
                                suid.add(liactivity.getactivityview(v.getId()));
                            }
                        });

                  /*      viewer.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                         //       CheckBox tv = (CheckBox) liactivity.getactivityview(v.getId());

                            //    vf.setDisplayedChild(vf.indexOfChild(liactivity.getactivityview(R.id.mapusers)));
                             //   vmus.getUsersDetails(tv.getText().toString());
                                //takeSurvey.CheckDBExists(tv.getText().toString());
                                //takeSurvey.configListener(tv.getText().toString());
                                Log.w("click for id",tv.getText().toString());
                            }
                        });
                   */ }
                    View wizardView = liactivity.getactivityinflator()
                            .inflate(R.layout.vmus_button_include, dynamicContent, false);

// add the inflated View to the layout
                    dynamicContent.addView(wizardView);
                    Button viewbut = (Button) liactivity.getactivityview(R.id.mapuserssurveysubmit);
                    viewbut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            vmusms.submitUserstoSurvey(sid, suid);
                            suid.clear();
                        }
                    });

                }
            }
        }catch(Exception e){
            liactivity.saveException(e);
            e.printStackTrace();
        }


    }

    public void getUsersDetails(String s){

        String data[] = s.split("-");
        sid = data[0];
        new ViewMapUsersSurvyes().execute(data[0]);
    }
}

