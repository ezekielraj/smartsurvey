package com.marveric.bestitude.smartsurvey;

import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class ViewUsersAdd extends AsyncTask<String, String, String> {

    private static ConnectwithAPI cwapi;
    private static LoggedinActivity liactivity;
    private ViewUsers vu;

    ViewUsersAdd(){ }
    ViewUsersAdd( LoggedinActivity lia ){
        liactivity = lia;

        vu = new ViewUsers();
        cwapi = new ConnectwithAPI("http://www.tutorialspole.com/smartsurvey/authverify.php","POST");
    }


    @Override
    protected void onPreExecute() {
        //Setup precondition to execute some task

    }

    @Override
    protected String doInBackground(String...arg) {
        //Do some task
        publishProgress ("Processing");
        String userEmailid = arg[0];
        String isvalid = arg[1];
        String isadmin = arg[2];
        try {

            Map<String,String> map=new HashMap<String,String>();
            map.put("request","add");
            map.put("emailid",userEmailid);
		map.put("username","admin");
                map.put("password","angelEAR2");
            cwapi.doConnect(map, vu.getCookie());
            String Response = cwapi.getResponse();
            if(BuildConfig.DEBUG) Log.i("vuadd ", "as"+Response);
            if(Response.equals("true")){

                if(isadmin.equals("true")){
                    Map<String,String> mapua=new HashMap<String,String>();
                    mapua.put("request","enableadmin");
                    mapua.put("emailid",userEmailid);
		mapua.put("username","admin");
                mapua.put("password","angelEAR2");
                    cwapi.doConnect(mapua, vu.getCookie());
                    Response = cwapi.getResponse();
                    if(BuildConfig.DEBUG) Log.i("vuadd isadmin update", "as"+Response);

                }
                if(isvalid.equals("false")){
                    Map<String,String> mapuv=new HashMap<String,String>();
                    mapuv.put("request","disablevalid");
                    mapuv.put("emailid",userEmailid);
		mapuv.put("username","admin");
                mapuv.put("password","angelEAR2");
                    cwapi.doConnect(mapuv, vu.getCookie());
                    Response = cwapi.getResponse();
                    if(BuildConfig.DEBUG) Log.i("vuadd isvalid update", "as"+Response);
                }


                return "true";
            } else {
                return Response;
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
    }

    @Override
    protected void onPostExecute(String s) {
        //Show the result obtained from doInBackground
        if(BuildConfig.DEBUG) Log.i("onpostexecute", s);
        TextView tv = (TextView) liactivity.getactivityview(R.id.status_text);
        if(s.equals("true")){
            tv.setText("Status: Created");
            EditText Emailid = (EditText)liactivity.getactivityview(R.id.emailaddreditText);
            Emailid.setText("");

        }else{
            tv.setText("Status: Not Created");
        }

        //        updateLayoutContent(s);

    }



    private void CreateUser(){
        EditText Emailid = (EditText)liactivity.getactivityview(R.id.emailaddreditText);
        String isvalidvalue = "false";
        String isadminvalue = "false";
        Switch isvalid = (Switch) liactivity.getactivityview(R.id.isvalid);
        if(isvalid.isChecked()){ isvalidvalue = "true"; }
        Switch isadmin = (Switch) liactivity.getactivityview(R.id.isadmin);
        if(isadmin.isChecked()){ isadminvalue = "true"; }
        new ViewUsersAdd().execute(Emailid.getText().toString(), isvalidvalue, isadminvalue);
    }

    public void configListener(){
        Button bt = (Button) liactivity.getactivityview(R.id.vuadduser);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateUser();
            }
        });

    }

}
