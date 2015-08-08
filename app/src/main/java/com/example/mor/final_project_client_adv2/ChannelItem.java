package com.example.mor.final_project_client_adv2;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;

import java.io.Serializable;

/**
 * class that pressent the channel
 */
public class ChannelItem implements Serializable {
    //members of the channel
    private String id;
    private String name;
    private String icon;
    View.OnClickListener listener;

    /**
     * constructor
     * @param id of the channel
     * @param name of the channel
     * @param icon (simple)
     * @param listener to other channels
     */
    public ChannelItem(String id, String name,String icon, View.OnClickListener listener) {
        this.listener = listener;
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    /**
     *
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     *
     * @return the channel name
     */
    public String getName(){
        return name;
    }

    /**
     *
     * @return the id of the channel
     */
    public String getID() {
        return id;
    }

    /**
     *
     * @return the listener
     */
    public View.OnClickListener getListener() {
        return listener;
    }
}
