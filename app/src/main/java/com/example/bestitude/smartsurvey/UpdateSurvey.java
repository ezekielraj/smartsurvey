package com.example.bestitude.smartsurvey;

import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateSurvey {

    private static LoggedinActivity liactivity;
    private static CheckAuthHandler cauth;
    private String Entryname;
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
            sformstatus.append("Name Should not be empty");
            i++;
        }else{
            setName(ename);
        }

        RadioGroup sexgroup = (RadioGroup) liactivity.getactivityview(R.id.sexgroup);
        int selectedId = sexgroup.getCheckedRadioButtonId();
        RadioButton radioSexButton = (RadioButton) liactivity.getactivityview(selectedId);

        Toast.makeText(liactivity,
                radioSexButton.getText(), Toast.LENGTH_SHORT).show();


        if(i==1){
            sformstatus.setVisibility(View.INVISIBLE);
            return true;
        }
        return false;
    }

    private void setName(String s){ Entryname = s; }
}
