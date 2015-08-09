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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mor on 09/08/2015.
 * class to implement the posr request
 */
public class PostRequest extends AsyncTask<String, String, String>{
    //members
    private String content;
    private Context context;
    private Activity myActivity;
    private String result = null;

    /**
     * constructor
     * @param act the current activity
     */
    public PostRequest(Activity act){
        myActivity = act;
        content =  null;
        context = myActivity.getBaseContext();
    }
    @Override
    protected String doInBackground(String... params) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(params[0]);
        HttpResponse response;
        //try to send post request
        try {
            List<NameValuePair> NVList = new ArrayList<NameValuePair>(1);
            NVList.add(new BasicNameValuePair("link", params[1]));
            httpPost.setEntity(new UrlEncodedFormEntity(NVList, "UTF-8"));
        }catch (Exception e) {
            e.printStackTrace();
            cancel(true);
        }
        try {
            response = httpclient.execute(httpPost);
            result = getASCIIContentFromEntity(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
            cancel(true);
        }
        return result;
    }

    //display the response from the request above
    protected void onPostExecute(Boolean result) {
        Toast.makeText(context, "Response from request: " + content,
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
