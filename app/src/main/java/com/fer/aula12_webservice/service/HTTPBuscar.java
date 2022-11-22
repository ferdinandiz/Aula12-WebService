package com.fer.aula12_webservice.service;

import android.os.AsyncTask;

import com.fer.aula12_webservice.model.Pessoa;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class HTTPBuscar extends AsyncTask<Void,Void, Pessoa> {
    private String id;
    public HTTPBuscar(String id) {
        this.id = id;
    }


    @Override
    protected Pessoa doInBackground(Void... voids) {
        StringBuilder resp = new StringBuilder();
        try {
            URL url = new URL("http://ferdinandizdoom.com/consultar.php?id="+this.id);
            HttpURLConnection connection = (HttpURLConnection)  url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept","application/json");
            connection.connect();
            Scanner sc = new Scanner(url.openStream());
            while (sc.hasNext()){
                resp.append(sc.next());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(resp.toString(),Pessoa.class);
    }
}
