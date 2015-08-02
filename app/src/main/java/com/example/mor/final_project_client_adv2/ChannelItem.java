package com.example.mor.final_project_client_adv2;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;

import java.io.Serializable;


public class ChannelItem implements Serializable {
    private int icon;
    private String title;
    View.OnClickListener listener;

    public ChannelItem(String name, String title, int icon, View.OnClickListener listener) {
        this.icon = icon;
        this.listener = listener;
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public View.OnClickListener getListener() {
        return listener;
    }
}
