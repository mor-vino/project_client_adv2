package com.example.mor.final_project_client_adv2;

import android.app.Activity;
import android.content.Context;
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
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * class to implement the post request of addChannel
 */
public class AddChannel extends AsyncTask<String,String,String>{
    //members
    private Context context;
    private Activity myActivity;
    private String result = null;

    /**
     * constructor
     * @param act the current activity
     */
    public AddChannel(Activity act){
        myActivity = act;
        context = myActivity.getBaseContext();

    }
    @Override
    protected String doInBackground(String... params) {
        HttpPost httpPost = new HttpPost(params[0]);
        HttpResponse response;
        //try to send post request
        try {
            List<NameValuePair> NVList = new ArrayList<NameValuePair>(3);
            NVList.add(new BasicNameValuePair("id", params[1]));
            NVList.add(new BasicNameValuePair("name", params[2]));
            NVList.add(new BasicNameValuePair("icon", params[3]));
            httpPost.setEntity(new UrlEncodedFormEntity(NVList));
            response = HttpClientStatic.httpClient.execute(httpPost);
            result = getASCIIContentFromEntity(response.getEntity());
            return result;

         }catch (Exception e) {
            e.printStackTrace();
            cancel(true);
        }
    return result;

    }

    //display the response from the request above
    protected void onPostExecute(String result) {
        Toast.makeText(context, "Response from request: " + result,
                Toast.LENGTH_LONG).show();
    }

    /**
     *
     * @param entity url
     * @return return the ASCII value
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
