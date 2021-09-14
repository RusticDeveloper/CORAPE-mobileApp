package com.example.conecta_culturas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
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
import java.util.Map;

public class Search extends AppCompatActivity {
protected EditText et_Busqueda;
protected ToggleButton tb_A,tb_Aud,tb_Vid,tb_Img,tb_Txt;
protected ListView LV_NP,LV_Item;

protected String Nac_y_Pue []={"Nacionalidad Achuar","Nacionalidad Andoa","Nacionalidad Awa","Nacionalidad Chachi","Nacionalidad Cofán","Nacionalidad Éperara Siapidara",
        "Nacionalidad Kichwa","Nacionalidad Sápara","Nacionalidad Sekoya","Nacionalidad Shiwiar","Nacionalidad Shiwiar","Nacionalidad Siona",
        "Nacionalidad Tsáchila","Nacionalidad Waorani","Pueblo AFroecuatoriano","Pueblo Huancavilca","Pueblo Manta","Pueblo Montuvios"};
//variables de instanciones de puebloas y naciones y volley
    String NacPue;
RequestQueue requestQueue;

protected String Eleccion [];
protected ArrayList<String> Datos=new ArrayList<>();
protected ArrayList<String> IDs=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        et_Busqueda=(EditText)findViewById(R.id.et_busqueda);
        tb_A=(ToggleButton)findViewById(R.id.tb_All);
        tb_Aud=(ToggleButton)findViewById(R.id.tb_Aud);
        tb_Vid=(ToggleButton)findViewById(R.id.tb_Vid);
        tb_Img=(ToggleButton)findViewById(R.id.tb_Img);
        tb_Txt=(ToggleButton)findViewById(R.id.tb_Txt);
        LV_NP=(ListView)findViewById(R.id.lv_NP);
        LV_Item=(ListView)findViewById(R.id.lv_Item);

        //setting up volley
        requestQueue= Volley.newRequestQueue(this);
        ArrayAdapter<String> adaptador=new ArrayAdapter<String>(this,R.layout.list_item_nac_pue,Nac_y_Pue);
        LV_NP.setAdapter(adaptador);
        tb_A.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tb_Aud.setChecked(false);
                    tb_Vid.setChecked(false);
                    tb_Img.setChecked(false);
                    tb_Txt.setChecked(false);
                }
            }
        });
        tb_Aud.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    boolean i=tb_Img.isChecked();
                    boolean v=tb_Vid.isChecked();
                    boolean t=tb_Txt.isChecked();
                    if(i && v && t){
                        tb_A.setChecked(true);
                    }else{
                        tb_A.setChecked(false);
                    }
                }else{
                    boolean i=tb_Img.isChecked();
                    boolean v=tb_Vid.isChecked();
                    boolean t=tb_Txt.isChecked();
                    if(!i && !v && !t){
                        tb_A.setChecked(true);
                    }
                }
            }
        });
        tb_Img.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    boolean a=tb_Aud.isChecked();
                    boolean v=tb_Vid.isChecked();
                    boolean t=tb_Txt.isChecked();
                    if(a && v && t){
                        tb_A.setChecked(true);
                    }else{
                        tb_A.setChecked(false);
                    }
                }else{
                    boolean a=tb_Aud.isChecked();
                    boolean v=tb_Vid.isChecked();
                    boolean t=tb_Txt.isChecked();
                    if(!a && !v && !t){
                        tb_A.setChecked(true);
                    }
                }
            }
        });
        tb_Txt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    boolean i=tb_Img.isChecked();
                    boolean v=tb_Vid.isChecked();
                    boolean a=tb_Aud.isChecked();
                    if(i && v && a){
                        tb_A.setChecked(true);
                    }else{
                        tb_A.setChecked(false);
                    }
                }else{
                    boolean i=tb_Img.isChecked();
                    boolean v=tb_Vid.isChecked();
                    boolean a=tb_Aud.isChecked();
                    if(!i && !v && !a){
                        tb_A.setChecked(true);
                    }
                }
            }
        });
        tb_Vid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    boolean i=tb_Img.isChecked();
                    boolean a=tb_Aud.isChecked();
                    boolean t=tb_Txt.isChecked();
                    if(i && a && t){
                        tb_A.setChecked(true);
                    }else{
                        tb_A.setChecked(false);
                    }
                }else{
                    boolean i=tb_Img.isChecked();
                    boolean a=tb_Aud.isChecked();
                    boolean t=tb_Txt.isChecked();
                    if(!i && !a && !t){
                        tb_A.setChecked(true);
                    }
                }
            }
        });
LV_NP.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NacPue=parent.getItemAtPosition(position).toString();
        view.setBackgroundColor(Color.rgb(255,146,23)) ;



    }
});
LV_Item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent Editar=new Intent(Search.this, vistaIndividual.class);
        Editar.putExtra("Identificador",IDs.get(position));
        finish();
        overridePendingTransition(0, 0);
        startActivity(Editar);
        overridePendingTransition(0, 0);
    }
});

    }
    public void Buscar(View v){
         String buscaet= et_Busqueda.getText().toString().trim();

        boolean all= tb_A.isChecked();
        boolean audio= tb_Aud.isChecked();
        boolean video= tb_Vid.isChecked();
        boolean texto= tb_Txt.isChecked();
        boolean imagen= tb_Img.isChecked();
        String every="",sound="",film="",letter="",graphic="";
        if(all){
            every="Todo";
        }else if (audio){
            sound="Audio";
        }else if(video){
            film="Video";
        }else if(texto){
            letter="Texto";
        }else if(imagen){
            graphic="Imagen";
        }
        /*if(buscaet.equals("")){
            buscaet="vacio";
        }*/
        String URL="http://192.168.1.51/Android/buscarEnhanced.php?Titulo="+buscaet+"&NacionalidadoPueblo="+NacPue+"&Todo="+every+"&Audio="+sound+"&Video="+film
                +"&Texto="+letter+"&Imagen="+graphic;

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Toast.makeText(Search.this,"Completo",Toast.LENGTH_LONG).show();
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
                                Log.i("Datos",Datos.toString());
                                Log.i("DatosIDS",IDs.toString());
                                et_Busqueda.setText("");
                                et_Busqueda.requestFocus();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Search.this,"No se encontro el Saber",Toast.LENGTH_LONG).show();
                et_Busqueda.setText("");
                et_Busqueda.requestFocus();
                //et_Busqueda.setText("el error es:"+error);
            }
        }
        );
        requestQueue.add(jsonArrayRequest);
        //estable un adaptador que lista los saberes en el listbiew
        ArrayAdapter<String> LAdapter= new ArrayAdapter<String> (Search.this,R.layout.list_item_nac_pue,Datos);
        LV_Item.setAdapter(LAdapter);

    }

    //metodo tomado de un ejemplo en la web
    //Método para obtener la posición de un ítem del spinner
    public static int obtenerPosicionItem(ListView listView, String fruta) {
        //Creamos la variable posicion y lo inicializamos en 0
        int posicion = 0;
        //Recorre el spinner en busca del ítem que coincida con el parametro `String fruta`
        //que lo pasaremos posteriormente
        for (int i = 0; i < listView.getCount(); i++) {
            //Almacena la posición del ítem que coincida con la búsqueda
            if (listView.getItemAtPosition(i).toString().equalsIgnoreCase(fruta)) {
                posicion = i;
            }
        }
        //Devuelve un valor entero (si encontro una coincidencia devuelve la
        // posición 0 o N, de lo contrario devuelve 0 = posición inicial)
        return posicion;
    }
}