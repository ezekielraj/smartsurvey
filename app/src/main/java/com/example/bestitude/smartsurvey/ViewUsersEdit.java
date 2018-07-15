package com.marveric.bestitude.smartsurvey;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class ViewUsersEdit extends AsyncTask<String, String, String> {

    private static ConnectwithAPI cwapi;
    private static LoggedinActivity liactivity;
    private ViewUsers vu;

    ViewUsersEdit(){ }
    ViewUsersEdit( LoggedinActivity lia ){
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
        String slno = arg[0];
        String userEmailid = arg[1];
        String isvalid = arg[2];
        String isadmin = arg[3];
        try {
if(slno.equals("update")) {
    Map<String, String> mapua = new HashMap<String, String>();
    if (isadmin.equals("true")) {
        mapua.put("request", "enableadmin");
    } else {
        mapua.put("request", "disableadmin");
    }
    mapua.put("emailid", userEmailid);
    cwapi.doConnect(mapua, vu.getCookie());
    String Response = cwapi.getResponse();
    Log.w("vuadd isadmin update", "as" + Response);

    Map<String, String> mapuv = new HashMap<String, String>();
    if (isvalid.equals("false")) {
        mapuv.put("request", "disablevalid");
    } else {
        mapuv.put("request", "enablevalid");
    }
    mapuv.put("emailid", userEmailid);
    cwapi.doConnect(mapuv, vu.getCookie());
    Response = cwapi.getResponse();
    Log.w("vuadd isvalid update", "as" + Response);
    return "Updated";
}
if(slno.equals("delete")){
    Map<String, String> mapd = new HashMap<String, String>();
    mapd.put("request", "delete");
    mapd.put("emailid", userEmailid);
    cwapi.doConnect(mapd, vu.getCookie());
    String Response = cwapi.getResponse();
    Log.w("vuadd delete", "as" + Response);
    return "Deleted";
}
            return "true";

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

        Log.w("onpostexecute edit", s);
        TextView tv = (TextView) liactivity.getactivityview(R.id.status_text_edit);
        tv.setText("Status: "+s);
        if(s.equals("Deleted")) {
            TextView Emailid = (TextView) liactivity.getactivityview(R.id.emailaddredittext);
            Emailid.setText("");
        }


    }

    public void configListener(String s){
        Log.w("view users edit", s);
        String data[] = s.split("-");
        TextView slnodbid = (TextView) liactivity.getactivityview(R.id.slnodbid);
        slnodbid.setText(data[0]);
        TextView email = (TextView) liactivity.getactivityview(R.id.emailaddredittext);
        email.setText(data[1]);
        Switch isvalid = (Switch) liactivity.getactivityview(R.id.isvalidedit);
        isvalid.setChecked(data[2].equals("1"));
        Switch isadmin = (Switch) liactivity.getactivityview(R.id.isadminedit);
        isadmin.setChecked(data[3].equals("1"));
        Button bt = (Button) liactivity.getactivityview(R.id.vuedituser);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Updateuser("update");
            }
        });
        Button btd = (Button) liactivity.getactivityview(R.id.vudeleteuser);
        btd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Updateuser("delete");
            }
        });
    }
    private void Updateuser(String st){
        String isvalidvalue = "false";
        String isadminvalue = "false";
        TextView slnodbid = (TextView) liactivity.getactivityview(R.id.slnodbid);
        TextView email = (TextView) liactivity.getactivityview(R.id.emailaddredittext);
        Switch isvalid = (Switch) liactivity.getactivityview(R.id.isvalidedit);
        if(isvalid.isChecked()){ isvalidvalue = "true"; }
        Switch isadmin = (Switch) liactivity.getactivityview(R.id.isadminedit);
        if(isadmin.isChecked()){ isadminvalue = "true"; }
        new ViewUsersEdit().execute(st, email.getText().toString(), isvalidvalue, isadminvalue);
    }


}
