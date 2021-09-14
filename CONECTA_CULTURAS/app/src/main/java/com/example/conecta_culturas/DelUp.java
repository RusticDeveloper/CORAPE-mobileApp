package com.example.conecta_culturas;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class DelUp extends AppCompatActivity {
    private Spinner spin_NoP,spin_TdA;
    private EditText Title,Description,Tematicas;
    private RadioButton R_si,R_no;
    private ImageView Imagen;
    private VideoView Video;
    private Button Del;
    private final String BaseDir = "Saberes/";
    private final String ImgDir = BaseDir + "Imagenes";
    private final String VidDir = BaseDir + "Video";
    private String path;
    Bitmap bitmap = null;
    String nombre = "";
    private static final String URL2="http://192.168.1.51/Android/Update.php",URL1="http://192.168.1.51/Android/Delete.php";
    RequestQueue requestQueue;
    String Ident;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_del_up);

        //Uso del requesstQuest
        requestQueue = Volley.newRequestQueue(this);
        Imagen=(ImageView)findViewById(R.id.Img_Saber);
        Video=(VideoView)findViewById(R.id.Vid_Saber);
        Tematicas=(EditText)findViewById(R.id.temas);
        Title=(EditText)findViewById(R.id.title2);
        Description=(EditText)findViewById(R.id.depiction2);
        R_si=(RadioButton)findViewById(R.id.R_yes);
        R_no=(RadioButton)findViewById(R.id.R_no);
        spin_NoP=(Spinner)findViewById(R.id.sp_NoP2);
        spin_TdA=(Spinner)findViewById(R.id.sp_TdA2);
        Del=(Button)findViewById(R.id.Delete);
        Bundle Extras=getIntent().getExtras();
        if (Extras!=null){
            Ident=Extras.getString("Identificador");
        }

//url para buscar los datos especificos y llenarlos en la base datos
        String URL="http://192.168.1.51/Android/buscarID.php?ID="+Ident;
//ingreso iniciol de datos en los campos para poder modificarlos o no
        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
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
                        String Publicado = jsonObject.getString("Publicado");
                        String Theme = jsonObject.getString("TagsTematicas");
                        String  Nombre=jsonObject.getString("NombreSaber");
                        CargarSaber(TipArch,Nombre);
                        Title.setText(title);
                        Description.setText(Descrip);
                        Tematicas.setText(Theme);
                        if(Publicado.equals("True")){
                            R_si.setChecked(true);
                        }else if(Publicado.equals("False")){
                            R_no.setChecked(true);
                        }
                        spin_NoP.setSelection(obtenerPosicionItem(spin_NoP,NacPue));
                        spin_TdA.setSelection(obtenerPosicionItem(spin_TdA,TipArch));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
            private void CargarSaber(String tipArch,String Nombre) {
                if(tipArch.equals("Video")){
                    //DDRC-TODO
                }else if(tipArch.equals("Imagen")){
                    String Ruta="http://192.168.1.51/wordpress/wp-content/uploads/Saberes/Imagenes/"+Nombre;
                    ImageRequest imageRequest=new ImageRequest(Ruta, new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            Imagen.setImageBitmap(response);
                            Imagen.setVisibility(View.VISIBLE);
                        }
                    }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(DelUp.this,"no se pudo obtener la imagen",Toast.LENGTH_LONG).show();
                        }
                    });
                    requestQueue.add(imageRequest);
                }else if(tipArch.equals("Audio")){
                    //DDRC-TODO
                }else if(tipArch.equals("Texto")){
                    //DDRC-TODO
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Description.setText("El Error: "+error);
            }
        });

        //enviando la peticional requestQueue
        requestQueue.add(jsonArrayRequest);

/*Recopila informacioin para elimina un saber*/
        Del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder Verificacion= new AlertDialog.Builder(DelUp.this);
                Verificacion.setMessage("⚠⚠⚠\n¿Seguro deseea eliminar este saber?")
                        .setCancelable(false)
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Eliminar(Ident);
                            }
                        });
                AlertDialog titulo=Verificacion.create();
                titulo.setTitle("Confirmación");
                titulo.show();
            }
        });

    }
    //fin del oncreate
