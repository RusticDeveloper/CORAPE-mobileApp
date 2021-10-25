package com.example.newconectaculturas;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.util.Base64;
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

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.common.server.response.FastJsonResponse;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
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
    private String nombreSaber, rutaSaber,encodedPdf;
    private Uri UriSaber;
    private File ArchivoCamara;
    private PDFView SABERtXT;
    //    directorio donde se guardan los archivos multimedia y tambien los datos
    private final String BaseDir = "Saberes/";
    private final String ImgDir = BaseDir + "Imagenes";
    private final String VidDir = BaseDir + "Video";
    private  boolean fromCamera=false;
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
        SABERtXT= findViewById(R.id.AportarPdf);
        GuardarSaber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t=Titulo.getText().toString().trim();
                String d=Descripcion.getText().toString().trim();
                String tem=Tematicas.getText().toString().trim();
                //validacion de campos vacios
                if(
                        !Titulo.isDirty() &&
                        !Tematicas.isDirty() &&
                        !Descripcion.isDirty() &&
                        (RadioNo.isChecked() || RadioSi.isChecked())
                ){
                    /*envia Texto al servidor para guardar en la base de datos*/
                    EnviarTexto();
                    /*envia saber al servidor para almacenarlo*/
                    try {
                        EnviarArchivos();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    /*regresa al activity anterior*/
                    finish();
                }else{
                    Toast.makeText(Aportar_Saber.this, "Llene todos los campos primero por favor", Toast.LENGTH_SHORT).show();
                }

            }
        });
        GuardarSaber.setEnabled(false);
        EscogerSaber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeleccionarSaber();
            }
        });
    }

    /*Envia los datos al texto*/
    private void EnviarTexto() {

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

                String msg = response.body().getMensaje();
                Toast.makeText(Aportar_Saber.this, msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                // if error occurs in network transaction then we can get the error in this method.

                Toast.makeText(Aportar_Saber.this, t.toString(), Toast.LENGTH_LONG).show();
                Log.i("error", t.toString());
            }
        });
    }   /*Envia los saberes al saber*/

    private void EnviarArchivos() throws URISyntaxException {
        // display a progress dialog
        final ProgressDialog prgdig = new ProgressDialog(Aportar_Saber.this);
        prgdig.setCancelable(false); // set cancelable to false
        prgdig.setMessage("Enviando Saber"); // set message
        prgdig.show(); // show progress dialog
        /*enviando saber al servidor*/
        String TA = SpinTA.getSelectedItem().toString();
        if (TA.equals("Texto")) {
            (Api.getClient().SendPdf(encodedPdf,nombreSaber,TA)).enqueue(new Callback<PostResponse>() {
                @Override
                public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                    if(prgdig!=null){
                        prgdig.dismiss();
                    }
                    Toast.makeText(Aportar_Saber.this, response.body().getMensaje(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<PostResponse> call, Throwable t) {
                    if(prgdig!=null){
                        prgdig.dismiss();
                    }
                    Toast.makeText(Aportar_Saber.this, t.toString(), Toast.LENGTH_SHORT).show();
                    Log.i("error", t.toString());
                }
            });
        }else{
            // Map is used to multipart the file using okhttp3.RequestBody
            File file;
            if(fromCamera && (TA.equals("Imagen")||TA.equals("Video"))) {
                file=ArchivoCamara;
            }else{
                file = new File(getPath(Aportar_Saber.this, UriSaber));
            }
            // Parsing any Media type file
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("nombreSaber", nombreSaber, requestBody);
            RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), nombreSaber);
            RequestBody filetype = RequestBody.create(MediaType.parse("text/plain"), TA);
            /*Envio de Archivos al servidor*/
            (Api.getClient().SendFile(fileToUpload, filename, filetype)).enqueue(new Callback<PostResponse>() {
                @Override
                public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                    if(prgdig!=null){
                        prgdig.dismiss();
                    }
                    Toast.makeText(Aportar_Saber.this, response.body().getMensaje(), Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onFailure(Call<PostResponse> call, Throwable t) {
                    if(prgdig!=null){
                        prgdig.dismiss();
                    }
                    Log.i("error", t.toString());
                    Toast.makeText(Aportar_Saber.this, "Error" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }

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
                        Intent IBSI = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        IBSI.setType("image/jpeg");
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
                        Intent IBSV = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);//tomar video
                        IBSV.setType("video/mp4");
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
            Intent IBST = new Intent(Intent.ACTION_GET_CONTENT);
            IBST.addCategory(Intent.CATEGORY_OPENABLE);
            IBST.setType("application/pdf");
            startActivityForResult(Intent.createChooser(IBST, "Seleccione la aplicacion"), FILE_GALERY_REQUEST);
        }
    }

    /*Realiza distintas acciones con los saberes recibidos desde las intents*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int nameindex;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AUDIO_GALERY_REQUEST:
                case VIDEO_GALERY_REQUEST:
                    UriSaber = data.getData();
                    /*obtiene el nombre verdadero del archivo*/
                    Cursor c = getContentResolver().query(UriSaber, null, null, null, null);
                    nameindex = c.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    c.moveToFirst();
                    nombreSaber = c.getString(nameindex);
                    /*alternar visibilidad*/
                    VidAudSaber.setVisibility(View.VISIBLE);
                    IMGSaber.setVisibility(View.INVISIBLE);
                    SABERtXT.setVisibility(View.INVISIBLE);
                    /*añade controles al reproductor e inicia la reproduccion*/
                    VidAudSaber.setMediaController(new MediaController(this));
                    VidAudSaber.setVideoURI(UriSaber);
                    VidAudSaber.start();
                    GuardarSaber.setEnabled(true);
                    fromCamera=false;
                    break;
                case IMAGE_GALERY_REQUEST:
                    UriSaber = data.getData();
                    Cursor cu = getContentResolver().query(UriSaber, null, null, null, null);
                    nameindex = cu.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    cu.moveToFirst();
                    nombreSaber = cu.getString(nameindex);
                    /*alternar visibilidad*/
                    VidAudSaber.setVisibility(View.INVISIBLE);
                    IMGSaber.setVisibility(View.VISIBLE);
                    SABERtXT.setVisibility(View.INVISIBLE);
                    /*Asigna la imagen a la imageview*/
                    IMGSaber.setImageURI(UriSaber);
                    GuardarSaber.setEnabled(true);
                    fromCamera=false;
                    break;
                case FILE_GALERY_REQUEST:
                    /*LOAD PDF IN THE PDFVIEWER*/
                    SABERtXT.fromUri(data.getData())
                    .load();
                    /*GET THE URI OF THE FILE TO SEND TO SERVER*/
                    UriSaber = data.getData();
                    /*GET THE NAME OF THE FILE*/
                    Cursor curs = getContentResolver().query(UriSaber, null, null, null, null);
                    nameindex = curs.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    curs.moveToFirst();
                    nombreSaber = curs.getString(nameindex);
                    /*TRANSFORM THE FILE IN ABYTES AND THEN IN STRING TO SEND TO SERVER*/
                    try {
                        InputStream inputStream=Aportar_Saber.this.getContentResolver().openInputStream(UriSaber);
                        byte[] pdfInBytes = new byte[inputStream.available()];
                        inputStream.read(pdfInBytes);
                        encodedPdf= Base64.encodeToString(pdfInBytes,Base64.DEFAULT);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    /*DEFINE VARIABLES TO ENABLE AND SET FILE UPDATED*/
                    fromCamera=false;
                    GuardarSaber.setEnabled(true);
                    /*Visivilidad del saber*/
                    VidAudSaber.setVisibility(View.INVISIBLE);
                    IMGSaber.setVisibility(View.INVISIBLE);
                    SABERtXT.setVisibility(View.VISIBLE);
                    break;
                case IMAGE_CAMERA_REQUEST:
                    Cursor curso = getContentResolver().query(UriSaber, null, null, null, null);
                    nameindex = curso.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    curso.moveToFirst();
                    nombreSaber = curso.getString(nameindex);
                    /*alternar visibilidad*/
                    VidAudSaber.setVisibility(View.INVISIBLE);
                    IMGSaber.setVisibility(View.VISIBLE);
                    SABERtXT.setVisibility(View.INVISIBLE);
                    /*Asigna la imagen a la imageview*/
                    IMGSaber.setImageURI(UriSaber);
                    fromCamera=true;
                    GuardarSaber.setEnabled(true);
                    break;
                case VIDEO_CAMERA_REQUEST:
                    Cursor cursor = getContentResolver().query(UriSaber, null, null, null, null);
                    nameindex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    cursor.moveToFirst();
                    nombreSaber = cursor.getString(nameindex);
                    /*alternar visibilidad*/
                    VidAudSaber.setVisibility(View.VISIBLE);
                    IMGSaber.setVisibility(View.INVISIBLE);
                    SABERtXT.setVisibility(View.INVISIBLE);
                    /*añade controles al reproductor e inicia la reproduccion*/
                    VidAudSaber.setMediaController(new MediaController(this));
                    VidAudSaber.setVideoURI(UriSaber);
                    VidAudSaber.start();
                    fromCamera=true;
                    GuardarSaber.setEnabled(true);
                    break;
            }
        }
    }

    /*captura de imagen y video*/
    private void capturarImagen() {
//codigo para crear el archivo en un directorio especifico
        File fileImagen = new File(Environment.getExternalStorageDirectory(), ImgDir);
        if (!fileImagen.exists()) {
            fileImagen.mkdirs();
        }
//codigo para nombrar el archivo creado
        if (fileImagen.exists() || fileImagen.isDirectory()) {
            Log.i("DDRC", "si creo el directorio: ");
            nombreSaber = "SABER_IMG_" + (System.currentTimeMillis() / 1000) + ".jpg";
        }
        //codigo para crear el archivo
        rutaSaber = Environment.getExternalStorageDirectory() + File.separator + ImgDir + File.separator + nombreSaber;
        File Imagen = new File(rutaSaber);
        ArchivoCamara=Imagen;
//crea un intent par hacer la captura de una imagen
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String Authorities = getApplicationContext().getPackageName() + ".provider";
            Uri ImageUri = FileProvider.getUriForFile(this, Authorities, Imagen);
            UriSaber = ImageUri;
            i.putExtra(MediaStore.EXTRA_OUTPUT, ImageUri);
        } else {
            UriSaber = Uri.fromFile(Imagen);
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
            nombreSaber = "SABER_VID_" + (System.currentTimeMillis() / 1000) + ".mp4";
        }
        rutaSaber = Environment.getExternalStorageDirectory() + File.separator + VidDir + File.separator + nombreSaber;
        File Vid = new File(rutaSaber);
        ArchivoCamara=Vid;
        Intent i = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String Authorities = getApplicationContext().getPackageName() + ".provider";
            Uri ImageUri = FileProvider.getUriForFile(this, Authorities, Vid);
            UriSaber = ImageUri;
            i.putExtra(MediaStore.EXTRA_OUTPUT, ImageUri);
        } else {
            UriSaber = Uri.fromFile(Vid);
            i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(Vid));
        }
