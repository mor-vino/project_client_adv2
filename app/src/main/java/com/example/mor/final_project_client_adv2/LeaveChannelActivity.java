package com.example.mor.final_project_client_adv2;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

public class LeaveChannelActivity extends ActionBarActivity {
    private String DEFAULT = "-1";
    private Spinner spinner;
    private ArrayList<String> channelsList;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_channel);
        // load the channels list from shared preference
        spinner = (Spinner) findViewById(R.id.act_leave_chan_spinner);
        channelsList = new ArrayList<String>();
        SharedPreferences myChannels_IdName_SP =  getSharedPreferences("MyChannels_IdName_SP", Context.MODE_PRIVATE);
        Map<String,?> keys = myChannels_IdName_SP.getAll();
        for(Map.Entry<String,?> entry : keys.entrySet()){
            String id =  entry.getKey();
            channelsList.add(id);
        }
        if(channelsList.size()==0) {
            Toast t = Toast.makeText(getApplicationContext(), "no channels to show", Toast.LENGTH_LONG);
            t.show();
        } else {
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, channelsList);
            dataAdapter.setDropDownViewResource(R.layout.spinner_item);
            spinner.setAdapter(dataAdapter);
            Button leaveBtn = (Button) findViewById(R.id.act_leave_chan_btn);
            leaveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // tell the user to wait
                    Toast t = Toast.makeText(getApplicationContext(), "please wait", Toast.LENGTH_LONG);
                    t.show();
                    int i = spinner.getSelectedItemPosition();
                    id = channelsList.get(i);
                    // ask the server to leave this channel
                    SharedPreferences sp = getSharedPreferences("MyServer", Context.MODE_PRIVATE);
                    String appId = sp.getString("serverName", "mpti-2048");
                    // TODO --  LEAVING CHANNEL CLASS
                    //new PostRequest(LeaveChannelActivity.this).execute("http://" + appId + ".appspot.com/leaveChannel", id);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_leave_channel, menu);
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
