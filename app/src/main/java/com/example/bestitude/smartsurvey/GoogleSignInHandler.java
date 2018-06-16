package com.example.bestitude.smartsurvey;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;


public class GoogleSignInHandler {

    private static GoogleSignInOptions gso;
    private static GoogleSignInClient mGoogleSignInClient;
    private static GoogleSignInAccount account;
    private static String PersonEmail;
    private static String PersonName;

    GoogleSignInHandler(MainActivity ma) {
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
            Log.w("hi", "signInResult:failed code=" + e.getStatusCode());


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
}
