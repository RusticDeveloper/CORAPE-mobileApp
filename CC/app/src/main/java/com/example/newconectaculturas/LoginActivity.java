package com.example.newconectaculturas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText username, password;
    private Button ingresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /*ocultar barra de titulo*/
        getSupportActionBar().hide();
        /*relacion con los campos*/
        username = (EditText) findViewById(R.id.et_username);
        password = (EditText) findViewById(R.id.et_password);
        ingresar = (Button) findViewById(R.id.botonLogin);
        /*funcionalidad para el boton ingresar*/

        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usuario = username.getText().toString().trim();
                String contraseña = password.getText().toString().trim();
                if (usuario.isEmpty() || contraseña.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Primero ingrese las credenciales", Toast.LENGTH_SHORT).show();
                }else{
                    Api.getClient().loginResponse(usuario, contraseña).enqueue(new Callback<loginResponse>() {
                        @Override
                        public void onResponse(Call<loginResponse> call, Response<loginResponse> response) {
                            String msg = response.body().getMensaje();
                            if (msg.equals("Usuario correcto")) {
                                Toast.makeText(LoginActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                                Intent splash = new Intent(LoginActivity.this, SplashActivity.class);
                                startActivity(splash);
                                finish();
                            } else if (msg.equals("no encontro")) {
                                Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<loginResponse> call, Throwable t) {
                            Log.i("loge",t.toString());
                            Toast.makeText(LoginActivity.this, "Ha ocurrido este error" + t.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


    }
}