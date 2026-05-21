package com.chat.dl.dao;
import com.chat.dl.interfaces.dto.*;
import com.chat.dl.interfaces.dao.*;
import com.chat.dl.exception.*;
import com.chat.dl.dto.*;
import java.util.*;
import java.time.Instant;
import java.sql.*;
public class DrossDAO implements DrossDAOInterface
{
Connection connection;
Statement statement;
PreparedStatement preparedStatement;
ResultSet resultSet;
public void loginUser(LoginDTOInterface loginDTO) throws DrossDAOException
{
try
{
connection=DAOConnection.getConnection();
String sql="insert into users(user_name,phone_number,password) values(?,?,?)";
preparedStatement=connection.prepareStatement(sql);
preparedStatement.setString(1,loginDTO.getUserName());
preparedStatement.setString(2,loginDTO.getPhoneNumber());
preparedStatement.setString(3,loginDTO.getPassword());
preparedStatement.executeUpdate();
preparedStatement.close();
connection.close();
}catch(Exception e)
{
e.printStackTrace();
throw new DrossDAOException(e.getMessage());
}
}
public void updateDetails(LoginDTOInterface loginDTO) throws DrossDAOException
{
try
{
connection=DAOConnection.getConnection();
String sql="update users set user_name=?,phone_number=? where user_id=?";
preparedStatement=connection.prepareStatement(sql);
preparedStatement.setString(1,loginDTO.getUserName());
preparedStatement.setString(2,loginDTO.getPhoneNumber());
preparedStatement.setInt(3,loginDTO.getUserId());
preparedStatement.executeUpdate();
preparedStatement.close();
connection.close();
}catch(Exception e)
{
e.printStackTrace();
throw new DrossDAOException(e.getMessage());
}
}
public void deleteUser(int userID) throws DrossDAOException
{
try
{
connection=DAOConnection.getConnection();
String sql="delete from users where user_id=?";
preparedStatement=connection.prepareStatement(sql);
preparedStatement.setInt(1,userID);
preparedStatement.executeUpdate();
preparedStatement.close();
connection.close();
}catch(Exception e)
{
e.printStackTrace();
throw new DrossDAOException(e.getMessage());
}
}
public LoginDTOInterface getUserByID(String phoneNumber,String password) throws DrossDAOException
{
try
{
connection=DAOConnection.getConnection();
String sql="select * from users where phone_number=? and password=?";
preparedStatement=connection.prepareStatement(sql);
preparedStatement.setString(1,phoneNumber);
preparedStatement.setString(2,password);
resultSet=preparedStatement.executeQuery();
LoginDTOInterface loginDTO=null;
if(resultSet.next())
{
loginDTO=new LoginDTO(
resultSet.getInt("user_id"),
resultSet.getString("user_name"),
resultSet.getString("phone_number"),
resultSet.getString("password")
);
}
resultSet.close();
preparedStatement.close();
connection.close();
return loginDTO;
}catch(Exception e)
{
e.printStackTrace();
throw new DrossDAOException(e.getMessage());
}
}
public LoginDTOInterface getUserByPhoneNumber(String phoneNumber) throws DrossDAOException
{
try
{
connection=DAOConnection.getConnection();
String sql="select * from users where phone_number=?";
preparedStatement=connection.prepareStatement(sql);
preparedStatement.setString(1,phoneNumber);
resultSet=preparedStatement.executeQuery();
LoginDTOInterface loginDTO=null;
if(resultSet.next())
{
loginDTO=new LoginDTO(
resultSet.getInt("user_id"),
resultSet.getString("user_name"),
resultSet.getString("phone_number"),
resultSet.getString("password")
);
}
resultSet.close();
preparedStatement.close();
connection.close();
return loginDTO;
}catch(Exception e)
{
e.printStackTrace();
throw new DrossDAOException(e.getMessage());
}
}
public LinkedList<LoginDTOInterface> getListOfUsers() throws DrossDAOException
{
try
{
connection=DAOConnection.getConnection();
String sql="select * from users";
statement=connection.createStatement();
resultSet=statement.executeQuery(sql);
LinkedList<LoginDTOInterface> users=new LinkedList<LoginDTOInterface>();
LoginDTOInterface loginDTO;
while(resultSet.next())
{
loginDTO=new LoginDTO(
resultSet.getInt("user_id"),
resultSet.getString("user_name"),
resultSet.getString("phone_number"),
resultSet.getString("password")
);
users.add(loginDTO);
}
resultSet.close();
statement.close();
connection.close();
return users;
}catch(Exception e)
{
e.printStackTrace();
throw new DrossDAOException(e.getMessage());
}
}
public void resetPassword(int userId,String newPassword) throws DrossDAOException
{
try
{
connection=DAOConnection.getConnection();
String sql="update users set password=? where user_id=?";
preparedStatement=connection.prepareStatement(sql);
preparedStatement.setString(1,newPassword);
preparedStatement.setInt(2,userId);
preparedStatement.executeUpdate();
preparedStatement.close();
connection.close();
}catch(Exception e)
{
e.printStackTrace();
throw new DrossDAOException(e.getMessage());
}
}
public void saveMessage(MessageDTOInterface messageDTO) throws DrossDAOException
{
try
{
connection=DAOConnection.getConnection();
String sql="insert into messages(sender_id,receiver_id,group_id,content,is_delivered) values(?,?,?,?,?)";
preparedStatement=connection.prepareStatement(sql);
preparedStatement.setInt(1,messageDTO.getSenderId());
if(messageDTO.getGroupId()==0)
{
preparedStatement.setInt(2,messageDTO.getReceiverId());
preparedStatement.setNull(3,java.sql.Types.INTEGER);
}
else
{
preparedStatement.setNull(2,java.sql.Types.INTEGER);
preparedStatement.setInt(3,messageDTO.getGroupId());
}
preparedStatement.setString(4,messageDTO.getContent());
preparedStatement.setBoolean(5,false);
preparedStatement.executeUpdate();
preparedStatement.close();
connection.close();
}catch(Exception e)
{
e.printStackTrace();
throw new DrossDAOException(e.getMessage());
}
}
public void deleteMessage(int messageID) throws DrossDAOException
{
try
{
connection=DAOConnection.getConnection();
String sql="delete from messages where message_id=?";
preparedStatement=connection.prepareStatement(sql);
preparedStatement.setInt(1,messageID);
preparedStatement.executeUpdate();
preparedStatement.close();
connection.close();
}catch(Exception e)
{
e.printStackTrace();
throw new DrossDAOException(e.getMessage());
}
}
public LinkedList<MessageDTOInterface> getMessagesBetweenUsers() throws DrossDAOException
{
try
{
connection=DAOConnection.getConnection();
String sql="select * from messages where group_id is null order by send_at asc";
statement=connection.createStatement();
resultSet=statement.executeQuery(sql);
LinkedList<MessageDTOInterface> messages=new LinkedList<MessageDTOInterface>();
MessageDTOInterface messageDTO;
while(resultSet.next())
{
messageDTO=new MessageDTO(
resultSet.getInt("sender_id"),
resultSet.getInt("receiver_id"),
resultSet.getTimestamp("send_at").toInstant(),
0,
resultSet.getString("content"),
resultSet.getBoolean("is_delivered")
);
messages.add(messageDTO);
}
resultSet.close();
statement.close();
connection.close();
return messages;
}catch(Exception e)
{
e.printStackTrace();
throw new DrossDAOException(e.getMessage());
}
}
public LinkedList<MessageDTOInterface> getMessagesByGroup() throws DrossDAOException
{
try
{
connection=DAOConnection.getConnection();
String sql="select * from messages where group_id is not null order by send_at asc";
statement=connection.createStatement();
resultSet=statement.executeQuery(sql);
LinkedList<MessageDTOInterface> messages=new LinkedList<MessageDTOInterface>();
MessageDTOInterface messageDTO;
while(resultSet.next())
{
messageDTO=new MessageDTO(
resultSet.getInt("sender_id"),
0,
resultSet.getTimestamp("send_at").toInstant(),
resultSet.getInt("group_id"),
resultSet.getString("content"),
resultSet.getBoolean("is_delivered")
);
messages.add(messageDTO);
}
resultSet.close();
statement.close();
connection.close();
return messages;
}catch(Exception e)
{
e.printStackTrace();
throw new DrossDAOException(e.getMessage());
}
}
public LinkedList<MessageDTOInterface> getMessageByDate(Instant date) throws DrossDAOException
{
try
{
connection=DAOConnection.getConnection();
String sql="select * from messages where send_at>=? order by send_at asc";
preparedStatement=connection.prepareStatement(sql);
preparedStatement.setTimestamp(1,Timestamp.from(date));
resultSet=preparedStatement.executeQuery();
LinkedList<MessageDTOInterface> messages=new LinkedList<MessageDTOInterface>();
MessageDTOInterface messageDTO;
while(resultSet.next())
{
messageDTO=new MessageDTO(
resultSet.getInt("sender_id"),
resultSet.getInt("receiver_id"),
resultSet.getTimestamp("send_at").toInstant(),
resultSet.getInt("group_id"),
resultSet.getString("content"),
resultSet.getBoolean("is_delivered")
);
messages.add(messageDTO);
}
resultSet.close();
preparedStatement.close();
connection.close();
return messages;
}catch(Exception e)
{
e.printStackTrace();
throw new DrossDAOException(e.getMessage());
}
}
//----------------------------------------------------------------
// phoneNumberExists — check if phone number exists
//----------------------------------------------------------------
public boolean phoneNumberExists(String phoneNumber) throws DrossDAOException
{
try
{
connection=DAOConnection.getConnection();
String sql="select count(*) from users where phone_number=?";
preparedStatement=connection.prepareStatement(sql);
preparedStatement.setString(1,phoneNumber);
resultSet=preparedStatement.executeQuery();
boolean exists=false;
if(resultSet.next())
{
exists=resultSet.getInt(1)>0;
}
resultSet.close();
preparedStatement.close();
connection.close();
return exists;
}catch(Exception e)
{
e.printStackTrace();
throw new DrossDAOException(e.getMessage());
}
}
//----------------------------------------------------------------
// userIDExists — check if user_id exists
//----------------------------------------------------------------
public boolean userIDExists(int userId) throws DrossDAOException
{
try
{
connection=DAOConnection.getConnection();
String sql="select count(*) from users where user_id=?";
preparedStatement=connection.prepareStatement(sql);
preparedStatement.setInt(1,userId);
resultSet=preparedStatement.executeQuery();
boolean exists=false;
if(resultSet.next())
{
exists=resultSet.getInt(1)>0;
}
resultSet.close();
preparedStatement.close();
connection.close();
return exists;
}catch(Exception e)
{
e.printStackTrace();
throw new DrossDAOException(e.getMessage());
}
}
//----------------------------------------------------------------
// drossValidation — validate MessageDTOInterface
//----------------------------------------------------------------
public void drossValidation(boolean isUpdate,MessageDTOInterface messageDTO) throws DrossDAOException
{
if(messageDTO==null)
{
throw new DrossDAOException("Message data cannot be null");
}
if(messageDTO.getSenderId()<=0)
{
throw new DrossDAOException("Invalid sender ID");
}
if(messageDTO.getContent()==null || messageDTO.getContent().trim().length()==0)
{
throw new DrossDAOException("Message content cannot be empty");
}
if(messageDTO.getReceiverId()==0 && messageDTO.getGroupId()==0)
{
throw new DrossDAOException("Receiver or group required");
}
}
//----------------------------------------------------------------
// drossValidation — validate LoginDTOInterface
//----------------------------------------------------------------
public void drossValidation(boolean isUpdate,LoginDTOInterface loginDTO) throws DrossDAOException
{
if(loginDTO==null)
{
throw new DrossDAOException("User data cannot be null");
}
if(loginDTO.getUserName()==null || loginDTO.getUserName().trim().length()==0)
{
throw new DrossDAOException("Username cannot be empty");
}
if(loginDTO.getUserName().trim().length()>50)
{
throw new DrossDAOException("Username cannot exceed 50 characters");
}
if(loginDTO.getPhoneNumber()==null || loginDTO.getPhoneNumber().trim().length()==0)
{
throw new DrossDAOException("Phone number cannot be empty");
}
if(!isUpdate)
{
if(loginDTO.getPassword()==null || loginDTO.getPassword().trim().length()<6)
{
throw new DrossDAOException("Password must be at least 6 characters");
}
}
}
}