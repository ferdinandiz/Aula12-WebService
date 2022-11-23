package com.fer.aula12_webservice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fer.aula12_webservice.service.HTTPInserir;

import java.util.concurrent.ExecutionException;

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
            String nome = edNome.getText().toString();
            String telefone = edTelefone.getText().toString();
            if(edNome.getText().toString().isEmpty() || edTelefone.getText().toString().isEmpty()){
               Toast.makeText(InserirActivity.this, "Nome e Telefone não podem estar em Branco!", Toast.LENGTH_SHORT).show();
            }
            else{
               HTTPInserir service = new HTTPInserir(nome, telefone);
               try{
                  String resp = service.execute().get();
                  if(resp.equals("Inserido")){
                     Toast.makeText(InserirActivity.this, "Contato Inserido com Sucesso!!", Toast.LENGTH_SHORT).show();
                     edNome.setText("");
                     edTelefone.setText("");
                  }else{
                     Toast.makeText(InserirActivity.this, "O Contato não foi Inserido!!", Toast.LENGTH_SHORT).show();
                  }
               }catch (InterruptedException | ExecutionException e) {
                  e.printStackTrace();
               }
            }
         }
      });

   }
}
