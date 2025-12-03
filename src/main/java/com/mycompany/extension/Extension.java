/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.extension;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import javax.swing.JTable;

public class Extension {
    public static String hashPassword(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] hashBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));

            BigInteger number = new BigInteger(1, hashBytes);
            StringBuilder hexString = new StringBuilder(number.toString(16));
            while (hexString.length() < 64) {
                hexString.insert(0, '0');
            }
            return hexString.toString();

        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException("Cannot found SHA-256 algorithm", e);
        }
    }

    public static void printdemo(String input){
        System.out.println(input);
    }
    public static void main(String[] args) {
        System.out.println(hashPassword("thuy@admin"));
    }
    
    public static int convertToInt(JTable t){
        return Integer.parseInt(t.getValueAt(t.getSelectedRow(), 0).toString());
    }
}
