package com.fer.aula12_webservice.service;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class HTTPInserir extends AsyncTask<Void,Void,String> {
   private String nome;
   private String telefone;
   public HTTPInserir(String nome, String telefone) {
      this.nome = nome;
      this.telefone = telefone;
   }

   @Override
   protected String doInBackground(Void... voids) {
      StringBuilder resp = new StringBuilder();
      try {
         URL url = new URL("http://ferdinandizdoom.com/cadastrar.php?nome="+this.nome+"telefone="+this.telefone);
         HttpURLConnection connection = (HttpURLConnection)url.openConnection();
         connection.setConnectTimeout(5000);
         connection.setRequestMethod("GET");
         connection.setRequestProperty("Accept","apllication/json");
         connection.connect();
         Scanner sc = new Scanner(url.openStream());
         while (sc.hasNext()){
            resp.append(sc.next());
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
      return resp.toString();
   }
}
