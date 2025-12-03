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
public record Tournament (
    int id,
    String name,
    String formula,
    LocalDate start,
    LocalDate end
        ){
    @Override
    public String toString(){
        return this.name;
    }
}
