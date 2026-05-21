package com.chat.dl.dao;
import com.chat.dl.dao.*;
import java.io.*;
import java.net.*;
import com.chat.dl.exception.*;
public class DrossServerConnection
{
public DrossServerConnection()
{
}
private static DrossServerConfiguration drossServerConfiguration;
static
{
try
{
File file=new File("dross.cfg");
if(file.exists()==false) throw new DrossServerConfigurationException("file does not found");
String portString=null;
String server=null;
String line=null;
String splits[];
try(RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw"))
{
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
line=randomAccessFile.readLine();
splits=line.split("=");
if(splits.length==2)
{
if(splits[0].equals("port")) portString=splits[1];
else if(splits[0].equals("server")) server=splits[1];
}
}
if(server==null) throw new DrossServerConfigurationException("Server does not found");
else if(portString==null) throw new DrossServerConfigurationException("Port does not found");
int port=Integer.parseInt(portString);
if(port<1 || port>65535) throw new NumberFormatException("Invalid port number");
drossServerConfiguration=new DrossServerConfiguration(server,port);
}catch(NumberFormatException numberFormatException)
{
System.out.println(numberFormatException.getMessage());
}
}catch(Throwable throwable)
{
System.out.println(throwable.getMessage());
System.exit(0);
}
}
public static Socket connect()
{
Socket socket=null;
try
{
socket=new Socket(drossServerConfiguration.getServer(),drossServerConfiguration.getPort());
}catch(Exception exception)
{
System.out.println(exception.getMessage());
System.exit(0);
}
return socket;
}
}