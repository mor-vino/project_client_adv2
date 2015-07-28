package com.example.mor.final_project_client_adv2;

import android.app.IntentService;
import android.content.Intent;


public class ReloadService extends IntentService {
    public static final String DONE = "com.example.mor.final_project_client_adv2.Services.ReloadService.DONE";

    // constructor
    public ReloadService() {
        super("ReloadService");
    }

    // handle function
    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        // send like a "massage" to the whole machine that now the "DONE" happend.
        Intent i = new Intent(DONE);
        sendBroadcast(i);
    }
}
