package com.mycompany.service;


import com.mycompany.access.UserAccess;
import static com.mycompany.extension.Extension.hashPassword;
import java.sql.*;
import com.mycompany.model.User;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author wolff
 */
public class LoginService {
    protected User currentUser;
    protected Connection conn;
    
    
    public LoginService(Connection conn){
        this.currentUser = null;
        this.conn = conn;
    }
    public LoginService(Connection conn, String username, String pwd){
        if(this.currentUser != null){
            System.out.println("Already login!");
            return;
        }
        this.conn = conn;
        UserAccess acc = new UserAccess(conn);
        User usr = acc.findUserWithPwd(username, hashPassword(pwd));
        if(usr != null){
            System.out.println("Login Successfully!");
            this.currentUser = usr;
            UserSession.setCurrentUser(usr);
        } else {
            System.out.println("Login failed!");
        }
    }
    
    public void login(String username, String pwd){
        if(this.currentUser != null){
            System.out.println("Already login!");
            return;
        }
        UserAccess acc = new UserAccess(conn);
        User usr = acc.findUserWithPwd(username, hashPassword(pwd));
        if(usr != null){
            System.out.println("Login Successfully!");
            this.currentUser = usr;
        } else {
            System.out.println("Login failed!");
        }
    }
    
    public boolean isLogin(){
        return currentUser != null;
    }
    
    public String userFunc(){
        String role = currentUser.roleUser();
        if(role.equals("admin")){
            return "admin";
        } else if(role.equals("user")){
            return "user";
        }
        return null;
    }
    
    public User getCurrentUser(){
        return this.currentUser;
    }
    
    public void logOut(){
        if(this.currentUser == null){
            System.out.println("Must login first!");
            return;
        }
        this.currentUser = null;
        UserSession.clear();
    }
    

}
