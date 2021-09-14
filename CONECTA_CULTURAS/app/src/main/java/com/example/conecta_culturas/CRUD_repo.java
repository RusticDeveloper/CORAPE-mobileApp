package com.example.conecta_culturas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.drawable.Drawable;
import android.icu.text.CaseMap;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static java.security.AccessController.getContext;


public class CRUD_repo extends AppCompatActivity {
    //varibles para el uso de volley
    /* peticion que permite poner en cola las peticion para servicios web*/
    RequestQueue requestQueue;
    /* url del archivo php que se conecta con la base de datos  */
    private static final String URL = "http://192.168.1.51/Android/campos.php";

    private Spinner spin_NoP, spin_TdA;
    private EditText Title, Description, Tematicas;
    private RadioButton R_si, R_no;
    private ImageView ImgSaber;
    private VideoView videoSaber;
    private Button SelecSaber;

    private final String BaseDir = "Saberes/";
    private final String ImgDir = BaseDir + "Imagenes";
    private final String VidDir = BaseDir + "Video";
    private String path;
    Bitmap bitmap = null;
    String nombre = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_r_u_d_repo);

        /*instanciando el requestQueue*/
        requestQueue = Volley.newRequestQueue(this);

        SelecSaber=(Button)findViewById(R.id.btn_saber);
/*declaracion de variables*/
        Tematicas = (EditText) findViewById(R.id.tagThemes);
        ImgSaber = (ImageView) findViewById(R.id.img);
        Title = (EditText) findViewById(R.id.Title);
        Description = (EditText) findViewById(R.id.Depiction);
        R_si = (RadioButton) findViewById(R.id.R_yes);
        R_no = (RadioButton) findViewById(R.id.R_no);
        spin_NoP = (Spinner) findViewById(R.id.sp_NoP);
        spin_TdA = (Spinner) findViewById(R.id.sp_TdA);
        videoSaber = (VideoView) findViewById(R.id.videoSaber);
        String Nac_y_Pue[] = {"Nacionalidad Achuar", "Nacionalidad Andoa", "Nacionalidad Awa", "Nacionalidad Chachi", "Nacionalidad Cofán", "Nacionalidad Éperara Siapidara",
                "Nacionalidad Kichwa", "Nacionalidad Sápara", "Nacionalidad Sekoya", "Nacionalidad Shiwiar", "Nacionalidad Shiwiar", "Nacionalidad Siona",
                "Nacionalidad Tsáchila", "Nacionalidad Waorani", "Pueblo AFroecuatoriano", "Pueblo Huancavilca", "Pueblo Manta", "Pueblo Montuvios"};
        String Tipo_Arch[] = {"Imagen", "Audio", "Video", "Texto"};
        ArrayAdapter<String> Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Nac_y_Pue);
        spin_NoP.setAdapter(Adapter);
        /*pedir permisos de audio camara y escrivuta*/
        if (ValidarPermisos()) {
            SelecSaber.setEnabled(true);
        } else {
            SelecSaber.setEnabled(false);
        }
    }
/*valida si ya estan otorgados los permisos*/
    private boolean ValidarPermisos() {
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M){return true;}
        if ((checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED)&&(checkSelfPermission(WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)&&(checkSelfPermission(RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED)){
            return  true;
        }
        if ((shouldShowRequestPermissionRationale(CAMERA))||(shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))||(shouldShowRequestPermissionRationale(RECORD_AUDIO))){
            cargarRecomendacion();
        }else{
            requestPermissions(new String[]{CAMERA,WRITE_EXTERNAL_STORAGE,RECORD_AUDIO},100);
        }
        return false;
    }
/*si no tiene permisos le recomienda hacerlo con el dialogo para otorgar permisos */
    private void cargarRecomendacion() {
        AlertDialog.Builder permisos=new AlertDialog.Builder(CRUD_repo.this);
        permisos.setTitle("Permita el acceso a estas configuraciones");
        permisos.setMessage("Debe permitir para tomar fotos");
        permisos.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    requestPermissions(new String[]{CAMERA,WRITE_EXTERNAL_STORAGE,RECORD_AUDIO},100);

            }
        });
        permisos.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==100){
            if (grantResults.length==3 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED&& grantResults[2]==PackageManager.PERMISSION_GRANTED){
                SelecSaber.setEnabled(true);
            }else{
                otorgarPermisosManual();
            }
        }
    }
/*Envia a las configuraciones de la app para otorgar permisos manualmente*/
    private void otorgarPermisosManual() {
        final CharSequence[] opciones = {"si", "no"};
        final AlertDialog.Builder opcinesAlert = new AlertDialog.Builder(CRUD_repo.this);
        opcinesAlert.setTitle("Configurar permisos Manualmente");
        opcinesAlert.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (opciones[which].equals("si")) {
                    Intent permisoManual=new Intent();
                    permisoManual.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package",getPackageName(),null);
                    permisoManual.setData(uri);
                    startActivity(permisoManual);
                } else if (opciones[which].equals("no")) {
                    Toast.makeText(getApplication(), "No se aceptaron los permisos", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }
        });
        opcinesAlert.show();
    }
