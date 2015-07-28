package com.example.mor.final_project_client_adv2;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import org.apache.http.impl.client.DefaultHttpClient;


public class OnTokenAcquired implements AccountManagerCallback<Bundle>  {
    private static final int USER_PERMISSION = 989;
    public static final String APP_ID = "projectserver-981";
    private DefaultHttpClient httpclient;
    Activity activity;

    public OnTokenAcquired(DefaultHttpClient httpclient, Activity activity) {
        this.httpclient = httpclient;
        this.activity = activity;
    }
    public void run(AccountManagerFuture<Bundle> result) {

        Bundle bundle;

        try {
            bundle = (Bundle) result.getResult();
            if (bundle.containsKey(AccountManager.KEY_INTENT)) {
                Intent intent = bundle.getParcelable(AccountManager.KEY_INTENT);
                intent.setFlags(intent.getFlags() & ~Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivityForResult(intent, USER_PERMISSION);
            } else {
                setAuthToken(bundle);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    //using the auth token and ask for a auth cookie
    protected void setAuthToken(Bundle bundle) {
        String authToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);

        new GetCookie(httpclient, APP_ID, activity.getBaseContext()).execute(authToken);
        Intent i = new Intent(activity, MapsActivity.class);
        activity.startActivity(i);
    }
}