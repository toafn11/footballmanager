/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.UI.frame;
import com.mycompany.UI.transfer.TransferManagement;
import com.mycompany.UI.transfer.TransferAdd;
import com.mycompany.UI.match.MatchManagement;
import com.mycompany.UI.tournament.TournamentAdd;
import com.mycompany.UI.tournament.TournamentManagement;
import com.mycompany.UI.player.AddPlayer;
import com.mycompany.UI.player.PlayerManagement;
import com.mycompany.UI.team.TeamAdd;
import com.mycompany.UI.team.TeamManagement;
import com.mycompany.UI.frame.Main;
import com.formdev.flatlaf.FlatDarkLaf;
import com.mycompany.UI.match.MatchMoment;
import com.mycompany.service.UserSession;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Component;
import java.sql.SQLException;
import javax.swing.UnsupportedLookAndFeelException;
/**
 *
 * @author Minh Dep Trai
 */
public class HomeFrame extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(HomeFrame.class.getName());
    /**
     * Creates new form TestFrame
     */
    public TournamentManagement tournament;
    public TeamManagement team;
    public PlayerManagement player;
    public MatchManagement match;
    public TransferManagement schedule;
    public TournamentAdd addTour;
    public TeamAdd addTeam;
    public AddPlayer addPlayer;
    public TransferAdd addMatch;
    public MatchMoment addMoment;
    
    
    public void setTournamentManagement() throws SQLException {
        for (Component c : jPanel9.getComponents()) {
            c.setVisible(false);
        }
        if (tournament == null) {
            tournament = new TournamentManagement(this);

            jPanel9.add(tournament);

            tournament.setSize(jPanel9.getWidth(), jPanel9.getHeight());
            tournament.setPreferredSize(jPanel9.getSize());
        }

        tournament.setVisible(true);

        jPanel9.revalidate();
        jPanel9.repaint();
    }
    
    public void setTeamManagement() throws SQLException {
        for (Component c : jPanel9.getComponents()) {
            c.setVisible(false);
        }

        if (team == null) {
            team = new TeamManagement(this);

            jPanel9.add(team);

            team.setSize(jPanel9.getWidth(), jPanel9.getHeight());
            team.setPreferredSize(jPanel9.getSize());
        }

        team.setVisible(true);

        jPanel9.revalidate();
        jPanel9.repaint();
    }
    
    public void setPlayerManagement() throws SQLException {
        for (Component c : jPanel9.getComponents()) {
            c.setVisible(false);
        }

        if (player == null) {
            player = new PlayerManagement(this);

            jPanel9.add(player);

            player.setSize(jPanel9.getWidth(), jPanel9.getHeight());
            player.setPreferredSize(jPanel9.getSize());
        }

        player.setVisible(true);

        jPanel9.revalidate();
        jPanel9.repaint();
    }
    public void setMatchManagement() throws SQLException {
        for (Component c : jPanel9.getComponents()) {
            c.setVisible(false);
        }

        if (match == null) {
            match = new MatchManagement(this);

            jPanel9.add(match);

            match.setSize(jPanel9.getWidth(), jPanel9.getHeight());
            match.setPreferredSize(jPanel9.getSize());
            }

        match.setVisible(true);

        jPanel9.revalidate();
        jPanel9.repaint();
    }
    public void setScheduleManagement() {
        for (Component c : jPanel9.getComponents()) {
            c.setVisible(false);
        }

        if (schedule == null) {
            schedule = new TransferManagement(this);

            jPanel9.add(schedule);

            schedule.setSize(jPanel9.getWidth(), jPanel9.getHeight());
            schedule.setPreferredSize(jPanel9.getSize());
        }

        schedule.setVisible(true);

        jPanel9.revalidate();
        jPanel9.repaint();
    }
    public void setTournamentAdd() throws SQLException {
        for (Component c : jPanel9.getComponents()) {
            c.setVisible(false);
        }

        if (addTour == null) {
            addTour = new TournamentAdd(this);

            jPanel9.add(addTour);

            addTour.setSize(jPanel9.getWidth(), jPanel9.getHeight());
            addTour.setPreferredSize(jPanel9.getSize());
        }

        addTour.setVisible(true);

        jPanel9.revalidate();
        jPanel9.repaint();
    }
    public void setTeamAdd() {
        for (Component c : jPanel9.getComponents()) {
            c.setVisible(false);
        }

        if (addTeam == null) {
            addTeam = new TeamAdd(this);

            jPanel9.add(addTeam);

            addTeam.setSize(jPanel9.getWidth(), jPanel9.getHeight());
            addTeam.setPreferredSize(jPanel9.getSize());
        }

        addTeam.setVisible(true);

        jPanel9.revalidate();
        jPanel9.repaint();
    }
    public void setPlayerAdd() {
        for (Component c : jPanel9.getComponents()) {
            c.setVisible(false);
        }

        if (addPlayer == null) {
            addPlayer = new AddPlayer(this);

            jPanel9.add(addPlayer);

            addPlayer.setSize(jPanel9.getWidth(), jPanel9.getHeight());
            addPlayer.setPreferredSize(jPanel9.getSize());
        }

        addPlayer.setVisible(true);

        jPanel9.revalidate();
        jPanel9.repaint();
    }
    
    public void setMatchAdd() {
        for (Component c : jPanel9.getComponents()) {
            c.setVisible(false);
        }

        if (addMatch == null) {
            addMatch = new TransferAdd(this);

            jPanel9.add(addMatch);

            addMatch.setSize(jPanel9.getWidth(), jPanel9.getHeight());
            addMatch.setPreferredSize(jPanel9.getSize());
        }

        addMatch.setVisible(true);

        jPanel9.revalidate();
        jPanel9.repaint();
    }
    public void setMomentAdd(int matchId, int homeId, int awayId ) {
        if (addMoment != null) {
            jPanel9.remove(addMoment);
        }
        
        for (Component c : jPanel9.getComponents()) {
            c.setVisible(false);
        }

        
        addMoment = new MatchMoment(this, matchId, homeId, awayId);

        jPanel9.add(addMoment);

        addMoment.setSize(jPanel9.getWidth(), jPanel9.getHeight());
        addMoment.setPreferredSize(jPanel9.getSize());

        addMoment.setVisible(true);

        jPanel9.revalidate();
        jPanel9.repaint();
    }

    public void setHomePanel() {
        if (tournament != null) {
            tournament.setVisible(false);
        }
        if (team != null) {
            team.setVisible(false);
        }
        if (player != null) {
            player.setVisible(false);
        }
        if (match != null) {
            match.setVisible(false);
        }
        if (schedule!= null) {
            schedule.setVisible(false);
        }
        if (addTour != null) {
            addTour.setVisible(false);
        }
        if (addTeam != null) {
            addTeam.setVisible(false);
        }
        if (addPlayer != null) {
            addPlayer.setVisible(false);
        }
        
        if (addMatch != null) {
            addMatch.setVisible(false);
        }
        if (addMoment != null) {
            addMoment.setVisible(false);
        }
        
        
        
        for (Component c : jPanel9.getComponents()) {
            if (c != tournament && c != team && c != player && c != match &&  c != schedule && c != addTour && c != addTeam && c != addPlayer && c != addMatch && c != addMoment) {
                c.setVisible(true);
            }
        }

        jPanel9.revalidate();
        jPanel9.repaint();
    }
    public HomeFrame(String username) {
        initComponents();
        welcome.setText("Welcome " + UserSession.getUserName() + "(" + UserSession.getRole() + ")");
        addImg();
    }
    public HomeFrame() {
        initComponents();
        welcome.setText("Welcome " + UserSession.getUserName() + "(" + UserSession.getRole() + ")");
        addImg();
    }
    
    public void addImg(){
        String path = "/img/FTM_full_logo_1.png"; 
        String path2 = "/img/admin.png";
        String teamp = "/img/group.png";
        String tourp = "/img/tournament.png";
        String playerp = "/img/player.png";
        String matchp = "/img/score.png";
        String transferp = "/img/transfericon.png";
        java.net.URL imgURL = getClass().getResource(path);
        java.net.URL imgURL2 = getClass().getResource(path2);
        java.net.URL tourUrl = getClass().getResource(tourp);
        java.net.URL teamUrl = getClass().getResource(teamp);
        java.net.URL playerUrl = getClass().getResource(playerp);
        java.net.URL matchUrl = getClass().getResource(matchp);
        java.net.URL transferUrl = getClass().getResource(transferp);
        if (imgURL != null && imgURL2 != null && teamUrl!= null && tourUrl!= null && playerUrl!= null && matchUrl!= null && transferUrl!= null) {
            label.setIcon(new javax.swing.ImageIcon(imgURL));
            adm.setIcon(new javax.swing.ImageIcon(imgURL2));
            teamImg.setIcon(new javax.swing.ImageIcon(teamUrl));
            tourImg.setIcon(new javax.swing.ImageIcon(tourUrl));
            playerImg.setIcon(new javax.swing.ImageIcon(playerUrl));
            matchImg.setIcon(new javax.swing.ImageIcon(matchUrl));
            transferImg.setIcon(new javax.swing.ImageIcon(transferUrl));
        } else {
            System.out.println("Img not found!");
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel9 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        matchImg = new javax.swing.JLabel();
        teamImg = new javax.swing.JLabel();
        tourImg = new javax.swing.JLabel();
        transferImg = new javax.swing.JLabel();
        playerImg = new javax.swing.JLabel();
        panel = new javax.swing.JPanel();
        label = new javax.swing.JLabel();
        welcome = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        adm = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel9.setBackground(new java.awt.Color(30, 30, 30));

        jButton2.setBackground(new java.awt.Color(204, 85, 0));
        jButton2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Tournament Management");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        jButton3.setBackground(new java.awt.Color(204, 85, 0));
        jButton3.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Team Management");
        jButton3.setPreferredSize(new java.awt.Dimension(201, 27));
        jButton3.addActionListener(this::jButton3ActionPerformed);

        jButton1.setBackground(new java.awt.Color(204, 85, 0));
        jButton1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Player Management");
        jButton1.setMaximumSize(new java.awt.Dimension(201, 27));
        jButton1.setMinimumSize(new java.awt.Dimension(201, 27));
        jButton1.setPreferredSize(new java.awt.Dimension(201, 27));
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jButton5.setBackground(new java.awt.Color(204, 85, 0));
        jButton5.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Match Management");
        jButton5.setPreferredSize(new java.awt.Dimension(201, 27));
        jButton5.addActionListener(this::jButton5ActionPerformed);

        jButton6.setBackground(new java.awt.Color(204, 85, 0));
        jButton6.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Transfer Management");
        jButton6.setPreferredSize(new java.awt.Dimension(201, 27));
        jButton6.addActionListener(this::jButton6ActionPerformed);

        matchImg.setText("jLabel1");

        teamImg.setText("jLabel1");

        tourImg.setText("jLabel1");

        transferImg.setText("jLabel1");

        playerImg.setText("jLabel1");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addComponent(jLabel3)
                .addGap(249, 249, 249)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addGap(109, 109, 109))
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(87, 87, 87)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(206, 206, 206)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(102, 102, 102))
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(271, 271, 271)
                        .addComponent(jLabel7)
                        .addGap(233, 233, 233)
                        .addComponent(tourImg, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(251, 251, 251)
                        .addComponent(playerImg, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(258, 258, 258)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(277, 277, 277)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(transferImg, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(275, 275, 275)
                        .addComponent(matchImg, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(125, Short.MAX_VALUE))
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addGap(104, 104, 104)
                    .addComponent(teamImg, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(936, Short.MAX_VALUE)))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(172, 172, 172)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6))
                        .addGap(12, 12, 12))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(playerImg, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tourImg, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 105, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(matchImg, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(transferImg, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(94, 94, 94))
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addGap(96, 96, 96)
                    .addComponent(teamImg, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(452, Short.MAX_VALUE)))
        );

        panel.setBackground(new java.awt.Color(30, 30, 30));
        panel.setForeground(new java.awt.Color(255, 102, 0));

        label.setBackground(new java.awt.Color(255, 51, 0));
        label.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        label.setForeground(new java.awt.Color(255, 255, 255));
        label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        welcome.setBackground(new java.awt.Color(255, 255, 255));
        welcome.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        welcome.setForeground(new java.awt.Color(255, 102, 0));
        welcome.setText("Welcome, ");

        jButton4.setBackground(new java.awt.Color(204, 85, 0));
        jButton4.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Log Out");
        jButton4.addActionListener(this::jButton4ActionPerformed);

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(adm, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(welcome, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label, javax.swing.GroupLayout.PREFERRED_SIZE, 617, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelLayout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(welcome, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(adm, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            setTeamManagement();        // TODO add your handling code here:
        } catch (SQLException ex) {
            System.getLogger(HomeFrame.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            setTournamentManagement();        // TODO add your handling code here:
        } catch (SQLException ex) {
            System.getLogger(HomeFrame.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            setPlayerManagement();        // TODO add your handling code here:
        } catch (SQLException ex) {
            System.getLogger(HomeFrame.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        new Main().setVisible(true);
        this.dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try {
            setMatchManagement();        // TODO add your handling code here:
        } catch (SQLException ex) {
            System.getLogger(HomeFrame.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        setScheduleManagement();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException ex) {
        }
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new HomeFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel adm;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel label;
    private javax.swing.JLabel matchImg;
    private javax.swing.JPanel panel;
    private javax.swing.JLabel playerImg;
    private javax.swing.JLabel teamImg;
    private javax.swing.JLabel tourImg;
    private javax.swing.JLabel transferImg;
    private javax.swing.JLabel welcome;
    // End of variables declaration//GEN-END:variables

}
