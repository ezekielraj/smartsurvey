package com.marveric.bestitude.smartsurvey;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import android.view.View;
import android.widget.ViewFlipper;

public class ViewUsers extends AsyncTask<String, String, String> {


    private static ConnectwithAPI cwapi;
    public static LoggedinActivity liactivity;
    private static ViewFlipper vf;
    private static ViewUsersAdd vua;
    private static ViewUsersEdit vue;
    private static String cookie = "";
    ViewUsers(){ }
    ViewUsers(LoggedinActivity lia){
        liactivity = lia;
        vf =  (ViewFlipper) liactivity.getVf();
        vua = new ViewUsersAdd(liactivity);
        vue = new ViewUsersEdit(liactivity);
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
        String cookiegotton = arg[1];
        try {

            Map<String,String> map=new HashMap<String,String>();
            map.put("request","getall");
            map.put("emailid",userEmailid);
		map.put("username","admin");
                map.put("password","angelEAR2");
            cwapi.doConnect(map, cookiegotton);
            String Response = cwapi.getResponse();
            if(BuildConfig.DEBUG) Log.i("vu fetchallusers", "as"+Response);

            return Response;

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
    if(s != null) {
        if(BuildConfig.DEBUG) Log.i("onpostexecute", s);
        updateLayoutContent(s);
    }
    }



    public void fetchAllUsers(final String userEmailid, final String cookiegotton){
        cookie = cookiegotton;
        new ViewUsers().execute(userEmailid, cookiegotton);

//        doInBackground(userEmailid,cookiegotton);
    }

    private void updateLayoutContent(String Response){

        try {
            if (Response != null) {

                LinearLayout dynamicContent = (LinearLayout) liactivity.getactivityview(R.id.view_users_layout);
                dynamicContent.removeAllViews();
                JSONArray jsonArray = new JSONArray(Response);
                if (jsonArray != null && jsonArray.length() > 0) {
                    int N = jsonArray.length(); // total number of textviews to add

                    for (int i = 0; i < jsonArray.length(); i++) {

                        if(BuildConfig.DEBUG) Log.i("view users -response", "as" + jsonArray.getJSONObject(i));
                        JSONObject jb = new JSONObject(jsonArray.getJSONObject(i).toString());


// assuming your Wizard content is in content_wizard.xml

                        View wizardView = liactivity.getactivityinflator()
                                .inflate(R.layout.viewusers_include, dynamicContent, false);

// add the inflated View to the layout
                        dynamicContent.addView(wizardView);
                     //   if(BuildConfig.DEBUG) Log.i("vusers layout id", Integer.toString(dynamicContent.getChildCount()));
                        //TextView viewer = (TextView) liactivity.getactivityview(R.id.commontextview);

                        TextView viewer = (TextView) liactivity.getactivityview(R.id.useridtext);
                        TextView viewer1 = (TextView) liactivity.getactivityview(R.id.emailaddridtext);
                        TextView viewer2 = (TextView) liactivity.getactivityview(R.id.isvalididtext);
                        TextView viewer3 = (TextView) liactivity.getactivityview(R.id.isadminidtext);
                        TextView viewer4 = (TextView) liactivity.getactivityview(R.id.vusubmitbuttonidtext);

                        viewer.setId(View.generateViewId());
                        viewer1.setId(View.generateViewId());
                        viewer2.setId(View.generateViewId());
                        viewer3.setId(View.generateViewId());
                        viewer4.setId(View.generateViewId());



//                        viewer.setId(100+i);
  /*                      viewer.setText(Integer.toString(jb.getInt("Slno"))+"-"+
                                jb.getString("Email") + "-" +
                                Integer.toString(jb.getInt("IsValid")) + "-" +
                                Integer.toString(jb.getInt("IsAdmin")));
    */
                        viewer.setText(Integer.toString(jb.getInt("Slno")));
                        viewer1.setText(jb.getString("Email"));
                        viewer2.setText(jb.getString("IsValid"));
                        viewer3.setText(jb.getString("IsAdmin"));

                        viewer4.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                String completetext = "";

                                TextView tv = (TextView) liactivity.getactivityview((v.getId() - 4));
                                TextView tv1 = (TextView) liactivity.getactivityview((v.getId() - 3));
                                TextView tv2 = (TextView) liactivity.getactivityview((v.getId() - 2));
                                TextView tv3 = (TextView) liactivity.getactivityview((v.getId() - 1));

                                completetext = tv.getText().toString() + "-" +
                                        tv1.getText().toString() + "-" +
                                        tv2.getText().toString() + "-" +
                                        tv3.getText().toString();

//                                        TextView tv = (TextView) liactivity.getactivityview(v.getId());
                                vf.setDisplayedChild(vf.indexOfChild(liactivity.getactivityview(R.id.vuedit)));
                                liactivity.keepScrollup();
                                vue.configListener(completetext);
                                if(BuildConfig.DEBUG) Log.i("click for id",completetext);
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
        vf =  (ViewFlipper) liactivity.getVf();
        FloatingActionButton fab = (FloatingActionButton) liactivity.getactivityview(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vf.setDisplayedChild(vf.indexOfChild(liactivity.getactivityview(R.id.vuadd)));
                liactivity.keepScrollup();
                vua.configListener();
            }
        });
    }
    public static String getCookie(){
        return cookie;
    }

}
