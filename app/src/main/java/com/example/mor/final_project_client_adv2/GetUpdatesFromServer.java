package com.example.mor.final_project_client_adv2;

import android.app.Activity;

/**
 * Created by mor on 08/08/2015.
 * get updates from the server
 */
public class GetUpdatesFromServer {
    //members
    private Activity myActivity;
    private String app_Id;

    /**
     * constructor
     * @param act the current activity
     * @param appId the id of the app
     */
    public GetUpdatesFromServer (Activity act, String appId){
        myActivity = act;
        app_Id = appId;
    }

    /**
     * syncronize the params
     */
    public void syncAllUpdates(){
        syncMyChannelsUpdates();
        syncAllChannelsUpdates();
        syncAllChannelsMembersUpdates();
    }

    /**
     * sync the current channels from the server
     */
    public void syncMyChannelsUpdates(){
        new GetMyChannels(myActivity).execute("http://" + app_Id + ".appspot.com/getMyChannels");
    }

    /**
     * sync all the cahnnels from the server
     */
    public void syncAllChannelsUpdates(){
        new GetAllChannels(myActivity).execute("http://" + app_Id + ".appspot.com/getChannels");
    }

    /**
     * sync all my channels from the server
     */
    public void syncAllChannelsMembersUpdates(){
        new GetNetwork(myActivity).execute("http://" + app_Id + ".appspot.com/getNetwork");
    }


}
