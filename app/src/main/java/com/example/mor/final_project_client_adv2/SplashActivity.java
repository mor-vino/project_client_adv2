package com.example.mor.final_project_client_adv2;

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v7.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class SplashActivity extends ActionBarActivity {
    private int count;
    private int i;
    ServerInfo si;
    public SplashActivity() {
        super();
        count = 0;
        i=0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final TextView text = (TextView) findViewById(R.id.act_splash_text);
        final ProgressBar pb = (ProgressBar)findViewById(R.id.act_splash_progress_bar);
        final TextView changingText = (TextView) findViewById(R.id.act_splash_chainging_text_id);
        final List<String> textLst = new ArrayList<String>(5);
        textLst.add("Final Project");
        textLst.add("Tomer Shalmon");
        textLst.add("Itay Parnafes");
        textLst.add("Mor Zohar");
        textLst.add("Paz Huber");
        si = new ServerInfo("http://projectserver-984.appspot.com");
        SharedPreferences sp = getSharedPreferences("MyServer", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        SharedPreferences spUpdate = getSharedPreferences("updates", Context.MODE_PRIVATE);
        SharedPreferences.Editor editUpdates = spUpdate.edit();
        edit.putString("serverName", si.getServerName());
        edit.putString("serverURL", si.getServerURL());
        editUpdates.putInt("bg", 5);
        editUpdates.putInt("fg", 10);
        edit.commit();
        editUpdates.commit();
        new Thread(new Runnable() {
            public void run() {
                try {
                    while (count <= 100) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pb.setProgress(count);
                                text.setText(Integer.toString(count) + "%");
                                if (count % 21 == 0) {
                                    changingText.setText(textLst.get(i));
                                    i++;
                                }
                            }
                        });
                        Thread.sleep(50);
                        count++;
                    }
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                } catch (Exception e) {
                }
                ;

            }

        }).start();
        setActionBar();

    }
    private void setActionBar() {
        ActionBar mAction = getSupportActionBar();
        mAction.hide();
    }
}
