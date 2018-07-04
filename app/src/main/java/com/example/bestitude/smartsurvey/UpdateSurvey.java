package com.example.bestitude.smartsurvey;

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

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class UpdateSurvey extends AsyncTask<String, String, String> {

    private static LoggedinActivity liactivity;
    private static CheckAuthHandler cauth;
    private static String Entryname;
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
    private static String od;
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
            map.put("name", Entryname);
                    map.put("sex", Entrysex);
                    map.put("age", Entryage);
                    map.put("agegroup", Entryagegroup);
                    map.put("alcohol", Boolean.toString(Entryhabitalcohol));
                    map.put("smooking", Boolean.toString(Entryhabitsmoking));
                    map.put("tobaccochewing", Boolean.toString(Entryhabittobacco));
                    map.put("farming", Entryformingchoice);
                    map.put("pesticideapplicator", Boolean.toString(pa));
                    map.put("mixingandhandlinofpesticide", Boolean.toString(mahp));
                    map.put("workingpesticidesprayedfield", Boolean.toString(wippf));
                    map.put("workinginpesticideshop", Boolean.toString(wis));
                    map.put("useofinsectrepellentsathome", Boolean.toString(udiah));
                    map.put("useofreverseosmosiswaterfordrinking", Boolean.toString(urowd));
                    map.put("diabetes", dv);
                    map.put("hypertension", ht);
                    map.put("otherdiseases", od);
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
        EditText entryname = (EditText) liactivity.getactivityview(R.id.entryname);
        entryname.setText("");
        RadioGroup sexgroup = (RadioGroup) liactivity.getactivityview(R.id.sexgroup);
        sexgroup.clearCheck();
        EditText entryage = (EditText) liactivity.getactivityview(R.id.entryage);
        entryage.setText("");
        RadioGroup agegroup = (RadioGroup) liactivity.getactivityview(R.id.agegroup);
        agegroup.clearCheck();
        ((CheckBox) liactivity.getactivityview(R.id.habitalcohol)).setChecked(false);
        ((CheckBox) liactivity.getactivityview(R.id.habitsmoking)).setChecked(false);
        ((CheckBox) liactivity.getactivityview(R.id.habittobacco)).setChecked(false);
        ((CheckBox) liactivity.getactivityview(R.id.habitnil)).setChecked(false);
        RadioGroup formingchoice = (RadioGroup) liactivity.getactivityview(R.id.farmingchoice);
        formingchoice.clearCheck();
        ((CheckBox) liactivity.getactivityview(R.id.pesticideapplicator)).setChecked(false);
        ((CheckBox) liactivity.getactivityview(R.id.mixesandhandlespesticides)).setChecked(false);
        ((CheckBox) liactivity.getactivityview(R.id.worksinpesticidesprayedfiled)).setChecked(false);
        ((CheckBox) liactivity.getactivityview(R.id.worksinpesticideshop)).setChecked(false);
        ((CheckBox) liactivity.getactivityview(R.id.usesdomesticinsecticidesathome)).setChecked(false);
        ((CheckBox) liactivity.getactivityview(R.id.usesreverseosmosiswaterfordrinking)).setChecked(false);
        RadioGroup diabeteschoice = (RadioGroup) liactivity.getactivityview(R.id.diabeteschoice);
        diabeteschoice.clearCheck();
        RadioGroup hypertensionchoice = (RadioGroup) liactivity.getactivityview(R.id.hypertensionchoice);
        hypertensionchoice.clearCheck();
        EditText otherdiseases = (EditText) liactivity.getactivityview(R.id.otherdiseases);
        otherdiseases.setText("");
    }


    public Boolean sendData(String s, String TableName){

        String data[] = s.split("-");
        if(liactivity.isOnline()) {
            new UpdateSurvey().execute("insertdata", cauth.getUserEmailid(), data[0]);
        }else {
            Log.w("Tablename", TableName);
            ContentValues values = new ContentValues();
            values.put("name", Entryname);
            values.put("sex", Entrysex);
            values.put("age", Entryage);
            values.put("agegroup", Entryagegroup);
            values.put("alcohol", Boolean.toString(Entryhabitalcohol));
            values.put("smooking", Boolean.toString(Entryhabitsmoking));
            values.put("tobacco_chewing", Boolean.toString(Entryhabittobacco));
            values.put("farming", Entryformingchoice);
            values.put("pesticide_applicator", Boolean.toString(pa));
            values.put("mixing_and_handlin_of_pesticide", Boolean.toString(mahp));
            values.put("working_pesticide_sprayed_field", Boolean.toString(wippf));
            values.put("working_in_pesticide_shop", Boolean.toString(wis));
            values.put("use_of_insect_repellents_at_home", Boolean.toString(udiah));
            values.put("use_of_reverse_osmosis_water_for_drinking", Boolean.toString(urowd));
            values.put("diabetes", dv);
            values.put("hypertension", ht);
            values.put("other_diseases", od);
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
        TextView sformstatus = (TextView) liactivity.getactivityview(R.id.sformstatus);
        sformstatus.setVisibility(View.VISIBLE);
        sformstatus.setText("Status:");
        if(ename.equals("")){
            sformstatus.append("Name Should not be empty\n");
            i++;
        }else {
            if (ename.matches("[A-Za-z][^.]*")) {
                setName(ename);
            }else{
                sformstatus.append("Name can only contain a-z A-Z and space ");
                i++;
            }
        }
//i++;

  //          sformstatus.append();

        RadioGroup sexgroup = (RadioGroup) liactivity.getactivityview(R.id.sexgroup);
        int selectedId = sexgroup.getCheckedRadioButtonId();
        RadioButton radioSexButton = (RadioButton) liactivity.getactivityview(selectedId);
        if(radioSexButton == null){
            sformstatus.append("Please choose sex\n");
            i++;
        }else{
            String sexbutton = radioSexButton.getText().toString();
            setSexValue(sexbutton);
        }

        EditText entryage = (EditText) liactivity.getactivityview(R.id.entryage);
        String eage = entryage.getText().toString();
        if(eage.equals("")){
            sformstatus.append("Please update age\n");
            i++;
        }else{
            if (eage.matches("[0-9]+")) {
                setAge(eage);
            }else{
                sformstatus.append("Age should only contain numbers");
                i++;
            }
        }


        RadioGroup agegroup = (RadioGroup) liactivity.getactivityview(R.id.agegroup);
        int selectedId1 = agegroup.getCheckedRadioButtonId();
        RadioButton radioAgeButton = (RadioButton) liactivity.getactivityview(selectedId1);
        if(radioAgeButton == null){
            sformstatus.append("Please choose age\n");
            i++;
        }else{
            String agebutton = radioAgeButton.getText().toString();
            setAgeValue(agebutton);
        }

        setHabitAlcohol(((CheckBox) liactivity.getactivityview(R.id.habitalcohol)).isChecked());
        setHabitSmoking(((CheckBox) liactivity.getactivityview(R.id.habitsmoking)).isChecked());
        setHabitTobacco(((CheckBox) liactivity.getactivityview(R.id.habittobacco)).isChecked());
        setHabitNil(((CheckBox) liactivity.getactivityview(R.id.habitnil)).isChecked());


        RadioGroup formingchoice = (RadioGroup) liactivity.getactivityview(R.id.farmingchoice);
        int selectedId2 = formingchoice.getCheckedRadioButtonId();
        RadioButton radioFormingChoice = (RadioButton) liactivity.getactivityview(selectedId2);

        if(radioFormingChoice == null ){
            sformstatus.append("Please choose Forming choice\n");
            i++;
        }else{
            String formingchoicevalue = radioFormingChoice.getText().toString();
            setFormingValue(formingchoicevalue);
        }

        setPA(((CheckBox) liactivity.getactivityview(R.id.pesticideapplicator)).isChecked());
        setMAHP(((CheckBox) liactivity.getactivityview(R.id.mixesandhandlespesticides)).isChecked());
        setWIPPF(((CheckBox) liactivity.getactivityview(R.id.worksinpesticidesprayedfiled)).isChecked());
        setWIS(((CheckBox) liactivity.getactivityview(R.id.worksinpesticideshop)).isChecked());
        setUDIAH(((CheckBox) liactivity.getactivityview(R.id.usesdomesticinsecticidesathome)).isChecked());
        setUROWD(((CheckBox) liactivity.getactivityview(R.id.usesreverseosmosiswaterfordrinking)).isChecked());

        RadioGroup diabeteschoice = (RadioGroup) liactivity.getactivityview(R.id.diabeteschoice);
        int selectedId3 = diabeteschoice.getCheckedRadioButtonId();
        RadioButton radioDiabetesChoice = (RadioButton) liactivity.getactivityview(selectedId3);

        if(radioDiabetesChoice == null){
            sformstatus.append("Please choose Diabetes\n");
            i++;
        }else{
            String diabeteschoicevalue = radioDiabetesChoice.getText().toString();
            setDiabetesValue(diabeteschoicevalue);
        }

        RadioGroup hypertensionchoice = (RadioGroup) liactivity.getactivityview(R.id.hypertensionchoice);
        int selectedId4 = hypertensionchoice.getCheckedRadioButtonId();
        RadioButton radioHTChoice = (RadioButton) liactivity.getactivityview(selectedId4);
        if(radioHTChoice == null){
  //          sformstatus.append("Please choose HyperTension\n");
        }else{
            String htchoicevalue = radioHTChoice.getText().toString();
            setHyperTension(htchoicevalue);
        }

        EditText otherdiseases = (EditText) liactivity.getactivityview(R.id.otherdiseases);
        String odiseases = otherdiseases.getText().toString();
        if(odiseases.equals("")){
    //        sformstatus.append("Please update Other Diseases\n");
  //          i++;
        }else{
            Base64.Encoder encoder = Base64.getEncoder();
             setOtherDiseases(encoder.encodeToString(odiseases.getBytes()));
        }

        //   Toast.makeText(liactivity,
       //         radioSexButton.getText(), Toast.LENGTH_SHORT).show();


        if(i==1){
            sformstatus.setVisibility(View.INVISIBLE);
            return true;
        }
        return false;
    }
    private static void setName(String s){ Entryname = s; }
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
}
