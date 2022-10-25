/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package com.curso.googleexample;


/**
 *
 * @author dpadilla
 */
public class GoogleExample {

    public static void main(String[] args) {
        ChuckNorrisAPI cnapi= new ChuckNorrisAPI();
        cnapi.CNGetRandom();
        
        UI ui=new UI();
        ui.menu();
        

    }
}
