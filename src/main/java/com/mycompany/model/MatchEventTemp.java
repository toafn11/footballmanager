/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.model;

/**
 *
 * @author wolff
 */
public record MatchEventTemp(
    int playerId,
    String playerName,
    boolean isHomeTeam, 
    String action,      
    int minute
) {}
