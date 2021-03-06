package com.marveric.bestitude.smartsurvey;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class vmusMapSubmit extends AsyncTask<String, String, String> {

    private static LoggedinActivity liactivity;
    private static CheckAuthHandler cauth;
    private static ConnectwithAPI cwapi;

    private static int ocount;



    vmusMapSubmit() {
    }

    vmusMapSubmit(LoggedinActivity lia) {
        liactivity = lia;
        cauth = new CheckAuthHandler();
        cwapi = new ConnectwithAPI("http://www.tutorialspole.com/smartsurvey/surveyif.php","POST");

    }

    public void submitUserstoSurvey(String surveyid, Vector<View> suid) {
/*        LinearLayout dynamicContent = (LinearLayout) liactivity.getactivityview(R.id.viewmap_users_surveys_layout);
        int count = dynamicContent.getChildCount();
        if(BuildConfig.DEBUG) Log.i("surveyid",surveyid);
        for (int i = 0; i < count; i++) {
            View v = dynamicContent.getChildAt(i);
            if (v instanceof CheckBox) {
                ocount++;
                if (((CheckBox) v).isChecked()) {
                    new vmusMapSubmit().execute(((CheckBox) v).getText().toString(), surveyid, "true");
                } else {
                    new vmusMapSubmit().execute(((CheckBox) v).getText().toString(), surveyid, "false");
                }
                //            totalValues = ((CheckBox) v).getText().toString();
            }
        }
*/

        Enumeration e=suid.elements();
        while(e.hasMoreElements()){
//            System.out.println(e.nextElement());
            View v = (View) e.nextElement();
            if (v instanceof CheckBox) {

                if (((CheckBox) v).isChecked()) {
                    new vmusMapSubmit().execute(((CheckBox) v).getText().toString(), surveyid, "true");
                } else {
                    new vmusMapSubmit().execute(((CheckBox) v).getText().toString(), surveyid, "false");
                }
                //            totalValues = ((CheckBox) v).getText().toString();
            }
        }
    }

    @Override
    protected void onPreExecute() {
        //Setup precondition to execute some task
    }

    @Override
    protected String doInBackground(String... arg) {
        //Do some task
        publishProgress("Processing");
        String email = arg[0];
        String sid = arg[1];
        String action = arg[2];
        try{
            Map<String,String> map=new HashMap<String,String>();
            if(action == "true"){
                map.put("request","checkandinsertusermap");
            }else{
                map.put("request","deleteusermap");
            }
            map.put("surveyid", sid);
            map.put("userid", email);

		map.put("username","admin");
                map.put("password","angelEAR2");
            cwapi.doConnect(map, cauth.getCookiegotten());
            String Response = cwapi.getResponse();

if(BuildConfig.DEBUG) Log.i("test result", Response);

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
        if(BuildConfig.DEBUG) Log.i("vsonpostexecute", s);
        ocount--;
        if(ocount == 0){
            Toast.makeText(liactivity,
                    "Data Updated Successfully", Toast.LENGTH_SHORT).show();
        }
    }

}

