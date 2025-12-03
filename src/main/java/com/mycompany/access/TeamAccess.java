/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.access;
import com.mycompany.model.Player;
import com.mycompany.model.PlayerOfTeam;
import com.mycompany.model.Ranking;
import com.mycompany.model.Team;
import com.mycompany.model.Versus;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author wolff
 */
public class TeamAccess {
    private Connection conn;
    public TeamAccess(Connection conn){
        this.conn = conn;
    }
    
    public Team findTeamById(int id){
        String querry = "SELECT * FROM teams WHERE id = ?";
        
        try(PreparedStatement pStmt = conn.prepareStatement(querry)){
            pStmt.setInt(1, id);
            
            try(ResultSet rs = pStmt.executeQuery()){
                if(rs.next()){    
                    return new Team(
                        rs.getInt("id"),
                        rs.getString("name_team")
                    );
                }
            }
        } catch(SQLException e){
            System.out.println("Error with Player access to database!" + e.getMessage());
        }
        return null;
    }
    public Team findTeamByName(String input){
        String querry = "SELECT * FROM teams WHERE name_team = ?";
        
        try(PreparedStatement pStmt = conn.prepareStatement(querry)){
            pStmt.setString(1, input);
            
            try(ResultSet rs = pStmt.executeQuery()){
                if(rs.next()){    
                    return new Team(
                        rs.getInt("id"),
                        rs.getString("name_team")
                    );
                }
            }
        } catch(SQLException e){
            System.out.println("Error with Team access to database!" + e.getMessage());
        }
        return null;
    }
   
