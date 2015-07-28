package com.example.mor.final_project_client_adv2;


import android.view.View;

public class ChatItem {
    private int icon;
    private String title;
    View.OnClickListener listener;

    public ChatItem(String title) {
        this.title = title;
        this.icon = 0;
        this.listener = null;
    }
    public ChatItem(int icon, String title, View.OnClickListener listener) {
        this.icon = icon;
        this.listener = listener;
        this.title = title;
    }

    public int getIcon() { return this.icon; }

    public String getTitle() { return this.title; }

    public View.OnClickListener getListener() { return this.listener;}

}
