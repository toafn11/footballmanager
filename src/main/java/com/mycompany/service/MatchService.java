/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.service;
import com.mycompany.access.MatchAccess;
import com.mycompany.access.RankingAccess;
import com.mycompany.extension.Pair;
import java.sql.*;
import java.time.LocalDate;
import com.mycompany.model.Match;
/**
 *
 * @author wolff
 */
public class MatchService {
    private final Connection conn;
    private MatchAccess matchAcc;
    private RankingAccess rankAcc;
    public MatchService(Connection conn, MatchAccess matchAcc, RankingAccess rankAcc){
        this.conn = conn;
        this.matchAcc = matchAcc;
        this.rankAcc = rankAcc;
    }
    
    public boolean finalizeMatch2(int matchId) throws SQLException{
        Match match = this.matchAcc.findMatchById(matchId);
        if(match == null || match.isDone() == true){
            System.out.println("Match not found or ended.");
            return false;
        }
        
        int tourId = match.tourId();
        int homeId = match.homeId();
        int awayId = match.awayId();
        try{
            this.conn.setAutoCommit(false);
            Pair score = this.matchAcc.calMatchScore(matchId);
            int homeScore = (int) score.first();
            int awayScore = (int) score.second();
            
            matchAcc.updateMatch(matchId, homeScore, awayScore);
         
            if(homeScore > awayScore){
                rankAcc.updateRanking(tourId, homeId, 3, 1, 0, 0, homeScore, awayScore);
                rankAcc.updateRanking(tourId, awayId, 0, 0, 0, 1, awayScore, homeScore);
            } else if (homeScore < awayScore){
                rankAcc.updateRanking(tourId, homeId, 0, 0, 0, 1, homeScore, awayScore);
                rankAcc.updateRanking(tourId, awayId, 3, 1, 0, 0, awayScore, homeScore);
            } else {
                rankAcc.updateRanking(tourId, homeId, 1, 0, 1, 0, homeScore, awayScore);
                rankAcc.updateRanking(tourId, awayId, 1, 0, 1, 0, awayScore, homeScore);
            }
            
            this.conn.commit();
            return true;
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException rbEx) { rbEx.printStackTrace(); }
            e.printStackTrace();
            return false;
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException acEx) { acEx.printStackTrace(); }
        }
    }
    
    public void finalizeMatch(int matchID) throws SQLException{
        String query = "{CALL finalizeMatch(?)}";
        try(CallableStatement Cstmt = this.conn.prepareCall(query)){
            Cstmt.setInt(1, matchID);
            Cstmt.execute();
        }
    }
}
