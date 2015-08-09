package com.example.mor.final_project_client_adv2;

import android.app.Activity;

/**
 * Created by mor on 08/08/2015.
 * connect to the server
 */
public class GetUpdatesFromServer {
    private Activity myActivity;
    private String app_Id;

    /**
     * constructor
     * @param act current activity
     * @param appId the id of the app
     */
    public GetUpdatesFromServer (Activity act, String appId){
        myActivity = act;
        app_Id = appId;
    }

    /**
     * sync all the update
     */
    public void syncAllUpdates(){
        syncAllChannelsUpdates();
        syncAllChannelsMembersUpdates();
    }

    /**
     * sync all the channel updates
     */
    public void syncAllChannelsUpdates(){
        new GetAllChannels(myActivity).execute("http://" + app_Id + ".appspot.com/getChannels");
    }

    /**
     * sync the channel members
     */
    public void syncAllChannelsMembersUpdates(){
        new GetNetwork(myActivity).execute("http://" + app_Id + ".appspot.com/getNetwork");
    }


}
