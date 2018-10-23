package com.marveric.bestitude.smartsurvey;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;

public class BSyncSurvey extends AsyncTask<String, String, String> {

    private static LoggedinActivity liactivity;
    private static SQLiteDatabase ldber;
    private static CheckAuthHandler cauth;
    private static ConnectwithAPI cwapi;
    private static DayCount dc;
    private static ArrayList<AsyncTask> ss;
    private static String UFilename;// = cauth.getUserEmailid().replaceAll("\\.","_").replaceAll("@","_").toLowerCase()+".sql";

    private static FileOutputStream outputStream;
    private static File UFile;

    private static int daycountvalue;



    BSyncSurvey(){ }
    BSyncSurvey(LoggedinActivity lia, SQLiteDatabase ldb){
        liactivity = lia;
        ldber = ldb;
        dc = new DayCount(liactivity);
        cauth = new CheckAuthHandler();
        cwapi = new ConnectwithAPI("http://www.tutorialspole.com/smartsurvey/takesurveyfile.php","POST");
        //dc.incrementonlinecount()
        ss = new ArrayList<AsyncTask>();
        UFilename = cauth.getUserEmailid().replaceAll("\\.","_").replaceAll("@","_").toLowerCase()+".sql";
        UFile = new File(liactivity.getFilesDir(), UFilename);
    }

    @Override
    protected void onPreExecute() {
        //Setup precondition to execute some task
    }

