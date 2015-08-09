package com.example.mor.final_project_client_adv2;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;


public class AddNewChannelActivity extends ActionBarActivity {

    String DEFAULT = "-1";

    String id;
    String name;
    String icon;

    EditText textBoxID;
    EditText textBoxNAME;
    EditText textBoxICON;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_channel);
        // set the text editors: id, name, icon
        textBoxID = (EditText) findViewById(R.id.act_add_new_chan_edit_text_id);
        textBoxNAME = (EditText)findViewById(R.id.act_add_new_chan_edit_text_name);
        textBoxICON = (EditText)findViewById(R.id.act_add_new_chan_edit_text_icon);
        // set the ADD button
        Button addBtn = (Button) findViewById(R.id.act_add_new_chann_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the user's inputs
                id = textBoxID.getText().toString();
                name = textBoxNAME.getText().toString();
                icon = textBoxICON.getText().toString();
                // check if already exist
                SharedPreferences allChannels = getSharedPreferences(
                        "AllChannels_IdName_SP", Context.MODE_PRIVATE);
                if(allChannels.contains(id)){
                    //let the user know that it already exist
                    Toast t = Toast.makeText(getApplicationContext(), "the channel already exist \n " +
                            "please enter JOIN EXISTING CHANNEL tab", Toast.LENGTH_LONG);
                    t.show();
                }else {
                    // tell the user to wait
                    Toast t = Toast.makeText(getApplicationContext(), "please wait", Toast.LENGTH_LONG);
                    t.show();
                    // ask the server to add this channel
                    SharedPreferences sp = getSharedPreferences("MyServer", Context.MODE_PRIVATE);
                    String appId = sp.getString("serverName", "mpti-2048");
                    // TODO -- ADDING NEW CHANNEL CLASS
                    new AddChannel(AddNewChannelActivity.this).execute("http://" + appId + ".appspot.com/addChannel", id, name, icon);

                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_new_channel, menu);
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
