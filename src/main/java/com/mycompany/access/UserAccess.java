/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.access;
import com.mycompany.db.DatabaseConnector;
import com.mycompany.extension.Extension;
import static com.mycompany.extension.Extension.hashPassword;
import com.mycompany.model.User;
import java.sql.*;
/**
 *
 * @author wolff
 */
public class UserAccess {
    private Connection conn;
    public UserAccess(Connection conn){
        this.conn = conn;
    }
    public User findUserWithPwd(String username, String hashpwd){
        String querry = "SELECT id, name_user, role_user, team_id FROM users WHERE name_user = ? AND hashedpwd = ?";
        try(PreparedStatement pStm = conn.prepareStatement(querry)){
            
            pStm.setString(1, username);
            pStm.setString(2, hashpwd);
            
            try(ResultSet rs = pStm.executeQuery()){
                if(rs.next()) {
                    return new User(
                        rs.getInt("id"),
                        rs.getString("name_user"),
                        rs.getString("role_user"),
                        rs.getInt("team_id")
                    );
                }
            }
        } catch(SQLException e){
            System.out.println("Error with User access to database!" + e.getMessage());
        }
        return null;
    }
    
    public void addUser(String user_name, String pwd, String role_user, int team_id) throws SQLException{
        
        PreparedStatement pStm = this.conn.prepareStatement("INSERT INTO users(name_user, hashedpwd, role_user, team_id) "
                    + "values(?,?,?,?);");
        pStm.setString(1, user_name);
        pStm.setString(2, hashPassword(pwd));
        pStm.setString(3, role_user);
        if(role_user.equals("admin")){
            pStm.setNull(4, Types.INTEGER);
        } else if(role_user.equals("user")){
            pStm.setInt(4, team_id);
        } else {
            System.out.println("Invalid user role " + role_user + "!");
            return;
        }

        try{
            pStm.executeUpdate();
            System.out.println("Added User!");
        } catch(SQLException e){
            e.printStackTrace();
        }
 
    }
    
    
    public static void printTable(Connection conn, String tableName) {
        String sql = "SELECT * FROM " + tableName;

        System.out.println("--- Table: " + tableName + " ---");

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            for (int i = 1; i <= columnCount; i++) {
                System.out.print("[" + metaData.getColumnName(i) + "]|");
            }
            System.out.println("\n----------------------------------------");

            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(rs.getString(i) + "|");
                }
                System.out.println(); 
            }
        } catch (SQLException e) {
            System.out.println("Cannot access table " + tableName + ": " + e.getMessage());
        }
        System.out.println("--- End: " + tableName + " ---");
        System.out.println(); 
    }
    
    public static void main(String[] args) throws SQLException {
        DatabaseConnector dbconn = new DatabaseConnector("jdbc:mysql://localhost/ftm" ,"root", "");
        UserAccess acc = new UserAccess(dbconn.getConn());
        acc.addUser("toan", "toan", "user", 2);
    }
}
