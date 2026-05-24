package com.chat.dl.interfaces.dto;
public interface MessageDTOInterface extends java.io.Serializable
{
public void setSenderId(int senderId);
public int getSenderId();
public void setReceiverId(int receiverid);
public int getReceiverId();
public void setTimestamp(java.time.Instant time);
public java.time.Instant getTimestamp();
public void setGroupId(int groupId);
public int getGroupId();
public void setContent(String content);
public String getContent();
public void setIsDelivered(boolean isDelivered);
public boolean getIsDelivered();
public String getSenderName();
}
