/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.access;
import com.mycompany.model.Ranking;
import com.mycompany.UI.tournament.RankingUI;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author wolff
 */
public class RankingAccess {
    private Connection conn;
    
    public RankingAccess(Connection conn){
        this.conn = conn;
    }
    
    public List<Ranking> listRanking(int tourId) throws SQLException{
        String sql = "SELECT * FROM ranking r "
                    +"JOIN teams t "
                    +"ON r.team_id = t.id "
                    +"WHERE tour_id =" + tourId;
        
        PreparedStatement pStm = this.conn.prepareStatement(sql);
        try(ResultSet rs = pStm.executeQuery()){
            List<Ranking> list = new ArrayList<>();
            while(rs.next()){
                list.add(new Ranking(
                        rs.getInt("team_id"),
                        rs.getString("t.name_team"),
                        rs.getInt("points"),
                        rs.getInt("played"),
                        rs.getInt("wins"),
                        rs.getInt("draws"),
                        rs.getInt("losses"),
                        rs.getInt("goals"),
                        rs.getInt("against_goal"),
                        rs.getInt("diff")
                    ));
                }
                   return list;
        } catch (SQLException e){
            System.out.println("Error with Ranking Access to database" + e.getMessage());
        }
        return null;
    }
    
    public boolean updateRanking(int tourId, int team_id, int point, int win, int draw, int loss, int gf, int ga) throws SQLException{
        String query = "UPDATE ranking "
                + "SET points = points + ? ,"
                    + "played = played + 1 ,"
                    + "wins = wins + ? ,"
                    + "draws = draws + ? ,"
                    + "losses = losses + ? ,"
                    + "goals = goals + ? ,"
                    + "against_goal = against_goal + ? ,"
                    + "diff = goals - against_goal "
                + " WHERE tour_id = ? AND team_id = ? ";
        try(PreparedStatement pStm = this.conn.prepareStatement(query)){
            try{
                pStm.setInt(1, point);
                pStm.setInt(2, win);
                pStm.setInt(3, draw);
                pStm.setInt(4, loss);
                pStm.setInt(5, gf);
                pStm.setInt(6, ga);
                pStm.setInt(7, tourId);
                pStm.setInt(8, team_id);
                int rowAff = pStm.executeUpdate();
                System.out.println("Ranking Updated.");
                return rowAff > 0;
            } catch(SQLException e){
                System.out.println("Error with update ranking " + tourId + " " + team_id + e.getMessage());
            }
            return false;
        }
    } 
    public static List<Ranking> listRankingTableByPoint(List<Ranking> list){
        list.sort((r1,r2) -> Integer.compare(r2.points(), r1.points()));
        return list;
    }
}
