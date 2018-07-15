package com.marveric.bestitude.smartsurvey;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private GoogleSignInHandler gsin;
    private CheckAuthHandler cauth;
    private int RC_SIGN_IN;
    private static DatabaseHelper mDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gsin = new GoogleSignInHandler(this);
        mDbHelper = new DatabaseHelper(this);
        cauth = new CheckAuthHandler(mDbHelper);

Log.w("create ","completed");
    }



    @Override
    protected void onStart(){
        super.onStart();
Log.w("start","started");
        if(gsin.CheckSignedIn()){

            changeContentView();

        }else{
            setContentView(R.layout.activity_main);
            configureClickListener();
        }
    }

    private void configureClickListener() {
        findViewById(R.id.sign_in_button).setOnClickListener(this);
    }

    public void changeContentView(){
    Log.w("changeContentView","Started");
        cauth.CheckEmailExists(gsin.getEmail());
        if(!cauth.IsFileExists()) {
            setContentView(R.layout.not_authorized);
            Button bt = (Button) findViewById(R.id.na_lo_button);
            bt.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    gsin.googleSignOut();
                    setContentView(R.layout.activity_main);
                    configureClickListener();
                }
            });
            //setContentView(R.layout.activity_main);
            //TextView tv = (TextView) findViewById(R.id.authmessage);
            //tv.setText("Your Are Not Authorized");
        }else{
                 Intent intent = new Intent(this, LoggedinActivity.class);
                startActivity(intent);

//            setContentView(layoutname);

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.sign_in_button:
                startActivityForResult(gsin.googleSignIn(), RC_SIGN_IN);
                break;

            case R.id.na_lo_button:
                setContentView(R.layout.activity_main);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            gsin.fetchSignedInData(data);
            this.onStart();
        }
    }


}
