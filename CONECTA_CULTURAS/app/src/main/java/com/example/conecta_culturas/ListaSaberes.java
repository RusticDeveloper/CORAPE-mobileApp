package com.example.conecta_culturas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListaSaberes extends AppCompatActivity {
private EditText Busqueda;
private ListView Saberes;
private ImageButton MsgInfo;
private ArrayList<String> Datos= new ArrayList();
private ArrayList<String> IDs= new ArrayList();

/*Instaciondo el requestQues para que funcione la pericuion*/
 RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_saberes);

        requestQueue= Volley.newRequestQueue(this);
/*Relacion entre layout y la clase java */
    Busqueda=(EditText)findViewById(R.id.et_Buscar);
    Saberes=(ListView)findViewById(R.id.listaSaberes);
    MsgInfo=(ImageButton)findViewById(R.id.Informacion);
    //llena el listview con los datos de la base de datos

    Llenar();
        Busqueda.requestFocus();
    //muestra un mensage de informacion sobre el uso de la aplicacion
    MsgInfo.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder Info= new AlertDialog.Builder(ListaSaberes.this);
            Info.setMessage("»Para Agregar un nuevo saber presione el boton de mas '+'.\n »para editar o eliminar un saber presione el saber que desea.")
                    .setCancelable(false)
                    .setNeutralButton("Entendido", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog titulo=Info.create();
            titulo.setTitle("Información📣📣📣");
            titulo.show();
        }
    });
    // agrega un listener al listview que envia al formulario de edicion y eliminacion del saber
    Saberes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent Editar=new Intent(ListaSaberes.this, DelUp.class);
            Editar.putExtra("Identificador",IDs.get(position));
            finish();
            overridePendingTransition(0, 0);
            startActivity(Editar);
            overridePendingTransition(0, 0);
        }
    });
    //pone un listener en el textview de busqueda que filtra los saberes por el titulo
        // cabe destacar que este filtro solo es por titulo y no por id es decir que puede hacer mas de un saber con el mismo titulo
        Busqueda.setOnKeyListener (new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // pregunta si el valor del textview es nulo o no y realiza una peticion
                    if(Busqueda.getText().toString().trim()!=""){

                        String URL1 = "http://192.168.1.51/Android/buscar.php?Titulo=" + Busqueda.getText().toString().trim();
                        JsonArrayRequest jsRequest = new JsonArrayRequest(
                                Request.Method.GET, URL1, null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Datos.clear();
                                IDs.clear();
                                int size = response.length();
                                for (int i = 0; i < size; i++) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response.get(i).toString());
                                        String title = jsonObject.getString("Titulo");
                                        String NacPue = jsonObject.getString("NacionalidadoPueblo");
                                        String TipArch = jsonObject.getString("TipoArchivo");
                                        String ID = jsonObject.getString("ID");
                                        Datos.add(title + " " + NacPue + " " + TipArch);
                                        IDs.add(ID);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                //establece un adaptador para el listview y tambien añade peticiones a la cola de volley para el use de los web services
                                ArrayAdapter<String> ListAdapter= new ArrayAdapter<String> (ListaSaberes.this,R.layout.list_item_nac_pue,Datos);
                                Saberes.setAdapter(ListAdapter);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(ListaSaberes.this,"No se encontro el saber",Toast.LENGTH_LONG).show();
                                Llenar();
                                Busqueda.setText("");
                                Busqueda.requestFocus();
                            }
                        });
                        //añade la peticion a la cola de peticiones de volley
                        requestQueue.add(jsRequest);

                    }else{
                        // usa el metodo llenar para pablar el listbiew cvon todos los saberes de la base de datos

                        Llenar();
                        Busqueda.setText("");
                        Busqueda.requestFocus();
                    }
                    return true;
                }
                //estable un adaptador que lista los saberes en el listbiew
                ArrayAdapter<String> LAdapter= new ArrayAdapter<String> (ListaSaberes.this,R.layout.list_item_nac_pue,Datos);
                Saberes.setAdapter(LAdapter);

                return false;
            }
        });
        //fin del listener de busqueda
    }
    //llena el listview con los saberes de la base de datos
    public void Llenar(){
        /* Varibles de informacion para que funcione volley y un array para guardar los datos que se mostraran en el listview*/
        String URL="http://192.168.1.51/Android/buscartodo.php";

        JsonArrayRequest jsonRequest =new JsonArrayRequest(
                Request.Method.POST, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Datos.clear();
                IDs.clear();
                int size=response.length();
                for(int i=0;i<size;i++){
                    try {
                        JSONObject jsonObject=new JSONObject(response.get(i).toString());
                        String title=jsonObject.getString("Titulo");
                        String NacPue=jsonObject.getString("NacionalidadoPueblo");
                        String TipArch=jsonObject.getString("TipoArchivo");
                        String ID=jsonObject.getString("ID");
                        Datos.add(title+" "+NacPue+" "+TipArch);
                        IDs.add(ID);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Busqueda.setText("El error es; "+error);
            }
        }
        );
        //establece un adaptador para el listview y tambien añade peticiones a la cola de volley para el use de los web services
        ArrayAdapter<String> ListAdapter= new ArrayAdapter<String> (this,R.layout.list_item_nac_pue,Datos);
        Saberes.setAdapter(ListAdapter);
        requestQueue.add(jsonRequest);
    }
// envia al activity de Create para crear un nuevo saber dentro de la base datos
    public void Agregar(View v){
        Intent guardarActivity=new Intent(this, CRUD_repo.class);
        finish();
        overridePendingTransition(0, 0);
        startActivity(guardarActivity);
        overridePendingTransition(0, 0);
    }
}