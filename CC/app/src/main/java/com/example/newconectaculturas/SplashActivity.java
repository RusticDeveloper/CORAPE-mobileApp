package com.example.newconectaculturas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    /* Ocultar la barra de titulo */
        getSupportActionBar().hide();
        TimerTask tarea=new TimerTask() {
            @Override
            public void run() {
                Intent Intente=new Intent(SplashActivity.this,MainActivity.class);
                startActivity(Intente);
                finish();
            }
        };
        Timer tiempo= new Timer();
        tiempo.schedule(tarea,2000);
    }
}