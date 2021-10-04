package com.example.newconectaculturas;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Listar_Saberes extends AppCompatActivity {
    List<StringResults> userListResponseData;
    private ArrayList<String> DatosCC=new ArrayList<>();
    private ArrayList<String> IDsCC =new ArrayList<>();
    private ArrayList<String> DatosFiltradosCC =new ArrayList<>();

    private ListView saberes;
    private ImageButton BotonInfo,BotonNuevo;
    private EditText CuadroBusqueda;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_saberes);

/*relacindo con la vista .xml*/
        saberes=(ListView)findViewById(R.id.Saberes);
        BotonInfo=(ImageButton)findViewById(R.id.BotonAyuda);
        BotonNuevo=(ImageButton)findViewById(R.id.AgregarSaber);
        CuadroBusqueda=(EditText)findViewById(R.id.tituloSaber);
 /*set selected item each time it changes*/
         BottomNavigationView navegacion=(BottomNavigationView) findViewById(R.id.mainnavigation);
        navegacion.setSelectedItemId(R.id.navigation_add);
//ejecutar el cambio de activitys
        navegacion.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_main:
                        startActivity(new Intent(getApplicationContext()
                                ,MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_add:
                        return true;
                    case R.id.navigation_search:
                        startActivity(new Intent(getApplicationContext()
                                ,Buscar_Saber.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        /*llena el listview con los datos de la api*/
        Llenar();

// agrega un listener al listview que envia al formulario de edicion y eliminacion del saber
        saberes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent Editar=new Intent(Listar_Saberes.this, editar_eliminar_saber.class);
                Editar.putExtra("Identificador",IDsCC.get(position));
                startActivity(Editar);
                overridePendingTransition(0, 0);
            }
        });

//muestra un mensage de informacion sobre el uso de la aplicacion
        BotonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder Info= new AlertDialog.Builder(Listar_Saberes.this);
                Info.setMessage("✔️Para agregar un saber presione el boton Agregar (➕).\n ✔️Para editar o eliminar presione sobre el saber.")
                        .setCancelable(false)
                        .setNeutralButton("Entendido", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog titulo=Info.create();
                titulo.setTitle("Información❗❗");
                titulo.show();
            }
        });

/*Envia A crear un nuevo saber*/
        BotonNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Agregar=new Intent(Listar_Saberes.this, Aportar_Saber.class);
                startActivity(Agregar);
                overridePendingTransition(0, 0);
            }
        });

/*Hacer una Busqueda de un saber especifico*/
        CuadroBusqueda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (String.valueOf(s)==""||String.valueOf(s)==null){
                    Toast.makeText(Listar_Saberes.this, "no hay ningun valor", Toast.LENGTH_SHORT).show();
                }
                for (String item:DatosFiltradosCC) {
//                    Log.i("intento",item);
//                    Log.i("intentosh", String.valueOf(s));
                    if (DatosFiltradosCC.contains(String.valueOf(s))){
                        int indice=DatosFiltradosCC.indexOf(String.valueOf(s));
                        ArrayList<String> SaberSeleccionado=new ArrayList<>();
                        SaberSeleccionado.add(DatosCC.get(indice));
                        String a=IDsCC.get(indice);
                        IDsCC.clear();
                        IDsCC.add(a);
                        Toast.makeText(Listar_Saberes.this,"funciona", Toast.LENGTH_SHORT).show();
                        //establece un adaptador para el listview y tambien añade peticiones a la cola de volley para el use de los web services
                        ArrayAdapter<String> ListAdapter= new ArrayAdapter<String> (Listar_Saberes.this,R.layout.list_item_nac_pue,SaberSeleccionado);
                        saberes.setAdapter(ListAdapter);
                        break;
                    }
                }
//                Toast.makeText(Listar_Saberes.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }
    /*fin de oncreate*/
    /*metodo para llenar el listview con los datos de la consulta*/
    private void Llenar(){
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(Listar_Saberes.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Cargando Datos"); // set message
        progressDialog.show(); // show progress dialog

        (Api.getClient().getUsersList()).enqueue(new Callback<List<StringResults>>() {
            @Override
            public void onResponse(Call<List<StringResults>> call, Response<List<StringResults>> response) {
//                Log.d("responseGET", response.body().get(0).getID());
                progressDialog.dismiss(); //dismiss progress dialog
                userListResponseData = response.body();
                int size=response.body().size();
                for (int i = 0; i < size; i++) {
                    String Title=userListResponseData.get(i).getTitulo();
                    String NP=userListResponseData.get(i).getNacionalidadoPueblo();
                    String Type=userListResponseData.get(i).getTipoArchivo();
                    String ID=userListResponseData.get(i).getID();
                    DatosCC.add("📜"+Title+" "+"🙍🏻‍♂️"+NP+" 🖼"+Type);
                    IDsCC.add(ID);
                    DatosFiltradosCC.add(Title.toLowerCase());
                    Log.i("valorese",Title+"📜 "+NP+"🙍🏻‍♂️"+Type+"📝🎭🖼🎨️");
                }
//establece un adaptador para el listview y tambien añade peticiones a la cola de volley para el use de los web services
                ArrayAdapter<String> ListAdapter= new ArrayAdapter<String> (Listar_Saberes.this,R.layout.list_item_nac_pue,DatosCC);
                saberes.setAdapter(ListAdapter);
            }

            @Override
            public void onFailure(Call<List<StringResults>> call, Throwable t) {
                // if error occurs in network transaction then we can get the error in this method.
                Toast.makeText(Listar_Saberes.this, t.toString(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss(); //dismiss progress dialog
            }
        });
    }
}