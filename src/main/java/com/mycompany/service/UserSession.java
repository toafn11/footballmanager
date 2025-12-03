package com.mycompany.service;

import com.mycompany.model.User;

public class UserSession {
    private static User currentUser;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }
    
    public static int getMyTeamId(){
        return currentUser.teamId();
    }
    
    public static void clear() {
        currentUser = null;
    }

    public static boolean isAdmin() {
        return currentUser != null && "admin".equals(currentUser.roleUser());
    }
    
    public static boolean isLoggedIn() {
        return currentUser != null;
    }
    
    public static String getUserName() {
        return currentUser.nameUser();
    }
    
    public static String getRole() {
        return currentUser.roleUser();
    }
}