package com.mycompany;


import com.mycompany.UI.frame.Main;
import com.mycompany.access.MatchAccess;
import com.mycompany.access.RankingAccess;
import com.mycompany.access.TeamAccess;
import com.mycompany.db.DatabaseConnector;
import com.mycompany.service.LoginService;
import java.sql.SQLException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author wolff
 */
public class Test {
    
    public static void main(String[] args) throws SQLException {
//        DatabaseConnector db = new DatabaseConnector();
//        RankingAccess racc = new RankingAccess(db.getConn());
//        LoginService newlogin = new LoginService(db.getConn(), "admin", "admin");
//        MatchAccess match = new MatchAccess(db.getConn());
//        TeamAccess tacc = new TeamAccess(db.getConn());
//        System.out.println(tacc.listTeam().toString());
//        db.closeConn();
          Main newmain = new Main();
    }
}
