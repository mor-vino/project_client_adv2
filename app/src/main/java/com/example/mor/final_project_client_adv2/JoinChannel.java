package com.example.mor.final_project_client_adv2;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mor on 07/08/2015.
 * class to add new channel
 */
public class JoinChannel extends AsyncTask<String, String, String> {
    //members
    String text = null;
    String id;
    private Activity myActivity;

    /**
     * constructor
     * @param act current activity
     */
    public JoinChannel(Activity act){
        this.myActivity = act;
    }

    /**
     * connect the server in buckground
     * @param params to the server
     * @return the result
     */
    protected String doInBackground(String... params) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(params[0]);
        HttpResponse response;
        try {
            List<NameValuePair> NVList = new ArrayList<NameValuePair>(1);
            NVList.add(new BasicNameValuePair("id", params[1]));
            httpPost.setEntity(new UrlEncodedFormEntity(NVList));
            response = httpclient.execute(httpPost);
            text = getASCIIContentFromEntity(response.getEntity());
        }catch (Exception e) {
            e.printStackTrace();
            cancel(true);
        }
        return text;
    }

    /**
     * send post request
     * @param result
     */
    protected void onPostExecute(String result) {
        JSONObject obj = null;
        try {
            obj = new JSONObject(result);
            int status = obj.getInt("status");
            if (status == 1) {
                Toast t = Toast.makeText(this.myActivity.getApplicationContext(),
                        "welcome to channel:" + id, Toast.LENGTH_SHORT);
                t.show();
            } else if (status == 0) {
                Toast t = Toast.makeText(this.myActivity.getApplicationContext(),
                        "response from server : error" , Toast.LENGTH_SHORT);
                t.show();
            } else {
                Toast t = Toast.makeText(this.myActivity.getApplicationContext(),
                        "error", Toast.LENGTH_SHORT);
                t.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * make get request
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
