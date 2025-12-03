/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.access;

import com.mycompany.model.PlayerOfTeam;
import com.mycompany.model.Tournament; // Giả sử bạn đã có model này
import com.mycompany.model.Versus;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author wolff
 */
public class TournamentAccess {
    private final Connection conn;

    public TournamentAccess(Connection conn) {
        this.conn = conn;
    }

    public Tournament findTournamentById(int id) {
        String query = "SELECT * FROM tournaments WHERE id = ?";
        
        try (PreparedStatement pStm = this.conn.prepareStatement(query)) {
            pStm.setInt(1, id);
            try (ResultSet rs = pStm.executeQuery()) {
                if (rs.next()) {
                    return new Tournament(
                        rs.getInt("id"),
                        rs.getString("name_tour"),
                        rs.getString("formula"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate()
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error finding tournament: " + e.getMessage());
        }
        return null;
    }
    public List<Tournament> listTournamentByTeamId(int teamId) {
        List<Tournament> list = new ArrayList<>();
        String query = "SELECT t.* FROM tournaments t "
                     + "JOIN ranking r ON t.id = r.tour_id "
                     + "WHERE r.team_id = ? "
                     + "ORDER BY t.start_date DESC";

        try (PreparedStatement pStm = this.conn.prepareStatement(query)) {
            pStm.setInt(1, teamId);

            try (ResultSet rs = pStm.executeQuery()) {
                while (rs.next()) {
                    list.add(new Tournament(
                        rs.getInt("id"),
                        rs.getString("name_tour"),
                        rs.getString("formula"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate()
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching tournaments for team: " + e.getMessage());
        }
        return list;
    }
    
    public List<Versus> getMatchesByTournamentId(int tourId) {
        List<Versus> list = new ArrayList<>();
        String query = "SELECT m.id,"
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
                     + "WHERE m.tour_id = ? "
                     + "ORDER BY m.dt ASC";

        try (PreparedStatement pStm = this.conn.prepareStatement(query)) {

            pStm.setInt(1, tourId);

            try (ResultSet rs = pStm.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String tourName = rs.getString("name_tour");
                    String homeName = rs.getString("home_name");
                    int homeScore = rs.getInt("home_result");
                    int awayScore = rs.getInt("away_result");
                    String awayName = rs.getString("away_name");
                    Date sqlDate = rs.getDate("dt");
                    LocalDate dt = (sqlDate != null) ? sqlDate.toLocalDate() : null;

                    boolean isDone = rs.getBoolean("isDone");
                    Versus v = new Versus(
                        id,
                        tourName,
                        homeName,
                        homeScore,
                        awayScore,
                        awayName,
                        dt,
                        isDone
                    );

                    list.add(v);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting matches for tour " + tourId + ": " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }
    
    public List<PlayerOfTeam> getPlayerStatsByTournament(int tourId) {
        List<PlayerOfTeam> list = new ArrayList<>();

        String query = "SELECT "
                     + "    t.name_team, "
                     + "    p.name_player, "
                     + "    p.pos, "
                     + "    p.shirt_num, "
                     + "    c.end_date, "
                     + "    COUNT(CASE WHEN (md.moment = 'goal' OR md.moment = 'pen') THEN 1 END) AS total_goals, "
                     + "    COUNT(CASE WHEN md.moment = 'yellow' THEN 1 END) AS total_yellow, "
                     + "    COUNT(CASE WHEN md.moment = 'red' THEN 1 END) AS total_red "
                     + "FROM players p "
                     + "JOIN contract c ON p.id = c.player_id "
                     + "JOIN teams t ON c.team_id = t.id "
                     + "JOIN match_detail md ON p.id = md.player_id "
                     + "JOIN matches m ON md.match_id = m.id "
                     + "WHERE m.tour_id = ? "
                     + "AND c.state = 'active' "
                     + "GROUP BY p.id, t.name_team, p.name_player, p.pos, p.shirt_num, c.end_date "
                     + "ORDER BY total_goals DESC";

        try (PreparedStatement pStm = this.conn.prepareStatement(query)) {

            pStm.setInt(1, tourId);

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
                    PlayerOfTeam p = new PlayerOfTeam(
                        teamName,
                        playerName,
                        pos,
                        shirtNum,
                        goals,
                        yellow,
                        red,
                        contractEnd
                    );

                    list.add(p);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting player stats for tour " + tourId + ": " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }
    
    public List<Tournament> getAllTournaments() {
        List<Tournament> list = new ArrayList<>();
        String query = "SELECT * FROM tournaments";

        try (PreparedStatement pStm = this.conn.prepareStatement(query);
             ResultSet rs = pStm.executeQuery()) {
            
            while (rs.next()) {
                list.add(new Tournament(
                    rs.getInt("id"),
                    rs.getString("name_tour"),
                    rs.getString("formula"),
                    rs.getDate("start_date").toLocalDate(),
                    rs.getDate("end_date").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error getting all tournaments: " + e.getMessage());
        }
        return list;
    }

    public int initTournament(String name, String formula, LocalDate startDate, LocalDate endDate) {
        String query = "INSERT INTO tournaments(name_tour, formula, start_date, end_date) VALUES(?,?,?,?)";
        
        try (PreparedStatement pStm = this.conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pStm.setString(1, name);
            pStm.setString(2, formula);
            pStm.setObject(3, startDate); 
            pStm.setObject(4, endDate);

            int added = pStm.executeUpdate();
            if (added > 0) {
                try (ResultSet rs = pStm.getGeneratedKeys()) {
                    if (rs.next()) {
                        System.out.println("Tournament Created: " + name);
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error initiating tournament: " + e.getMessage());
        }
        return -1;
    }

    public boolean addTeamToTournament(int tourId, int teamId) {
        String query = "INSERT INTO ranking(tour_id, team_id, points, played, wins, draws, losses, goals, against_goal, diff) "
                     + "VALUES(?, ?, 0, 0, 0, 0, 0, 0, 0, 0)";
        
        try (PreparedStatement pStm = this.conn.prepareStatement(query)) {
            pStm.setInt(1, tourId);
            pStm.setInt(2, teamId);
            
            int rowAff = pStm.executeUpdate();
            if (rowAff > 0) {
                System.out.println("Team ID " + teamId + " added to Tournament ID " + tourId);
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error adding team to tournament (Duplicate?): " + e.getMessage());
        }
        return false;
    }

    public void showRankingTable(int tourId) {
        String query = "SELECT t.name_team, r.points, r.played, r.wins, r.draws, r.losses, r.goals, r.against_goal, r.diff "
                     + "FROM ranking r "
                     + "JOIN teams t ON r.team_id = t.id "
                     + "WHERE r.tour_id = ? "
                     + "ORDER BY r.points DESC, r.diff DESC, r.goals DESC";

        try (PreparedStatement pStm = this.conn.prepareStatement(query)) {
            pStm.setInt(1, tourId);
            
            try (ResultSet rs = pStm.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                System.out.println("\n--- RANKING TABLE (Tour ID: " + tourId + ") ---");
                
                for (int i = 1; i <= columnCount; i++) {
                    System.out.printf("%-15s | ", metaData.getColumnName(i));
                }
                System.out.println("\n" + "-".repeat(columnCount * 18));

                while (rs.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        System.out.printf("%-15s | ", rs.getString(i));
                    }
                    System.out.println();
                }
                System.out.println("--- End Ranking ---\n");
            }
        } catch (SQLException e) {
            System.out.println("Error showing ranking table: " + e.getMessage());
        }
    }
    public void generateSchedule(int tourId, String formula, List<Integer> teamIds, LocalDate startDate) throws SQLException {
        MatchAccess matchAccess = new MatchAccess(this.conn); 
        for (int teamId : teamIds) {
            addTeamToTournament(tourId, teamId);
        }

        if ("round".equalsIgnoreCase(formula)) {
            generateRoundRobin(tourId, teamIds, startDate, matchAccess);
            
        } else if ("cup".equalsIgnoreCase(formula)) {
            if (teamIds.size() != 8) {
                throw new SQLException("Must be exactly 8 team, now: " + teamIds.size());
            }
            generateCupRound1(tourId, teamIds, startDate, matchAccess);
        }
    }

    private void generateRoundRobin(int tourId, List<Integer> teams, LocalDate startDate, MatchAccess matchAccess) throws SQLException {
        List<Integer> tempList = new ArrayList<>(teams);
        if (tempList.size() % 2 != 0) {
            tempList.add(-1);
        }

        int numTeams = tempList.size();
        int numRounds = numTeams - 1; 
        int matchesPerRound = numTeams / 2;
        for (int phase = 0; phase < 2; phase++) {
            for (int round = 0; round < numRounds; round++) {
                int currentRound = (phase * numRounds) + (round + 1);
                LocalDate matchDate = startDate.plusWeeks(currentRound - 1);

                for (int i = 0; i < matchesPerRound; i++) {
                    int home = tempList.get(i);
                    int away = tempList.get(numTeams - 1 - i);

                    if (home == -1 || away == -1) continue; 
                    if (phase == 0) {
                        matchAccess.initMatch(tourId, currentRound, home, away, matchDate);
                    } else {
                        matchAccess.initMatch(tourId, currentRound, away, home, matchDate);
                    }
                }
                Integer last = tempList.remove(tempList.size() - 1);
                tempList.add(1, last);
            }
        }
    }
    private void generateCupRound1(int tourId, List<Integer> teams, LocalDate startDate, MatchAccess matchAccess) throws SQLException {
        Collections.shuffle(teams);
        for (int i = 0; i < teams.size(); i += 2) {
            int home = teams.get(i);
            int away = teams.get(i + 1);
            matchAccess.initMatch(tourId, 1, home, away, startDate);
        }
    }
    private int getCurrentRound(int tourId) throws SQLException {
        String sql = "SELECT MAX(rounds) FROM matches WHERE tour_id = ?";
        try (PreparedStatement pStm = conn.prepareStatement(sql)) {
            pStm.setInt(1, tourId);
            try (ResultSet rs = pStm.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }
    private boolean isRoundFinished(int tourId, int round) throws SQLException {
        String sql = "SELECT COUNT(*) FROM matches WHERE tour_id = ? AND rounds = ? AND isDone = false";
        try (PreparedStatement pStm = conn.prepareStatement(sql)) {
            pStm.setInt(1, tourId);
            pStm.setInt(2, round);
            try (ResultSet rs = pStm.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }
        }
        return false;
    }
    private List<Integer> getRoundWinners(int tourId, int round) throws SQLException {
        List<Integer> winners = new ArrayList<>();
        
        String sql = "SELECT id, home_id, away_id, home_result, away_result FROM matches "
                   + "WHERE tour_id = ? AND rounds = ? ORDER BY id ASC";

        try (PreparedStatement pStm = conn.prepareStatement(sql)) {
            pStm.setInt(1, tourId);
            pStm.setInt(2, round);
            try (ResultSet rs = pStm.executeQuery()) {
                while (rs.next()) {
                    int homeId = rs.getInt("home_id");
                    int awayId = rs.getInt("away_id");
                    int homeScore = rs.getInt("home_result");
                    int awayScore = rs.getInt("away_result");

                    if (homeScore > awayScore) {
                        winners.add(homeId);
                    } else if (awayScore > homeScore) {
                        winners.add(awayId);
                    } else {
                        throw new SQLException("Match ID " + rs.getInt("id") + " got draw result. Cannot draw in cup format.");
                    }
                }
            }
        }
        return winners;
    }
    
    public String generateNextCupRound(int tourId) {
        MatchAccess matchAccess = new MatchAccess(conn);
        
        try {
            int currentRound = getCurrentRound(tourId);
            if (currentRound == 0) return "Tournament not start yet";
            
            if (!isRoundFinished(tourId, currentRound)) {
                return "Round " + currentRound + " is not finished yet";
            }

            List<Integer> winners = getRoundWinners(tourId, currentRound);

            if (winners.size() < 2) {
                return "Tournament ended! Champion is TeamId: " + winners.get(0);
            }

            int nextRound = currentRound + 1;
            

            LocalDate nextDate = LocalDate.now().plusDays(7); 

            conn.setAutoCommit(false);

            for (int i = 0; i < winners.size(); i += 2) {
                int home = winners.get(i);
                int away = winners.get(i + 1);
                
                matchAccess.initMatch(tourId, nextRound, home, away, nextDate);
            }

            conn.commit();
            return "Created round " + nextRound + " (" + (winners.size()/2) + " matche(s)).";

        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
            return e.getMessage();
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException ex) {}
        }
    }
    public boolean updateTeamRankingCup(int tourId, int teamId, int points, int gf, int ga, int win, int draw, int loss) {
        String query = "UPDATE ranking SET "
                     + "points = points + ?, "
                     + "played = played + 1, "
                     + "wins = wins + ?, "
                     + "draws = draws + ?, "
                     + "losses = losses + ?, "
                     + "goals = goals + ?, "
                     + "against_goal = against_goal + ?, "
                     + "diff = (goals + ?) - (against_goal + ?) "
                     + "WHERE tour_id = ? AND team_id = ?";
                     
        try (PreparedStatement pStm = this.conn.prepareStatement(query)) {
            pStm.setInt(1, points);
            pStm.setInt(2, win);
            pStm.setInt(3, draw);
            pStm.setInt(4, loss);
            pStm.setInt(5, gf);
            pStm.setInt(6, ga);
            pStm.setInt(7, gf);
            pStm.setInt(8, ga); 
            pStm.setInt(9, tourId);
            pStm.setInt(10, teamId);
            
            return pStm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating ranking stats: " + e.getMessage());
            return false;
        }
    }
    public boolean delTournamentById(int tourId) {
        String query = "{call deleteTournament(?, ?)}";
        
        try (CallableStatement cStm = this.conn.prepareCall(query)) {
            
            cStm.setInt(1, tourId);
            cStm.registerOutParameter(2, java.sql.Types.BOOLEAN);
          
            cStm.execute();
            boolean isDeleted = cStm.getBoolean(2);
            
            if (isDeleted) {
                System.out.println("Successfully deleted Tournament ID: " + tourId);
            } else {
                System.out.println("Failed to delete Tournament ID: " + tourId + " (Not found or Error)");
            }
            
            return isDeleted;
            
        } catch (SQLException e) {
            System.out.println("Error calling deleteTournament procedure: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}