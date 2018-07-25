package com.marveric.bestitude.smartsurvey;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class LoggedinActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private GoogleSignInHandler gsin;
    private CheckAuthHandler cauth;
    private ViewFlipper vf;
    private ViewUsers vusers;
    private ViewSurveys vsurveys;
    private ViewMapSurveys vmsurveys;
    private static ScrollView mainScrollView;
    private static LoggingSupport ls;
    private MenuItem menuItem;
  //  private static SyncSurvey syncSurvey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.loading);
        gsin = new GoogleSignInHandler();
        cauth = new CheckAuthHandler();
        vusers = new ViewUsers( this );
        vsurveys = new ViewSurveys(this);
        vmsurveys = new ViewMapSurveys(this);
    //    syncSurvey = new SyncSurvey(this);
        ls = new LoggingSupport(this);
        setContentView(R.layout.activity_loggedin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
/*        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.textViewemailaddr);
        navUsername.setText(cauth.getUserEmailid());


        vf = (ViewFlipper)findViewById(R.id.viewflippers);
        mainScrollView = (ScrollView)findViewById(R.id.groupscrollview);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
          //  super.onBackPressed();
            Snackbar.make(findViewById(R.id.viewflippers), "Back button is diabled use menu", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.loggedin, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.view_surveys) {
            // Handle the camera action
            setTitle("View Surveys");
            vf.setDisplayedChild(vf.indexOfChild(findViewById(R.id.cl)));
            keepScrollup();
            if(cauth.getIsAdmin()) {
                findViewById(R.id.fab).setVisibility(View.VISIBLE);
                vsurveys.updateFloatingbutton();
            }
            vsurveys.fetchAllSurveys(cauth.getIsAdmin(), cauth.getUserEmailid() , cauth.getCookiegotten());
//        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_synconline) {
            if(isOnline()) {
                vsurveys.syncAll();
            }else{
                Toast.makeText(this,
                        "Phone is offline!!! Not Synced", Toast.LENGTH_SHORT).show();

            }
        } else if (id == R.id.nav_logout) {

            gsin.googleSignOut();
            cauth.Signout();
            finish();
            this.finishActivity(0);

            //finishAndRemoveTask();
   //         Intent intent = new Intent(this, LoggedinActivity.class);
     //       startActivity(intent);
            //System.exit(0);
        } else if (id == R.id.view_users_menu) {
            if(cauth.getIsAdmin()) {
                setTitle("View Users");
                vf.setDisplayedChild(vf.indexOfChild(findViewById(R.id.vu)));
                keepScrollup();
                findViewById(R.id.fab).setVisibility(View.VISIBLE);
                vusers.updateFloatingbutton();
                vusers.fetchAllUsers(cauth.getUserEmailid(),cauth.getCookiegotten());
            }
        } else if (id == R.id.view_map_surveys) {
            if(cauth.getIsAdmin()) {
                setTitle("View/Map Surveys");
                vf.setDisplayedChild(vf.indexOfChild(findViewById(R.id.vms)));
                keepScrollup();
                findViewById(R.id.fab).setVisibility(View.VISIBLE);
                vmsurveys.updateFloatingbutton();
                vmsurveys.fetchAllSurveys(cauth.getIsAdmin(), cauth.getUserEmailid() , cauth.getCookiegotten());
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    protected void onStart(){
        super.onStart();
        if(!gsin.CheckSignedIn()){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else{
            if(!cauth.getIsValid()){
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }else{
                setTitle("Rural Diabetes Survey");
                vf.setDisplayedChild(vf.indexOfChild(findViewById(R.id.sshome)));
                TextView tv = (TextView) findViewById(R.id.sshomeonlinestatus);
                if(isOnline()) {
                    tv.setText("Mode: Online");
                    tv.setTextColor(Color.parseColor("#29DE04"));
                }else{
                    tv.setText("Mode Offline");
                    tv.setTextColor(Color.parseColor("#85A181"));
                }
                keepScrollup();
                findViewById(R.id.fab).setVisibility(View.INVISIBLE);
                if(!cauth.getIsAdmin()){
                    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                    Menu nav_Menu = navigationView.getMenu();
                    nav_Menu.findItem(R.id.admin_links_hiding).setVisible(false);

                }

//                if(cauth.getIsAdmin()) {
  //                  vsurveys.updateFloatingbutton();
    //            }
      //          vsurveys.fetchAllSurveys(cauth.getIsAdmin(), cauth.getUserEmailid() , cauth.getCookiegotten());
            }
        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fab:
                Log.w("testing","testing");
                break;

        }
        Log.w("onclick listener", Integer.toString(v.getId()));
        this.saveString("onclick listener"+Integer.toString(v.getId()));
    }
    public LayoutInflater getactivityinflator(){
        return getLayoutInflater();
    }
    public View getactivityview(int id){
        return findViewById(id);
    }



    public ViewFlipper getVf(){
        return vf;
    }


    public void keepScrollup(){
        mainScrollView.fullScroll(ScrollView.FOCUS_UP);
    }

    public boolean isOnline()
    {
        try
        {
            ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public void saveException(Exception e){
        ls.WriteExcepData(e);
    }
    public void saveString(String s){ ls.writeStringData(s); }
}
