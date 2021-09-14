package com.example.conecta_culturas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class vistaIndividual extends AppCompatActivity {
    String Ident;
    private TextView Title, Description, Nacionalidad;
    private ImageView Img;
    private VideoView Vid;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_individual);
        Title = (TextView) findViewById(R.id.titulo);
        Description = (TextView) findViewById(R.id.descripcioin);
        Nacionalidad = (TextView) findViewById(R.id.nacion);
        Img = (ImageView) findViewById(R.id.Img_Saber);
        Vid = (VideoView) findViewById(R.id.vid_Saber);
        requestQueue= Volley.newRequestQueue(this);
        Bundle Extras = getIntent().getExtras();
        if (Extras != null) {
            Ident = Extras.getString("Identificador");
        }

//url para buscar los datos especificos y llenarlos en la base datos
        String URL = "http://192.168.1.51/Android/buscarID.php?ID=" + Ident;
//ingreso iniciol de datos en los campos para poder modificarlos o no
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                int size = response.length();
                for (int i = 0; i < size; i++) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.get(i).toString());
                        String title = jsonObject.getString("Titulo");
                        String Descrip = jsonObject.getString("Descripcion");
                        String NacPue = jsonObject.getString("NacionalidadoPueblo");
                        String TipArch = jsonObject.getString("TipoArchivo");
                        String Theme = jsonObject.getString("TagsTematicas");
                        String Nombre = jsonObject.getString("NombreSaber");
                        Title.setText(title);
                        Description.setText(Descrip);
                        Nacionalidad.setText(NacPue);
                        CargarSaber(TipArch, Nombre);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(vistaIndividual.this,"No se recibieron datos",Toast.LENGTH_LONG).show();

            }
        });
        requestQueue.add(jsonArrayRequest);

    }
    public void Cancelar(View v){
        Intent Home=new Intent(this,Search.class);
        finish();
        overridePendingTransition(0, 0);
        startActivity(Home);
        overridePendingTransition(0, 0);
    }
    private void CargarSaber(String tipArch, String nombre) {
        if(tipArch.equals("Video")){
            //DDRC-TODO
        }else if(tipArch.equals("Imagen")){
            Log.i("Nombre_Imagen",nombre);
            String Ruta="http://192.168.1.51/wordpress/wp-content/uploads/Saberes/Imagenes/"+nombre;
            ImageRequest imageRequest=new ImageRequest(Ruta, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    if (response!=null) {
                        Img.setImageBitmap(response);
                        Img.setVisibility(View.VISIBLE);
                        Vid.setVisibility(View.INVISIBLE);
                    }else{
                        Toast.makeText(vistaIndividual.this,"no se pudo obtener la imagen",Toast.LENGTH_LONG).show();
                    }
                }
            }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(vistaIndividual.this,"no se pudo obtener la imagen",Toast.LENGTH_LONG).show();
                }
            });
            requestQueue.add(imageRequest);
        }else if(tipArch.equals("Audio")){
            //DDRC-TODO
        }else if(tipArch.equals("Texto")){
            //DDRC-TODO
        }
    }
    }