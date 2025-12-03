/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.model;

import java.sql.Connection;

/**
 *
 * @author wolff
 */
public record Team(
    int id,
    String nameTeam  
        ) {
    @Override
    public String toString() {
        return this.nameTeam; 
    }
}
