package com.marveric.bestitude.smartsurvey;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import android.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class UpdateSurvey extends AsyncTask<String, String, String> {

    private static LoggedinActivity liactivity;
    private static CheckAuthHandler cauth;

    /*private static String Entryname;
    private static String Entrysex;
    private static String Entryage;
    private static String Entryagegroup;
    private static Boolean Entryhabitalcohol = false;
    private static Boolean Entryhabitsmoking = false;
    private static Boolean Entryhabittobacco = false;
    private static Boolean Entryhabitnil = false;
    private static String Entryformingchoice;
    private static Boolean pa = false;
    private static Boolean mahp = false;
    private static Boolean wippf = false;
    private static Boolean wis = false;
    private static Boolean udiah = false;
    private static Boolean urowd = false;
    private static String dv;
    private static String ht;
    private static String od;*/

    private static String entrynamevalue;
    private static String sexgroupvalue;
    private static String entryagevalue;
    private static String bodyphysiquevalue;
    private static String habitalcoholvalue;
    private static String habitsmokingvalue;
    private static String habittobaccovalue;
    private static String occupationvalue;
    private static String pesticideapplicatorvalue;
    private static String mixesandhandlespesticidesvalue;
    private static String worksinpesticidesprayedfiledvalue;
    private static String worksinpesticideshopvalue;
    private static String usesdomesticinsecticidesathomevalue;
    private static String nodirectexposurevalue;
    private static String usesreverseosmosiswaterfordrinkingvalue;
    private static String diabeteschoicevalue;
    private static String hypertensionchoicevalue;
    private static String otherdiseasesvalue;


    private static ConnectwithAPI cwapi;
    private static SQLiteDatabase ldber;

    UpdateSurvey(){ }
    UpdateSurvey(LoggedinActivity lia, SQLiteDatabase ldb){
        liactivity = lia;
        ldber = ldb;
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
        String edata[] = emailId.replaceAll("\\."  ,  "_").split("@");
        String surveyid = arg[2];

        try{
            Map<String,String> map=new HashMap<String,String>();
            map.put("request", request);
            map.put("emailid", edata[0] + "_" + edata[1]);
            map.put("surveyid", surveyid);
                    map.put("name", entrynamevalue);
                    map.put("sex", sexgroupvalue);
                    map.put("age", entryagevalue);
                    map.put("bodyphysique",bodyphysiquevalue);
                    map.put("alcohol", habitalcoholvalue);
                    map.put("smooking", habitsmokingvalue);
                    map.put("tobaccochewing", habittobaccovalue);
                    map.put("occupation", occupationvalue);
                    map.put("pesticideapplicator", pesticideapplicatorvalue);
                    map.put("mixingandhandlinofpesticide", mixesandhandlespesticidesvalue);
                    map.put("workingpesticidesprayedfield", worksinpesticidesprayedfiledvalue);
                    map.put("workinginpesticideshop", worksinpesticideshopvalue);
                    map.put("useofinsectrepellentsathome", usesdomesticinsecticidesathomevalue);
                    map.put("nodirectexposure", nodirectexposurevalue);
                    map.put("useofreverseosmosiswaterfordrinking", usesreverseosmosiswaterfordrinkingvalue);
                    map.put("diabetes", diabeteschoicevalue);
                    map.put("hypertension", hypertensionchoicevalue);
                    map.put("otherdiseases", otherdiseasesvalue);
		map.put("username","admin");
                map.put("password","angelEAR2");

            cwapi.doConnect(map, cauth.getCookiegotten());
            String Response = cwapi.getResponse();
            Log.w("vs fas ts updatesurvey", "as"+Response);

            return "Updated";
        }catch(Exception e){
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
        if(s == "Updated") {
            ClearData();
            Toast.makeText(liactivity,
                    "Data Updated Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    public void ClearData(){
        ((EditText) liactivity.getactivityview(R.id.entryname)).setText("");
        ((RadioGroup) liactivity.getactivityview(R.id.sexgroup)).clearCheck();
        ((EditText) liactivity.getactivityview(R.id.entryage)).setText("");
        ((RadioGroup) liactivity.getactivityview(R.id.bodyphysique)).clearCheck();

        ((RadioGroup) liactivity.getactivityview(R.id.habitalcohol)).clearCheck();
        ((RadioGroup) liactivity.getactivityview(R.id.habitsmoking)).clearCheck();
        ((RadioGroup) liactivity.getactivityview(R.id.habittobacco)).clearCheck();
        ((RadioGroup) liactivity.getactivityview(R.id.occupation)).clearCheck();
        ((RadioGroup) liactivity.getactivityview(R.id.occupation)).clearCheck();

        ((CheckBox) liactivity.getactivityview(R.id.pesticideapplicator)).setChecked(false);
        ((CheckBox) liactivity.getactivityview(R.id.mixesandhandlespesticides)).setChecked(false);
        ((CheckBox) liactivity.getactivityview(R.id.worksinpesticidesprayedfiled)).setChecked(false);
        ((CheckBox) liactivity.getactivityview(R.id.worksinpesticideshop)).setChecked(false);
        ((CheckBox) liactivity.getactivityview(R.id.usesdomesticinsecticidesathome)).setChecked(false);
        ((CheckBox) liactivity.getactivityview(R.id.nodirectexposure)).setChecked(false);

        ((RadioGroup) liactivity.getactivityview(R.id.usesreverseosmosiswaterfordrinking)).clearCheck();
        ((RadioGroup) liactivity.getactivityview(R.id.diabeteschoice)).clearCheck();
        ((RadioGroup) liactivity.getactivityview(R.id.hypertensionchoice)).clearCheck();

        ((EditText) liactivity.getactivityview(R.id.otherdiseases)).setText("");

    }


    public Boolean sendData(String s, String TableName){

        String data[] = s.split("-");
        if(liactivity.isOnline()) {
            new UpdateSurvey().execute("insertdata", cauth.getUserEmailid(), data[0]);
        }else {
            Log.w("Tablename", TableName);
            ContentValues values = new ContentValues();
            values.put("name", entrynamevalue);
            values.put("sex", sexgroupvalue);
            values.put("age", entryagevalue);
            values.put("bodyphysique", bodyphysiquevalue);
            values.put("alcohol", habitalcoholvalue);
            values.put("smooking", habitsmokingvalue);
            values.put("tobacco_chewing", habittobaccovalue);
            values.put("occupation", occupationvalue);
            values.put("pesticide_applicator", pesticideapplicatorvalue);
            values.put("mixing_and_handlin_of_pesticide", mixesandhandlespesticidesvalue);
            values.put("working_pesticide_sprayed_field", worksinpesticidesprayedfiledvalue);
            values.put("working_in_pesticide_shop", worksinpesticideshopvalue);
            values.put("use_of_insect_repellents_at_home", usesdomesticinsecticidesathomevalue);
            values.put("no_direct_exposure", nodirectexposurevalue);
            values.put("use_of_reverse_osmosis_water_for_drinking", usesreverseosmosiswaterfordrinkingvalue);
            values.put("diabetes", diabeteschoicevalue);
            values.put("hypertension", hypertensionchoicevalue);
            values.put("other_diseases", otherdiseasesvalue);
            if (ldber != null) {
                ldber.insert(TableName, null, values);

                ClearData();
                Toast.makeText(liactivity,
                        "Data Saved Locally! please Sync online Later", Toast.LENGTH_SHORT).show();

                Cursor cursor = ldber.query(
                        TableName,   // The table to query
                        null,             // The array of columns to return (pass null to get all)
                        null,              // The columns for the WHERE clause
                        null,          // The values for the WHERE clause
                        null,                   // don't group the rows
                        null,                   // don't filter by row groups
                        null               // The sort order
                );
//            cursor.getCount();
                Log.w("rowCount", Integer.toString(cursor.getCount()));

            }else{
                Toast.makeText(liactivity,
                        "Some Issue with DB!! Please Logout and login with online mode", Toast.LENGTH_SHORT).show();

            }
        }
        return true;
    }

    public Boolean ValidateData(){
        int i = 1;
        EditText entryname = (EditText) liactivity.getactivityview(R.id.entryname);
        String ename = entryname.getText().toString();
        if(ename.equals("")){
            entryname.setError("Name Should not be empty");
            i++;
        }else {
            if (ename.matches("[A-Za-z][^.]*")) {
                entrynamevalue = ename;
                entryname.setError(null);
            }else{
                entryname.setError("Name can only contain a-z A-Z and space ");
                i++;
            }
        }
//i++;

  //          sformstatus.append();

        RadioGroup sexgroup = (RadioGroup) liactivity.getactivityview(R.id.sexgroup);
        int selectedId = sexgroup.getCheckedRadioButtonId();
        RadioButton radioSexButton = (RadioButton) liactivity.getactivityview(selectedId);
        TextView sgem = (TextView) liactivity.getactivityview(R.id.sexgrouperrmsg);

        if(radioSexButton == null){
            sgem.setText("Please choose sex");
            i++;
        }else{
            sexgroupvalue = radioSexButton.getText().toString();
            sgem.setText("");
        }

        EditText entryage = (EditText) liactivity.getactivityview(R.id.entryage);
        String eage = entryage.getText().toString();
        if(eage.equals("")){
            entryage.setError("Please update age");
            i++;
        }else{
            if (eage.matches("[0-9]+")) {
                if((Integer.parseInt(eage) >= 18) && (Integer.parseInt(eage) <= 120)) {
                    entryagevalue = eage;
                    entryage.setError(null);
                }else{
                    entryage.setError("Age should be between 18 to 120");
                    i++;
                }
            }else{
                entryage.setError("Age should only contain numbers");
                i++;
            }
        }


        RadioGroup bpgroup = (RadioGroup) liactivity.getactivityview(R.id.bodyphysique);
        int selectedId1 = bpgroup.getCheckedRadioButtonId();
        RadioButton radiobpButton = (RadioButton) liactivity.getactivityview(selectedId1);
        TextView bpem = (TextView) liactivity.getactivityview(R.id.bperrmsg);
        if(radiobpButton == null){
            bpem.setText("Please choose body physique");

            i++;
        }else{
            bodyphysiquevalue = radiobpButton.getText().toString();
            bpem.setText("");
        }

        RadioGroup hagroup = (RadioGroup) liactivity.getactivityview(R.id.habitalcohol);
        int selectedId2 = hagroup.getCheckedRadioButtonId();
        RadioButton radiohaButton = (RadioButton) liactivity.getactivityview(selectedId2);
        TextView haem = (TextView) liactivity.getactivityview(R.id.haerrmsg);
        if(radiohaButton == null){
            haem.setText("Please select above habit");
            i++;
        }else{
            habitalcoholvalue = radiohaButton.getText().toString();
            haem.setText("");
        }
        RadioGroup hsgroup = (RadioGroup) liactivity.getactivityview(R.id.habitsmoking);
        int selectedId3 = hsgroup.getCheckedRadioButtonId();
        RadioButton radiohsButton = (RadioButton) liactivity.getactivityview(selectedId3);
        TextView hsem = (TextView) liactivity.getactivityview(R.id.hserrmsg);
        if(radiohsButton == null){
            hsem.setText("Please select above habit");
            i++;
        }else{
            habitsmokingvalue = radiohsButton.getText().toString();
            hsem.setText("");
        }

        RadioGroup htgroup = (RadioGroup) liactivity.getactivityview(R.id.habittobacco);
        selectedId1 = htgroup.getCheckedRadioButtonId();
        RadioButton radiohtButton = (RadioButton) liactivity.getactivityview(selectedId1);
        TextView htem = (TextView) liactivity.getactivityview(R.id.hterrmsg);
        if(radiohtButton == null){
            htem.setText("Please select above habit");
            i++;
        }else{
            habittobaccovalue = radiohtButton.getText().toString();
            htem.setText("");
        }

        RadioGroup occgroup = (RadioGroup) liactivity.getactivityview(R.id.occupation);
        selectedId1 = occgroup.getCheckedRadioButtonId();
        RadioButton radiooccButton = (RadioButton) liactivity.getactivityview(selectedId1);
        TextView occem = (TextView) liactivity.getactivityview(R.id.occerrmsg);
        if(radiooccButton == null){
            occem.setText("Please select above habit");
            i++;
        }else{
            occupationvalue = radiooccButton.getText().toString();
            occem.setText("");
        }

        int j = 0;
        pesticideapplicatorvalue = Boolean.toString(((CheckBox) liactivity.getactivityview(R.id.pesticideapplicator)).isChecked());
        if(pesticideapplicatorvalue.equals("true")){ j++; }
        mixesandhandlespesticidesvalue = Boolean.toString(((CheckBox) liactivity.getactivityview(R.id.mixesandhandlespesticides)).isChecked());
        if(mixesandhandlespesticidesvalue.equals("true")){ j++; }
        worksinpesticidesprayedfiledvalue = Boolean.toString(((CheckBox) liactivity.getactivityview(R.id.worksinpesticidesprayedfiled)).isChecked());
        if(worksinpesticidesprayedfiledvalue.equals("true")){ j++; }
        worksinpesticideshopvalue = Boolean.toString(((CheckBox) liactivity.getactivityview(R.id.worksinpesticideshop)).isChecked());
        if(worksinpesticideshopvalue.equals("true")){ j++; }
        usesdomesticinsecticidesathomevalue = Boolean.toString(((CheckBox) liactivity.getactivityview(R.id.usesdomesticinsecticidesathome)).isChecked());
        if(usesdomesticinsecticidesathomevalue.equals("true")){ j++; }
        nodirectexposurevalue = Boolean.toString(((CheckBox) liactivity.getactivityview(R.id.nodirectexposure)).isChecked());
        if(nodirectexposurevalue.equals("true")){ j++; }
        //setUROWD(((CheckBox) liactivity.getactivityview(R.id.usesreverseosmosiswaterfordrinking)).isChecked());
        TextView mpem = (TextView) liactivity.getactivityview(R.id.eperrmsg);
        if(j==0){
            i++;

            mpem.setText("select atleast one option above");
        }else{
            mpem.setText("");
        }

        RadioGroup urowdgrp = (RadioGroup) liactivity.getactivityview(R.id.usesreverseosmosiswaterfordrinking);
        selectedId1 = urowdgrp.getCheckedRadioButtonId();
        RadioButton radiourowdButton = (RadioButton) liactivity.getactivityview(selectedId1);
        TextView urowdem = (TextView) liactivity.getactivityview(R.id.urowderrmsg);
        if(radiourowdButton == null){
            urowdem.setText("Please select above");
            i++;
        }else{
            usesreverseosmosiswaterfordrinkingvalue = radiourowdButton.getText().toString();
            urowdem.setText("");
        }


        RadioGroup diabeteschoice = (RadioGroup) liactivity.getactivityview(R.id.diabeteschoice);
        int selectedId5 = diabeteschoice.getCheckedRadioButtonId();
        RadioButton radioDiabetesChoice = (RadioButton) liactivity.getactivityview(selectedId5);
        TextView diaerr = (TextView) liactivity.getactivityview(R.id.diaerrmsg);
        if(radioDiabetesChoice == null){
            diaerr.setText("Please choose Diabetes");
            i++;
        }else{
            diabeteschoicevalue = radioDiabetesChoice.getText().toString();
            diaerr.setText("");
        }

        RadioGroup hypertensionchoice = (RadioGroup) liactivity.getactivityview(R.id.hypertensionchoice);
        int selectedId4 = hypertensionchoice.getCheckedRadioButtonId();
        RadioButton radioHTChoice = (RadioButton) liactivity.getactivityview(selectedId4);
        TextView htenerr = (TextView) liactivity.getactivityview(R.id.htenerrmsg);
        if(radioHTChoice == null){
            htenerr.setText("Please choose Hyper Tension");
            i++;
        }else{
            hypertensionchoicevalue = radioHTChoice.getText().toString();
            htenerr.setText("");
        }

        EditText otherdiseases = (EditText) liactivity.getactivityview(R.id.otherdiseases);
        String odiseases = otherdiseases.getText().toString();
        if(odiseases.equals("")){
    //        sformstatus.append("Please update Other Diseases\n");
  //          i++;
        }else{
//            Base64.Encoder encoder = Base64.getEncoder();
             otherdiseasesvalue = Base64.encodeToString(odiseases.getBytes(),Base64.DEFAULT);//encoder.encodeToString(odiseases.getBytes()));
        }

        //   Toast.makeText(liactivity,
       //         radioSexButton.getText(), Toast.LENGTH_SHORT).show();


        if(i==1){
            return true;
        }
        return false;
    }
/*    private static void setName(String s){ Entryname = s; }
    private static void setSexValue(String s){ Entrysex = s; }
    private static void setAge(String s){ Entryage = s; }
    private static void setAgeValue(String s){ Entryagegroup = s; }
    private static void setHabitAlcohol(Boolean s){ Entryhabitalcohol = s; }
    private static void setHabitSmoking(Boolean s){ Entryhabitsmoking = s; }
    private static void setHabitTobacco(Boolean s){ Entryhabittobacco = s; }
    private static void setHabitNil(Boolean s){ Entryhabitnil = s; }
    private static void setFormingValue(String s){ Entryformingchoice = s; }
    private static void setPA(Boolean s){ pa = s; }
    private static void setMAHP(Boolean s){ mahp = s; }
    private static void setWIPPF(Boolean s){ wippf = s; }
    private static void setWIS(Boolean s){ wis = s; }
    private static void setUDIAH(Boolean s){ udiah = s; }
    private static void setUROWD(Boolean s){ urowd = s; }
    private static void setDiabetesValue(String s){ dv = s; }
    private static void setHyperTension(String s){ ht = s; }
    private static void setOtherDiseases(String s){ od = s; }
*/
}
