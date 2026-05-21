package com.chat.server;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
public class DrossChatServer
{
volatile private boolean keepRunning;
private ServerSocket serverSocket;
static private int port=Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
private List<RequestProcessor> clientRequestProcessor;
static ConcurrentHashMap<String,PrintWriter> clientMap=new ConcurrentHashMap<>();
static ConcurrentHashMap<String,Set<String>> groupMap=new ConcurrentHashMap<>();
public DrossChatServer()
{
try
{
this.serverSocket=new ServerSocket(port);
this.clientRequestProcessor=new ArrayList<RequestProcessor>();
}catch(Exception exception)
{
System.out.println(exception.getMessage());
}
}

public void startServer()
{
try
{
Socket socket;
RequestProcessor requestProcessor;
this.keepRunning=true;
while(keepRunning)
{
try
{
socket=serverSocket.accept();
requestProcessor=new RequestProcessor(socket);
clientRequestProcessor.add(requestProcessor);
}catch(Exception exception)
{
System.out.println(exception.getMessage());
}
}
}catch(Exception exception)
{
System.out.println(exception.getMessage());
System.exit(0);
}
}
static public int getPort()
{
return port;
}
public static void main(String as[])
{
try
{
DrossChatServer drossChatServer=new DrossChatServer();
System.out.println("Dross chat server started on port : "+DrossChatServer.getPort());
drossChatServer.startServer();
}catch(Exception exception)
{
System.out.println(exception);
}
}
}