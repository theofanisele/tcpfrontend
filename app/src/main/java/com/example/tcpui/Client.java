package com.example.tcpui;

import android.net.Uri;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import io.jenetics.jpx.GPX;

public class Client {
    private static Client clientt = null;
    private Socket client;
    private BufferedOutputStream fos;
    private BufferedReader in;
    private Results results;
    private Results averages;
    private Map<String,Double>percDiffCalc;

    public Client(String host, int port) {
        try {
            System.out.println("hello");
            try {
                this.client = new Socket(host, port);
            } catch(IOException e){
                e.printStackTrace();
            }
            this.fos = new BufferedOutputStream(client.getOutputStream());
            this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            System.out.println("Client " + client.getInetAddress().getHostName() + " connected to the server.");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static Client getClient(String host, int port) throws IOException {
        if (clientt == null) {
            clientt = new Client(host, port);
        }
        return clientt;
    }

    public static Client getInstance() throws IOException {
        if (clientt == null) {
            throw new IllegalStateException("Client has not been initialized. Call getInstance(String, int) first.");
        }
        return clientt;
    }

    public void upload(byte[] fileData) throws IOException  {
        if(fileData == null) {
            throw new IllegalArgumentException("Cannot upload null object");

        }

        this.fos.write(fileData);
        this.fos.flush();
        this.client.shutdownOutput();

    }

    public void receive() throws IOException, ClassNotFoundException {
        String jsonString = in.readLine();
        String jsonAverages = in.readLine();
        String jsonPercs = in.readLine();
        Gson gson = new Gson();
        results = gson.fromJson(jsonString, Results.class);
        averages = gson.fromJson(jsonAverages, Results.class);
        PercentDiffCalculator percentDiffCalculator = gson.fromJson(jsonPercs, PercentDiffCalculator.class);
        percDiffCalc = percentDiffCalculator.getPercDiffCalc();

    }
    public Results getResults(){
        return results;
    }

    public Results getAverages(){
        return averages;
    }

    public PercentDiffCalculator getPercDiffCalculator() {
        return (PercentDiffCalculator) percDiffCalc;
    }
    public Socket getSocket(){
        return client;
    }
}

