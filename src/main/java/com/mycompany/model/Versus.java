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
public record Versus(
    int matchId,
    String tourName,
    String homeName,
    int homeScore,
    int awayScore,
    String awayName,
    LocalDate dt,
    Boolean isDone
        ) {
}
