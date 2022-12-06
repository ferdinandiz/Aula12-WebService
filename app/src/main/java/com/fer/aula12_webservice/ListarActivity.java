package com.fer.aula12_webservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.fer.aula12_webservice.model.Pessoa;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ListarActivity extends AppCompatActivity {
    Button btnBuscar, btnDeletar, btnListar, btnInserir;
    ListView listView;
    List <Pessoa> listaPessoa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);
        btnDeletar = findViewById(R.id.btnDelete);
        btnBuscar = findViewById(R.id.btnBusca);
        btnListar = findViewById(R.id.btnLista);
        btnInserir = findViewById(R.id.btnInsere);
        listView = findViewById(R.id.lista);
        btnListar.setEnabled(false);
        List<String> listaPessoasString = new ArrayList<>();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListarActivity.this, BuscarActivity.class);
                startActivity(i);
                finish();
            }
        });
        btnDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListarActivity.this, DeletarActivity.class);
                startActivity(i);
                finish();
            }
        });
        btnInserir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListarActivity.this, InserirActivity.class);
                startActivity(i);
                finish();
            }
        });


        //primeira tentativa

        executor.execute(new Runnable() {
            @Override
            public void run() {
                listaPessoa = new ArrayList<>();
                HttpURLConnection connection = null;
                BufferedReader buffer = null;
                try {
                    URL url = new URL("http://ferdinandizdoom.com/listar.php");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(5000);
                    connection.connect();
                    InputStream inputStream = connection.getInputStream();
                    buffer = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuffer = new StringBuilder();
                    String linha = "";
                    while ((linha = buffer.readLine())!=null){
                        stringBuffer.append(linha);
                    }
                    String arqJson = stringBuffer.toString();
                    JSONObject objetoPessoa= new JSONObject(arqJson);
                    JSONArray array = objetoPessoa.getJSONArray("pessoa");
                    for(int i =0;i<array.length();i++){
                        JSONObject objetoArray = array.getJSONObject(i);
                        Pessoa pessoa = new Pessoa();
                        pessoa.setId(objetoArray.getString("id"));
                        pessoa.setNome(objetoArray.getString("nome"));
                        pessoa.setTelefone(objetoArray.getString("telefone"));
                        listaPessoa.add(pessoa);
                    }

                    for (Pessoa p : listaPessoa){
                        listaPessoasString.add(p.getId()+" - "+p.getNome()+" - "+p.getTelefone());
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                } finally {

                    try {
                        assert connection != null;
                        connection.disconnect();
                        assert buffer != null;
                        buffer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.lista_modificada, listaPessoasString);
                        listView.setAdapter(adapter);
                    }
                });

            }
        });

    }

}