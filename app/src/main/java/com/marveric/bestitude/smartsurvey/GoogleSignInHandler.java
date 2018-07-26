package com.marveric.bestitude.smartsurvey;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class GoogleSignInHandler {

    private static GoogleSignInOptions gso;
    private static GoogleSignInClient mGoogleSignInClient;
    private static GoogleSignInAccount account;
    private static String PersonEmail;
    private static String PersonName;
    private static MainActivity maa;

    GoogleSignInHandler(){

    }
    GoogleSignInHandler(MainActivity ma) {
        maa = ma;
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(ma, gso);
        account = GoogleSignIn.getLastSignedInAccount(ma);
        if(account != null){
            fetchAccount(account);
        }
    }

    public boolean CheckSignedIn() {
        if (account != null) {
            return true;
        } else {
            return false;
        }
    }

    public Intent googleSignIn() {
        return mGoogleSignInClient.getSignInIntent();
    }

    public void fetchAccount(GoogleSignInAccount acc) {
        account = acc;
        setEmail(account.getEmail());
        setName(account.getDisplayName());
    }

    public void fetchSignedInData(Intent data) {
        try {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInAccount account = task.getResult(ApiException.class);
            fetchAccount(account);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            if(BuildConfig.DEBUG) Log.i("hi", "signInResult:failed code=" + e.getStatusCode());


        }
    }

    public void setEmail(String email){
        PersonEmail = email;
    }
    public String getEmail(){
        return PersonEmail;
    }
    public void setName(String name){
        PersonName = name;
    }
    public String getName(){
        return PersonName;
    }

    public static void googleSignOut(){
        account = null;
        PersonEmail = null;
        PersonName = null;
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(maa, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    //    System.exit(0);
                    }
                });

    }


    public boolean isOnline()
    {
        try
        {
            ConnectivityManager cm = (ConnectivityManager) maa.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        }
        catch (Exception e)
        {
            return false;
        }
    }

}
