package com.chat.server;
import com.chat.dl.dao.*;
import com.chat.dl.dto.*;
import com.chat.dl.interfaces.dao.*;
import com.chat.dl.interfaces.dto.*;
import com.chat.dl.exception.*;
import com.client.server.protocol.*;
import java.util.*;
public class UserHandler implements RequestHandler
{
public String handleRequest(String splits[])
{
System.out.println("User Handler : Handler got called");
String cmd=splits[1];
if(cmd.equals(Protocol.LOGIN))
{
if(splits.length!=4)
{
return Protocol.FAILURE+Protocol.SEPERATOR+"DAOException"+Protocol.SEPERATOR+"INSUFFICIENT DATA TO Login";
}
String phoneNumber=splits[2].trim();
String password=splits[3].trim();
if(phoneNumber.length()==0)
{
return Protocol.FAILURE+Protocol.SEPERATOR+"DAOException"+Protocol.SEPERATOR+"Phone number cannot be null";
}
if(password.length()==0)
{
return Protocol.FAILURE+Protocol.SEPERATOR+"DAOException"+Protocol.SEPERATOR+"password cannot be null";
}
try
{
LoginDTOInterface loginDTO=getUserByPhoneNumber(phoneNumber);
if(loginDTO==null)
{
return Protocol.FAILURE+Protocol.SEPERATOR+"User not found";
}
if(!loginDTO.getPassword().equals(password))
{
return Protocol.FAILURE+Protocol.SEPERATOR+"Invalid password";
}
String responseData=Protocol.SUCCESS;
responseData+=Protocol.SEPERATOR;
responseData+=loginDTO.getUserId();
responseData+=Protocol.SEPERATOR;
responseData+=loginDTO.getUserName();
responseData+=Protocol.SEPERATOR;
responseData+=loginDTO.getPhoneNumber();
responseData+=Protocol.SEPERATOR;
return responseData;
}catch(DrossDAOException drossDAOException)
{
return Protocol.FAILURE+Protocol.SEPERATOR+drossDAOException.getMessage();
}
}
if(cmd.equals(Protocol.REGISTER))
{
if(splits.length!=5)
{
return Protocol.FAILURE+Protocol.SEPERATOR+"DAOException"+Protocol.SEPERATOR+"INSUFFICIENT DATA TO Login";
}
String userName=splits[2].trim();
String phoneNumber=splits[3].trim();
String password=splits[4].trim();
if(userName.length()==0 || userName.length()>50) return Protocol.FAILURE+Protocol.SEPERATOR+"Username between 1 and 50 characters";
if(phoneNumber.length()==0) return Protocol.FAILURE+Protocol.SEPERATOR+"Phone number required";
if(password.length()<6) return Protocol.FAILURE+Protocol.SEPERATOR+"password must 6 character or nuemeric";
try
{
boolean exists=phoneNumberExists(phoneNumber);
if(exists)
{
return Protocol.FAILURE+Protocol.SEPERATOR+"this phone number already exists";
}
LoginDTOInterface loginDTO=new LoginDTO(0,userName,phoneNumber,password);
loginUser(loginDTO);
return Protocol.SUCCESS+Protocol.SEPERATOR+"User Registerd";
}catch(DrossDAOException drossDAOException)
{
return Protocol.FAILURE+Protocol.SEPERATOR+drossDAOException.getMessage();
}
}
if(cmd.equals(Protocol.UPDATE))
{
if(splits.length!=5)
{
return Protocol.FAILURE+Protocol.SEPERATOR+"DAOException"+Protocol.SEPERATOR+"INSUFFICIENT DATA TO Update";
}
int userId;
try
{
userId=Integer.parseInt(splits[2].trim());
if(userId<=0) return Protocol.FAILURE+Protocol.SEPERATOR+"Invalid user id";
}catch(NumberFormatException numberFormatException)
{
return Protocol.FAILURE+Protocol.SEPERATOR+"Invalid userId";
}
String userName=splits[3].trim();
String phoneNumber=splits[4].trim();
if(userName.length()==0 || userName.length()>50) return Protocol.FAILURE+Protocol.SEPERATOR+"Username between 1 and 50 characters";
if(phoneNumber.length()==0) return Protocol.FAILURE+Protocol.SEPERATOR+"Phone number required";
LoginDTO loginDTO=new LoginDTO(userId,userName,phoneNumber,"");
try
{
updateDetails(loginDTO);
return Protocol.SUCCESS+Protocol.SEPERATOR+"User Updated";
}catch(DrossDAOException drossDAOException)
{
return Protocol.FAILURE+Protocol.SEPERATOR+drossDAOException.getMessage();
}
}
if(cmd.equals(Protocol.DELETE))
{
if(splits.length!=3)
{
return Protocol.FAILURE+Protocol.SEPERATOR+"DAOException"+Protocol.SEPERATOR+"INSUFFICIENT DATA TO Update";
}
int userId;
try
{
userId=Integer.parseInt(splits[2].trim());
if(userId<=0) return Protocol.FAILURE+Protocol.SEPERATOR+"Invalid user id";
}catch(NumberFormatException numberFormatException)
{
return Protocol.FAILURE+Protocol.SEPERATOR+"Invalid userId";
}
try
{
boolean exists=userIdExists(userId);
if(!exists) return Protocol.FAILURE+Protocol.SEPERATOR+"User id does not exists";
deleteUser(userId);
return Protocol.SUCCESS+Protocol.SEPERATOR+"User deleted";
}catch(DrossDAOException drossDAOException)
{
return Protocol.FAILURE+Protocol.SEPERATOR+drossDAOException.getMessage();
}
}
if(cmd.equals(Protocol.RESET_PASS))
{
if(splits.length!=4)
{
return Protocol.FAILURE+Protocol.SEPERATOR+"DAOException"+Protocol.SEPERATOR+"INSUFFICIENT DATA TO RESET PASSWORD";
}
int userId;
try
{
userId=Integer.parseInt(splits[2].trim());
if(userId<=0) return Protocol.FAILURE+Protocol.SEPERATOR+"Invalid user id";
}catch(NumberFormatException numberFormatException)
{
return Protocol.FAILURE+Protocol.SEPERATOR+"Invalid userId";
}
String newPassword=splits[3].trim();
if(newPassword.length()<6) return Protocol.FAILURE+Protocol.SEPERATOR+"new password must 6 cheracter and neumeric";
try
{
resetPassword(userId,newPassword);
return Protocol.SUCCESS+Protocol.SEPERATOR+"password updated";
}catch(DrossDAOException drossDAOException)
{
return Protocol.FAILURE+Protocol.SEPERATOR+drossDAOException.getMessage();
}
}
if(cmd.equals(Protocol.GET_USERS))
{
try
{
LoginDTOInterface loginDTO;
LinkedList<LoginDTOInterface> users=getListOfUsers();
String responseData=Protocol.SUCCESS;
responseData+=Protocol.SEPERATOR;
ListIterator<LoginDTOInterface> iter=users.listIterator();
while(iter.hasNext())
{
loginDTO=iter.next();
responseData+=loginDTO.getUserId();
responseData+=Protocol.SEPERATOR;
responseData+=loginDTO.getUserName();
responseData+=Protocol.SEPERATOR;
responseData+=loginDTO.getPhoneNumber();
responseData+=Protocol.SEPERATOR;
}
return responseData;
}catch(DrossDAOException drossDAOException)
{
return Protocol.FAILURE+Protocol.SEPERATOR+drossDAOException.getMessage();
}
}
return Protocol.FAILURE+Protocol.SEPERATOR+"Invalid operation";
}
public void loginUser(LoginDTOInterface loginDTO) throws DrossDAOException
{
DrossDAOInterface ddao=new DrossDAO();
ddao.loginUser(loginDTO);
}
public LoginDTOInterface getUserByPhoneNumber(String phoneNumber) throws DrossDAOException
{
DrossDAOInterface ddao=new DrossDAO();
return ddao.getUserByPhoneNumber(phoneNumber);
}
public void updateDetails(LoginDTOInterface loginDTO) throws DrossDAOException
{
DrossDAOInterface ddao=new DrossDAO();
ddao.updateDetails(loginDTO);
}
public void deleteUser(int userId) throws DrossDAOException
{
DrossDAOInterface ddao=new DrossDAO();
ddao.deleteUser(userId);
}
public void resetPassword(int userId,String newPassword) throws DrossDAOException
{
DrossDAOInterface ddao=new DrossDAO();
ddao.resetPassword(userId,newPassword);
}
public LinkedList<LoginDTOInterface> getListOfUsers() throws DrossDAOException
{
DrossDAOInterface ddao=new DrossDAO();
return ddao.getListOfUsers();
}
public boolean userIdExists(int userId) throws DrossDAOException
{
DrossDAOInterface ddao=new DrossDAO();
return ddao.userIDExists(userId);
}
public boolean phoneNumberExists(String phoneNumber) throws DrossDAOException
{
DrossDAOInterface ddao=new DrossDAO();
return ddao.phoneNumberExists(phoneNumber);
}
}