package com.example.mor.final_project_client_adv2;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChannelListFragment extends Fragment {
    private ListView lstChannels;

    public ChannelListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel_list, container, false);
        ListView lstChannels = (ListView) view.findViewById(R.id.frag_channels_list_view_id);

        List<ChannelItem> channelItemsList = new ArrayList<ChannelItem>();
        //channelItemsList = (List<ChannelItem>)(getArguments().get("channelsList"));
        channelItemsList.add(new ChannelItem("5","paz","M", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                startActivity(intent);
            }
        }));
        channelItemsList.add(new ChannelItem("6","mor","M",new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                startActivity(intent);
            }
        }));
        channelItemsList.add(new ChannelItem("1","shira","M",new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                startActivity(intent);
            }
        }));

        ChannelsListAdapter channelsListAdapter = new ChannelsListAdapter((FragmentActivity) getActivity(), channelItemsList);
        lstChannels.setAdapter(channelsListAdapter);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            ListView lstMenu = (ListView) view.findViewById(R.id.frag_channels_list_view_id);
            lstMenu.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
                @Override
                public void onSwipeLeft() {
                    setFragment();
                }
                @Override
                public void onSwipeRight() {
                    setFragment();
                }
            });
        }
        return view;
    }
    private void setFragment() {
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            FragmentManager fm1 = getFragmentManager();
            FragmentTransaction ft1 = fm1.beginTransaction();
            fm1.popBackStack();
            ft1.commit();
        }
    }
}