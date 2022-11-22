package com.fer.aula12_webservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.AndroidCharacter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.fer.aula12_webservice.model.Pessoa;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListarActivity extends AppCompatActivity {
    Button btnBuscar, btnDeletar, btnListar, btnInserir;
    ListView lista;
    List <Pessoa> listaPessoa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);
        btnDeletar = findViewById(R.id.btnDelete);
        btnBuscar = findViewById(R.id.btnBusca);
        btnListar = findViewById(R.id.btnLista);
        btnInserir = findViewById(R.id.btnInsere);
        btnListar.setEnabled(false);
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

        HTTPListar listarJson = new HTTPListar();
        listarJson.execute();
    }

    public class HTTPListar extends AsyncTask<String, String, List<Pessoa>>{

        @Override
        protected List<Pessoa> doInBackground(String... strings) {
            listaPessoa = new ArrayList<>();
            HttpURLConnection connection = null;
            BufferedReader buffer = null;
            try{
                URL url = new URL("http://ferdinandizdoom.com/listar.php");
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000);
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                buffer = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();
                String linha = "";
                while ((linha = buffer.readLine()) != null){
                    stringBuffer.append(linha);
                }
                String arqJson = stringBuffer.toString();
                JSONObject objetoPessoa = new JSONObject(arqJson);
                JSONArray array = objetoPessoa.getJSONArray("pessoa");
                for(int i = 0; i < array.length();i++){
                    JSONObject objectArray = array.getJSONObject(i);
                    Pessoa pessoa = new Pessoa();
                    pessoa.setId(objectArray.getString("id"));
                    pessoa.setNome(objetoPessoa.getString("nome"));
                    pessoa.setTelefone(objetoPessoa.getString("telefone"));
                    listaPessoa.add(pessoa);
                }
                return listaPessoa;
            }catch (Exception e){
                e.printStackTrace();
            } finally {
                try {
                    connection.disconnect();
                    buffer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Pessoa> pessoas) {
            super.onPostExecute(pessoas);
            List<String> listaPessoas = new ArrayList<>();
            for (Pessoa p : pessoas){
                listaPessoas.add(p.getId()+" - "+p.getNome()+" - "+p.getTelefone());
            }
            ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, listaPessoas);
            lista.setAdapter(adapter);
        }
    }

}