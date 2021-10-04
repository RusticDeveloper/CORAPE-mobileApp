package com.example.newconectaculturas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Buscar_Saber extends AppCompatActivity {
    /*Inicializacion de arrays para guardar la informacion*/
    List<StringResults> userListResponseData;
    private ArrayList<String> DatosCC=new ArrayList<>();
    private ArrayList<String> IDsCC =new ArrayList<>();
    private ArrayList<String> DatosFiltradosCC =new ArrayList<>();
    private ListView saberes;
    private EditText CuadroBusqueda;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_saber);

        saberes=(ListView)findViewById(R.id.ListaSaberes);
        CuadroBusqueda=(EditText) findViewById(R.id.buscarSaber);
        /*set selected item each time it changes and link it with the xml file*/
        BottomNavigationView navegacion=(BottomNavigationView)findViewById(R.id.mainnavigation);
        navegacion.setSelectedItemId(R.id.navigation_search);
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
                        startActivity(new Intent(getApplicationContext()
                                ,Listar_Saberes.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_search:
                        return true;
                }
                return false;
            }
        });
/*llena las informaciones de los saberes*/
        Llenar();
        // agrega un listener al listview que envia al formulario de edicion y eliminacion del saber
        saberes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent Editar=new Intent(Buscar_Saber.this, vista_Individual.class);
                Editar.putExtra("Identificador",IDsCC.get(position));
                startActivity(Editar);
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
                    Toast.makeText(Buscar_Saber.this, "no hay ningun valor", Toast.LENGTH_SHORT).show();
                }
                for (String item:DatosFiltradosCC) {
//                    Log.i("intento",item);
//                    Log.i("intentosh", String.valueOf(s));
                    if (DatosFiltradosCC.contains(String.valueOf(s))){
                        int indice=DatosFiltradosCC.indexOf(String.valueOf(s));
                        ArrayList<String> SaberSeleccionado=new ArrayList<>();
                        SaberSeleccionado.add(DatosCC.get(indice));
                        Toast.makeText(Buscar_Saber.this,"funciona", Toast.LENGTH_SHORT).show();
                        //establece un adaptador para el listview y tambien añade peticiones a la cola de volley para el use de los web services
                        ArrayAdapter<String> ListAdapter= new ArrayAdapter<String> (Buscar_Saber.this,R.layout.list_item_nac_pue,SaberSeleccionado);
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
        final ProgressDialog progressDialog = new ProgressDialog(Buscar_Saber.this);
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
                ArrayAdapter<String> ListAdapter= new ArrayAdapter<String> (Buscar_Saber.this,R.layout.list_item_nac_pue,DatosCC);
                saberes.setAdapter(ListAdapter);
            }

            @Override
            public void onFailure(Call<List<StringResults>> call, Throwable t) {
                // if error occurs in network transaction then we can get the error in this method.
                Toast.makeText(Buscar_Saber.this, t.toString(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss(); //dismiss progress dialog
            }
        });
    }
}