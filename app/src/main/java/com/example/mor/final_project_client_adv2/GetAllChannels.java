package com.example.mor.final_project_client_adv2;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

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
 * Created by mor on 07/08/2015.
 */
public class GetAllChannels extends AsyncTask<String, String, String> {
    String text = null;
    String message = null , name , id , icon;
    public SharedPreferences sharedPref;
    public SharedPreferences allChannelsSP;

    // input: the current activity
    public GetAllChannels(Activity act) {
        allChannelsSP =  act.getSharedPreferences("AllChannelsSP", Context.MODE_PRIVATE);
    }
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
                SharedPreferences.Editor editor = allChannelsSP.edit();
                editor.putString(id, name);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
}
