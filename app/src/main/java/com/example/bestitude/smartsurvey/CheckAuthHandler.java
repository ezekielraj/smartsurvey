package com.example.bestitude.smartsurvey;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.xml.validation.Validator;

class CheckAuthHandler {

    private static ConnectwithAPI cwapi;
    private String username = "admin";
    private String password = "angelEAR2";
    private int checkerforsession = 0;
    private static String cookiegotten;
    private static SQLiteDatabase db;
    private static String UserEmailid = null;
    private static int ValidUser=0;
    private static int AdminUser=0;
    private int CheckEmailExistsfnflag = 1;
    private final static String Authtablename = "AuthVerification";

    CheckAuthHandler(DatabaseHelper mDbHelper){
        checkerforsession = 0;
        db = mDbHelper.getWritableDatabase();


        cwapi = new ConnectwithAPI("http://www.tutorialspole.com/smartsurvey/login.php","POST");

        Thread thread = new Thread(new Runnable(){
            public void run() {
                try {
                    checkerforsession = 0;
                    Map<String,String> map=new HashMap<String,String>();
                    map.put("username","admin");
                    map.put("password","angelEAR2");
                    cwapi.doConnect(map, null);
                    cookiegotten = cwapi.getCookie();
                    Log.w("login-response", "as"+cwapi.getCookie());
                    Log.w("login-response", "as"+cwapi.getResponseCode());
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    checkerforsession = 1;
                }
            }
        });

        thread.start();
        while(true){
            if(checkerforsession == 1){
                break;
            }
        }
        Log.w("Checkauthhandler ctor","stopped");
    }

    public void CheckEmailExists(final String emailid){
        Log.w("CheckEmailExists","Started");

        UserEmailid = emailid;
        if(!this.IsFileExists()){
while(true){
    if(checkerforsession == 1){
        break;
    }
}
            Thread thread = new Thread(new Runnable(){
                public void run() {
                    try {
                        Log.w("CheckEmailExiststhread","Started");
                        CheckEmailExistsfnflag = 0;
                        cwapi.newConnection("http://www.tutorialspole.com/smartsurvey/authverify.php", "POST");
                        Map<String,String> map=new HashMap<String,String>();
                        map.put("request","getone");
                        map.put("emailid",emailid);

                        cwapi.doConnect(map, cookiegotten);

                        String Response = cwapi.getResponse();

                      //  Response = Response.substring(0, (Response.length()-1));
                      //  Response = Response.substring(1);
                        Log.w("getall-response", "as"+Response);
                        if(Response != ""){
                           // JSONObject resobj = new JSONObject(Response);
                            JSONArray jsonArray = new JSONArray(Response);
                            if(jsonArray!=null && jsonArray.length()>0){
                                for (int i=0; i < jsonArray.length(); i++) {
                                    Log.w("getall-response", "as"+jsonArray.getJSONObject(i));
                                    JSONObject jb = new JSONObject(jsonArray.getJSONObject(i).toString());
                                    Log.w("getall-response", "as"+jb.get("IsValid"));
                                    if(jb.getInt("IsValid") == 1) {
                                        ValidUser = 1;
                                    }else{
                                        ValidUser = 0;
                                    }
                                    if(jb.getInt("IsAdmin") == 1){
                                        AdminUser = 1;
                                    }else{
                                        AdminUser = 0;
                                    }
                                    Log.w("checkemailexists","values"+Integer.toString(ValidUser)+Integer.toString(AdminUser));
                                    updateLocalAuthVerification();
                                    CheckEmailExistsfnflag = 1;
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();


        }
        while(true){
            if(CheckEmailExistsfnflag == 1){
                break;
            }
        }
        Log.w("CheckEmailExists","Completed");

    }

    private void updateLocalAuthVerification(){
        Log.w("ulav","started");
        ContentValues values = new ContentValues();
        values.put("IsValid", ValidUser);
        values.put("IsAdmin", AdminUser);
    Log.w("ulav","values"+Integer.toString(ValidUser)+Integer.toString(AdminUser));
        // Which row to update, based on the title
        String selection = "Emailid LIKE ?";
        String[] selectionArgs = { UserEmailid };

        int count = db.update(
                Authtablename,
                values,
                selection,
                selectionArgs);

Log.w("ulav","completed");
    }
    public boolean IsFileExists(){
    Log.w("IsFileExists","Started");
        while(true){
            if(CheckEmailExistsfnflag == 1){
                break;
            }
        }
        String[] projection = {
                "id",
                "Emailid",
                "IsValid",
                "IsAdmin"
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = "Emailid = ?";
        String[] selectionArgs = { UserEmailid };

        String sortOrder =
                "Emailid DESC";

        Cursor cursor = db.query(
                Authtablename,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

Log.w("rowCount", Integer.toString(cursor.getCount()));

        if(cursor.getCount() == 0){
            ContentValues values = new ContentValues();
            values.put("EmailId", UserEmailid);
            values.put("IsValid", 0);
            values.put("IsAdmin", 0);

            long newRowId = db.insert(Authtablename, null, values);
            CheckEmailExistsfnflag = 0;
            return false;
        }else{
            while(cursor.moveToNext()) {
//                if(cursor.getString(cursor.getColumnIndexOrThrow("Emailid")) == UserEmailid){
                    ValidUser = cursor.getInt(cursor.getColumnIndexOrThrow("IsValid"));
                    AdminUser = cursor.getInt(cursor.getColumnIndexOrThrow("IsAdmin"));
                    Log.w("rowinvalid" ,Integer.toString(cursor.getInt(cursor.getColumnIndex("IsValid"))));
  //              }
            }
            Log.w("IsFileExists","Stoped"+UserEmailid);
            if(ValidUser == 1){
                Log.w("VAlidUser","true");
                return true;
            }else{
                CheckEmailExistsfnflag = 0;

                return false;
            }

        }

    }

    public boolean getIsValid(){
        if(ValidUser == 1){
            return true;
        }else{
            return false;
        }
    }

    public boolean getIsAdmin(){
        if(AdminUser == 1){
            return true;
        }else{
            return false;
        }
    }

}

