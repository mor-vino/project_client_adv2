package com.example.mor.final_project_client_adv2;

import android.app.Activity;

/**
 * Created by mor on 08/08/2015.
 */
public class GetUpdatesFromServer {
    private Activity myActivity;
    private String app_Id;

    public GetUpdatesFromServer (Activity act, String appId){
        myActivity = act;
        app_Id = appId;
    }
    public void syncAllUpdates(){
        syncMyChannelsUpdates();
        syncAllChannelsUpdates();
        syncAllChannelsMembersUpdates();
    }
    public void syncMyChannelsUpdates(){
        new GetMyChannels(myActivity).execute("http://" + app_Id + ".appspot.com/getMyChannels");
    }
    public void syncAllChannelsUpdates(){
        new GetAllChannels(myActivity).execute("http://" + app_Id + ".appspot.com/getChannels");
    }
    public void syncAllChannelsMembersUpdates(){
        new GetNetwork(myActivity).execute("http://" + app_Id + ".appspot.com/getNetwork");
    }


}
