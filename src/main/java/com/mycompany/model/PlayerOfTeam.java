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
public record PlayerOfTeam(
        String team,
        String name,
        String pos,
        int shirtNum,
        int goal,
        int yellowCard,
        int redCard,
        LocalDate contractEnd
        ){
    
}
