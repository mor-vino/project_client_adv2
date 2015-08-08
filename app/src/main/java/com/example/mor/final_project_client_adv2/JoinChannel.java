package com.example.mor.final_project_client_adv2;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
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

/**
 * Created by mor on 07/08/2015.
 */
public class JoinChannel extends AsyncTask<String, String, String> {
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
            if (status == 1) {/*
                SharedPreferences.Editor editor = getSharedPreferences("",).edit();
                JSONArray arr = new JSONArray();
                JSONObject objArr = new JSONObject();
                objArr.put("arrMessages", arr);
                editor.putString(id, objArr.toString());
                editor.commit();*/
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
