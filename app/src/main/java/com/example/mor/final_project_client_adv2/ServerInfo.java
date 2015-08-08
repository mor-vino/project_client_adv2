package com.example.mor.final_project_client_adv2;

/**
 * Created by paz on 07/08/2015.
 */
public class ServerInfo {
    private String serverURL;
    private String serverName;
    public ServerInfo(String server){
        this.serverURL = server;
        if(serverURL.contains("http")) {
            this.serverName = serverURL.substring(7, serverURL.length() - 12);
        } else {
            this.serverName = serverURL.substring(0, serverURL.length() - 12);
        }

    }
    public void setServerURL(String newServer) {
        this.serverURL = newServer;
        if(serverURL.contains("http")) {
            this.serverName = serverURL.substring(7, serverURL.length() - 12);
        } else {
            this.serverName = serverURL.substring(0, serverURL.length() - 12);
        }
    }
    public String getServerURL() {
        return this.serverURL;
    }
    public String getServerName() {
        return this.serverName;
    }
    public void setServerName(String name) {
        this.serverName = name;
        this.serverURL = "http://" + name + ".appspot.com";

    }
}
