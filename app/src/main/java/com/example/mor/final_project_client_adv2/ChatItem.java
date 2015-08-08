package com.example.mor.final_project_client_adv2;


import android.view.View;

/**
 * chat item to enter the chat adapter
 */
public class ChatItem {
    //members
    private int icon;
    private String title;
    View.OnClickListener listener;

    /**
     * constructor
     * @param title of the chat
     */
    public ChatItem(String title) {
        this.title = title;
        this.icon = 0;
        this.listener = null;
    }

    /**
     * constructor
     * @param icon of the channel
     * @param title of the chat
     * @param listener to the channel
     */
    public ChatItem(int icon, String title, View.OnClickListener listener) {
        this.icon = icon;
        this.listener = listener;
        this.title = title;
    }

    /**
     *
     * @return the icon
     */
    public int getIcon() { return this.icon; }

    /**
     *
     * @return the title
     */
    public String getTitle() { return this.title; }

    /**
     *
     * @return the listener
     */
    public View.OnClickListener getListener() { return this.listener;}

}