//para  tomar un video de la camara
        startActivityForResult(i, VIDEO_CAMERA_REQUEST);
    }

    /*obtener el url original del archivo ----->(deprecated because of the null implementation of videos)>------*/
    private String getRealPathFromURI(Uri contentUri) {
        String TA = SpinTA.getSelectedItem().toString();
        String[] filePathColumn;
        switch (TA) {
            case "Imagen":
                // Obtiene la imagen de la uri
                filePathColumn = new String[]{MediaStore.Images.Media.DATA};
                break;
            case "Video":
                // Obtiene el video de la uri
                filePathColumn = new String[]{MediaStore.Video.Media.DATA};
                break;
            case "Audio":
                // Obtiene el audio de la uri
                filePathColumn = new String[]{MediaStore.Audio.Media.DATA};
                break;
            default:
                // Obtiene el audio de la uri
                filePathColumn = new String[]{MediaStore.Images.Media.DATA};
                break;
        }
        Cursor cursor = getContentResolver().query(contentUri, filePathColumn, null, null, null);
        assert cursor != null;
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);//antes estaba filePathColumn[0]
        String path = cursor.getString(columnIndex);
        Toast.makeText(Aportar_Saber.this, path, Toast.LENGTH_SHORT).show();
        return path;
    }
    /*DDRC-COMMENT: Above code is not used anymore but could be useful*/
    
    /*Metodo usado para obtener el uri real del archivo que se ha seleccionado desde el activity*/
    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
}