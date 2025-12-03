/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.access;
import com.mycompany.db.DatabaseConnector;
import com.mycompany.model.Player;
import com.mycompany.model.PlayerOfTeam;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author wolff
 */
public class PlayerAccess {
    private final Connection conn;
    
    public PlayerAccess(Connection conn){
        this.conn = conn;
    }
    
    public Player findPlayerByID(int id){
        String querry = "SELECT * FROM players WHERE id = ?";
        
        try(PreparedStatement pStmt = conn.prepareStatement(querry)){
            pStmt.setInt(1, id);
            
            try(ResultSet rs = pStmt.executeQuery()){
                if(rs.next()){    
                    return new Player(
                        rs.getInt("id"),
                        rs.getString("name_player"),
                        rs.getDate("birthday").toLocalDate(),
                        rs.getString("pos"),
                        rs.getInt("shirt_num")
                    );
                }
            }
        } catch(SQLException e){
            System.out.println("Error with Player access to database!" + e.getMessage());
        }
        return null;
    }
    public boolean updatePlayerInfo(int playerId, int newShirtNum, String newPosition) {
        String query = "UPDATE players SET shirt_num = ?, pos = ? WHERE id = ?";
        
        try (PreparedStatement pStm = this.conn.prepareStatement(query)) {
            
            pStm.setInt(1, newShirtNum);
            pStm.setString(2, newPosition);
            pStm.setInt(3, playerId);
            
            return pStm.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void addPlayer(String name, LocalDate birthday, String position, int shirtNumber){
        String querry = "INSERT INTO players(name_player, birthday, pos, shirt_num) VALUES(?,?,?,?);";
        
        try(PreparedStatement pStm = this.conn.prepareStatement(querry)){
            pStm.setString(1, name);
            pStm.setObject(2, birthday);
            pStm.setString(3, position);
            pStm.setInt(4, shirtNumber);
            
            try{
                pStm.executeUpdate();
                System.out.println("New player added!");
            }catch(SQLException e){
                System.out.println("Cannnot add player to database!" + e.getMessage());
            }
        } catch(SQLException e){
            System.out.println("Error with Player access to database!" + e.getMessage());
        }
    }
    
    public List<Player> listPlayer() throws SQLException{
        String sql = "SELECT * FROM players ";
        
        PreparedStatement pStm = this.conn.prepareStatement(sql);
        List<Player> list = new ArrayList<>();
        try(ResultSet rs = pStm.executeQuery()){
            while(rs.next()){
                list.add(new Player(
                        rs.getInt("id"),
                        rs.getString("name_player"),
                        rs.getDate("birthday").toLocalDate(),
                        rs.getString("pos"),
                        rs.getInt("shirt_num")
                    ));
                }
                   return list;
        }
    }
    
    
    public void delPlayerbyId(int id){
        String querry = "DELETE FROM players WHERE id = ?";
        
        try(PreparedStatement pStm = this.conn.prepareStatement(querry)){
            pStm.setInt(1, id);
            try{
                pStm.executeUpdate();
                System.out.println("player " + id +" deleted!");
            }catch(SQLException e){
                System.out.println("Cannnot del player!" + e.getMessage());
            }
        } catch(SQLException e){
            System.out.println("Error with Player access to database!" + e.getMessage());
        }
    }

    public boolean isNumberTaken(int playerId, int newShirtNum) {        
        String sql = "SELECT COUNT(*) FROM players p "
                   + "JOIN contact c ON p.id = c.player_id "
                   + "WHERE c.state = 'active' "
                   + "AND p.shirt_num = ? "
                   + "AND p.id != ? "
                   + "AND c.team_id = ("
                       + "SELECT team_id FROM contact WHERE player_id = ? AND state = 'active' LIMIT 1"
                   + ")";

        try (PreparedStatement pStm = this.conn.prepareStatement(sql)) {
            pStm.setInt(1, newShirtNum);
            pStm.setInt(2, playerId);
            pStm.setInt(3, playerId);
            
            try (ResultSet rs = pStm.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<PlayerOfTeam> getPlayerCareerStats(int playerId) {
        List<PlayerOfTeam> list = new ArrayList<>();
        String sql = "SELECT "
                   + "    t.name_team, "
                   + "    p.name_player, "
                   + "    p.pos, "
                   + "    p.shirt_num, " 
                   + "    c.end_date, "
                   + "    c.state, "    
                   + "    COUNT(CASE WHEN md.moment IN ('goal', 'pen') THEN 1 END) AS total_goals, "
                   + "    COUNT(CASE WHEN md.moment = 'yellow' THEN 1 END) AS total_yellow, "
                   + "    COUNT(CASE WHEN md.moment = 'red' THEN 1 END) AS total_red "
                   + "FROM contract c "
                   + "JOIN players p ON c.player_id = p.id "
                   + "JOIN teams t ON c.team_id = t.id "
                   + "LEFT JOIN match_detail md ON p.id = md.player_id "
                   + "LEFT JOIN matches m ON md.match_id = m.id "
                   + "     AND (m.home_id = c.team_id OR m.away_id = c.team_id) " 

                   + "WHERE c.player_id = ? "
                   + "GROUP BY c.id, t.name_team, p.name_player, p.pos, p.shirt_num, c.end_date "
                   + "ORDER BY c.start_date DESC"; 

        try (PreparedStatement pStm = this.conn.prepareStatement(sql)) {

            pStm.setInt(1, playerId);

            try (ResultSet rs = pStm.executeQuery()) {
                while (rs.next()) {
                    String teamName = rs.getString("name_team");
                    String playerName = rs.getString("name_player");
                    String pos = rs.getString("pos");
                    int shirtNum = rs.getInt("shirt_num");

                    int goals = rs.getInt("total_goals");
                    int yellow = rs.getInt("total_yellow");
                    int red = rs.getInt("total_red");

                    Date sqlDate = rs.getDate("end_date");
                    LocalDate contractEnd = (sqlDate != null) ? sqlDate.toLocalDate() : null;
                    String state = rs.getString("state");
                    if ("expired".equals(state)) {
                        teamName = teamName + " (Old)";
                    }

                    list.add(new PlayerOfTeam(
                        teamName,
                        playerName,
                        pos,
                        shirtNum,
                        goals,
                        yellow,
                        red,
                        contractEnd
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting career stats: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }
    
    public List<Player> getFreeAgents() {
        List<Player> list = new ArrayList<>();
        String sql = "SELECT id, name_player, birthday, pos, shirt_num "
                   + "FROM players "
                   + "WHERE id NOT IN ("
                   + "    SELECT player_id FROM contract WHERE state = 'active'"
                   + ")";

        try (PreparedStatement pStm = this.conn.prepareStatement(sql);
             ResultSet rs = pStm.executeQuery()) {

            while (rs.next()) {

                Date sqlDate = rs.getDate("birthday");
                LocalDate dob = (sqlDate != null) ? sqlDate.toLocalDate() : null;

                list.add(new Player(
                    rs.getInt("id"),
                    rs.getString("name_player"),
                    dob,
                    rs.getString("pos"),
                    rs.getInt("shirt_num")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
    
    public boolean signFreeAgent(int playerId, int teamId, LocalDate endDate, int salary) {
        if (!isFreeAgent(playerId)) {
            return false;
        }

        String sql = "INSERT INTO contract(player_id, team_id, start_date, end_date, salary, state) "
                   + "VALUES(?, ?, CURDATE(), ?, ?, 'active')";

        try (PreparedStatement pStm = this.conn.prepareStatement(sql)) {

            pStm.setInt(1, playerId);
            pStm.setInt(2, teamId);
            pStm.setObject(3, endDate);
            pStm.setInt(4, salary);

            return pStm.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean isFreeAgent(int playerId) {
        String sql = "SELECT COUNT(*) FROM contract WHERE player_id = ? AND state = 'active'";
        try (PreparedStatement pStm = this.conn.prepareStatement(sql)) {
            pStm.setInt(1, playerId);
            try (ResultSet rs = pStm.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0; 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
