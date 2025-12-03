/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.model;

/**
 *
 * @author wolff
 */
public record Ranking(
        int teamId,
        String teamName,
        int points,
        int played,
        int wins,
        int draws,
        int losses,
        int goals,
        int against,
        int diff
        ) {
    
}
