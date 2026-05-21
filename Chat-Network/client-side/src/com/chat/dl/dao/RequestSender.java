package com.chat.dl.dao;
import com.client.server.protocol.*;
import java.io.*;
import java.net.*;
import com.chat.dl.exception.*;
import com.chat.dl.dao.*;
public class RequestSender
{
public static String sendRequest(String requestData)
{
requestData+=Protocol.TERMINATOR;
try
{
Socket socket=DrossServerConnection.connect();
OutputStream outputStream=socket.getOutputStream();
OutputStreamWriter outputStreamWriter=new OutputStreamWriter(outputStream);
outputStreamWriter.write(requestData);
outputStreamWriter.flush();
System.out.println(requestData+" sent");
InputStream inputStream=socket.getInputStream();
InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
StringBuffer stringBuffer=new StringBuffer();
int lengthOfTerminator=Protocol.TERMINATOR.length();
int element;
int i=0;
while(true)
{
element=inputStreamReader.read();
if(element==-1) break;
stringBuffer.append((char)element);
if(stringBuffer.length()>=lengthOfTerminator)
{
String ep=stringBuffer.substring(stringBuffer.length()-lengthOfTerminator);
if(stringBuffer.equals(Protocol.TERMINATOR)) break;
}
}
String responseData=stringBuffer.toString();
inputStreamReader.close();
socket.close();
System.out.println(responseData);
return responseData;
}catch(Exception exception)
{
return "EXCEPTION"+Protocol.SEPERATOR+exception.getMessage();
}
}
}