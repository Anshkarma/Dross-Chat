package com.chat.server;
import com.chat.dl.dao.*;
import com.chat.dl.dto.*;
import com.chat.dl.interfaces.dao.*;
import com.chat.dl.interfaces.dto.*;
import com.chat.dl.exception.*;
import com.client.server.protocol.*;
import java.util.*;
import java.io.*;
import java.time.*;
public class ChatHandler implements RequestHandler
{
public String handleRequest(String splits[])
{
System.out.println("Chat Handler:Handler got called");
String cmd=splits[1];
if(cmd.toUpperCase().equals(Protocol.SEND))
{
if(splits.length!=5)
{
return Protocol.FAILURE+Protocol.SEPERATOR+"DAOException"+Protocol.SEPERATOR+"INSUFFICIENT DATA TO SEND MESSAGE";
}
int senderId;
int targetId;
try
{
senderId=Integer.parseInt(splits[2].trim());
if(senderId<=0)
{
return Protocol.FAILURE+Protocol.SEPERATOR+"Invalid sender ID ("+splits[2]+")";
}
}catch(NumberFormatException numberFormatException)
{
return Protocol.FAILURE+Protocol.SEPERATOR+"Invalid sender ID";
}
try
{
targetId=Integer.parseInt(splits[3].trim());
if(targetId<=0)
{
return Protocol.FAILURE+Protocol.SEPERATOR+"Invalid target ID ("+splits[3]+")";
}
}catch(NumberFormatException numberFormatException)
{
return Protocol.FAILURE+Protocol.SEPERATOR+"Invalid target ID";
}
String content=splits[4].trim();
if(content.length()==0)
{
return Protocol.FAILURE+Protocol.SEPERATOR+"Message cannot be empty";
}
if(senderId==targetId)
{
return Protocol.FAILURE+Protocol.SEPERATOR+"You cannot message yourself";
}
boolean isGroup=DrossChatServer.groupMap.containsKey(String.valueOf(targetId));
try
{
MessageDTOInterface messageDTO;
if(isGroup)
{
messageDTO=new MessageDTO(senderId,0,Instant.now(),targetId,content,false);
saveMessage(messageDTO);
Set<String> members=DrossChatServer.groupMap.get(String.valueOf(targetId));
String something=Protocol.SUCCESS+Protocol.SEPERATOR+"[GROUP:"+targetId+"] "+senderId+": "+content;
for(String member:members)
{
PrintWriter pw=DrossChatServer.clientMap.get(member);
if(pw!=null)
{
pw.println(Protocol.SUCCESS+Protocol.SEPERATOR+"Done");
pw.flush();
}
}
return Protocol.SUCCESS+Protocol.SEPERATOR+"Message sent to group "+targetId;
}
else
{
messageDTO=new MessageDTO(senderId,targetId,Instant.now(),0,content,false);
saveMessage(messageDTO);
PrintWriter pw=DrossChatServer.clientMap.get(String.valueOf(targetId));
if(pw==null)
{
return Protocol.FAILURE+Protocol.SEPERATOR+"User is not online";
}
String something=Protocol.SUCCESS+Protocol.SEPERATOR+"[PM:"+targetId+"] "+senderId+": "+content;
pw.println(something);
pw.flush();
return Protocol.SUCCESS+Protocol.SEPERATOR+"Message sent to user ";
}
}catch(DrossDAOException drossDAOException)
{
return Protocol.FAILURE+Protocol.SEPERATOR+drossDAOException.getMessage();
}
}
if(cmd.equals(Protocol.GET_MSGS))
{
try
{
LinkedList<MessageDTOInterface> messages=getMessagesBetweenUsers();
String responseData=Protocol.SUCCESS;
responseData+=Protocol.SEPERATOR;
ListIterator<MessageDTOInterface> iter=messages.listIterator();
MessageDTOInterface messageDTO;
while(iter.hasNext())
{
messageDTO=iter.next();
responseData+=messageDTO.getSenderId();
responseData+=Protocol.SEPERATOR;
responseData+=messageDTO.getReceiverId();
responseData+=Protocol.SEPERATOR;
responseData+=messageDTO.getContent();
responseData+=Protocol.SEPERATOR;
responseData+=messageDTO.getTimestamp();
responseData+=Protocol.SEPERATOR;
}
return responseData;
}catch(DrossDAOException drossDAOException)
{
return "EXCEPTION"+Protocol.SEPERATOR+drossDAOException.getMessage();
}
}
if(cmd.equals(Protocol.DELETE_MSG))
{
if(splits.length!=3)
{
return Protocol.FAILURE+Protocol.SEPERATOR+"Insufficient data to delete message";
}
int messageId;
try
{
messageId=Integer.parseInt(splits[2].trim());
if(messageId<=0)
{
return "Exception"+Protocol.SEPERATOR+"Invalid message id ("+splits[2]+")";
}
}catch(NumberFormatException numberFormatException)
{
return "EXCEPTION"+Protocol.SEPERATOR+"Invalid message id";
}
try
{
deleteMessage(messageId);
return Protocol.SUCCESS+Protocol.SEPERATOR+"Message deleted successfully";
}catch(DrossDAOException drossDAOException)
{
return "EXCEPTION"+Protocol.SEPERATOR+drossDAOException.getMessage();
}
}
return Protocol.FAILURE+Protocol.SEPERATOR+"Invalid operation";
}
public void saveMessage(MessageDTOInterface messageDTO) throws DrossDAOException
{
DrossDAOInterface ddao=new DrossDAO();
ddao.saveMessage(messageDTO);
}
public void deleteMessage(int messageID) throws DrossDAOException
{
DrossDAOInterface ddao=new DrossDAO();
ddao.deleteMessage(messageID);
}
public LinkedList<MessageDTOInterface> getMessagesBetweenUsers() throws DrossDAOException
{
DrossDAOInterface ddao=new DrossDAO();
return ddao.getMessagesBetweenUsers();
}
public LinkedList<MessageDTOInterface> getMessagesByGroup() throws DrossDAOException
{
DrossDAOInterface ddao=new DrossDAO();
return ddao.getMessagesByGroup();
}
}