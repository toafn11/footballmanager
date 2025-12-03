/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.access;
import com.mycompany.model.Transfer;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
/**
 *
 * @author wolff
 */
public class TransferAccess {
    private Connection conn;
    
    public TransferAccess(Connection conn){
        this.conn = conn;
    }
    
    public boolean sendTransferRequest(int playerId, int buyerId, int sellerId, int fee, int salaryOffer) {
        String sql = "INSERT INTO transfer (player_id, from_team, to_team, dt, fee, salary_offer, state) "
                   + "VALUES (?, ?, ?, CURDATE(), ?, ?, 'pending')";

        try (PreparedStatement pStm = conn.prepareStatement(sql)) {

            if (!conn.getAutoCommit()) conn.setAutoCommit(true);

            pStm.setInt(1, playerId);
            pStm.setInt(2, sellerId); // <--- FROM (Đội bán)
            pStm.setInt(3, buyerId);  // <--- TO (Đội mình/Đội mua)
            pStm.setInt(4, fee);
            pStm.setInt(5, salaryOffer);

            return pStm.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean disableOldcontract(int id, LocalDate date) throws SQLException{
        String query = "UPDATE contract SET state = 'expired', end_date = ? WHERE player_id = ? AND state = 'active'";
        try(PreparedStatement pStm = this.conn.prepareStatement(query)){
            pStm.setObject(1, date);
            pStm.setInt(2, id);
            int rowAff = pStm.executeUpdate();
            if(rowAff > 0){
                return true;
            }
        }
        return false;
    }
    
    public boolean newcontract(int id, int toTeam, LocalDate start, LocalDate end, int salary) throws SQLException{
        String query = "INSERT INTO contract(player_id, team_id, start_date, end_date, salary, state) VALUES(?,?,?,?,?,'active')";
        try(PreparedStatement pStm = this.conn.prepareCall(query)){
            pStm.setInt(1, id);
            pStm.setInt(2, toTeam);
            pStm.setObject(3, start);
            pStm.setObject(4, end);
            pStm.setInt(5, salary);
            int rowAff = pStm.executeUpdate();
            if(rowAff > 0) return true;
        }
        return false;
    }
    
    public void handleTransfer(int playerId, int toTeam, LocalDate start, LocalDate end, int fee, int salary) throws SQLException{
        String query = "{CALL handleTransfer(?,?,?,?,?,?)}";
        try(CallableStatement Cstmt = this.conn.prepareCall(query)){
            Cstmt.setInt(1, playerId);
            Cstmt.setInt(2, toTeam);
            Cstmt.setObject(3, start);
            Cstmt.setObject(4, end);
            Cstmt.setInt(5, fee);
            Cstmt.setInt(6, salary);
            
            Cstmt.execute();
        }
    }
    
    public List<Transfer> getPendingTransfers(int myTeamId) {
        List<Transfer> list = new ArrayList<>();

        String sql = "SELECT t.id, p.name_player, team_buy.name_team AS buyer_name, team_buy.id AS buyer_id, t.salary_offer, t.fee, t.dt, t.state, t.player_id "
                   + "FROM transfer t "
                   + "JOIN players p ON t.player_id = p.id "
                   + "JOIN teams team_buy ON t.to_team = team_buy.id "
                   + "WHERE t.from_team = ? AND t.state = 'pending'";

        try (PreparedStatement pStm = conn.prepareStatement(sql)) {
            pStm.setInt(1, myTeamId);
            try (ResultSet rs = pStm.executeQuery()) {
                while (rs.next()) {
                    list.add( new Transfer(
                    rs.getInt("id"),
                    rs.getInt("player_id"),
                    rs.getString("name_player"),
                    rs.getString("buyer_name"),
                    rs.getInt("buyer_id"),
                    rs.getInt("salary_offer"),
                    rs.getInt("fee"),
                    rs.getDate("dt").toLocalDate(),
                    rs.getString("state")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public List<Transfer> getAllTransfers() {
        List<Transfer> list = new ArrayList<>();

        String sql = "SELECT t.id, p.name_player, team_buy.name_team AS buyer_name, team_buy.id AS buyer_id, t.salary_offer, t.fee, t.dt, t.state, t.player_id "
                   + "FROM transfer t "
                   + "JOIN players p ON t.player_id = p.id "
                   + "JOIN teams team_buy ON t.to_team = team_buy.id ";


        try (PreparedStatement pStm = conn.prepareStatement(sql)) {
            try (ResultSet rs = pStm.executeQuery()) {
                while (rs.next()) {
                    list.add( new Transfer(
                    rs.getInt("id"),
                    rs.getInt("player_id"),
                    rs.getString("name_player"),
                    rs.getString("buyer_name"),
                    rs.getInt("buyer_id"),
                    rs.getInt("salary_offer"),
                    rs.getInt("fee"),
                    rs.getDate("dt").toLocalDate(),
                    rs.getString("state")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public List<Transfer> getSentOffers(int myTeamId) {
        List<Transfer> list = new ArrayList<>();
        String query = "SELECT t.id, "
                   + "       t.player_id, "
                   + "       p.name_player, "
                   + "       ts.name_team AS seller_name, " 
                   + "       ts.id AS seller_id, "          
                   + "       t.salary_offer, "
                   + "       t.fee, "
                   + "       t.dt, "
                   + "       t.state "
                   + "FROM transfer t "
                   + "JOIN players p ON t.player_id = p.id "
                   + "JOIN teams ts ON t.from_team = ts.id "  
                   + "WHERE t.to_team = ? "               
                   + "ORDER BY t.dt DESC";

        try (PreparedStatement pStm = conn.prepareStatement(query)) {
            pStm.setInt(1, myTeamId);

            try (ResultSet rs = pStm.executeQuery()) {
                while (rs.next()) {

                    list.add(new Transfer(
                        rs.getInt("id"),
                        rs.getInt("player_id"),
                        rs.getString("name_player"),
                        rs.getString("seller_name"), 
                        rs.getInt("seller_id"),     
                        rs.getInt("salary_offer"),
                        rs.getInt("fee"),
                        rs.getDate("dt").toLocalDate(),
                        rs.getString("state")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public String respondToTransfer(int transferId, boolean isAccepted, LocalDate contractEndDate) {
        String sql = "{call processTransfer(?, ?, ?, ?)}";

        try (CallableStatement cStm = conn.prepareCall(sql)) {

            cStm.setInt(1, transferId);

            cStm.setString(2, isAccepted ? "accepted" : "rejected");

            if (isAccepted && contractEndDate != null) {
                cStm.setObject(3, contractEndDate);
            } else {
                cStm.setNull(3, java.sql.Types.DATE);
            }

            cStm.registerOutParameter(4, java.sql.Types.VARCHAR);

            cStm.execute();
            return cStm.getString(4);

        } catch (SQLException e) {
            e.printStackTrace();
            return  e.getMessage();
        }
    }
}
