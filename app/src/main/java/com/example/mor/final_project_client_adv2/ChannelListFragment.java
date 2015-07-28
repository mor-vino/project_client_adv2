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
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChannelListFragment extends Fragment {
    private List<ChannelItem> channels;

    public ChannelListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel_list, container, false);
        ListView lstChannels = (ListView) view.findViewById(R.id.frag_channels_list_view_id);

        List<ChannelItem> channelItemsList = new ArrayList<ChannelItem>();
        channelItemsList = (List<ChannelItem>)(getArguments().get("channelsList"));
        channelItemsList.add(new ChannelItem("5", R.mipmap.icon_channels_lg, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                startActivity(intent);
            }
        }));
        channelItemsList.add(new ChannelItem("6", R.mipmap.icon_channels_lg, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                startActivity(intent);
            }
        }));
        channelItemsList.add(new ChannelItem("1", R.mipmap.icon_channels_lg, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                startActivity(intent);
            }
        }));
        channelItemsList.add(new ChannelItem("10", R.mipmap.icon_channels_lg, new View.OnClickListener() {
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
    List<ChannelItem> getChannelsList() {
        return channels;
    }
    /*
    private class GetChannels extends AsyncTask<String, String, String> {
        String text = null;
        String message = null , name , id , icon;
        private List<ChannelItem> menuChannels = getChannelsList();

        protected String doInBackground(String... params) {
            try {
                HttpContext localContext = new BasicHttpContext();
                DefaultHttpClient httpClient = new DefaultHttpClient();

                HttpGet httpGet = new HttpGet(params[0]);

                HttpResponse response = httpClient.execute(httpGet, localContext);
                HttpEntity entity = response.getEntity();
                text = getASCIIContentFromEntity(entity);

            } catch (Exception e) {}

            return text;
        }

        protected void onPostExecute(String result) {
            menuChannels = new ArrayList<ChannelItem>();
            JSONObject obj = null;
            try {
                obj = new JSONObject(result);
                JSONArray channels = obj.getJSONArray("channels");
                JSONObject data;
                for (int i = 0; i < channels.length(); i++) {
                    data = channels.getJSONObject(i);
                    String icon = data.getString("icon");
                    String name = data.getString("name");
                    final String id = data.getString("id");
                    menuChannels.add(new ChannelItem(name, id, Integer.parseInt(icon), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String checkConnection = MapsActivity.sharedPref.getString(id, "-1");
                            //if the user hasn't connected to the channel, connect!
                            if (checkConnection.equals("-1")) {
                                new JoinChannel().execute("http://" + appId + ".appspot.com/joinChannel", id);
                            }
                            Intent intent = new Intent(getActivity(), ChatActivity.class);
                            intent.putExtra("id", id);
                            startActivity(intent);
                        }
                    }));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            ChannelsListAdapter friendsAdapter = new ChannelsListAdapter((FragmentActivity) getActivity(), menuChannels);
            lstMenu.setAdapter(friendsAdapter);
        }
    }

    private class JoinChannel extends AsyncTask<String, String, String> {
        String text = null;
        String id;

        protected String doInBackground(String... params) {
            try {
                HttpContext localContext = new BasicHttpContext();
                DefaultHttpClient httpClient = new DefaultHttpClient();

                HttpPost httpPost = new HttpPost(params[0]);
                ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
                parameters.add(new BasicNameValuePair("id", params[1]));
                id = params[1];
                httpPost.setEntity(new UrlEncodedFormEntity(parameters));

                HttpResponse response = httpClient.execute(httpPost, localContext);
                HttpEntity entity = response.getEntity();
                text = getASCIIContentFromEntity(entity);
                return text;

            } catch (Exception e) {}
            return text;
        }

        protected void onPostExecute(String result) {
            JSONObject obj = null;
            try {
                obj = new JSONObject(result);
                int status = obj.getInt("status");
                if (status == 1) {
                    SharedPreferences.Editor editor = MapsActivity.sharedPref.edit();
                    JSONArray arr = new JSONArray();
                    JSONObject objArr = new JSONObject();
                    objArr.put("arrMessages", arr);
                    editor.putString(id, objArr.toString());
                    editor.commit();
                    Toast.makeText(getActivity(), getString(R.string.connectChannel), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected String getASCIIContentFromEntity(HttpEntity entity)
            throws IllegalStateException, IOException {
        InputStream in = entity.getContent();
        StringBuffer out = new StringBuffer();
        int n = 1;
        while (n > 0) {
            byte[] b = new byte[4096];
            n = in.read(b);
            if (n > 0)
                out.append(new String(b, 0, n));
        }
        return out.toString();
    }
    */
}