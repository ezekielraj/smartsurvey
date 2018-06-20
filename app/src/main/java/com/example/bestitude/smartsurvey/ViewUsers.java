package com.example.bestitude.smartsurvey;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class ViewUsers {

    private static ConnectwithAPI cwapi;
    ViewUsers(){
        cwapi = new ConnectwithAPI("http://www.tutorialspole.com/smartsurvey/authverify.php","POST");
    }
    public void fetchAllUsers(final String userEmailid, final String cookiegotton){
        Thread thread = new Thread(new Runnable(){
            public void run() {
                try {
                    Map<String,String> map=new HashMap<String,String>();
                    map.put("request","getall");
                    map.put("emailid",userEmailid);

                    cwapi.doConnect(map, cookiegotton);

                    String Response = cwapi.getResponse();
                    Log.w("vu fetchallusers", "as"+Response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

}
