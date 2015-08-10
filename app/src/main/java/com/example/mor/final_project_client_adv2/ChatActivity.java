package com.example.mor.final_project_client_adv2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.location.LocationListener;

import java.util.ArrayList;
import java.util.List;

/**
 * class to make the chat
 */
public class ChatActivity extends ActionBarActivity {
    //members
    Button sendButton;
    List<ChatItem> chatItems = new ArrayList<ChatItem>();
    EditText textBox;
    ListView mChatList;
    ChatAdapter mChatAdapter;
    Double latitude;
    Double longitude;
    String chanId;
    String mess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        chanId = intent.getExtras().getString("currentId");

        mChatList = (ListView)findViewById(R.id.activityChatLV);
        mChatAdapter = new ChatAdapter(this, chatItems);
        mChatList.setAdapter(mChatAdapter);
        this.textBox = (EditText) findViewById(R.id.act_chat_edit_text);
        this.sendButton = (Button) findViewById(R.id.act_cht_send_btn);
        this.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mess = (textBox.getText().toString());
                if (mess.length() > 0) {
                    chatItems.add(new ChatItem((mess)));
                    mChatAdapter.notifyDataSetChanged();
                    textBox.setText("");
                    //this thread responsible on sending the data
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                // check the current server
                                SharedPreferences sp = getSharedPreferences("MyServer", Context.MODE_PRIVATE);
                                String appId = sp.getString("serverName", "mpti-2048");
                                // check current location
                                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                // prob here
                                double longitude = location.getLongitude();
                                String longi = String.valueOf(longitude);
                                double latitude = location.getLatitude();
                                String lati = String.valueOf(latitude);
                                // send the message to server
                                //String sendMessage( String channel_id, String text, Double longtitude, Double latitude)
                                new SendMessage(ChatActivity.this).execute("http://" + appId + ".appspot.com/sendMessage", chanId, mess, longi, lati);
                            } catch (Exception e) {

                            };
                        }
                    }).start();
                }
                SharedPreferences newMessages = getSharedPreferences("newMessages", Context.MODE_PRIVATE);
                String messa;
                if (!((messa = newMessages.getString(chanId, "-1")).equals("-1"))){
                    // the value is not "-1"
                    chatItems.add(new ChatItem((messa)));
                    mChatAdapter.notifyDataSetChanged();
                    // the message is not new anymore
                    SharedPreferences.Editor edit = newMessages.edit();
                    edit.putString(chanId,"-1");
                    edit.commit();
                }
            }
        });
        setActionBar();

        new Thread(new Runnable() {
            public void run() {
                try {
                    SharedPreferences sharedPreferences = getSharedPreferences("MyServer", Context.MODE_PRIVATE);
                    String appId= sharedPreferences.getString("serverName", "mpti-2048");
                    // get updating messages from server
                    while (true){
                        new GetUpdatedMessg(ChatActivity.this, chanId).execute("http://" + appId + ".appspot.com/getUpdates");
                    }
                } catch (Exception e) {

                };
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
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
    private void setActionBar() {
        ActionBar mAction = getSupportActionBar();
        mAction.hide();
    }
}
