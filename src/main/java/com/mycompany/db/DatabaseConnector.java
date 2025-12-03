package com.mycompany.db;


import java.sql.*;
import static com.mycompany.extension.Extension.hashPassword;


/**
 *
 * @author wolff
 */
public class DatabaseConnector {

    protected Connection conn = null;
    
    public DatabaseConnector(){
        try{
            this.conn = DriverManager.getConnection("jdbc:mysql://localhost/ftm" ,"root", "");
            System.out.println("Connected to database!");
        } catch(SQLException e){
            System.out.println("Cannot connect to SQL");
            e.printStackTrace();
        }
    }
    
    public DatabaseConnector(String url, String user, String pwd){
        try{
            this.conn = DriverManager.getConnection(url, user, pwd);
            System.out.println("Connected to database!");
        } catch(SQLException e){
            System.out.println("Cannot connect to SQL");
            e.printStackTrace();
        }
    }
    
    public Connection getConn(){
        return this.conn;
    }
    
    public void closeConn(){
        try{
            if(this.conn != null && !this.conn.isClosed()){
                this.conn.close();
                System.out.println("Connection closed.");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    
    
    
  
    
    public static void main(String args[]) throws SQLException {
        DatabaseConnector newconn = new DatabaseConnector("jdbc:mysql://localhost/ftm" ,"root", "");
        
        if(newconn.getConn() != null){
            System.out.println("An object is connected successfully!");
        }
        //newconn.addUser("toan", "toan123", "admin", 1);
        //newconn.addUser("admin", "admin", "admin", 0);
        //newconn.printTable("users");
        
        newconn.closeConn();
    }

}
