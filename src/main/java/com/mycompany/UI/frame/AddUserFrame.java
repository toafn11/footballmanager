/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.UI.frame;

/**
 *
 * @author wolff
 */
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import static com.mycompany.UI.frame.Main.conn; 
import com.mycompany.access.UserAccess;
import java.sql.SQLException;

public class AddUserFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JTextField txtTeamId;
    private JButton btnAdd;

    public AddUserFrame() {
        setTitle("Add new User");
        setSize(350, 250);
        setLocationRelativeTo(null); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(4, 2, 10, 10)); 
        add(new JLabel("  Username:"));
        txtUsername = new JTextField();
        add(txtUsername);

        add(new JLabel("  Password:"));
        txtPassword = new JPasswordField();
        add(txtPassword);

        add(new JLabel("  Team ID:"));
        txtTeamId = new JTextField();
        add(txtTeamId);

        add(new JLabel("")); 
        btnAdd = new JButton("Add User");
        add(btnAdd);
        btnAdd.addActionListener(e -> {
            String user = txtUsername.getText();
            String pwd = new String(txtPassword.getPassword());
            String teamIdStr = txtTeamId.getText();

            if (user.isEmpty() || pwd.isEmpty() || teamIdStr.isEmpty()) {
                return;
            }

            try {
                int teamId = Integer.parseInt(teamIdStr);
                UserAccess uAcc = new UserAccess(conn);
                boolean success = uAcc.addUser(user, pwd, teamId);

                if (success) {
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error, may be this username is already taken!");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Team ID must be integer!");
            } catch (SQLException ex) {
                System.getLogger(AddUserFrame.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        });
    }
}