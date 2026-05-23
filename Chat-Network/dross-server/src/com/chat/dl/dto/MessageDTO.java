package com.chat.dl.dto;
import com.chat.dl.interfaces.dto.*;
import com.chat.dl.exception.*;
import java.time.Instant;
public class MessageDTO implements MessageDTOInterface
{
private int senderId;
private int recieverId;
private java.time.Instant time;
private int groupId;
private String content;
private boolean isDelivered;
private String senderName;
public MessageDTO()
{
this.senderId=0;
this.recieverId=0;
this.time=Instant.now();
this.groupId=0;
this.content="";
this.isDelivered=false;
}

public MessageDTO(int senderId,int recieverId,java.time.Instant time,int groupId,String content,boolean isDelivered)
{
this.senderId=senderId;
this.recieverId=recieverId;
this.time=time;
this.groupId=groupId;
this.content=content;
this.isDelivered=isDelivered;
}

public void setSenderId(int senderId)
{
this.senderId=senderId;
}
public int getSenderId()
{
return this.senderId;
}
public void setSenderName(int senderName)
{
this.senderName=senderName;
}
public int getSenderId()
{
return this.senderName;
}
public void setReceiverId(int recieverId)
{
this.recieverId=recieverId;
}
public int getReceiverId()
{
return this.recieverId;
}
public void setGroupId(int groupId)
{
this.groupId=groupId;
}
public int getGroupId()
{
return this.groupId;
}
public void setTimestamp(java.time.Instant time)
{
this.time=time;
}
public java.time.Instant getTimestamp()
{
return this.time;
}
public void setContent(String content)
{
this.content=content;
}
public String getContent()
{
return this.content;
}
public void setIsDelivered(boolean isDelivered)
{
this.isDelivered=isDelivered;
}
public boolean getIsDelivered()
{
return this.isDelivered;
}

}
