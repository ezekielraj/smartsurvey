package com.marveric.bestitude.smartsurvey;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class AddSurvey extends AsyncTask<String, String, String> {

    private static ConnectwithAPI cwapi;
    public static LoggedinActivity liactivity;
    private static CheckAuthHandler cauth;
    private static String cookie = "";

    private static String villagevalue;
    private static String subdistrictvalue;
    private static String districtvalue;
    private static String zonevalue;
    private static String pincodevalue;
    private static String populationvalue;
    private static String cbcvalue;
    private static String fbuvalue;
    private static String pbuvalue;
    private static String pofvalue;
    private static String pluofvalue;
    private static String omovalue;
    private static String mofrivervalue;
    private static String moflakevalue;
    private static String mofowvalue;
    private static String mofbwvalue;
    private static String mofdmvalue;
    private static String msdwrivervalue;
    private static String msdwlakevalue;
    private static String msdwowvalue;
    private static String msdwbwvalue;
    private static String anymccvalue;
    private static String ioeivalue;
    private static String anyorivalue;
    private static String surveyenddatevalue;

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

        try{
            Map<String,String> map=new HashMap<String,String>();
            map.put("request","add");
            map.put("villagevalue", villagevalue);
            map.put("subdistrictvalue", subdistrictvalue);
            map.put("districtvalue", districtvalue);
            map.put("zonevalue", zonevalue);
            map.put("pincodevalue", pincodevalue);
            map.put("populationvalue", populationvalue);
            map.put("cbcvalue", cbcvalue);
            map.put("fbuvalue", fbuvalue);
            map.put("pbuvalue", pbuvalue);
            map.put("pofvalue", pofvalue);
            map.put("pluofvalue", pluofvalue);
            map.put("omovalue", omovalue);
            map.put("mofrivervalue", mofrivervalue);
            map.put("moflakevalue", moflakevalue);
            map.put("mofowvalue", mofowvalue);
            map.put("mofbwvalue", mofbwvalue);
            map.put("mofdmvalue", mofdmvalue);
            map.put("msdwrivervalue", msdwrivervalue);
            map.put("msdwlakevalue", msdwlakevalue);
            map.put("msdwowvalue", msdwowvalue);
            map.put("msdwbwvalue", msdwbwvalue);
            map.put("anymccvalue", anymccvalue);
            map.put("ioeivalue", ioeivalue);
            map.put("anyorivalue", anyorivalue);
            map.put("surveyenddatevalue", surveyenddatevalue);

            cwapi.doConnect(map, cauth.getCookiegotten());
            String Response = cwapi.getResponse();
            Log.w("vuadd ", "as"+Response);
            liactivity.saveString("vuadd: "+Response);

            if(Response.equals("true")) {
                return "true";
            }


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
        Log.w("onpostexecute", s);
        liactivity.saveString("onpostexecute"+ s);
        if(s.equals("true")){
            ClearData();
            Toast.makeText(liactivity,
                    "Data Updated Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private void ClearData(){
        ((EditText)liactivity.getactivityview(R.id.village)).setText("");
        ((EditText)liactivity.getactivityview(R.id.subdistrict)).setText("");
        ((EditText)liactivity.getactivityview(R.id.districtname)).setText("");
        ((EditText)liactivity.getactivityview(R.id.zone)).setText("");
        ((EditText)liactivity.getactivityview(R.id.pincode)).setText("");
        ((EditText)liactivity.getactivityview(R.id.population)).setText("");
        ((EditText)liactivity.getactivityview(R.id.cropsbeingcultivated)).setText("");
        ((EditText)liactivity.getactivityview(R.id.fertilizersbeingused)).setText("");
        ((EditText)liactivity.getactivityview(R.id.pesticidesbeingused)).setText("");
        ((RadioGroup) liactivity.getactivityview(R.id.practiceoforganicfarming)).clearCheck();
        ((EditText)liactivity.getactivityview(R.id.percentageoflandunderorganicfarming)).setText("");
        ((EditText)liactivity.getactivityview(R.id.othermajoroccupations)).setText("");
        ((CheckBox) liactivity.getactivityview(R.id.moiriver)).setChecked(false);
        ((CheckBox) liactivity.getactivityview(R.id.moilake)).setChecked(false);
        ((CheckBox) liactivity.getactivityview(R.id.moiopenwell)).setChecked(false);
        ((CheckBox) liactivity.getactivityview(R.id.moiborewell)).setChecked(false);
        ((CheckBox) liactivity.getactivityview(R.id.moidrippingmode)).setChecked(false);
        ((CheckBox) liactivity.getactivityview(R.id.msdwriver)).setChecked(false);
        ((CheckBox) liactivity.getactivityview(R.id.msdwlake)).setChecked(false);
        ((CheckBox) liactivity.getactivityview(R.id.msdwopenwell)).setChecked(false);
        ((CheckBox) liactivity.getactivityview(R.id.msdwborewell)).setChecked(false);
        ((RadioGroup) liactivity.getactivityview(R.id.anymedicalcampconductedinlastfiveyears)).clearCheck();
        ((EditText)liactivity.getactivityview(R.id.industriesofotherenvironmentalissuesnearby)).setText("");
        ((EditText)liactivity.getactivityview(R.id.anyotherrelevantinformation)).setText("");
        ((EditText)liactivity.getactivityview(R.id.surveyenddate)).setText("");

    }

    private Boolean ValidateEntries(){
        int i = 1;

        EditText village = (EditText)liactivity.getactivityview(R.id.village);
        if(village.getText().toString().equals("")){
            village.setError("Village is required!!");
            i++;
        }else{
            village.setError(null);
            villagevalue = village.getText().toString();
        }
        EditText subdistrict = (EditText)liactivity.getactivityview(R.id.subdistrict);
        if(subdistrict.getText().toString().equals("")){
            subdistrict.setError("Please fill this!!");
            i++;
        }else{
            subdistrict.setError(null);
            subdistrictvalue = subdistrict.getText().toString();
        }
        EditText districtname = (EditText)liactivity.getactivityview(R.id.districtname);
        if(districtname.getText().toString().equals("")){
            districtname.setError("Please fill this!!");
            i++;
        }else{
            districtname.setError(null);
            districtvalue = districtname.getText().toString();
        }
        EditText zone = (EditText)liactivity.getactivityview(R.id.zone);
        if(zone.getText().toString().equals("")){
            zone.setError("Please fill this!!");
            i++;
        }else{
            zone.setError(null);
            zonevalue = zone.getText().toString();
        }
        EditText pincode = (EditText)liactivity.getactivityview(R.id.pincode);
        if(pincode.getText().toString().equals("")){
            pincode.setError("Please fill this!!");
            i++;
        }else{
            pincode.setError(null);
            pincodevalue = pincode.getText().toString();
        }
        EditText population = (EditText)liactivity.getactivityview(R.id.population);
        if(population.getText().toString().equals("")){
            population.setError("Please fill this!!");
            i++;
        }else{
            population.setError(null);
            populationvalue = population.getText().toString();
        }
        EditText cropsbeingcultivated = (EditText)liactivity.getactivityview(R.id.cropsbeingcultivated);
        if(cropsbeingcultivated.getText().toString().equals("")){
            cropsbeingcultivated.setError("Please fill this!!");
            i++;
        }else{
            cropsbeingcultivated.setError(null);
            cbcvalue = cropsbeingcultivated.getText().toString();
        }
        EditText fertilizersbeingused = (EditText)liactivity.getactivityview(R.id.fertilizersbeingused);
        if(fertilizersbeingused.getText().toString().equals("")){
            fertilizersbeingused.setError("Please fill this!!");
            i++;
        }else{
            fertilizersbeingused.setError(null);
            fbuvalue = fertilizersbeingused.getText().toString();
        }
        EditText pesticidesbeingused = (EditText)liactivity.getactivityview(R.id.pesticidesbeingused);
        if(pesticidesbeingused.getText().toString().equals("")){
            pesticidesbeingused.setError("Please fill this!!");
            i++;
        }else{
            pesticidesbeingused.setError(null);
            pbuvalue = pesticidesbeingused.getText().toString();
        }

        RadioGroup practiceoforganicfarming = (RadioGroup) liactivity.getactivityview(R.id.practiceoforganicfarming);
        int selectedId = practiceoforganicfarming.getCheckedRadioButtonId();
        RadioButton pofbutton = (RadioButton) liactivity.getactivityview(selectedId);
        TextView et = (TextView) liactivity.getactivityview(R.id.poferromsg);
        if(pofbutton == null){

            et.setText("Please Select Practice of Organic farming");
            i++;
        }else{
            et.setText(null);
            pofvalue = pofbutton.getText().toString();
        }

        EditText percentageoflandunderorganicfarming = (EditText)liactivity.getactivityview(R.id.percentageoflandunderorganicfarming);
        if(percentageoflandunderorganicfarming.getText().toString().equals("")){
            percentageoflandunderorganicfarming.setError("Please fill this!!");
            i++;
        }else{
            percentageoflandunderorganicfarming.setError(null);
            pluofvalue = percentageoflandunderorganicfarming.getText().toString();
        }

        EditText othermajoroccupations = (EditText)liactivity.getactivityview(R.id.othermajoroccupations);
        if(othermajoroccupations.getText().toString().equals("")){
            othermajoroccupations.setError("Please fill this!!");
            i++;
        }else{
            othermajoroccupations.setError(null);
            omovalue = othermajoroccupations.getText().toString();
        }

        int j = 0;
        mofrivervalue = Boolean.toString(((CheckBox) liactivity.getactivityview(R.id.moiriver)).isChecked());
        if(mofrivervalue.equals("true")){
            j++;
        }
        moflakevalue = Boolean.toString(((CheckBox) liactivity.getactivityview(R.id.moilake)).isChecked());
        if(moflakevalue.equals("true")){ j++; }
        mofowvalue = Boolean.toString(((CheckBox) liactivity.getactivityview(R.id.moiopenwell)).isChecked());
        if(mofowvalue.equals("true")){ j++; }
        mofbwvalue = Boolean.toString(((CheckBox) liactivity.getactivityview(R.id.moiborewell)).isChecked());
        if(mofbwvalue.equals("true")){ j++; }
        mofdmvalue = Boolean.toString(((CheckBox) liactivity.getactivityview(R.id.moidrippingmode)).isChecked());
        if(mofdmvalue.equals("true")){ j++; }
        TextView moiem = (TextView) liactivity.getactivityview(R.id.moierrormsg);
        if(j == 0){
            moiem.setText("Please Select atleast one above");
            i++;
        }else{
            moiem.setText(null);
        }

        j = 0;
        msdwrivervalue = Boolean.toString(((CheckBox) liactivity.getactivityview(R.id.msdwriver)).isChecked());
        if(msdwrivervalue.equals("true")){ j++; }
        msdwlakevalue = Boolean.toString(((CheckBox) liactivity.getactivityview(R.id.msdwlake)).isChecked());
        if(msdwlakevalue.equals("true")){ j++; }
        msdwowvalue = Boolean.toString(((CheckBox) liactivity.getactivityview(R.id.msdwopenwell)).isChecked());
        if(msdwowvalue.equals("true")){ j++; }
        msdwbwvalue = Boolean.toString(((CheckBox) liactivity.getactivityview(R.id.msdwborewell)).isChecked());
        if(msdwbwvalue.equals("true")){ j++; }
        TextView msdwem = (TextView) liactivity.getactivityview(R.id.msdwerrormsg);
        if(j == 0){
            msdwem.setText("Please Select atleast one above");
            i++;
        }else{
            msdwem.setText(null);
        }



        RadioGroup anymedicalcampconductedinlastfiveyears = (RadioGroup) liactivity.getactivityview(R.id.anymedicalcampconductedinlastfiveyears);
        int selectedId1 = anymedicalcampconductedinlastfiveyears.getCheckedRadioButtonId();
        RadioButton mccbutton = (RadioButton) liactivity.getactivityview(selectedId1);
        TextView etmcc = (TextView) liactivity.getactivityview(R.id.mccerrormsg);
        if(mccbutton == null){
            etmcc.setText("Please select above");
            i++;
        }else{
            etmcc.setText(null);
            anymccvalue = mccbutton.getText().toString();
        }




        EditText industriesofotherenvironmentalissuesnearby = (EditText)liactivity.getactivityview(R.id.industriesofotherenvironmentalissuesnearby);
        if(industriesofotherenvironmentalissuesnearby.getText().toString().equals("")){
            industriesofotherenvironmentalissuesnearby.setError("Please fill this!!");
            i++;
        }else{
            industriesofotherenvironmentalissuesnearby.setError(null);
            ioeivalue = industriesofotherenvironmentalissuesnearby.getText().toString();
        }

        EditText anyotherrelevantinformation = (EditText)liactivity.getactivityview(R.id.anyotherrelevantinformation);
        if(anyotherrelevantinformation.getText().toString().equals("")){
//            anyotherrelevantinformation.setError(" is required!!");
  //          i++;
            anyorivalue = "";
        }else{
    //        anyotherrelevantinformation.setError(null);
            anyorivalue = anyotherrelevantinformation.getText().toString();
        }
        EditText surveyenddate = (EditText)liactivity.getactivityview(R.id.surveyenddate);
        if(surveyenddate.getText().toString().equals("")){
            surveyenddate.setError("Survey end date is required!!");
            i++;
        }else{
            surveyenddate.setError(null);
            surveyenddatevalue = surveyenddate.getText().toString();
        }

        if(i == 1){
            return true;
        }else{
            return false;
        }
    }

    private void CreateSurvey(){
        new AddSurvey().execute();
    }

    public void configListener(){
        Button bt = (Button) liactivity.getactivityview(R.id.submitsurvey);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ValidateEntries()){
                    CreateSurvey();
                }
            }
        });

    }

}