    public void showTeamPlayer(int id) throws SQLException{
        String query = "SELECT name_team, name_player, shirt_num, pos, birthday, start_date, end_date, salary, state "
                    + "FROM teams t "
                    + "JOIN contract c ON t.id = c.team_id "
                    + "JOIN players p ON p.id = c.player_id "
                    + "WHERE t.id = ?";
        try(PreparedStatement pStm = this.conn.prepareStatement(query)){
            pStm.setInt(1, id);
            try(ResultSet rs = pStm.executeQuery()){
                System.out.println("Team | Name | Number | Position | Birthday | From | To | Salary | State");
                while(rs.next()){
                    System.out.println(rs.getString("name_team") + " | "
                                    + rs.getString("name_player")+ " | "
                                    + rs.getString("shirt_num")+ " | "
                                    + rs.getString("pos")+ " | "
                                    + rs.getObject("birthday")+ " | "
                                    + rs.getObject("start_date")+ " | "
                                    + rs.getObject("end_date")+ " | "
                                    + rs.getInt("salary")+ " | "
                                    + rs.getString("state")
                                    );
                }
            }
        }
    }
    public List<PlayerOfTeam> listPlayerOfTeam(int teamId) {
        List<PlayerOfTeam> list = new ArrayList<>();
        Team team = this.findTeamById(teamId);
        String nameTeam = team.nameTeam();
        String query = "SELECT "
                     + "    p.name_player, "
                     + "    p.pos, "
                     + "    p.shirt_num, "
                     + "    c.end_date, "
                     + "    COUNT(CASE WHEN md.moment = 'goal' OR md.moment = 'pen' THEN 1 END) AS total_goals, "
                     + "    COUNT(CASE WHEN md.moment = 'yellow' THEN 1 END) AS total_yellow, "
                     + "    COUNT(CASE WHEN md.moment = 'red' THEN 1 END) AS total_red "
                     + "FROM players p "
                     + "JOIN contract c ON p.id = c.player_id "
                     + "LEFT JOIN match_detail md ON p.id = md.player_id "
                     + "WHERE c.team_id = ? "
                     + "  AND c.state = 'active' " 
                     + "GROUP BY p.id, p.name_player, p.pos, p.shirt_num, c.end_date";

        try (PreparedStatement pStm = this.conn.prepareStatement(query)) {

            pStm.setInt(1, teamId);

            try (ResultSet rs = pStm.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("name_player");
                    String pos = rs.getString("pos");
                    int shirtNum = rs.getInt("shirt_num");
                    int goals = rs.getInt("total_goals");
                    int yellow = rs.getInt("total_yellow");
                    int red = rs.getInt("total_red");
                    Date sqlDate = rs.getDate("end_date");
                    LocalDate contractEnd = (sqlDate != null) ? sqlDate.toLocalDate() : null;
                    list.add(new PlayerOfTeam(
                        nameTeam,
                        name, 
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
            System.out.println("Error listing players for team " + teamId + ": " + e.getMessage());
            e.printStackTrace();
        }
    
        return list;
}
    public List<Player> listPlayByTeam(int teamId) {
        List<Player> list = new ArrayList<>();
        
        String sql = "SELECT p.id, p.name_player, p.birthday, p.pos, p.shirt_num "
                   + "FROM players p "
                   + "JOIN contract c ON p.id = c.player_id "
                   + "WHERE c.team_id = ? AND c.state = 'active'";

        try (PreparedStatement pStm = this.conn.prepareStatement(sql)) {
            pStm.setInt(1, teamId);
            
            try (ResultSet rs = pStm.executeQuery()) {
                while (rs.next()) {
                    java.sql.Date sqlDate = rs.getDate("birthday");
                    LocalDate dob = (sqlDate != null) ? sqlDate.toLocalDate() : null;

                    list.add(new Player(
                        rs.getInt("id"),
                        rs.getString("name_player"),
                        dob,                   
                        rs.getString("pos"),    
                        rs.getInt("shirt_num")  
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách cầu thủ: " + e.getMessage());
            e.printStackTrace();
        }
        
        // DEBUG: Kiểm tra xem có lấy được ai không
        System.out.println("Team ID " + teamId + " found " + list.size() + " players.");
        
        return list;
    }
    public void addTeam(String name){
        String querry = "INSERT INTO teams(name_team) VALUES(?);";
        
        try(PreparedStatement pStm = this.conn.prepareStatement(querry)){
            pStm.setString(1, name);
            try{
                pStm.executeUpdate();
                System.out.println("New team " + name +" added!");
            }catch(SQLException e){
                System.out.println("Cannnot add team to database!" + e.getMessage());
            }
        } catch(SQLException e){
            System.out.println("Error with Team access to database!" + e.getMessage());
        }
    }
    
    public List<Team> listTeam() throws SQLException{
        String sql = "SELECT * FROM teams ";
        
        PreparedStatement pStm = this.conn.prepareStatement(sql);
        List<Team> list = new ArrayList<>();
        try(ResultSet rs = pStm.executeQuery()){
            while(rs.next()){
                list.add(new Team(
                        rs.getInt("id"),
                        rs.getString("name_team")
                    ));
                }
                   return list;
        }
    }
    
    public List<Versus> getMatchesByTeamId(int teamId) {
        List<Versus> history = new ArrayList<>();

        String query = "SELECT m.id, tour.name_tour, "
                     + "t1.name_team AS home_name, "
                     + "m.home_result, "
                     + "m.away_result, "
                     + "t2.name_team AS away_name, "
                     + "m.dt, "
                     + "m.isDone "
                     + "FROM matches m "
                     + "JOIN tournaments tour ON m.tour_id = tour.id "
                     + "JOIN teams t1 ON m.home_id = t1.id "
                     + "JOIN teams t2 ON m.away_id = t2.id "
                     + "WHERE m.home_id = ? OR m.away_id = ? "
                     + "ORDER BY m.dt DESC"; 

        try (PreparedStatement pStm = this.conn.prepareStatement(query)) {
            pStm.setInt(1, teamId);
            pStm.setInt(2, teamId);

            try (ResultSet rs = pStm.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String tourName = rs.getString("name_tour");
                    String homeName = rs.getString("home_name");
                    int homeScore = rs.getInt("home_result"); 
                    int awayScore = rs.getInt("away_result");

                    String awayName = rs.getString("away_name");
                    java.sql.Date sqlDate = rs.getDate("dt");
                    LocalDate matchDate = (sqlDate != null) ? sqlDate.toLocalDate() : null;

                    boolean isDone = rs.getBoolean("isDone");
                    Versus matchRecord = new Versus(
                        id,
                        tourName, 
                        homeName, 
                        homeScore, 
                        awayScore, 
                        awayName, 
                        matchDate, 
                        isDone
                    );

                    history.add(matchRecord);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting match history: " + e.getMessage());
            e.printStackTrace();
    }
    
    return history;
    }
    public void delTeambyId(int id){
        String querry = "DELETE FROM teams WHERE id = ?";
        
        try(PreparedStatement pStm = this.conn.prepareStatement(querry)){
            pStm.setInt(1, id);
            try{
                pStm.executeUpdate();
                System.out.println("team " + id +" deleted!");
            }catch(SQLException e){
                System.out.println("Cannnot del team!" + e.getMessage());
            }
        } catch(SQLException e){
            System.out.println("Error with Team access to database!" + e.getMessage());
        }
    }

}
