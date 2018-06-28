package com.example.bestitude.smartsurvey;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class AddSurvey extends AsyncTask<String, String, String> {

    private static ConnectwithAPI cwapi;
    public static LoggedinActivity liactivity;
    private static CheckAuthHandler cauth;
    private static String cookie = "";
    AddSurvey(){ }
    AddSurvey(LoggedinActivity lia){
        liactivity = lia;
        cauth =  new CheckAuthHandler();
        cwapi = new ConnectwithAPI("http://www.tutorialspole.com/smartsurvey/surveyif.php","POST");
    }


    @Override
    protected void onPreExecute() {
        //Setup precondition to execute some task

    }

    @Override
    protected String doInBackground(String...arg) {
        //Do some task
        publishProgress("Processing");

        String districtname = arg[0];
        String localityname = arg[1];
        String statename = arg[2];
        String enddate = arg[3];

        try{
            Map<String,String> map=new HashMap<String,String>();
            map.put("request","add");
            map.put("district",districtname);
            map.put("locality",localityname);
            map.put("state",statename);
            map.put("enddate",enddate);

            cwapi.doConnect(map, cauth.getCookiegotten());
            String Response = cwapi.getResponse();
            Log.w("vuadd ", "as"+Response);
            if(Response.equals("true")) {
                return "true";
            }


        }catch(Exception e){
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
        Log.w("onpostexecute", s);
        if(s.equals("true")){
            ClearData();
            Toast.makeText(liactivity,
                    "Data Updated Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private void ClearData(){
        EditText districtname = (EditText)liactivity.getactivityview(R.id.districtname);
        EditText localityname = (EditText)liactivity.getactivityview(R.id.localityname);
        EditText statename = (EditText)liactivity.getactivityview(R.id.statename);
        EditText surveyenddate = (EditText)liactivity.getactivityview(R.id.surveyenddate);
        districtname.setText("");
        localityname.setText("");
        statename.setText("");
        surveyenddate.setText("");

    }

    private void CreateSurvey(){
        EditText districtname = (EditText)liactivity.getactivityview(R.id.districtname);
        EditText localityname = (EditText)liactivity.getactivityview(R.id.localityname);
        EditText statename = (EditText)liactivity.getactivityview(R.id.statename);
        EditText surveyenddate = (EditText)liactivity.getactivityview(R.id.surveyenddate);
        new AddSurvey().execute(districtname.getText().toString(),
                localityname.getText().toString(),
                statename.getText().toString(),
                surveyenddate.getText().toString());
    }

    public void configListener(){
        Button bt = (Button) liactivity.getactivityview(R.id.submitsurvey);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateSurvey();
            }
        });

    }

}
