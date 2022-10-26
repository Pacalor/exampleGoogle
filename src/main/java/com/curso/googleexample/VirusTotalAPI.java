/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.curso.googleexample;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dpadilla
 */
public class VirusTotalAPI {

    private String key = "dc2be971392eb4b73ba29e350800164bae1e565815ba8233cf43aca4084ffc6a";
    private String url = "https://www.virustotal.com/api/v3";
    public VirusTotalAPI() {
    }

    public void scanFile(String name) {

        String askUrl=url+"/files";
        File file = new File(name);

        Path sourcePath = Path.of(file.getAbsolutePath());

        Logger.getLogger(VirusTotalAPI.class.getName()).info(file.toPath().toString());

        String attachmentFileName = name;
        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        DataOutputStream request;

        try {

            HttpURLConnection con = (HttpURLConnection) URI.create(askUrl).toURL().openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("x-apikey", key);
            con.setRequestProperty("accept", "application/json");
            con.setRequestProperty(
                    "Content-Type", "multipart/form-data;boundary=" + boundary);

            request = new DataOutputStream(con.getOutputStream());

            request.writeBytes(twoHyphens + boundary + crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""
                    + attachmentFileName + "\"" + crlf);
            request.writeBytes(crlf);
            request.write(Files.readAllBytes(sourcePath));
            request.writeBytes("\r\n");
            request.writeBytes("--" + boundary + "--\r\n");
            request.flush();

            BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = rd.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            rd.close();
            JsonObject jsonO = new Gson().fromJson(stringBuilder.toString(), JsonObject.class);

            //System.out.println(jsonO);
            String urlScan= jsonO.getAsJsonObject("data").get("id").toString();
            
            urlScan = urlScan.replaceAll("\"", "");
            Logger.getLogger(VirusTotalAPI.class.getName()).info(urlScan);
            getAnalisys(urlScan);
            
            

        } catch (MalformedURLException ex) {
            Logger.getLogger(VirusTotalAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(VirusTotalAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(VirusTotalAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void getAnalisys(String name){
        //conection http
        
        
        String newUrl = url + "/analyses/"+name;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(newUrl))
                .header("accept", "application/json")
                .header("x-apikey", key)
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException ex) {
            Logger.getLogger(GoogleExample.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(GoogleExample.class.getName()).log(Level.SEVERE, null, ex);
        }

        //json cast
        JsonObject album = new Gson().fromJson(response.body(), JsonObject.class);
        System.out.print("Your file has: ");
        System.out.print(album.getAsJsonObject("data").getAsJsonObject("attributes").getAsJsonObject("stats").get("harmless").toString());
        System.out.println(" virus");
    }

}
