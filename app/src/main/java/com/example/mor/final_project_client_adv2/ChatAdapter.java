package com.example.mor.final_project_client_adv2;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


public class ChatAdapter  extends BaseAdapter {
    private Activity activity;
    private List<ChatItem> items;
    private LayoutInflater inflater;
    public ChatAdapter(Activity activity, List<ChatItem> items){
        this.activity = activity;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.chat_list, null);
        }

        TextView textTitle = (TextView) convertView.findViewById(R.id.chatListMessageTV);
        LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.chatListLayout);

        ChatItem item = items.get(position);

        textTitle.setText(item.getTitle());
        layout.setOnClickListener(item.getListener());

        return convertView;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return this.items.get(position);
    }
}
