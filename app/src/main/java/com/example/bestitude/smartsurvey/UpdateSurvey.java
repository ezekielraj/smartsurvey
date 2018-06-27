package com.example.bestitude.smartsurvey;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateSurvey {

    private static LoggedinActivity liactivity;
    private static CheckAuthHandler cauth;
    private String Entryname;
    private String Entrysex;
    private String Entryage;
    private String Entryagegroup;
    private Boolean Entryhabitalcohol;
    private Boolean Entryhabitsmoking;
    private Boolean Entryhabittobacco;
    private Boolean Entryhabitnil;
    private String Entryformingchoice;
    private Boolean pa;
    private Boolean mahp;
    private Boolean wippf;
    private Boolean wis;
    private Boolean udiah;
    private String dv;
    private String ht;

    UpdateSurvey(){ }
    UpdateSurvey(LoggedinActivity lia){
        liactivity = lia;
        cauth =  new CheckAuthHandler();
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
        }else{
            setName(ename);
        }
//i++;

  //          sformstatus.append(Boolean.toString(ename.matches("[A-Za-z.][^.]*")));

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
            setAge(eage);
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
            sformstatus.append("Please update Other Diseases\n");
  //          i++;
        }else{
            setAge(odiseases);
        }
        sformstatus.append(odiseases);

        //   Toast.makeText(liactivity,
       //         radioSexButton.getText(), Toast.LENGTH_SHORT).show();


        if(i==1){
            sformstatus.setVisibility(View.INVISIBLE);
            return true;
        }
        return false;
    }
    public Boolean ValidateTextData(){
        return true;
    }
    private void setName(String s){ Entryname = s; }
    private void setSexValue(String s){ Entrysex = s; }
    private void setAge(String s){ Entryage = s; }
    private void setAgeValue(String s){ Entryagegroup = s; }
    private void setHabitAlcohol(Boolean s){ Entryhabitalcohol = s; }
    private void setHabitSmoking(Boolean s){ Entryhabitsmoking = s; }
    private void setHabitTobacco(Boolean s){ Entryhabittobacco = s; }
    private void setHabitNil(Boolean s){ Entryhabitnil = s; }
    private void setFormingValue(String s){ Entryformingchoice = s; }
    private void setPA(Boolean s){ pa = s; }
    private void setMAHP(Boolean s){ mahp = s; }
    private void setWIPPF(Boolean s){ wippf = s; }
    private void setWIS(Boolean s){ wis = s; }
    private void setUDIAH(Boolean s){ udiah = s; }
    private void setDiabetesValue(String s){ dv = s; }
    private void setHyperTension(String s){ ht = s; }
}
