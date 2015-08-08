package com.example.mor.final_project_client_adv2;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

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


public class JoinChannelActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_channel);
        /*Button b = (Button) findViewById(R.id.act_join_chan_btn);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/
        SharedPreferences sp = getSharedPreferences("MyServer", MODE_PRIVATE);
        new GetMyChannels(this).execute("http://" + sp.getString("serverName", "mpti-2048") + ".appspot.com/getMyChannels");
        //new GetChannels().execute("http://" + OnTokenAcquired.APP_ID + ".appspot.com/getChannels");
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
