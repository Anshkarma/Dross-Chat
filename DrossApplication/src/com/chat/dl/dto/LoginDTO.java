package com.chat.dl.dto;
import com.chat.dl.interfaces.dto.*;
import com.chat.dl.exception.*;

public class LoginDTO implements LoginDTOInterface
{
private int userId;
private String userName;
private String phoneNumber;
private String password;

public LoginDTO()
{
this.userId=0;
this.userName="";
this.phoneNumber="";
this.password="";
}

public LoginDTO(int userId,String userName,String phoneNumber,String password)
{
this.userId=userId;
this.userName=userName;
this.phoneNumber=phoneNumber;
this.password=password;
}

public void setUserId(int userId)
{
this.userId=userId;
}
public int getUserId()
{
return this.userId;
}
public void setPhoneNumber(String phoneNumber)
{
this.phoneNumber=phoneNumber;
}
public String getPhoneNumber()
{
return this.phoneNumber;
}
public void setUserName(String userName)
{
this.userName=userName;
}
public String getUserName()
{
return this.userName;
}
public void setPassword(String Password)
{
this.password=password;
}
public String getPassword()
{
return this.password;
}
}