package com.chat.dl.interfaces.dao;
import com.chat.dl.interfaces.dto.*;
import com.chat.dl.exception.*;
import java.util.LinkedList;
import java.time.Instant;
public interface DrossDAOInterface
{
void loginUser(LoginDTOInterface loginDTO) throws DrossDAOException;
void updateDetails(LoginDTOInterface loginDTO) throws DrossDAOException;
void deleteUser(int userID) throws DrossDAOException;
LoginDTOInterface getUserByID(String phoneNumber,String password) throws DrossDAOException;
LoginDTOInterface getUserByPhoneNumber(String phoneNumber) throws DrossDAOException;
LinkedList<LoginDTOInterface> getListOfUsers() throws DrossDAOException;
void resetPassword(int  userId,String newPassword) throws DrossDAOException;
void saveMessage(MessageDTOInterface messageDTO) throws DrossDAOException;
void deleteMessage(int messageID) throws DrossDAOException;
LinkedList<MessageDTOInterface> getMessagesBetweenUsers() throws DrossDAOException;
LinkedList<MessageDTOInterface> getMessagesByGroup() throws DrossDAOException;
LinkedList<MessageDTOInterface> getMessageByDate(Instant date) throws DrossDAOException;
void drossValidation(boolean isUpdate,MessageDTOInterface messageDTO) throws DrossDAOException;
void drossValidation(boolean isUpdate,LoginDTOInterface loginDTO) throws DrossDAOException;
boolean userIDExists(int userId) throws DrossDAOException;
boolean phoneNumberExists(String phoneNumber) throws DrossDAOException;
public int getUserIdByName(String name) throws DrossDAOException;
}
