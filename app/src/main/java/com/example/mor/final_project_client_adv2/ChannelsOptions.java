package com.example.mor.final_project_client_adv2;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class ChannelsOptions extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channels_options);
        // define the adding new channel button
        Button addChannelBtn = (Button) findViewById(R.id.act_chans_opt_add_chan);
        addChannelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChannelsOptions.this, AddNewChannelActivity.class);
                startActivity(i);
            }
        });
        // define the removing channel button
        Button rmvChannelBtn = (Button) findViewById(R.id.act_chans_opt_remove_chan);
        rmvChannelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChannelsOptions.this, RemoveChannelActivity.class);
                startActivity(i);
            }
        });

        // define the removing channel button
        Button joinChannelBtn = (Button) findViewById(R.id.act_chans_opt_join_chan);
        joinChannelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChannelsOptions.this, JoinChannelActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_channels_options, menu);
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