/*cierra la activity y nos envia a la anctivitry principal*/
    public void Cancelar(View v) {
        Intent Home = new Intent(this, ListaSaberes.class);
        startActivity(Home);
        finish();
    }
/*Recopila datos de la activity*/
    public void Guardar(View v) {
        String Saber="";
        String Temas = Tematicas.getText().toString().trim();
        String Titulo = Title.getText().toString().trim();
        String Descrip = Description.getText().toString().trim();
        String NacPue = spin_NoP.getSelectedItem().toString();
        String TipoArch = spin_TdA.getSelectedItem().toString();
        if(TipoArch.equals("Imagen")){
             Saber=ConvertirEnString(bitmap);
        }else if(TipoArch.equals("Imagen")){
            Saber="";
        }else if(TipoArch.equals("Imagen")){
            Saber="";
        }

        String Show = "False";
        if (R_si.isChecked()) {
            Show = "True";
        } else if (R_no.isChecked()) {
            Show = "False";
        }
        CreateRecord(Titulo, Descrip, NacPue, TipoArch, Show, Temas,Saber,nombre);
    }
    /*Convierte imagenes en Strings*/
    private String ConvertirEnString(Bitmap pic){
        ByteArrayOutputStream arrat = new ByteArrayOutputStream();
        pic.compress(Bitmap.CompressFormat.JPEG,100,arrat);
        byte[] data = arrat.toByteArray();
        String IMGSting=Base64.encodeToString(data,Base64.DEFAULT);
        return IMGSting;
    }
/*Guarda los datos recopilados en Guardar*/
    private void CreateRecord(final String T, final String D, final String NP, final String TA, final String S, final String THM,final String img,final  String name) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(CRUD_repo.this, "Saber guardado con éxito", Toast.LENGTH_LONG).show();
                        finish();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CRUD_repo.this, "Ha ocurrido un error", Toast.LENGTH_LONG).show();
                Description.setText("El error es; " + error);
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Titulo", T);
                params.put("Descripcion", D);
                params.put("NacionalidadoPueblo", NP);
                params.put("TipoArchivo", TA);
                params.put("Publicado", S);
                params.put("TagsTematicas", THM);
                params.put("Imagen",img);
                params.put("NombreSaber",name);
                return params;
            }
        };
        requestQueue.add(request);
    }
/*Permiter obtener los saberes desde la galeria de android o de la camara*/
    public void SeleccionarSaber(View v) {
        String TipoArch = spin_TdA.getSelectedItem().toString();
        if (TipoArch.equals("Imagen")) {
            final CharSequence[] opciones = {"Obtener de la camara", "Buscar en galeria", "Cancelar"};
            final AlertDialog.Builder opcineesAlert = new AlertDialog.Builder(CRUD_repo.this);
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
            final AlertDialog.Builder opcineesAlert = new AlertDialog.Builder(CRUD_repo.this);
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
                ImgSaber.setImageURI(myPath);
                nombre=(System.currentTimeMillis() / 1000) + ".jpg";
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), myPath);
                    ImgSaber.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                videoSaber.setVisibility(View.INVISIBLE);
                ImgSaber.setVisibility(View.VISIBLE);
            } else if (requestCode == 2) {
                Uri myPath = data.getData();
                videoSaber.setMediaController(new MediaController(this));
                videoSaber.setVideoURI(myPath);
                videoSaber.setVisibility(View.VISIBLE);
                ImgSaber.setVisibility(View.INVISIBLE);
            } else if (requestCode == 3) {
                Uri myPath = data.getData();
                //MediaPlayer md = MediaPlayer.create(CRUD_repo.this, myPath);
                videoSaber.setMediaController(new MediaController(this));
                videoSaber.setVideoURI(myPath);
                videoSaber.setVisibility(View.VISIBLE);
                ImgSaber.setVisibility(View.INVISIBLE);
            } else if (requestCode == 4) {
                Uri myPath = data.getData();
                ImgSaber.setImageURI(myPath);
            } else if (requestCode == 5) {
                MediaScannerConnection.scanFile(this, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String s, Uri uri) {
                        Log.i("Ruta de almacenamiento", "Path:" + path);
                    }
                });
                bitmap = BitmapFactory.decodeFile(path);
                ImgSaber.setImageBitmap(bitmap);
                videoSaber.setVisibility(View.INVISIBLE);
                ImgSaber.setVisibility(View.VISIBLE);
            } else if (requestCode == 6) {
                Uri viData = Uri.parse(path);
                videoSaber.setMediaController(new MediaController(this));
                videoSaber.setVideoURI(viData);
                videoSaber.requestFocus();
                videoSaber.start();
                videoSaber.setVisibility(View.VISIBLE);
                ImgSaber.setVisibility(View.INVISIBLE);
            }
        }
    }


}
