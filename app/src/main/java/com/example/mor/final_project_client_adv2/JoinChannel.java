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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

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
        HttpPost httpPost = new HttpPost(params[0]);
        HttpResponse response;
        try {
            List<NameValuePair> NVList = new ArrayList<NameValuePair>(1);
            NVList.add(new BasicNameValuePair("id", params[1]));
            httpPost.setEntity(new UrlEncodedFormEntity(NVList));
            response = HttpClientStatic.httpClient.execute(httpPost);
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
    //display the response from the request above
    protected void onPostExecute(String result) {
        Toast.makeText(myActivity.getBaseContext(), "Response from request: " + result,
                Toast.LENGTH_LONG).show();
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
