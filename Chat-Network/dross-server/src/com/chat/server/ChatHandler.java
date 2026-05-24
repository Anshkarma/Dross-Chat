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
