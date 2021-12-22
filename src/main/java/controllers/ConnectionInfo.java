package controllers;

public class ConnectionInfo
{
    public String uri;
    public String user;
    public String password;

    public ConnectionInfo(String uri, String user, String password)
    {
        this.uri = uri;
        this.user = user;
        this.password = password;
    }
}
