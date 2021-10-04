package com.example.newconectaculturas;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.common.server.response.FastJsonResponse;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Aportar_Saber extends AppCompatActivity {

    public static final int IMAGE_GALERY_REQUEST = 1;
    public static final int VIDEO_GALERY_REQUEST = 2;
    public static final int AUDIO_GALERY_REQUEST = 3;
    public static final int FILE_GALERY_REQUEST = 4;
    public static final int IMAGE_CAMERA_REQUEST = 5;
    public static final int VIDEO_CAMERA_REQUEST = 6;
    /*Instanciacion de varibles y relacion con la vista .xml*/
    private EditText Titulo, Descripcion, Tematicas;
    private RadioButton RadioSi, RadioNo;
    private Spinner SpinNP, SpinTA;
    private Button EscogerSaber, GuardarSaber;
    private VideoView VidAudSaber;
    private ImageView IMGSaber;
    private String nombreSaber, rutaSaber;
    private Uri UriSaber;
    //    directorio donde se guardan los archivos multimedia y tambien los datos
    private final String BaseDir = "Saberes/";
    private final String ImgDir = BaseDir + "Imagenes";
    private final String VidDir = BaseDir + "Video";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aportar_saber);

        //boton de regreso en la barra de titulo(a la activity padre)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*instanciando los valores de texto*/
        Titulo = (EditText) findViewById(R.id.tituloSaber);
        Descripcion = (EditText) findViewById(R.id.DescripSaber);
        Tematicas = (EditText) findViewById(R.id.Tematics);
        RadioNo = (RadioButton) findViewById(R.id.Rno);
        RadioSi = (RadioButton) findViewById(R.id.Rsi);
        SpinNP = (Spinner) findViewById(R.id.spNoP);
        SpinTA = (Spinner) findViewById(R.id.spTA);
        EscogerSaber = (Button) findViewById(R.id.BotonEscoger);
        GuardarSaber = (Button) findViewById(R.id.BotonAgregar);
        VidAudSaber = (VideoView) findViewById(R.id.VideoSaber);
        IMGSaber = (ImageView) findViewById(R.id.ImagenSaber);

        GuardarSaber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*envia Texto al servidor para guardar en la base de datos*/
                EnviarTexto();
                /*envia saber al servidor para almacenarlo*/
                EnviarArchivos();
            }
        });
        EscogerSaber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeleccionarSaber();
                GuardarSaber.setEnabled(true);
            }
        });
    }

    /*Envia los datos al texto*/
    private void EnviarTexto() {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(Aportar_Saber.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Enviando Datos"); // set message
        progressDialog.show(); // show progress dialog
        /*Obteniendo información de los elementos*/
        String T = Titulo.getText().toString().trim();
        String D = Descripcion.getText().toString().trim();
        String NP = SpinNP.getSelectedItem().toString();
        String TA = SpinTA.getSelectedItem().toString();
        String Temat = Tematicas.getText().toString().trim();
        String Public = "False";
        if (RadioSi.isChecked()) {
            Public = "True";
        } else if (RadioNo.isChecked()) {
            Public = "False";
        }
        /*Creando el json para enviar*/
        Map<String, String> DatosSaberes = new HashMap<>();
        DatosSaberes.put("Titulo", T);
        DatosSaberes.put("Descripcion", D);
        DatosSaberes.put("NacionalidadoPueblo", NP);
        DatosSaberes.put("TipoArchivo", TA);
        DatosSaberes.put("Publicado", Public);
        DatosSaberes.put("TagsTematicas", Temat);
        DatosSaberes.put("NombreSaber", nombreSaber);

        /*recuperando y mostrando informacion en la vista*/
        (Api.getClient().SendKnowData(DatosSaberes)).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
//                Log.d("responseGET", response.body().get(0).getID());
                progressDialog.dismiss(); //dismiss progress dialog
                String msg = response.body().getMensaje();
                Toast.makeText(Aportar_Saber.this, msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                // if error occurs in network transaction then we can get the error in this method.
                progressDialog.dismiss(); //dismiss progress dialog
                Toast.makeText(Aportar_Saber.this, t.toString(), Toast.LENGTH_LONG).show();
                Log.i("error", t.toString());
            }
        });
    }   /*Envia los saberes al saber*/

    private void EnviarArchivos() {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(Aportar_Saber.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Enviando Saber"); // set message
        progressDialog.show(); // show progress dialog
        /*enviando saber al servidor*/
        String TA = SpinTA.getSelectedItem().toString();

        // Map is used to multipart the file using okhttp3.RequestBody
        File file = new File(getRealPathFromURI(UriSaber));

        // Parsing any Media type file
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("nombreSaber", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), nombreSaber);
        RequestBody filetype = RequestBody.create(MediaType.parse("text/plain"), TA);

        /*Envio de Archivos al servidor*/
        (Api.getClient().SendFile(fileToUpload, filename, filetype)).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                progressDialog.dismiss();
                Toast.makeText(Aportar_Saber.this, response.body().getMensaje(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.i("error", t.toString());
                Toast.makeText(Aportar_Saber.this, "Error"+t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*Permiter obtener los saberes desde la galeria de android o de la camara*/
    public void SeleccionarSaber() {
        String TipoArch = SpinTA.getSelectedItem().toString();
        if (TipoArch.equals("Imagen")) {
            final CharSequence[] opciones = {"Obtener de la camara", "Buscar en galeria", "Cancelar"};
            final AlertDialog.Builder opcineesAlert = new AlertDialog.Builder(Aportar_Saber.this);
            opcineesAlert.setTitle("Seleccione un Saber");
            opcineesAlert.setItems(opciones, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (opciones[which].equals("Obtener de la camara")) {
                        capturarImagen();
//                        Toast.makeText(getApplication(), "Tomara una foto", Toast.LENGTH_LONG).show();
                    } else if (opciones[which].equals("Buscar en galeria")) {
                        Intent IBSI = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        IBSI.setType("image/");
                        startActivityForResult(Intent.createChooser(IBSI, "Seleccione la aplicacion"), IMAGE_GALERY_REQUEST);
                    } else if (opciones[which].equals("Cancelar")) {
                        dialog.dismiss();
                    }
                }
            });
            opcineesAlert.show();
        } else if (TipoArch.equals("Video")) {
            final CharSequence[] opciones = {"Obtener de la camara", "Buscar en galeria", "Cancelar"};
            final AlertDialog.Builder opcineesAlert = new AlertDialog.Builder(Aportar_Saber.this);
            opcineesAlert.setTitle("Seleccione un Saber");
            opcineesAlert.setItems(opciones, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (opciones[which].equals("Obtener de la camara")) {
                        grabarVideo();
                        Toast.makeText(getApplication(), "Tomara un video", Toast.LENGTH_LONG).show();
                    } else if (opciones[which].equals("Buscar en galeria")) {
                        Intent IBSV = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);//tomar video
                        IBSV.setType("video/");
                        startActivityForResult(Intent.createChooser(IBSV, "Seleccione la aplicacion"), VIDEO_GALERY_REQUEST);
                    } else if (opciones[which].equals("Cancelar")) {
                        dialog.dismiss();
                    }
                }
            });
            opcineesAlert.show();
        } else if (TipoArch.equals("Audio")) {
//            Toast.makeText(this, "valio Audio", Toast.LENGTH_LONG).show();
            Intent IBSA = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            IBSA.addCategory(Intent.CATEGORY_OPENABLE);
            IBSA.setType("audio/*");
            startActivityForResult(Intent.createChooser(IBSA, "Seleccione la aplicacion"), AUDIO_GALERY_REQUEST);
        } else if (TipoArch.equals("Texto")) {
//            Toast.makeText(this, "valio Texto", Toast.LENGTH_LONG).show();
            Intent IBST = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            IBST.setType("application/pdf, aplication/");
            startActivityForResult(Intent.createChooser(IBST, "Seleccione la aplicacion"), FILE_GALERY_REQUEST);
        }
    }

    /*Realiza distintas acciones con los saberes recibidos desde las intents*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Cursor cursors = getContentResolver().query(data.getData(), null, null, null, null);
        int nameindex = cursors.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        cursors.moveToFirst();

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AUDIO_GALERY_REQUEST:
                    UriSaber=data.getData();
                    nombreSaber = cursors.getString(nameindex);
                    break;
                case IMAGE_GALERY_REQUEST:
                    UriSaber=data.getData();

                    nombreSaber = cursors.getString(nameindex);
                    break;
                case VIDEO_GALERY_REQUEST:
                    UriSaber=data.getData();
                    nombreSaber = cursors.getString(nameindex);
                    break;
                case FILE_GALERY_REQUEST:
                    UriSaber=data.getData();
                    nombreSaber = cursors.getString(nameindex);
                    break;
                case IMAGE_CAMERA_REQUEST:
                    UriSaber=data.getData();
                    nombreSaber = cursors.getString(nameindex);
                    break;
                case VIDEO_CAMERA_REQUEST:
                    UriSaber=data.getData();
                    nombreSaber = cursors.getString(nameindex);
                    break;
            }
        }
    }

    private void capturarImagen() {
//codigo para crear el archivo en un directorio especifico
        File fileImagen = new File(Environment.getExternalStorageDirectory(), ImgDir);
        if (!fileImagen.exists()) {
            fileImagen.mkdirs();
            Toast.makeText(this, "creo el directorio creo", Toast.LENGTH_SHORT).show();
        }
//codigo para nombrar el archivo creado
        if (fileImagen.exists() || fileImagen.isDirectory()) {
            Toast.makeText(this, "creo directorio de deberitas", Toast.LENGTH_SHORT).show();
            Log.i("DDRC", "si creo el directorio: ");
            nombreSaber = "SABER_IMG_"+(System.currentTimeMillis() / 1000) + ".jpg";
        }
        //codigo para crear el archivo
        rutaSaber = Environment.getExternalStorageDirectory() + File.separator + ImgDir + File.separator + nombreSaber;
        File Imagen = new File(rutaSaber);
//crea un intent par hacer la captura de una imagen
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String Authorities = getApplicationContext().getPackageName() + ".provider";
            Uri ImageUri = FileProvider.getUriForFile(this, Authorities, Imagen);
            i.putExtra(MediaStore.EXTRA_OUTPUT, ImageUri);
        } else {
            i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(Imagen));
        }

        startActivityForResult(i, IMAGE_CAMERA_REQUEST);
    }

    private void grabarVideo() {
        File fileVideo = new File(Environment.getExternalStorageDirectory(), VidDir);

        if (!fileVideo.exists() || !fileVideo.isDirectory()) {
            fileVideo.mkdirs();
        }

        if (fileVideo.exists() || fileVideo.isDirectory()) {
            Log.i("DDRC", "si creo el directorio: ");
            nombreSaber = "SABER_VID_"+(System.currentTimeMillis() / 1000) + ".mp4";
        }
        rutaSaber = Environment.getExternalStorageDirectory() + File.separator + VidDir + File.separator + nombreSaber;
        File Vid = new File(rutaSaber);

        Intent i = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String Authorities = getApplicationContext().getPackageName() + ".provider";
            Uri ImageUri = FileProvider.getUriForFile(this, Authorities, Vid);
            i.putExtra(MediaStore.EXTRA_OUTPUT, ImageUri);
        } else {
            i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(Vid));
        }
//para  tomar un video de la camara
        startActivityForResult(i, VIDEO_CAMERA_REQUEST);
    }
    /*
     * This method is fetching the absolute path of the image file
     * if you want to upload other kind of files like .pdf, .docx
     * you need to make changes on this method only
     * Rest part will be the same
     * */
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
}