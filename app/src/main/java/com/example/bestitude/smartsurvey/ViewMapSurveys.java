package com.example.bestitude.smartsurvey;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;

public class ViewMapSurveys {

    public static LoggedinActivity liactivity;
    ViewMapSurveys(){ }
    ViewMapSurveys(LoggedinActivity lia){
        liactivity = lia;
      //  cwapi = new ConnectwithAPI("http://www.tutorialspole.com/smartsurvey/authverify.php","POST");
    }


    public void updateFloatingbutton(){
        FloatingActionButton fab = (FloatingActionButton) liactivity.getactivityview(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "clicked from view map surveys", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
