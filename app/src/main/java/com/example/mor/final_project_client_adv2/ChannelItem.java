package com.example.mor.final_project_client_adv2;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;

import java.io.Serializable;


public class ChannelItem implements Serializable {
    private String id;
    private String name;
    private String icon;
    View.OnClickListener listener;

    public ChannelItem(String id, String name,String icon, View.OnClickListener listener) {
        this.listener = listener;
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public String getName(){
        return name;
    }

    public String getID() {
        return id;
    }

    public View.OnClickListener getListener() {
        return listener;
    }
}
