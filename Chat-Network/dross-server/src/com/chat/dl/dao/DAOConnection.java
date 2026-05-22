package com.chat.dl.dao;
import java.sql.*;
public class DAOConnection
{
private static final String URL = System.getenv().getOrDefault("DB_URL", "jdbc:mysql://10.49.199.25:3306/dross_db");
private static final String USERNAME = System.getenv().getOrDefault("DB_USER", "aadesh_san");
private static final String PASSWORD = System.getenv().getOrDefault("DB_PASSWORD", "2421as");
static
{
try
{
Class.forName("com.mysql.cj.jdbc.Driver");
}catch(ClassNotFoundException cnfe)
{
System.out.println("Driver not found: "+cnfe.getMessage());
System.exit(0);
}
}
public static Connection getConnection() throws SQLException
{
return DriverManager.getConnection(URL,USERNAME,PASSWORD);
}
}
