/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.curso.googleexample;

import java.util.Scanner;
/**
 *
 * @author dpadilla
 */
public class UI {


        private Scanner keyboard;
        private boolean exit = false;
        AnonfilesAPI anoapi;
        
        public UI() {
            keyboard = new Scanner(System.in);
            anoapi=new AnonfilesAPI();
        }

        public void menu() {
            String line = "";
            while (!exit) {
                System.out.println("Select a option:");
                line = keyboard.nextLine();

                switch (line) {
                    case "upload":
                        System.out.println("Insert file name");
                        line = keyboard.nextLine();
                        upload(line);
                        break;
                    case "get":
                        System.out.println("Insert file name");
                        line = keyboard.nextLine();
                        get(line);
                        break;
                    case "exit":
                        exit = true;
                        break;
                    default:
                        System.out.println("upload \nget\nexit");
                }

            }

        }

        private void upload(String name) {
            anoapi.uploadFile(name);
        }
        private void get(String name){
            anoapi.getFile(name);
        }
}
