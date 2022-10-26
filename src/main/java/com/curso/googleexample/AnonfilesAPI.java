/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.curso.googleexample;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
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
public class AnonfilesAPI {

    private String url = "https://api.anonfiles.com/";
    private VirusTotalAPI vtapi;

    public AnonfilesAPI() {
        vtapi = new VirusTotalAPI();
    }

    public void uploadFile(String name) {

        vtapi.scanFile(name);

        String uploadUrl = url + "upload";
        File file = new File(name);

        Path sourcePath = Path.of(file.getAbsolutePath());

        Logger.getLogger(AnonfilesAPI.class.getName()).info(file.toPath().toString());

        String attachmentFileName = name;
        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        DataOutputStream request;

        try {

            HttpURLConnection con = (HttpURLConnection) URI.create(uploadUrl).toURL().openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");

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
            //request.close();

            //InputStream responseStream = new BufferedInputStream(con.getInputStream());
            BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = rd.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            rd.close();
            JsonObject jsonO = new Gson().fromJson(stringBuilder.toString(), JsonObject.class);
            //System.out.println(stringBuilder);
            System.out.println(jsonO.getAsJsonObject("data").getAsJsonObject("file").getAsJsonObject("url").get("short"));
            //System.out.println(peticionHTTPPost(uploadUrl, file.getAbsolutePath()));

        } catch (MalformedURLException ex) {
            Logger.getLogger(AnonfilesAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AnonfilesAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(AnonfilesAPI.class.getName()).log(Level.SEVERE, null, ex);
        }

        //HttpResponse<JsonNode> response = Unirest.post(uploadUrl).field("file", file).asJson();
        //System.out.println(response.getBody());
    }

    public void getFile(String name) {
        //conection http
        String newUrl = url + "v2/file/" + name + "/info";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(newUrl))
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException ex) {
            Logger.getLogger(AnonfilesAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(AnonfilesAPI.class.getName()).log(Level.SEVERE, null, ex);
        }

        //json cast
        JsonObject album = new Gson().fromJson(response.body(), JsonObject.class);
        System.out.println("URL to download the file");
        String urldownload = album.getAsJsonObject("data").getAsJsonObject("file").getAsJsonObject("url").get("full").toString();

        urldownload = urldownload.replaceAll("\"", "");

        System.out.println(URI.create(urldownload));

        request = HttpRequest.newBuilder()
                .uri(URI.create(urldownload))
                .build();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException ex) {
            Logger.getLogger(AnonfilesAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(AnonfilesAPI.class.getName()).log(Level.SEVERE, null, ex);
        }

        //String urlDownloadFile= response.body()
        String[] html = response.body().split("\"");
        String urlGood = "no text";

        for (String string : html) {
            if (string.contains(name)) {
                urlGood = string;
            }
        }

        Logger.getLogger(AnonfilesAPI.class.getName()).info(urlGood);

        URL url = null;
        try {
            url = new URL(urlGood);
            BufferedInputStream bis = new BufferedInputStream(url.openStream());
            FileOutputStream fis = new FileOutputStream("example.txt");
            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = bis.read(buffer, 0, 1024)) != -1) {
                fis.write(buffer, 0, count);
            }
            fis.close();
            bis.close();
            
        } catch (Exception ex) {
            Logger.getLogger(AnonfilesAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}
