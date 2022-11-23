package com.fer.aula12_webservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DeletarActivity extends AppCompatActivity {
    EditText ed_id;
    Button btnDeletarInfo, btnBuscar, btnDeletar, btnListar, btnInserir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deletar);
        ed_id = findViewById(R.id.ed_id);
        btnDeletarInfo = findViewById(R.id.btnDeletarInfo);
        btnDeletar = findViewById(R.id.btnDelete);
        btnBuscar = findViewById(R.id.btnBusca);
        btnListar = findViewById(R.id.btnLista);
        btnInserir = findViewById(R.id.btnInsere);
        btnDeletar.setEnabled(false);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DeletarActivity.this, BuscarActivity.class);
                startActivity(i);
                finish();
            }
        });
        btnListar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DeletarActivity.this, ListarActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnInserir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DeletarActivity.this, InserirActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnDeletarInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                if(ed_id.getText().toString().isEmpty()){
                    Toast.makeText(DeletarActivity.this, "ID não pode estar em Branco!", Toast.LENGTH_SHORT).show();
                } else {

                    StringBuilder resp = new StringBuilder();

                    executor.execute(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                URL url = new URL("http://ferdinandizdoom.com/deletar.php?id="+ed_id.getText().toString());
                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                connection.setConnectTimeout(5000);
                                connection.setRequestMethod("GET");
                                connection.setRequestProperty("Accept","apllication;json");
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
                                    if(resposta.equals("excluido")){
                                        Toast.makeText(DeletarActivity.this, "Deletado com Sucesso!!", Toast.LENGTH_SHORT).show();
                                    }
                                    else if(resposta.equals("naoExiste")){
                                        Toast.makeText(DeletarActivity.this, "Esse ID não está cadastrado.", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(DeletarActivity.this, "Erro ao deletar contato", Toast.LENGTH_SHORT).show();
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