package com.marveric.bestitude.smartsurvey;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.CountDownTimer;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private GoogleSignInHandler gsin;
    private CheckAuthHandler cauth;
    private int RC_SIGN_IN;
    private static DatabaseHelper mDbHelper;
    private static CountDownTimer cdt = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        gsin = new GoogleSignInHandler(this);
        mDbHelper = new DatabaseHelper(this);
        cauth = new CheckAuthHandler(mDbHelper, this);

if(BuildConfig.DEBUG) Log.i("create ","completed");
    }



    @Override
    protected void onStart(){
        super.onStart();

if(BuildConfig.DEBUG) Log.i("start","started");
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
    if(BuildConfig.DEBUG) Log.i("changeContentView","Started");
        cauth.CheckEmailExists(gsin.getEmail());
        int isfileexists = cauth.IsFileExists();
        if(isfileexists == 0) {
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
        }else if(isfileexists == 1){
            if(cdt != null){
                cdt.cancel();
            }
            if(BuildConfig.DEBUG) Log.i("timer stop","stop");
                 Intent intent = new Intent(this, LoggedinActivity.class);
                startActivity(intent);

//            setContentView(layoutname);

        }else if(isfileexists == 2){
            setContentView(R.layout.loading);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.sign_in_button:
                cdt = new CountDownTimer(10000, 1000) {

                    public void onTick(long millisUntilFinished) {
                       // mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                    }

                    public void onFinish() {
                        //mTextField.setText("done!");
                 //       Toast.makeText(getApplicationContext() , "Slow network!!! please use offline mode", Toast.LENGTH_SHORT).show();
                    }
                }.start();

                if(BuildConfig.DEBUG) Log.i("timer start","start");
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

    public void ChangeIntent(int vu){
        if(vu == 1) {
            Intent intent = new Intent(this, LoggedinActivity.class);
            startActivity(intent);
        }else{
            MainActivity.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {

            setContentView(R.layout.not_authorized);
            Button bt = (Button) findViewById(R.id.na_lo_button);
            bt.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    gsin.googleSignOut();
                    setContentView(R.layout.activity_main);
                    configureClickListener();
                }
            });
                }

            });
        }
    }


}
