package com.example.mor.final_project_client_adv2;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import java.io.ByteArrayOutputStream;

public class GetCookie extends AsyncTask<String, Void, Boolean> {

    String appId;
    HttpParams params;
    private HttpResponse response;
    private static final String LINK_TO_GET_AUTHENTICATED = "http://projectserver-984.appspot.com/login";
    Context context;
    private DefaultHttpClient httpclient;

    public GetCookie(DefaultHttpClient httpclient, String appId, Context context)
    {
        this.httpclient = httpclient;
        params = httpclient.getParams();
        this.appId = appId;
        this.context = context;
    }

    protected Boolean doInBackground(String... tokens) {

        try {

            // Don't follow redirects
            params.setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, false);

            HttpGet httpGet = new HttpGet("http://" + appId + ".appspot.com/_ah/login?continue=http://" + appId + ".appspot.com/&auth=" + tokens[0]);
            response = httpclient.execute(httpGet);
            //HttpEntity entity = response.getEntity();
            //String text = getASCIIContentFromEntity(entity);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            out.close();

            if(response.getStatusLine().getStatusCode() != 302){
                // Response should be a redirect
                return false;
            }

            //check if we received the ACSID or the SACSID cookie, depends on http or https request
            for(Cookie cookie : httpclient.getCookieStore().getCookies()) {
                if(cookie.getName().equals("ACSID") || cookie.getName().equals("SACSID")){
                    return true;
                }
            }

        }  catch (Exception e) {
            e.printStackTrace();
            cancel(true);
        } finally {
            params.setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, true);
        }
        return false;
    }

    protected void onPostExecute(Boolean result)
    {
        new Auth(httpclient, context).execute(LINK_TO_GET_AUTHENTICATED);
    }
}