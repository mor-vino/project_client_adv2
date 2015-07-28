package com.example.mor.final_project_client_adv2;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.ByteArrayOutputStream;


public class Auth extends AsyncTask<String, Void, Boolean> {

    private DefaultHttpClient httpclient;
    private HttpResponse response;
    private String content =  null;
    Context context;

    public Auth(DefaultHttpClient httpclient, Context context)
    {
        this.httpclient = httpclient;
        this.context = context;
    }

    protected Boolean doInBackground(String... urls) {

        try {

            HttpGet httpGet = new HttpGet(urls[0]);
            response = httpclient.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                content = out.toString();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            cancel(true);
        }
        return false;
    }

    //display the response from the request above
    protected void onPostExecute(Boolean result) {
        Toast.makeText(context, "Response from request: " + content,
                Toast.LENGTH_LONG).show();
    }

    /*protected String getASCIIContentFromEntity(HttpEntity entity)
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
    }*/
}
