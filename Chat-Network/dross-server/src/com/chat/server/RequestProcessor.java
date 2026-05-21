package com.chat.server;
import java.net.*;
import java.io.*;
import java.util.*;
import com.client.server.protocol.*;
public class RequestProcessor extends Thread
{
static Map<String,Class> modules=new HashMap<String,Class>();
private Socket socket;
static
{
try
{
modules.put(Protocol.MODULE_USER, Class.forName("com.chat.server.UserHandler"));
modules.put(Protocol.MODULE_CHAT, Class.forName("com.chat.server.ChatHandler"));
}catch(Exception exception)
{
System.out.println(exception.getMessage());
System.exit(0);
}
}
RequestProcessor(Socket socket)
{
this.socket=socket;
start();
}
public void run()
{
try
{
InputStream inputStream=socket.getInputStream();
InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
StringBuffer stringBuffer=new StringBuffer();
int element;
int lengthOfTerminator=Protocol.TERMINATOR.length();
String endPart;
while(true)
{
element=inputStreamReader.read();
if(element==-1) break;
stringBuffer.append((char)element);
System.out.println(element);
if(stringBuffer.length()>=lengthOfTerminator)
{
endPart=stringBuffer.substring(stringBuffer.length()-lengthOfTerminator);
System.out.println(endPart);
if(endPart.equals(Protocol.TERMINATOR)) break;
}
}
String requestData=stringBuffer.toString();
System.out.println("[mil gaya]"+requestData);
if(requestData==null) requestData="";
requestData=requestData.trim();
String splits[]=requestData.split(java.util.regex.Pattern.quote(Protocol.SEPERATOR));;
System.out.println("{"+requestData+"}");
int lastIndexOfRequstData=splits.length-1;
splits[lastIndexOfRequstData]=splits[lastIndexOfRequstData].substring(0,splits[lastIndexOfRequstData].length()-lengthOfTerminator);
String modulesName=splits[0];
System.out.println("["+modulesName+"]");
Class c=modules.get(modulesName);
if(c==null)
{
System.out.println("Invalid modules name");
return;
}
RequestHandler requestHandler=(RequestHandler)c.getDeclaredConstructor().newInstance();
String responseData=requestHandler.handleRequest(splits);
responseData+=Protocol.TERMINATOR;
OutputStream outputStream=socket.getOutputStream();
OutputStreamWriter outputStreamWriter=new OutputStreamWriter(outputStream);
outputStreamWriter.write(responseData);
outputStreamWriter.flush();
inputStream.close();
outputStream.close();
}catch(Exception exception)
{
System.out.println(exception.getMessage());
}
}
}