/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.access;
import com.mycompany.extension.Pair;
import com.mycompany.model.Match;
import com.mycompany.model.Versus;
import java.sql.*;
import java.time.LocalDate;

/**
 *
 * @author wolff
 */
public class MatchAccess {
    private final Connection conn;
    
    public MatchAccess(Connection conn){
        this.conn = conn;
    }
    public Match findMatchById(int id) throws SQLException{
        String querry = "SELECT * FROM MATCHES WHERE id = ?";
        
        try (PreparedStatement pSmt = this.conn.prepareStatement(querry)){
            pSmt.setInt(1,id);
            try(ResultSet rs = pSmt.executeQuery()){
                if(rs.next()){
                    return new Match(
                            rs.getInt("id"),
                            rs.getInt("tour_id"),
                            rs.getInt("rounds"),
                            rs.getInt("home_id"),
                            rs.getInt("away_id"),
                            rs.getInt("home_result"),
                            rs.getInt("away_result"),
                            rs.getDate("dt").toLocalDate(),
                            rs.getBoolean("isDone")
                    );
                }
            }
        } catch(SQLException e){
            System.out.println("Error with Match access to database! " + e.getMessage());
            return null;
        }
        return null;
    }
    public int initMoment(int matchId, int playerId, String moment, int minutes){
        String query ="INSERT INTO match_detail(match_id, player_id, moment, minutes)  VALUES(?,?,?,?)";
        try (PreparedStatement pStm = this.conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            pStm.setInt(1, matchId);
            pStm.setInt(2, playerId);
            pStm.setString(3, moment);
            pStm.setInt(4, minutes);
            int added = pStm.executeUpdate();
            if(added > 0){
                try(ResultSet rs = pStm.getGeneratedKeys()){
                    if(rs.next()){
                        System.out.println("Added Moment");
                        return rs.getInt(1);
                    }
                }
            }
        } catch(SQLException e){
            System.out.println("Error with initiating moment to database! " + e.getMessage());
        }
        return -1;
    }
    public int initMatch(int tourId, int round, int homeId, int awayId, LocalDate date) throws SQLException{
        String query ="INSERT INTO matches(tour_id, rounds, home_id, away_id, dt, isDone, home_result, away_result)  VALUES(?,?,?,?,?,false,null,null)";
        try (PreparedStatement pStm = this.conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            pStm.setInt(1, tourId);
            pStm.setInt(2, round);
            pStm.setInt(3, homeId);
            pStm.setInt(4, awayId);
            pStm.setObject(5, date);
            int added = pStm.executeUpdate();
            if(added > 0){
                try(ResultSet rs = pStm.getGeneratedKeys()){
                    if(rs.next()){
                        System.out.println("Match Initiated.");
                        return rs.getInt(1);
                    }
                }
            }
        } 
        return -1;
    }
    
    public boolean updateMatch(int matchID, int homeScore, int awayScore){
        String query = "UPDATE matches "
                    + "SET home_result = ? ,"
                        + "away_result = ? ,"
                        + "isDone = true "
                    + "WHERE id = ? ";
        try(PreparedStatement pStm = this.conn.prepareStatement(query)){
            pStm.setInt(1, homeScore);
            pStm.setInt(2, awayScore);
            pStm.setInt(3, matchID);
            int rowAff = pStm.executeUpdate();
            System.out.println("Match Updated!");
            return rowAff > 0;
        } catch (SQLException e){
            System.out.println("Error with update match " + e.getMessage() );
        }
        return false;
    }
    
    public Versus getVersusByMatchId(int matchId) {
        String query = "SELECT m.id, "
                     + "    tour.name_tour, "
                     + "    t1.name_team AS home_name, "
                     + "    m.home_result, "
                     + "    m.away_result, "
                     + "    t2.name_team AS away_name, "
                     + "    m.dt, "
                     + "    m.isDone "
                     + "FROM matches m "
                     + "JOIN tournaments tour ON m.tour_id = tour.id "
                     + "JOIN teams t1 ON m.home_id = t1.id "
                     + "JOIN teams t2 ON m.away_id = t2.id "
                     + "WHERE m.id = ?"; 

        try (PreparedStatement pStm = this.conn.prepareStatement(query)) {
            
            pStm.setInt(1, matchId);
            
            try (ResultSet rs = pStm.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String tourName = rs.getString("name_tour");
                    String homeName = rs.getString("home_name");
                    int homeScore = rs.getInt("home_result");
                    int awayScore = rs.getInt("away_result");
                    String awayName = rs.getString("away_name");
                    
                    Date sqlDate = rs.getDate("dt");
                    LocalDate dt = (sqlDate != null) ? sqlDate.toLocalDate() : null;
                    
                    boolean isDone = rs.getBoolean("isDone");
                    return new Versus(
                        id, 
                        tourName, 
                        homeName, 
                        homeScore, 
                        awayScore, 
                        awayName, 
                        dt, 
                        isDone
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting versus for match ID " + matchId + ": " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    public void showMatchDetailByMatchId(int id){
        String querry = "SELECT m.id, m.minutes, m.moment, p.name_player, p.shirt_num FROM match_detail m JOIN players p ON m.player_id = p.id  WHERE match_id = ?";
        
        try (PreparedStatement pStm = this.conn.prepareStatement(querry)){
            pStm.setInt(1, id);
            try (ResultSet rs = pStm.executeQuery()){
                ResultSetMetaData metaData = rs.getMetaData();
                int column = metaData.getColumnCount();
                System.out.println("---Start: Macth ID:" + id + " ---");
                for(int i = 1; i <= column; i++){
                    System.out.print("[" + metaData.getColumnName(i) + "]|");
                }
                
                System.out.println("--------------------------------------------------");
                while(rs.next()){
                    for(int i = 1; i <= column; i++){
                        System.out.print(rs.getString(i) + " | ");
                    }
                    System.out.println();
                }
            }
        } catch (SQLException e){
            System.out.println("Error with Match Detail access to database! " + e.getMessage() );
        }
        System.out.println("--- End: Macth ID:" + id + " ---");
        System.out.println();
    }
    
       
    public Pair calMatchScore(int matchId) throws SQLException{
        Match match = this.findMatchById(matchId);
        
        if (match == null)
            throw new SQLException("Match not found with ID: " + matchId);
        
        String querry = "{call calMatchScore(?,?,?)}";
        try(CallableStatement Cstmt = conn.prepareCall(querry)){

            Cstmt.setInt(1, matchId);
            Cstmt.registerOutParameter(2, java.sql.Types.INTEGER);
            Cstmt.registerOutParameter(3, java.sql.Types.INTEGER);

            Cstmt.execute();

            return new Pair<>(Cstmt.getInt(2), Cstmt.getInt(3));
        }
    }
    
    public String finalizeMatch(int matchId) {
        String sql = "{call finalizeMatch(?)}";
        
        try (CallableStatement cStm = this.conn.prepareCall(sql)) {
            cStm.setInt(1, matchId);
            boolean hasResults = cStm.execute();
            while (!hasResults && cStm.getUpdateCount() != -1) {
                hasResults = cStm.getMoreResults();
            }
            if (hasResults) {
                try (ResultSet rs = cStm.getResultSet()) {
                    if (rs.next()) {
                        return rs.getString("Message");
                    }
                }
            }
            
            return "No Message";

        } catch (SQLException e) {
            e.printStackTrace();
            return  e.getMessage();
        }
    }
}




