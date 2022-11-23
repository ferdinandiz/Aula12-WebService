package com.fer.aula12_webservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fer.aula12_webservice.model.Pessoa;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BuscarActivity extends AppCompatActivity {
    Button btnBuscar, btnInserir, btnDeletar, btnListar, btnBuscarInfo;
    EditText edID;
    TextView txtID, txtNome, txtTelefone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);
        btnBuscar = findViewById(R.id.btnBusca);
        btnBuscar.setEnabled(false);
        btnInserir = findViewById(R.id.btnInsere);
        btnDeletar = findViewById(R.id.btnDelete);
        btnListar = findViewById(R.id.btnLista);
        btnBuscarInfo = findViewById(R.id.btnBuscarInfo);
        edID = findViewById(R.id.ed_id);
        txtID = findViewById(R.id.txtID);
        txtNome = findViewById(R.id.txtNome);
        txtTelefone = findViewById(R.id.txtTelefone);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        btnInserir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BuscarActivity.this, InserirActivity.class);
                startActivity(i);
                finish();
            }
        });
        btnDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BuscarActivity.this, DeletarActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnListar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BuscarActivity.this, ListarActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnBuscarInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(edID.getText().toString().isEmpty()){
                    Toast.makeText(BuscarActivity.this, "ID não Inserido!!!!!!!!!!!", Toast.LENGTH_SHORT).show();
                } else {

                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            StringBuilder resp = new StringBuilder();
                            try {
                                URL url = new URL("http://ferdinandizdoom.com/consultar.php?id=" + edID.getText().toString());
                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                connection.setConnectTimeout(5000);
                                connection.setRequestMethod("GET");
                                connection.setRequestProperty("Accept", "application/json");
                                connection.connect();
                                Scanner sc = new Scanner(url.openStream());
                                while (sc.hasNext()) {
                                    resp.append(sc.next());
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            handler.post(new Runnable() {
                                @Override
                                public void run() {

                                    Pessoa pessoa = new Gson().fromJson(resp.toString(), Pessoa.class);
                                    if (!pessoa.getNome().isEmpty()) {
                                        try {
                                            txtID.setText(pessoa.getId());
                                            txtNome.setText(pessoa.getNome());
                                            txtTelefone.setText(pessoa.getTelefone());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
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