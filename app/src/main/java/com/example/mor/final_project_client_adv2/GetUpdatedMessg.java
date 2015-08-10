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
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mor on 10/08/2015.
 */
public class GetUpdatedMessg  extends AsyncTask<String, String, String> {
    //members
    String text = null;
    String name , id , icon;
    Activity myActivity;
    String chanID;

    // constructor
    public GetUpdatedMessg(Activity act, String chanID) {
        myActivity = act;
        this.chanID = chanID;
    }

    /**
     * connection in the background
     * @param params for the communication
     * @return the message
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
     * @param result kye value
     */
    protected void onPostExecute(String result) {
        JSONObject obj = null;
        try {
            obj = new JSONObject(result);
            JSONArray channels = obj.getJSONArray("messages");
            JSONObject data;
            // pass over all the channels
            for (int i = 0; i < channels.length(); i++) {
                SharedPreferences  newMessages = myActivity.getSharedPreferences("newMessages", Context.MODE_PRIVATE);
                data = channels.getJSONObject(i);
                id = data.getString("id");
                if (chanID.equals(id)){
                    String currentMessg = data.getString("text");
                    SharedPreferences.Editor edit = newMessages.edit();
                    edit.putString(id, currentMessg);
                    edit.commit();
                }
            }
            Toast toast = Toast.makeText( myActivity.getApplicationContext(),
                    "finish update MY CHANNELS SharedPref",  Toast.LENGTH_SHORT);
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * make get request for the server
     * @param entity url
     * @return the result
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
