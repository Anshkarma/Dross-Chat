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

if(cmd.toUpperCase().equals(Protocol.SEND))
{
if(splits.length!=5)
{
return Protocol.FAILURE+Protocol.SEPERATOR+"INSUFFICIENT DATA TO SEND MESSAGE";
}
String senderName=splits[2].trim();
String receiverName=splits[3].trim();
String content=splits[4].trim();
if(senderName.length()==0)
{
return Protocol.FAILURE+Protocol.SEPERATOR+"Invalid sender name";
}
if(receiverName.length()==0)
{
return Protocol.FAILURE+Protocol.SEPERATOR+"Invalid receiver name";
}
if(content.length()==0)
{
return Protocol.FAILURE+Protocol.SEPERATOR+"Message cannot be empty";
}
if(senderName.equals(receiverName))
{
return Protocol.FAILURE+Protocol.SEPERATOR+"You cannot message yourself";
}
try
{
DrossDAOInterface ddao=new DrossDAO();
int senderId=ddao.getUserIdByName(senderName);
int receiverId=ddao.getUserIdByName(receiverName);
if(senderId==-1)
{
return Protocol.FAILURE+Protocol.SEPERATOR+"Sender not found: "+senderName;
}
if(receiverId==-1)
{
return Protocol.FAILURE+Protocol.SEPERATOR+"Receiver not found: "+receiverName;
}
MessageDTOInterface messageDTO=new MessageDTO(senderId,receiverId,Instant.now(),0,content,false);
saveMessage(messageDTO);
PrintWriter pw=DrossChatServer.clientMap.get(receiverName);
if(pw!=null)
{
pw.println(Protocol.SUCCESS+Protocol.SEPERATOR+"[PM:"+senderName+"] "+content);
pw.flush();
}
return Protocol.SUCCESS+Protocol.SEPERATOR+"Message sent";
}catch(DrossDAOException drossDAOException)
{
return Protocol.FAILURE+Protocol.SEPERATOR+drossDAOException.getMessage();
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
