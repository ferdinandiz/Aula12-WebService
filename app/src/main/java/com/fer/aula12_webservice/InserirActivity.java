package com.fer.aula12_webservice;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InserirActivity extends AppCompatActivity {
   Button btnBuscar, btnDeletar, btnListar, btnInserir, btnInserirInfo;
   EditText edNome, edTelefone;
   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_inserir);
      btnInserirInfo = findViewById(R.id.btnInserirInfo);
      btnDeletar = findViewById(R.id.btnDelete);
      btnBuscar = findViewById(R.id.btnBusca);
      btnListar = findViewById(R.id.btnLista);
      btnInserir = findViewById(R.id.btnInsere);
      edNome = findViewById(R.id.edNome);
      edTelefone = findViewById(R.id.edTelefone);
      btnInserir.setEnabled(false);
      ExecutorService executor = Executors.newSingleThreadExecutor();
      Handler handler = new Handler(Looper.getMainLooper());
      btnBuscar.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent i = new Intent(InserirActivity.this, BuscarActivity.class);
            startActivity(i);
            finish();
         }
      });
      btnListar.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent i = new Intent(InserirActivity.this, ListarActivity.class);
            startActivity(i);
            finish();
         }
      });
      btnDeletar.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent i = new Intent(InserirActivity.this, DeletarActivity.class);
            startActivity(i);
            finish();
         }
      });

      btnInserirInfo.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {



            if(edNome.getText().toString().isEmpty() || edTelefone.getText().toString().isEmpty()){
               Toast.makeText(InserirActivity.this, "Nome e Telefone não podem estar em Branco!", Toast.LENGTH_SHORT).show();
            } else {
               String nome = edNome.getText().toString();
               String telefone = edTelefone.getText().toString();
               StringBuilder resp = new StringBuilder();
               executor.execute(new Runnable() {
                  @Override
                  public void run() {

                     try {
                        URL url = new URL("http://ferdinandizdoom.com/cadastrar.php?nome="+nome+"&telefone="+telefone);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setRequestProperty("Accept","application/json");
                        connection.setConnectTimeout(5000);
                        connection.connect();
                        Scanner sc = new Scanner(url.openStream());
                        while (sc.hasNext()){
                           resp.append(sc.next());
                        }
                     } catch (IOException e) {
                        e.printStackTrace();
                     }
                     handler.post(new Runnable() {
                        @Override
                        public void run() {

                           String resposta = resp.toString();
                           if(resposta.equals("Inserido")){
                              Toast.makeText(InserirActivity.this, "Contato Inserido com Sucesso!!", Toast.LENGTH_SHORT).show();
                              edNome.setText("");
                              edTelefone.setText("");
                           }else{
                              Toast.makeText(InserirActivity.this, "O Contato não foi Inserido!!", Toast.LENGTH_SHORT).show();
                           }

                        }
                     });
                  }
               });
            }


         }
      });

   }
}
