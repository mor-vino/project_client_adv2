package com.example.mor.final_project_client_adv2;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.params.HttpParams;
import java.io.ByteArrayOutputStream;

/**
 * class to manage the cookies
 * authentication infront the server
 */
public class GetCookie extends AsyncTask<String, Void, Boolean> {
    //members
    String appId;
    HttpParams params;
    private HttpResponse response;
    private String LINK_TO_GET_AUTHENTICATED;
    Context context;

    /**
     * constructor
     * @param appId the id tof the app
     * @param context of the device
     * @param act activity
     */
    public GetCookie(String appId, Context context, Activity act) {
        params = HttpClientStatic.httpClient.getParams();
        this.appId = appId;
        this.context = context;
        SharedPreferences SRVRsharedPreferences = act.getSharedPreferences("MyServer", Context.MODE_PRIVATE);
        this.LINK_TO_GET_AUTHENTICATED = SRVRsharedPreferences.getString("serverURL", "mpti-2048");
    }

    /**
     * authentication infront the server in the buckground
     * @param tokens
     * @return successful status
     */
    protected Boolean doInBackground(String... tokens) {
        try {
            // Don't follow redirects
            params.setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, false);

            HttpGet httpGet = new HttpGet("http://" + appId + ".appspot.com/_ah/login?continue=http://" + appId + ".appspot.com/&auth=" + tokens[0]);
            response = HttpClientStatic.httpClient.execute(httpGet);
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
            for(Cookie cookie : HttpClientStatic.httpClient.getCookieStore().getCookies()) {
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

    /**
     * send post request for the server
     * @param result boolean
     */
    protected void onPostExecute(Boolean result)
    {
        new Login(context).execute(LINK_TO_GET_AUTHENTICATED);
    }
}