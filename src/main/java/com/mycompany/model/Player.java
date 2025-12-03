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
public record Player(
    int id,
    String name,
    LocalDate birthday,
    String position,
    int shirtNumber
    ) {
    @Override
    public String toString() {
        return name + " (" + position + ")";
    }
}
