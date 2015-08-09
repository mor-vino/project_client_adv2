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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by paz on 08/08/2015.
 */
public class GetNetwork extends AsyncTask<String, String, String> {
    //members
    String text = null;
    String message = null , name , id , icon;
    Set<String> members = new HashSet<String>();
    public SharedPreferences allChannelsAndMembers_IdNames_SP;
    public SharedPreferences myChannels_Id_SP;
    Activity myActivity;

    /**
     * constructor
     * @param act the current activity
     */
    public GetNetwork(Activity act) {
        myActivity = act;
        allChannelsAndMembers_IdNames_SP = act.getSharedPreferences("AllChannelsAndMembers_IdNames_SP", Context.MODE_PRIVATE);
        myChannels_Id_SP = act.getSharedPreferences("MyChannels_Id_SP", Context.MODE_PRIVATE);
    }

    /**
     * connection in the buckground
     * @param params for the comunication
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
     * make post request for the server
     * @param result kye value
     */
    protected void onPostExecute(String result) {
        JSONObject obj = null;
        try {
            obj = new JSONObject(result);
            JSONArray channels = obj.getJSONArray("channels");
            JSONObject data;
            // pass over all the channels
            for (int i = 0; i < channels.length(); i++) {
                data = channels.getJSONObject(i);
                id = data.getString("id");
                JSONArray members = data.getJSONArray("members");
                // pass over all the members of a channel
                for (int j=0; j <members.length(); j++) {
                    String currentMember = members.getString(j);
                    this.members.add(currentMember);
                    // check if this nickname is my nickname
                    SharedPreferences  myAccount= myActivity.getSharedPreferences("MyAccount", Context.MODE_PRIVATE);
                    if((currentMember.equals(myAccount.getString("nickname", "-1"))) &&
                            (!currentMember.equals("-1"))){
                        SharedPreferences.Editor edit = myChannels_Id_SP.edit();
                        // add this channel id to MY channels shared pref'
                        edit.putString(id,"");
                        edit.commit();
                        // exit
                        j = members.length();
                    }
                }
                final String id = data.getString("id");
                SharedPreferences.Editor editor1 = allChannelsAndMembers_IdNames_SP.edit();
                editor1.putStringSet(id, this.members);
                editor1.commit();
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
