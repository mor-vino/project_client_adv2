package com.example.mor.final_project_client_adv2;

/**
 * Created by paz on 07/08/2015.
 * class to contain info on the server
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

    /**
     * set the server url
     * @param newServer the url of the new server
     */
    public void setServerURL(String newServer) {
        this.serverURL = newServer;
        if(serverURL.contains("http")) {
            this.serverName = serverURL.substring(7, serverURL.length() - 12);
        } else {
            this.serverName = serverURL.substring(0, serverURL.length() - 12);
        }
    }

    /**
     * @return the url
     */
    public String getServerURL() {
        return this.serverURL;
    }

    /**
     * @return the name
     */
    public String getServerName() {
        return this.serverName;
    }

    /**
     * @param name of the server
     */
    public void setServerName(String name) {
        this.serverName = name;
        this.serverURL = "http://" + name + ".appspot.com";

    }
}
