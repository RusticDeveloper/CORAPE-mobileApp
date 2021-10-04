package com.example.newconectaculturas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class vista_Individual extends AppCompatActivity {
    /*Declaracion de variables y relacion con la vista*/
    List<singleResponse> userListResponseData;
    private String Ident;
private TextView Titulo,Descripcion,Nacionalidad,Tematicas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_individual);

        //boton de regreso en la barra de titulo(a la activity padre)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*instanciacion de varibles */
        Bundle extraInfo=getIntent().getExtras();
        if (extraInfo!=null){
            Ident=extraInfo.getString("Identificador");
        }
        Titulo=(TextView) findViewById(R.id.tituloSaber);
        Descripcion=(TextView) findViewById(R.id.DescSaber);
        Nacionalidad=(TextView) findViewById(R.id.NoP);
        Tematicas=(TextView) findViewById(R.id.Tematicas);

/*LLena la activity con los datos obtenidos de los request*/
        LLenarCampos();

    }
/*Llenar los datos que se mostraran*/
    private void LLenarCampos() {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(vista_Individual.this);
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
                String Description=userListResponseData.get(0).getDescripcion();
                String Themes=userListResponseData.get(0).getTagsTematicas();
                String Type=userListResponseData.get(0).getTipoArchivo();
                String NombreSaber=userListResponseData.get(0).getNombreSaber();
                Titulo.setText(Title);
                Descripcion.setText(Description);
                Tematicas.setText(Themes);
                Nacionalidad.setText(NP);

            }
            @Override
            public void onFailure(Call<List<singleResponse>> call, Throwable t) {
                // if error occurs in network transaction then we can get the error in this method.
                Toast.makeText(vista_Individual.this, t.toString(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss(); //dismiss progress dialog
            }
        });
    }
    /*Cargar el saber dependiendo del tipo  que sea*/
    private void CargarSaber(String Tipo,String Nombre){
        String Ruta="http://192.168.1.51/wordpress/wp-content/uploads/Saberes/Imagenes/"+Nombre;
        switch (Tipo){
            case "Audio":
                Toast.makeText(vista_Individual.this, "Entro Audio", Toast.LENGTH_SHORT).show();
                break;
            case "Video":
                Toast.makeText(vista_Individual.this, "Entro Video", Toast.LENGTH_SHORT).show();
                break;
            case "Texto":
                Toast.makeText(vista_Individual.this, "Entro Texto", Toast.LENGTH_SHORT).show();
                break;
            case "Imagen":
                Toast.makeText(vista_Individual.this, "Entro Imagen", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}