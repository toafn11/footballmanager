/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.model;

import java.time.LocalDate;

/**
 *
 * @author wolff
 */
public record Transfer(
    int id,
    int playerId,
    String playerName,
    String toTeamName,  
    int toTeamId,
    int salary,
    int fee,
    LocalDate offerDate,
    String state
) {
}