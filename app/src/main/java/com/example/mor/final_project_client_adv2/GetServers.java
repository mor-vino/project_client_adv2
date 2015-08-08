package com.example.mor.final_project_client_adv2;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.view.View;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by paz on 07/08/2015.
 */
public class GetServers extends AsyncTask<String, String, String> {
    private String text = null;
    private String message = null;
    private ArrayList<String> serversList;

    @Override
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

    @Override
    protected void onPostExecute(String result) {
        serversList = new ArrayList<String>();
        JSONObject obj = null;
        try {
            obj = new JSONObject(result);
            JSONArray servers = obj.getJSONArray("servers");
            for (int i = 0; i < servers.length(); i++) {
                serversList.add(servers.getString(i));
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
    public ArrayList<String> getServersList() {
        return this.serversList;
    }
}
