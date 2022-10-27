/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.curso.googleexample;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author dpadilla
 */
public class ChuckNorrisAPI {
    private String url="https://api.chucknorris.io/jokes/random";
    
    public ChuckNorrisAPI() {
    }
    
    public void CNGetRandom(){

        //conection http
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        
        HttpResponse<String> response=null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException ex) {
            Logger.getLogger(GoogleExample.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(GoogleExample.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //json cast
        
        JsonObject album = new Gson().fromJson(response.body(), JsonObject.class);
        System.out.println(album.get("value"));
        
    }
    
}
