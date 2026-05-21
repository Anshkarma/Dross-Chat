package com.chat.dl.dao;
public class DrossServerConfiguration
{
private String server;
private int port;
public DrossServerConfiguration()
{
this.server="";
this.port=0;
}
public DrossServerConfiguration(String server,int port)
{
this.server=server;
this.port=port;
}
public void setServer(String server)
{
this.server=server;
}
public void setPort(String Port)
{
this.port=port;
}
public String getServer()
{
return this.server;
}
public int getPort()
{
return this.port;
}
}