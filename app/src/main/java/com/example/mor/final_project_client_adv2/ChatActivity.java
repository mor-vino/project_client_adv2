package com.example.mor.final_project_client_adv2;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mChatList = (ListView)findViewById(R.id.activityChatLV);
        mChatAdapter = new ChatAdapter(this, chatItems);
        mChatList.setAdapter(mChatAdapter);
        this.textBox = (EditText) findViewById(R.id.act_chat_edit_text);
        this.sendButton = (Button) findViewById(R.id.act_cht_send_btn);
        this.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String m = textBox.getText().toString();
                if (m.length() > 0) {
                    chatItems.add(new ChatItem((m)));
                    mChatAdapter.notifyDataSetChanged();
                    textBox.setText("");
                }
            }
        });
        setActionBar();
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
