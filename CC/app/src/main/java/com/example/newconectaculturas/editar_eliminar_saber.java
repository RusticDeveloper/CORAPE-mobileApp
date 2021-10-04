package com.example.newconectaculturas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class editar_eliminar_saber extends AppCompatActivity {
    /*Obtener los datos del activity anterior, declarar variables y enlazar la view*/
    private String Ident;
    List<singleResponse> userListResponseData;
    private EditText Titulo,Desc,Tematica;
    private RadioButton PSi,PNo;
    private Spinner spNP,spTA;
    private VideoView VASaber;
    private ImageView IMGSaber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_eliminar_saber);

        /*Inicializacion de variables y enlazamiento con la parte view*/
        Titulo=(EditText)findViewById(R.id.NombreSaber);
        Desc=(EditText) findViewById(R.id.DescripcionSaber);
        Tematica=(EditText)findViewById(R.id.TemaSaber);
        PSi=(RadioButton)findViewById(R.id.Rsi);
        PNo=(RadioButton)findViewById(R.id.Rno);
        spNP=(Spinner) findViewById(R.id.spNP);
        spTA=(Spinner) findViewById(R.id.spTA);
        VASaber=(VideoView) findViewById(R.id.VideoAudioSaber);
        IMGSaber=(ImageView) findViewById(R.id.ImagenSaber);
        Bundle extraInfo=getIntent().getExtras();
        if (extraInfo!=null){
            Ident=extraInfo.getString("Identificador");
        }
        //boton de regreso en la barra de titulo(a la activity padre)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*llena los campos obtenidos con el uso de los datos del anterior activity*/
        LLenarCampos();

    }
    private void LLenarCampos() {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(editar_eliminar_saber.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Recuperando Datos"); // set message
        progressDialog.show(); // show progress dialog
        /*recuperando y mostrando informacion en la vista*/
        (Api.getClient().getSingleData(Ident)).enqueue(new Callback<List<singleResponse>>() {
            @Override
            public void onResponse(Call<List<singleResponse>> call, Response<List<singleResponse>> response) {
//                Log.d("responseGET", response.body().get(0).getID());
                progressDialog.dismiss(); //dismiss progress dialog
                userListResponseData = response.body();
                String Title=userListResponseData.get(0).getTitulo();
                String NP=userListResponseData.get(0).getNacionalidadoPueblo();
                String Type=userListResponseData.get(0).getTipoArchivo();
                String Publicado=userListResponseData.get(0).getPublicado();
                String Description=userListResponseData.get(0).getDescripcion();
                String Themes=userListResponseData.get(0).getTagsTematicas();
                String NombreSaber=userListResponseData.get(0).getNombreSaber();
                Titulo.setText(Title);
                Desc.setText(Description);
                Tematica.setText(Themes);
                if(Publicado.equals("True")){
                    PSi.setChecked(true);
                }else if(Publicado.equals("False")){
                    PNo.setChecked(true);
                }
                spNP.setSelection(obtenerPosicionItem(spNP,NP));
                spTA.setSelection(obtenerPosicionItem(spTA,Type));

            }
            @Override
            public void onFailure(Call<List<singleResponse>> call, Throwable t) {
                // if error occurs in network transaction then we can get the error in this method.
                Toast.makeText(editar_eliminar_saber.this, t.toString(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss(); //dismiss progress dialog
            }
        });
    }
    //Método para obtener la posición de un ítem del spinner
    public static int obtenerPosicionItem(Spinner spinner, String fruta) {
        //Creamos la variable posicion y lo inicializamos en 0
        int posicion = 0;
        //Recorre el spinner en busca del ítem que coincida con el parametro `String fruta`
        //que lo pasaremos posteriormente
        for (int i = 0; i < spinner.getCount(); i++) {
            //Almacena la posición del ítem que coincida con la búsqueda
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(fruta)) {
                posicion = i;
            }
        }
        //Devuelve un valor entero (si encontro una coincidencia devuelve la
        // posición 0 o N, de lo contrario devuelve 0 = posición inicial)
        return posicion;
    }

    /*Cargar el saber dependiendo del tipo  que sea*/
    private void CargarSaber(String Tipo,String Nombre) {
        String Ruta = "http://192.168.1.51/wordpress/wp-content/uploads/Saberes/Imagenes/" + Nombre;
        switch (Tipo) {
            case "Audio":
                Toast.makeText(editar_eliminar_saber.this, "Entro Audio", Toast.LENGTH_SHORT).show();
                break;
            case "Video":
                Toast.makeText(editar_eliminar_saber.this, "Entro Video", Toast.LENGTH_SHORT).show();
                break;
            case "Texto":
                Toast.makeText(editar_eliminar_saber.this, "Entro Texto", Toast.LENGTH_SHORT).show();
                break;
            case "Imagen":
                Toast.makeText(editar_eliminar_saber.this, "Entro Imagen", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}