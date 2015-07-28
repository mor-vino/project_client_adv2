package com.example.mor.final_project_client_adv2;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class ChannelsListAdapter extends BaseAdapter {
    private FragmentActivity activity;
    private List<ChannelItem> items;
    private LayoutInflater inflater;

    public ChannelsListAdapter(FragmentActivity activity, List<ChannelItem> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_channel_item, null);
        }

        ImageView imageIcon = (ImageView) convertView.findViewById(R.id.frag_channel_item_imge_icon);
        TextView textTitle = (TextView) convertView.findViewById(R.id.frag_channel_item_text_id);
        RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.frag_channel_item);

        ChannelItem item = items.get(position);

        imageIcon.setImageResource(item.getIcon());
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
