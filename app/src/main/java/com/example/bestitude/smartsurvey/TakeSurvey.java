package com.example.bestitude.smartsurvey;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;

public class TakeSurvey extends AsyncTask<String, String, String> {

    private static LoggedinActivity liactivity;
    private static CheckAuthHandler cauth;
//    private static TakeSurvey takeSurvey;
    private static UpdateSurvey updateSurvey;
    private static ConnectwithAPI cwapi;
    TakeSurvey(){ }
    TakeSurvey(LoggedinActivity lia){
        liactivity = lia;
  //      takeSurvey = new TakeSurvey();
        updateSurvey = new UpdateSurvey(liactivity);
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
        String edata[] = emailId.split("@");
        String surveyid = arg[2];
        try {

            Map<String,String> map=new HashMap<String,String>();
            map.put("request", "createtable");
            map.put("emailid", edata[0] + "_" + edata[1]);
            map.put("surveyid", surveyid);
            cwapi.doConnect(map, cauth.getCookiegotten());
            String Response = cwapi.getResponse();
            Log.w("vs fas takesurvey", "as"+Response);

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
    }


    public void CheckDBExists(String s){
        String data[] = s.split("-");
        new TakeSurvey().execute("createtable",cauth.getUserEmailid(), data[0]);
    }
    public void configListener(String s){
        final String value = s;
        Button btsaverepeat = (Button) liactivity.getactivityview(R.id.saverepeat);
        btsaverepeat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                if(updateSurvey.ValidateData()){
                        updateSurvey.sendData(value);
                        Log.w("takesurvey", "success");

                }
            }
        });
    }
}
