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
    
    public boolean addUser(String user_name, String pwd, int team_id) throws SQLException{
        
        PreparedStatement pStm = this.conn.prepareStatement("INSERT INTO users(name_user, hashedpwd, role_user, team_id) "
                    + "values(?,?,'user',?);");
        pStm.setString(1, user_name);
        pStm.setString(2, hashPassword(pwd));
        if (team_id > 0) {
            pStm.setInt(3, team_id);
        } else {
             pStm.setNull(3, java.sql.Types.INTEGER); 
        }
        try{
            System.out.println("Added User!");
            return pStm.executeUpdate() > 0;
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
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
    
}