//regresa a los saberes
    public void Cancelar(View v){
        Intent Home=new Intent(this,ListaSaberes.class);
        finish();
        overridePendingTransition(0, 0);
        startActivity(Home);
        overridePendingTransition(0, 0);

    }
    //metodo tomado de un ejemplo en la web
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
/*Recopila indromacion*/
    public void Actualizar (View view){
        String Titulo=Title.getText().toString().trim();
        String Descrip=Description.getText().toString().trim();
        String NacPue=spin_NoP.getSelectedItem().toString().trim();
        String TipoArch=spin_TdA.getSelectedItem().toString().trim();
        String topic=Tematicas.getText().toString().trim();
        String Show="False";
        if(R_si.isChecked()){
            Show="True";
        }else if(R_no.isChecked()){
            Show="False";
        }
        String StringIMG=ConvertirEnString(bitmap);
        Update(Titulo,Descrip,NacPue,TipoArch,Show,Ident,topic,nombre,StringIMG);
    }
/*Guarda la informacion en la base de datos*/
    private void Update(final String T,final String D,final String NP,final String TA,final String S,final String I,final String THM,final String name,final String pic) {

        StringRequest request=new StringRequest(
                Request.Method.POST,
                URL2,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(DelUp.this,"Se actualizo correctamente",Toast.LENGTH_LONG).show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Description.setText("El error es"+error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("Titulo",T);
                params.put("Descripcion",D);
                params.put("NacionalidadoPueblo",NP);
                params.put("TipoArchivo",TA);
                params.put("Publicado",S);
                params.put("Identificador",I);
                params.put("TagsTematicas",THM);
                params.put("NombreSaber",name);
                params.put("Imagen",pic);
                return params;
            }
        };
        requestQueue.add(request);
    }
