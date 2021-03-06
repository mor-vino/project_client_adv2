package com.example.mor.final_project_client_adv2;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.mor.final_project_client_adv2.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * menu fragment class
 */
public class MenuFragment extends Fragment {
    //member
    private  String appId;

    /**
     * constructor
     */
    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            SharedPreferences sp = getActivity().getSharedPreferences("MyServer", Context.MODE_PRIVATE);
            this.appId = sp.getString("serverName", "err");
            if(this.appId.equals("err")) {
                this.appId = "mpti-2048";
            }
            Button channelsListBtn = (Button) view.findViewById(R.id.frag_menu_chan_list_btnId);
            // define the channels list button
            channelsListBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.add(R.id.act_maps_channel_list_layout_portrait_id, new ChannelListFragment());
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });

            // enter the channels options button
            Button channelsOptionsBtn = (Button) view.findViewById(R.id.frag_menu_chans_opt_btnId);
            channelsOptionsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), ChannelsOptions.class);
                    startActivity(i);
                }
            });
            // this button is to logOut
            Button logOutBtn = (Button) view.findViewById(R.id.frag_menu_logout_btn);
            logOutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new LogOff(getActivity()).execute("http://" + appId + ".appspot.com/logoff");                }
            });
            // this button is to enter the settings activity
            Button settingsBtn = (Button) view.findViewById(R.id.frag_menu_settings_btnId);
            settingsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), SettingsActivity.class);
                    startActivity(i);
                }
            });

            Button updatesBtn = (Button) view.findViewById(R.id.frag_menu_updates_btnId);
            updatesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // tell the user to wait
                    Toast t = Toast.makeText(getActivity().getApplicationContext(), "please wait", Toast.LENGTH_LONG);
                    t.show();
                    // create an object of the updates class
                    GetUpdatesFromServer upd = new GetUpdatesFromServer(getActivity(), appId);
                    // syncronize all updates with the current server
                    upd.syncAllUpdates();
                }
            });
        }


        return view;
    }

    /**
     * log off the server method
     */
    private class Logoff extends AsyncTask<String, String, String> {
        String text = null;

        /**
         * make the log off on buckground
         * @param params
         * @return the status
         */
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

        /**
         * make post request
         * @param result
         */
        protected void onPostExecute(String result) {
            JSONObject obj = null;
            try {
                obj = new JSONObject(result);
                int status = obj.getInt("status");
                if (status == 1) {
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    i.putExtra("urString", 'a');
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    // Checking the intent extra at "HOME"
                    if(i.hasExtra("urString")){
                        // manage your own way
                        getActivity().finish();
                    }
                }
                else {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * get request
     * @param entity url
     * @return the status result
     * @throws IllegalStateException
     * @throws IOException
     */
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
}
