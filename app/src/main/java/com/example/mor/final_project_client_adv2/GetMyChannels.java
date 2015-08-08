package com.example.mor.final_project_client_adv2;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

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
import java.util.List;

/**
 * Created by mor on 03/08/2015.
 */
public class GetMyChannels extends AsyncTask<String, String, String> {
    String text = null;
    String message = null , name , id , icon;
    public SharedPreferences myChannels_IdName_SP;
    public SharedPreferences myChannels_IdIcon_SP;
    Activity myActivity;

    // input: the current activity
    public GetMyChannels(Activity act) {
        myActivity = act;
        myChannels_IdName_SP =  act.getSharedPreferences("MyChannels_IdName_SP", Context.MODE_PRIVATE);
        myChannels_IdIcon_SP =  act.getSharedPreferences("MyChannels_IdIcon_SP", Context.MODE_PRIVATE);
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
                SharedPreferences.Editor editor1 = myChannels_IdName_SP.edit();
                SharedPreferences.Editor editor2 = myChannels_IdIcon_SP.edit();
                editor1.putString(id, name);
                editor2.putString(id, icon);
                editor1.commit();
                editor2.commit();
            }
            Toast toast = Toast.makeText( myActivity.getApplicationContext(),
                    "FINISHED ENTER MY CHANNELS TO SP!",  Toast.LENGTH_SHORT);
            toast.show();
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
