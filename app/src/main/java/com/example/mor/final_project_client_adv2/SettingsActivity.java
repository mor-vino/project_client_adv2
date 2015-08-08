package com.example.mor.final_project_client_adv2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;


public class SettingsActivity extends ActionBarActivity {
    private Spinner serversListSpinner;
    private Spinner bgUpdateSpinner;
    private Spinner fgUpdateSpinner;
    private String selectedServer;
    private ArrayList<String> serversList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        serversList = new ArrayList<String>();
        //GetServers gs = new GetServers();
        new Thread(new Runnable() {
            GetServers gs =  new GetServers();
            public void run() {
                try {

                        runOnUiThread(new Runnable() {
                            SharedPreferences sharedPreferences = getSharedPreferences("MyServer", MODE_PRIVATE);
                            String url = sharedPreferences.getString("serverURL", "err");

                            @Override
                            public void run() {

                                gs.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, url + "/getServers");
                            }
                        });
                        Thread.sleep(1000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            serversList = gs.getServersList();
                            serversListSpinner = (Spinner) findViewById(R.id.act_set_spinner_svr_addr);
                            ArrayAdapter<String> serversAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, serversList);
                            serversAdapter.setDropDownViewResource(R.layout.spinner_item);
                            serversListSpinner.setAdapter(serversAdapter);
                            ImageButton updateServerBtn = (ImageButton) findViewById(R.id.act_set_svr_addr_btn);
                            updateServerBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(serversList.size()==0) {
                                        Toast t = Toast.makeText(getApplicationContext(), "no servers to show", Toast.LENGTH_LONG);
                                        Intent i = new Intent(SettingsActivity.this, MapsActivity.class);
                                        startActivity(i);
                                    }
                                    int i = serversListSpinner.getSelectedItemPosition();
                                    selectedServer = serversList.get(i);
                                    SharedPreferences sharedPreferences = getSharedPreferences("MyServer", MODE_PRIVATE);
                                    SharedPreferences.Editor edit = sharedPreferences.edit();
                                    edit.putString("serverURL", selectedServer);
                                    String serverName;
                                    if (selectedServer.contains("http")) {
                                        serverName = selectedServer.substring(7, selectedServer.length() - 12);
                                    } else {
                                        serverName = selectedServer.substring(0, selectedServer.length() - 12);
                                    }
                                    edit.putString("serverName", serverName);
                                    edit.commit();
                                    Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                } catch (Exception e) {
                }

            }

        }).start();
        bgUpdateSpinner = (Spinner) findViewById(R.id.act_set_spinner_update_bg);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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
}
