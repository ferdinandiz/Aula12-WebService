package com.fer.aula12_webservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fer.aula12_webservice.service.HTTPDeletar;

import java.util.concurrent.ExecutionException;

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
                }
                else {
                    HTTPDeletar service = new HTTPDeletar(ed_id.getText().toString());
                    try{
                        String resp = service.execute().get();
                        if(resp.equals("excluido")){
                            Toast.makeText(DeletarActivity.this, "Deletado com Sucesso!!", Toast.LENGTH_SHORT).show();
                        }
                        else if(resp.equals("naoExiste")){
                            Toast.makeText(DeletarActivity.this, "Esse ID não está cadastrado.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(DeletarActivity.this, "Erro ao deletar contato", Toast.LENGTH_SHORT).show();
                        }
                    }catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}