    @Override
    protected String doInBackground(String...arg) {
        publishProgress("Starting to push");
        String OutputResponse;
        try {
            cwapi.doConnectFile(UFile);
            OutputResponse = cwapi.getResponse();

            String resultOut[] = OutputResponse.split(",");
            publishProgress("<br></br>"+OutputResponse);
            //Log.w("output",resultOut[1]);
            if(resultOut[1].trim().equals("true")){
              //  Log.w("output1",resultOut[1]);
                incrementall();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return "true";
    }

    @Override
    protected void onProgressUpdate(String...values) {
        //Update the progress of current task
        TextView stv = (TextView) liactivity.getactivityview(R.id.syncalllayoutstatus);

        stv.append(Html.fromHtml(values[0]));
        // stv = null;
//        delete stv;
    }

    @Override
    protected void onPostExecute(String s) {
        if(BuildConfig.DEBUG) Log.i("bulk vsonpostexecute", s);
        Toast toast = Toast.makeText(liactivity,
                "Bulk Data Successfully Synced Online", Toast.LENGTH_SHORT);
        ViewGroup group = (ViewGroup) toast.getView();
        TextView messageTextView = (TextView) group.getChildAt(0);
        messageTextView.setTextSize(25);
        if(s.equals("true")) {
            toast.show();
            try {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(liactivity.getApplicationContext(), notification);
                r.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public void bulkSyncAll(){
        daycountvalue = 0;
        String emptyContent = "";
        TextView stv = (TextView) liactivity.getactivityview(R.id.syncalllayoutstatus);
        stv.setText("");

        //if(UFile.exists()){

            try {
                //outputStream = new FileOutputStream(UFile);
                outputStream = liactivity.openFileOutput(UFilename, liactivity.MODE_PRIVATE);
                outputStream.write(emptyContent.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


        //}
        Cursor c = ldber.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        stv.append("Selected All local TAbles");
        ArrayList<String> Tablename = new ArrayList<String>();
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {

                if(c.getString(0).toLowerCase().contains(cauth.getUserEmailid().replaceAll("\\.","_").replaceAll("@","_").toLowerCase())) {
                    Cursor ac = ldber.rawQuery("select * from " + c.getString(0), null);
                    int deleteStateColumnIndex = ac.getColumnIndex("SYNC_STATUS");
                    if (deleteStateColumnIndex < 0) {
                        // missing_column not there - add it
                        ldber.execSQL("ALTER TABLE " + c.getString(0) + " ADD COLUMN SYNC_STATUS int default 0;");
                    }
                    ac = ldber.rawQuery("select * from " + c.getString(0) + " where SYNC_STATUS = ?", new String[]{"0"});
                    if(ac.getCount() > 0){
                        Tablename.add(0,c.getString(0));
                    }else{
                        //Tablename.add(c.getString(0));
                    }
                }
                c.moveToNext();
            }
        }
        for (String s : Tablename) {
            if (s.toLowerCase().contains(cauth.getUserEmailid().replaceAll("\\.", "_").replaceAll("@", "_").toLowerCase())) {
                String Email[] = s.split("_");
                String Emailid = "";
                for (int i = 0; i < (Email.length - 1); i++) {
                    Emailid = Emailid + Email[i] + "_";
                }
                stv.append("\n"+s+" Table added");
                String uemailid = Emailid.substring(0, Emailid.length() - 1);
                String uSurvey_id = Email[Email.length - 1];

                String AddContent =
                "\nCREATE TABLE IF NOT EXISTS `"+uemailid+"_"+uSurvey_id+"` ("+
                        "`id` int(50) NOT NULL AUTO_INCREMENT,"+
                        "`name` varchar(255) NOT NULL,"+
                        "`sex` varchar(255) NOT NULL,"+
                        "`age` varchar(255) NOT NULL,"+
                        "`bodyphysique` varchar(255) NOT NULL,"+
                        "`alcohol` varchar(255) NOT NULL,"+
                        "`smooking` varchar(255) NOT NULL,"+
                        "`tobacco_chewing` varchar(255) NOT NULL,"+
                        "`occupation` varchar(255) NOT NULL,"+
                        "`pesticide_applicator` varchar(255) NOT NULL,"+
                        "`mixing_and_handlin_of_pesticide` varchar(255) NOT NULL,"+
                        "`working_pesticide_sprayed_field` varchar(255) NOT NULL,"+
                        "`working_in_pesticide_shop` varchar(255) NOT NULL,"+
                        "`use_of_insect_repellents_at_home` varchar(255) NOT NULL,"+
                        "`no_direct_exposure` varchar(255) NOT NULL,"+
                        "`use_of_reverse_osmosis_water_for_drinking` varchar(255) NOT NULL,"+
                        "`diabetes` varchar(255) NOT NULL,"+
                        "`hypertension` varchar(255) NOT NULL,"+
                        "`other_diseases` varchar(1000) NOT NULL,"+
                        "`user_remarks` varchar(1000) NOT NULL,"+
                        "`inserted_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,"+
                        "PRIMARY KEY(`id`)"+
                        ") ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;\n";

                Cursor  cursor = ldber.rawQuery("select * from " + uemailid + "_" + uSurvey_id + " where SYNC_STATUS = ?", new String[]{"0"});
                stv.append("\nCount:"+Integer.toString(cursor.getCount()));
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {

                        AddContent = AddContent +
                                "INSERT INTO `"+uemailid+"_"+uSurvey_id+"`" +
                                "(" +
                                "`name`," +
                                "`sex`," +
                                "`age`," +
                                "`bodyphysique`," +
                                "`alcohol`," +
                                "`smooking`," +
                                "`tobacco_chewing`," +
                                "`occupation`," +
                                "`pesticide_applicator`," +
                                "`mixing_and_handlin_of_pesticide`," +
                                "`working_pesticide_sprayed_field`," +
                                "`working_in_pesticide_shop`," +
                                "`use_of_insect_repellents_at_home`," +
                                "`no_direct_exposure`," +
                                "`use_of_reverse_osmosis_water_for_drinking`," +
                                "`diabetes`," +
                                "`hypertension`," +
                                "`other_diseases` " +
                                ")" +
                                "VALUES (" +
                                "'"+cursor.getString(cursor.getColumnIndex("name"))+"' ," +
                                "'"+cursor.getString(cursor.getColumnIndex("sex"))+"' ," +
                                "'"+cursor.getString(cursor.getColumnIndex("age"))+"' ," +
                                "'"+cursor.getString(cursor.getColumnIndex("bodyphysique"))+"' ," +
                                "'"+cursor.getString(cursor.getColumnIndex("alcohol"))+"' ," +
                                "'"+cursor.getString(cursor.getColumnIndex("smooking"))+"' ," +
                                "'"+cursor.getString(cursor.getColumnIndex("tobacco_chewing"))+"' ," +
                                "'"+cursor.getString(cursor.getColumnIndex("occupation"))+"' ," +
                                "'"+cursor.getString(cursor.getColumnIndex("pesticide_applicator"))+"' ," +
                                "'"+cursor.getString(cursor.getColumnIndex("mixing_and_handlin_of_pesticide"))+"' ," +
                                "'"+cursor.getString(cursor.getColumnIndex("working_pesticide_sprayed_field"))+"' ," +
                                "'"+cursor.getString(cursor.getColumnIndex("working_in_pesticide_shop"))+"' ," +
                                "'"+cursor.getString(cursor.getColumnIndex("use_of_insect_repellents_at_home"))+"' ," +
                                "'"+cursor.getString(cursor.getColumnIndex("no_direct_exposure"))+"' ," +
                                "'"+cursor.getString(cursor.getColumnIndex("use_of_reverse_osmosis_water_for_drinking"))+"' ," +
                                "'"+cursor.getString(cursor.getColumnIndex("diabetes"))+"' ," +
                                "'"+cursor.getString(cursor.getColumnIndex("hypertension"))+"' ," +
                                "'"+cursor.getString(cursor.getColumnIndex("other_diseases"))+"'" +
                                ");";
                        daycountvalue++;

                        cursor.moveToNext();
                    }
                }
                //if(UFile.exists()){

                    try {
                        //outputStream = new FileOutputStream(UFile, true);
                        outputStream = liactivity.openFileOutput(UFilename, liactivity.MODE_APPEND);
                        outputStream.write(AddContent.getBytes());
                        outputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                //}

            }

        }
        /*try {
            BufferedReader br = new BufferedReader(new FileReader(UFile));
            String line;

            while ((line = br.readLine()) != null) {
                stv.append(line);
                stv.append("\n");
            }
            br.close();
        }
        catch (Exception e) {
            //You'll need to add proper error handling here
            e.printStackTrace();
        }
        */
        new BSyncSurvey().execute();
    }

    public void incrementall(){


        Cursor c = ldber.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        ArrayList<String> Tablename = new ArrayList<String>();
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {

                if(c.getString(0).toLowerCase().contains(cauth.getUserEmailid().replaceAll("\\.","_").replaceAll("@","_").toLowerCase())) {
                    Cursor ac = ldber.rawQuery("select * from " + c.getString(0), null);
                    int deleteStateColumnIndex = ac.getColumnIndex("SYNC_STATUS");
                    if (deleteStateColumnIndex < 0) {
                        // missing_column not there - add it
                        ldber.execSQL("ALTER TABLE " + c.getString(0) + " ADD COLUMN SYNC_STATUS int default 0;");
                    }
                    ac = ldber.rawQuery("select * from " + c.getString(0) + " where SYNC_STATUS = ?", new String[]{"0"});
                    if(ac.getCount() > 0){
                        Tablename.add(0,c.getString(0));
                    }else{
                        //Tablename.add(c.getString(0));
                    }
                }
                c.moveToNext();
            }
        }

        for (String s : Tablename) {

            if (s.toLowerCase().contains(cauth.getUserEmailid().replaceAll("\\.", "_").replaceAll("@", "_").toLowerCase())) {
//                Log.w("outputtable", s);
                //String selection1 = "id = ?";
                //String[] selectionArgs1 = {cursor.getString(cursor.getColumnIndex("id"))};

                ContentValues values1 = new ContentValues();
                values1.put("SYNC_STATUS", "1");

                ldber.update(
                        s,
                        values1,
                        null,
                        null);

            }
        }
        for(int i = 0; i < daycountvalue; i++){
            dc.incrementsynccount();
        }



    }
}