/*Elimina un saber de la base de datos*/
    public void Eliminar(final String ident){
        if(ident!=null) {
            StringRequest request1 = new StringRequest(
                    Request.Method.POST,
                    URL1,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response!=null){
                                Toast.makeText(DelUp.this, "Se elimino correctamente", Toast.LENGTH_LONG).show();
                                finish();
                            }

                            //Intent Lista=new Intent(DelUp.this,ListaSaberes.class);
                            //startActivity(Lista);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Description.setText("El error:" + error);
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("ID", ident);
                    return params;
                }
            };
            //pone en cola la peticion
            requestQueue.add(request1);
        } else{
            Toast.makeText(DelUp.this, "envie un valor que no sea nulo", Toast.LENGTH_LONG).show();
        }
    }

    /*Permiter obtener los saberes desde la galeria de android o de la camara*/
    public void SeleccionarSaber(View v) {
        String TipoArch = spin_TdA.getSelectedItem().toString();
        if (TipoArch.equals("Imagen")) {
            final CharSequence[] opciones = {"Obtener de la camara", "Buscar en galeria", "Cancelar"};
            final AlertDialog.Builder opcineesAlert = new AlertDialog.Builder(DelUp.this);
            opcineesAlert.setTitle("Seleccione un Saber");
            opcineesAlert.setItems(opciones, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (opciones[which].equals("Obtener de la camara")) {
                        CapturarImagen();
                        Toast.makeText(getApplication(), "Tomara una foto", Toast.LENGTH_LONG).show();
                    } else if (opciones[which].equals("Buscar en galeria")) {
                        Intent IBSI = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        IBSI.setType("image/");
                        startActivityForResult(Intent.createChooser(IBSI, "Seleccione la aplicacion"), 1);
                    } else if (opciones[which].equals("Cancelar")) {
                        dialog.dismiss();
                    }
                }
            });
            opcineesAlert.show();
        } else if (TipoArch.equals("Video")) {
            final CharSequence[] opciones = {"Obtener de la camara", "Buscar en galeria", "Cancelar"};
            final AlertDialog.Builder opcineesAlert = new AlertDialog.Builder(DelUp.this);
            opcineesAlert.setTitle("Seleccione un Saber");
            opcineesAlert.setItems(opciones, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (opciones[which].equals("Obtener de la camara")) {
                        grabarVideo();
                        Toast.makeText(getApplication(), "Tomara un video", Toast.LENGTH_LONG).show();
                    } else if (opciones[which].equals("Buscar en galeria")) {
                        Intent IBSV = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                        IBSV.setType("video/");
                        startActivityForResult(Intent.createChooser(IBSV, "Seleccione la aplicacion"), 2);
                    } else if (opciones[which].equals("Cancelar")) {
                        dialog.dismiss();
                    }
                }
            });
            opcineesAlert.show();
        } else if (TipoArch.equals("Audio")) {
            Toast.makeText(this, "valio Audio", Toast.LENGTH_LONG);
            Intent IBSA = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            IBSA.addCategory(Intent.CATEGORY_OPENABLE);
            IBSA.setType("audio/*");
            startActivityForResult(Intent.createChooser(IBSA, "Seleccione la aplicacion"), 3);
        } else if (TipoArch.equals("Texto")) {
            Toast.makeText(this, "valio Texto", Toast.LENGTH_LONG);
            Intent IBST = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            IBST.setType("application/pdf");
            startActivityForResult(Intent.createChooser(IBST, "Seleccione la aplicacion"), 4);
        }

    }
    /*Accede a la camara para grabar un video*/
    private void grabarVideo() {

        File fileImagen = new File(Environment.getExternalStorageDirectory(), VidDir);

        if (!fileImagen.exists() || !fileImagen.isDirectory()) {
            fileImagen.mkdirs();
        }

        if (fileImagen.exists() || fileImagen.isDirectory()) {
            Log.i("DDRC", "si creo el directorio: ");
            nombre = (System.currentTimeMillis() / 1000) + ".mp4";
        }
        path = Environment.getExternalStorageDirectory()+ File.separator + VidDir + File.separator + nombre;
        File Vid = new File(path);

        Intent i = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N) {
            String Authorities=getApplicationContext().getPackageName()+".provider";
            Uri ImageUri= FileProvider.getUriForFile(this,Authorities,Vid);
            i.putExtra(MediaStore.EXTRA_OUTPUT,ImageUri);
        }else{
            i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(Vid));
        }

        startActivityForResult(i, 6);
    }
    /*Accede a la camara para tomar una foto*/
    private void CapturarImagen() {

        File fileImagen = new File(Environment.getExternalStorageDirectory(), ImgDir);

        if (!fileImagen.exists() || !fileImagen.isDirectory()) {
            fileImagen.mkdirs();
        }

        if (fileImagen.exists() || fileImagen.isDirectory()) {
            Log.i("DDRC", "si creo el directorio: ");
            nombre = (System.currentTimeMillis() / 1000) + ".jpg";
        }
        path = Environment.getExternalStorageDirectory()+ File.separator + ImgDir + File.separator + nombre;
        File Imagen = new File(path);

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N) {
            String Authorities=getApplicationContext().getPackageName()+".provider";
            Uri ImageUri= FileProvider.getUriForFile(this,Authorities,Imagen);
            i.putExtra(MediaStore.EXTRA_OUTPUT,ImageUri);
        }else{
            i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(Imagen));
        }

        startActivityForResult(i, 5);
    }
    /*Realiza distintas acciones con los saberes revibidos desde las intents*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {
                Uri myPath = data.getData();
                Imagen.setImageURI(myPath);
                nombre=(System.currentTimeMillis() / 1000) + ".jpg";
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), myPath);
                    Imagen.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Video.setVisibility(View.INVISIBLE);
                Imagen.setVisibility(View.VISIBLE);
            } else if (requestCode == 2) {
                Uri myPath = data.getData();
                Video.setMediaController(new MediaController(this));
                Video.setVideoURI(myPath);
                Video.setVisibility(View.VISIBLE);
                Imagen.setVisibility(View.INVISIBLE);
            } else if (requestCode == 3) {
                Uri myPath = data.getData();
                //MediaPlayer md = MediaPlayer.create(CRUD_repo.this, myPath);
                Video.setMediaController(new MediaController(this));
                Video.setVideoURI(myPath);
                Video.setVisibility(View.VISIBLE);
                Imagen.setVisibility(View.INVISIBLE);
            } else if (requestCode == 4) {
                Uri myPath = data.getData();
                Imagen.setImageURI(myPath);
            } else if (requestCode == 5) {
                MediaScannerConnection.scanFile(this, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String s, Uri uri) {
                        Log.i("Ruta de almacenamiento", "Path:" + path);
                    }
                });
                bitmap = BitmapFactory.decodeFile(path);
                Imagen.setImageBitmap(bitmap);
                Video.setVisibility(View.INVISIBLE);
                Imagen.setVisibility(View.VISIBLE);
            } else if (requestCode == 6) {
                Uri viData = Uri.parse(path);
                Video.setMediaController(new MediaController(this));
                Video.setVideoURI(viData);
                Video.requestFocus();
                Video.start();
                Video.setVisibility(View.VISIBLE);
                Imagen.setVisibility(View.INVISIBLE);
            }
        }
    }

    /*Convierte imagenes en Strings*/
    private String ConvertirEnString(Bitmap pic){
        ByteArrayOutputStream arrat = new ByteArrayOutputStream();
        pic.compress(Bitmap.CompressFormat.JPEG,100,arrat);
        byte[] data = arrat.toByteArray();
        String IMGSting= Base64.encodeToString(data,Base64.DEFAULT);
        return IMGSting;
    }
}