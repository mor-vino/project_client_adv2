package com.example.mor.final_project_client_adv2;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * manage the account cookies
 */
public class OnTokenAcquired implements AccountManagerCallback<Bundle>  {
    private static final int USER_PERMISSION = 989;
    public String APP_ID;
    Activity activity;

    /**
     * constructor
     * @param activity - current activity
     */
    public OnTokenAcquired(Activity activity) {
        this.activity = activity;
        SharedPreferences sharedPreferences = activity.getSharedPreferences("MyServer", Context.MODE_PRIVATE);
        this.APP_ID = sharedPreferences.getString("serverName", "err");
        if(this.APP_ID.equals("err")) {
            this.APP_ID = "mpti-2048";
        }
    }

    /**
     * the method on the
     * new thread
     * @param result
     */
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
    /**
     * using the auth token and ask for a auth cookie
     * @param bundle
     */
    protected void setAuthToken(Bundle bundle) {
        String authToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);

        //
        new GetCookie(APP_ID, activity.getBaseContext(), activity).execute(authToken);

        Intent i = new Intent(activity, MapsActivity.class);
        activity.startActivity(i);
    }
}