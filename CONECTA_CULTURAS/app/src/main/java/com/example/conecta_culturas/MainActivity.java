package com.example.conecta_culturas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void Repositorio(View v){
        Intent Repo=new Intent(this,repo_main.class);
        startActivity(Repo);
    }
    public void Crud(View v){
        Intent CRUD=new Intent(this,ListaSaberes.class);
        startActivity(CRUD);
    }
    public void Buscar(View v){
        Intent Busqueda=new Intent(this,Search.class);
        startActivity(Busqueda);
    }
}
