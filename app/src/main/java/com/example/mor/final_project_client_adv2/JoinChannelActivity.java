package com.example.mor.final_project_client_adv2;

import android.content.Context;
import android.content.Intent;
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

import org.apache.http.impl.client.DefaultHttpClient;

import java.util.ArrayList;
import java.util.Map;


public class JoinChannelActivity extends ActionBarActivity {

    private String DEFAULT = "-1";
    private Spinner spinner;
    private ArrayList<String> channelsList;
    private String id;
    private String name;
    private String icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_channel);

        spinner = (Spinner) findViewById(R.id.act_join_chan_spinner_account);
        channelsList = new ArrayList<String>();

        SharedPreferences allChannels_IdName_SP =  getSharedPreferences("AllChannels_IdName_SP", Context.MODE_PRIVATE);
        Map<String,?> keys = allChannels_IdName_SP.getAll();
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
            Button join = (Button) findViewById(R.id.act_join_chan_btn);
            join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = spinner.getSelectedItemPosition();
                    id = channelsList.get(i);
                    SharedPreferences myChannels_IdName_SP =  getSharedPreferences("MyChannels_IdName_SP", Context.MODE_PRIVATE);
                    // if the return value is not the DEFAULT, it means that it already joined that channel
                    if (!(name = myChannels_IdName_SP.getString(id, DEFAULT)).equals(DEFAULT)){
                        Toast t = Toast.makeText(getApplicationContext(), "you are already connected to this channel",
                                Toast.LENGTH_LONG);
                        t.show();
                    } else {
                        // tell the user to wait
                        Toast t = Toast.makeText(getApplicationContext(), "please wait", Toast.LENGTH_LONG);
                        t.show();
                        // ask the server to join to this channel
                        SharedPreferences sp = getSharedPreferences("MyServer", Context.MODE_PRIVATE);
                        String appId = sp.getString("serverName", "err");
                        if(appId.equals("err")) {
                            appId = "mpti-2048";
                        }
                        new JoinChannel(JoinChannelActivity.this).execute("http://" + appId + ".appspot.com/joinChannel", id);
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_join_channel, menu);
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

/*
    private class GetChannels extends AsyncTask<String, String, String> {
        String text = null;
        String message = null , name , id , icon;
        private List<String> allChannels;

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
            allChannels = new ArrayList<String>();
            Spinner chnls = (Spinner) findViewById(R.id.act_join_chan_spinner_account);
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
                    allChannels.add(id);
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AddNewChannelActivity.this,
                            android.R.layout.simple_spinner_item, allChannels);
                    dataAdapter.setDropDownViewResource(R.layout.spinner_item);
                    chnls.setAdapter(dataAdapter);
                    Button addChannelBtn = (Button) findViewById(R.id.act_join_chan_btn);
                    addChannelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String checkConnection = sharedPref.getString(id, "-1");
                            //if the user hasn't connected to the channel, connect!
                            if (checkConnection.equals("-1")) {
                                new JoinChannel().execute("http://" + OnTokenAcquired.APP_ID + ".appspot.com/joinChannel", id);
                            }
                        }
                    });


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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
                    Toast.makeText(getActivity(), "connect Channel", Toast.LENGTH_SHORT).show();
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
