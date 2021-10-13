package com.example.newconectaculturas;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.loader.content.AsyncTaskLoader;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class editar_eliminar_saber extends AppCompatActivity {
    private static final int IMAGE_GALERY_REQUEST = 1;
    private static final int VIDEO_GALERY_REQUEST = 2;
    private static final int AUDIO_GALERY_REQUEST = 3;
    private static final int FILE_GALERY_REQUEST = 4;
    private static final int IMAGE_CAMERA_REQUEST = 5;
    private static final int VIDEO_CAMERA_REQUEST = 6;
    /*Obtener los datos del activity anterior, declarar variables y enlazar la view*/
    private String Ident;
    List<singleResponse> userListResponseData;
    private EditText Titulo, Desc, Tematica;
    private RadioButton PSi, PNo;
    private Spinner spNP, spTA;
    private VideoView VASaber;
    private ImageView IMGSaber;
    private Button ActualizarSaber, EliminarSaber, SeleccionarSaber;
    private String nombreSaber, rutaSaber, encodedPdf;
    private Uri UriSaber;
    private PDFView SABERtXT;
    /*rutas de guardado de archivos*/
    private final String BaseDir = "Saberes/";
    private final String ImgDir = BaseDir + "Imagenes";
    private final String VidDir = BaseDir + "Video";
    File ArchivoCamara;
    Boolean fromCamera = false, fileUpdated = false;
    Handler mainHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_eliminar_saber);

        /*Inicializacion de variables y enlazamiento con la parte view*/
        Titulo = (EditText) findViewById(R.id.NombreSaber);
        Desc = (EditText) findViewById(R.id.DescripcionSaber);
        Tematica = (EditText) findViewById(R.id.TemaSaber);
        PSi = (RadioButton) findViewById(R.id.Rsi);
        PNo = (RadioButton) findViewById(R.id.Rno);
        spNP = (Spinner) findViewById(R.id.spNP);
        spTA = (Spinner) findViewById(R.id.spTA);
        VASaber = (VideoView) findViewById(R.id.VideoAudioSaber);
        IMGSaber = (ImageView) findViewById(R.id.ImagenSaber);
        SeleccionarSaber = (Button) findViewById(R.id.SelectSaber);
        ActualizarSaber = (Button) findViewById(R.id.ActualizarSaber);
        EliminarSaber = (Button) findViewById(R.id.EliminarSaber);
        SABERtXT = findViewById(R.id.PdfActualizar);
        Bundle extraInfo = getIntent().getExtras();
        if (extraInfo != null) {
            Ident = extraInfo.getString("Identificador");
        }
        //boton de regreso en la barra de titulo(a la activity padre)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*llena los campos obtenidos con el uso de los datos del anterior activity*/
        LLenarCampos();
        /*dar las funcionalidades a la actualizacion seleccion y eliminacion de archivos*/
        SeleccionarSaber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeleccionarSaber();
            }
        });
        EliminarSaber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EliminarSaber();
            }
        });
        ActualizarSaber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ActualizarSaber();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });

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
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                userListResponseData = response.body();
                String Title = userListResponseData.get(0).getTitulo();
                String NP = userListResponseData.get(0).getNacionalidadoPueblo();
                String Type = userListResponseData.get(0).getTipoArchivo();
                String Publicado = userListResponseData.get(0).getPublicado();
                String Description = userListResponseData.get(0).getDescripcion();
                String Themes = userListResponseData.get(0).getTagsTematicas();
                String NombreSaber = userListResponseData.get(0).getNombreSaber();
                Titulo.setText(Title);
                Desc.setText(Description);
                Tematica.setText(Themes);
                if (Publicado.equals("True")) {
                    PSi.setChecked(true);
                } else if (Publicado.equals("False")) {
                    PNo.setChecked(true);
                }
                spNP.setSelection(obtenerPosicionItem(spNP, NP));
                spTA.setSelection(obtenerPosicionItem(spTA, Type));
                CargarSaber(Type, NombreSaber);

            }

            @Override
            public void onFailure(Call<List<singleResponse>> call, Throwable t) {
                // if error occurs in network transaction then we can get the error in this method.
                Toast.makeText(editar_eliminar_saber.this, t.toString(), Toast.LENGTH_LONG).show();
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }//dismiss progress dialog
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
    private void CargarSaber(String Tipo, String Nombre) {
        String Ruta;
        switch (Tipo) {
            case "Audio":
                Ruta = "http://192.168.1.51/wordpress/wp-content/uploads/Saberes/Audios/" + Nombre;
                VASaber.setVisibility(View.VISIBLE);
                IMGSaber.setVisibility(View.INVISIBLE);
                SABERtXT.setVisibility(View.INVISIBLE);
                CargarVidAud(Ruta);
                break;
            case "Video":
                Ruta = "http://192.168.1.51/wordpress/wp-content/uploads/Saberes/Videos/" + Nombre;
                VASaber.setVisibility(View.VISIBLE);
                IMGSaber.setVisibility(View.INVISIBLE);
                SABERtXT.setVisibility(View.INVISIBLE);
                CargarVidAud(Ruta);
                break;
            case "Texto":
                VASaber.setVisibility(View.VISIBLE);
                IMGSaber.setVisibility(View.INVISIBLE);
                SABERtXT.setVisibility(View.VISIBLE);
                Ruta = "http://192.168.1.51/wordpress/wp-content/uploads/Saberes/Textos/" + Nombre;
                File fS=new File(Environment.getExternalStorageDirectory()+"/Saberes/Textos/"+Nombre);
                if(!fS.exists()){
                    Toast.makeText(editar_eliminar_saber.this, "Entro por que el archivo no exite", Toast.LENGTH_SHORT).show();
                    new DownloadPdf()
                            .execute(Ruta,Nombre);
                }
                Uri fileRoute=Uri.fromFile(fS);
                SABERtXT.fromUri(fileRoute)
                        .load();
                Toast.makeText(editar_eliminar_saber.this, "Hacer que el pdf se muestre en el activity", Toast.LENGTH_SHORT).show();
                break;
            case "Imagen":
                Ruta = "http://192.168.1.51/wordpress/wp-content/uploads/Saberes/Imagenes/" + Nombre;
                VASaber.setVisibility(View.INVISIBLE);
                IMGSaber.setVisibility(View.VISIBLE);
                SABERtXT.setVisibility(View.INVISIBLE);
                Glide.with(editar_eliminar_saber.this)
                        .load(Ruta)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(IMGSaber);
                break;
        }
    }

    private void CargarVidAud(String ruta) {
        ProgressDialog pd = new ProgressDialog(editar_eliminar_saber.this);
        pd.setMessage("CargandoVideo");
        pd.show();
        Uri uri = Uri.parse(ruta);
        VASaber.setVideoURI(uri);
        VASaber.start();
        VASaber.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (pd != null) {
                    pd.dismiss();
                }
            }
        });
    }

    private void ActualizarSaber() throws URISyntaxException {
        /*Actualiza el texto*/
        ActualizarTextoSaber();
        /*Actualiza el Archivo del saber*/
        if (fileUpdated) {
            ActualizarArchivoSaber();
        }
    }

    private void ActualizarTextoSaber() {
        /*Obteniendo información de los elementos*/
        String T = Titulo.getText().toString().trim();
        String D = Desc.getText().toString().trim();
        String NP = spNP.getSelectedItem().toString();
        String TA = spTA.getSelectedItem().toString();
        String Temat = Tematica.getText().toString().trim();
        String Public = "False";
        if (PSi.isChecked()) {
            Public = "True";
        } else if (PNo.isChecked()) {
            Public = "False";
        }
        /*Creando el json para enviar*/
        Map<String, String> DatosSaber = new HashMap<>();
        DatosSaber.put("Titulo", T);
        DatosSaber.put("Descripcion", D);
        DatosSaber.put("NacionalidadoPueblo", NP);
        DatosSaber.put("TipoArchivo", TA);
        DatosSaber.put("Publicado", Public);
        DatosSaber.put("TagsTematicas", Temat);
        DatosSaber.put("NombreSaber", nombreSaber);
        DatosSaber.put("Identificador", Ident);

        (Api.getClient().UpdateText(DatosSaber)).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {

                Toast.makeText(editar_eliminar_saber.this, "Saber actualizado correctamente", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Toast.makeText(editar_eliminar_saber.this, t.toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void ActualizarArchivoSaber() throws URISyntaxException {
// display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(editar_eliminar_saber.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Enviando Saber"); // set message
        progressDialog.show(); // show progress dialog
        /*enviando saber al servidor*/
        String TA = spTA.getSelectedItem().toString();
        if (TA.equals("Texto")) {
            (Api.getClient().SendPdf(encodedPdf, nombreSaber, TA)).enqueue(new Callback<PostResponse>() {
                @Override
                public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Toast.makeText(editar_eliminar_saber.this, "Archivo Guardado con ecxito", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<PostResponse> call, Throwable t) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Toast.makeText(editar_eliminar_saber.this, t.toString(), Toast.LENGTH_SHORT).show();
                    Log.i("error", t.toString());
                }
            });
        } else {
            // Map is used to multipart the file using okhttp3.RequestBody
            File file;
            if (fromCamera && (TA.equals("Imagen") || TA.equals("Video"))) {
                file = ArchivoCamara;
            } else {
                file = new File(getPath(editar_eliminar_saber.this, UriSaber));
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
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Toast.makeText(editar_eliminar_saber.this, response.body().getMensaje(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<PostResponse> call, Throwable t) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Log.i("error", t.toString());
                    Toast.makeText(editar_eliminar_saber.this, "Error" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void EliminarSaber() {

        (Api.getClient().DeleteText(Ident)).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                String respuesta = response.body().getMensaje();
                Toast.makeText(editar_eliminar_saber.this, respuesta, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Log.i("error", "onFailure: " + t.toString());
                Toast.makeText(editar_eliminar_saber.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void SeleccionarSaber() {
        String TipoArch = spTA.getSelectedItem().toString();
        if (TipoArch.equals("Imagen")) {
            final CharSequence[] opciones = {"Obtener de la camara", "Buscar en galeria", "Cancelar"};
            final AlertDialog.Builder opcineesAlert = new AlertDialog.Builder(editar_eliminar_saber.this);
            opcineesAlert.setTitle("Seleccione un Saber");
            opcineesAlert.setItems(opciones, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (opciones[which].equals("Obtener de la camara")) {
                        capturarImagen();
//                        Toast.makeText(getApplication(), "Tomara una foto", Toast.LENGTH_LONG).show();
                    } else if (opciones[which].equals("Buscar en galeria")) {
                        Intent IBSI = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
            final AlertDialog.Builder opcineesAlert = new AlertDialog.Builder(editar_eliminar_saber.this);
            opcineesAlert.setTitle("Seleccione un Saber");
            opcineesAlert.setItems(opciones, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (opciones[which].equals("Obtener de la camara")) {
                        grabarVideo();
                        Toast.makeText(getApplication(), "Tomara un video", Toast.LENGTH_LONG).show();
                    } else if (opciones[which].equals("Buscar en galeria")) {
                        Intent IBSV = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);//tomar video
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
                    if (data.getData() != null) {
                        fileUpdated = true;
                    }
                    UriSaber = data.getData();
                    /*obtiene el nombre verdadero del archivo*/
                    Cursor c = getContentResolver().query(UriSaber, null, null, null, null);
                    nameindex = c.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    c.moveToFirst();
                    nombreSaber = c.getString(nameindex);
                    /*alternar visibilidad*/
                    VASaber.setVisibility(View.VISIBLE);
                    IMGSaber.setVisibility(View.INVISIBLE);
                    SABERtXT.setVisibility(View.INVISIBLE);
                    /*añade controles al reproductor e inicia la reproduccion*/
                    VASaber.setMediaController(new MediaController(this));
                    VASaber.setVideoURI(UriSaber);
                    VASaber.start();
                    fromCamera = false;
                    break;
                case IMAGE_GALERY_REQUEST:
                    if (data.getData() != null) {
                        fileUpdated = true;
                    }
                    UriSaber = data.getData();
                    Cursor cu = getContentResolver().query(UriSaber, null, null, null, null);
                    nameindex = cu.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    cu.moveToFirst();
                    nombreSaber = cu.getString(nameindex);
                    /*alternar visibilidad*/
                    VASaber.setVisibility(View.INVISIBLE);
                    IMGSaber.setVisibility(View.VISIBLE);
                    SABERtXT.setVisibility(View.INVISIBLE);
                    /*Asigna la imagen a la imageview*/
                    IMGSaber.setImageURI(UriSaber);
                    fromCamera = false;
                    break;
                case FILE_GALERY_REQUEST:

                    if (data.getData() != null) {
                        fileUpdated = true;
                        SABERtXT.fromUri(data.getData())
                                .load();
                    }
                    UriSaber = data.getData();
                    Cursor curs = getContentResolver().query(UriSaber, null, null, null, null);
                    nameindex = curs.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    curs.moveToFirst();
                    nombreSaber = curs.getString(nameindex);
                    try {
                        InputStream inputStream = editar_eliminar_saber.this.getContentResolver().openInputStream(UriSaber);
                        byte[] pdfInBytes = new byte[inputStream.available()];
                        inputStream.read(pdfInBytes);
                        encodedPdf = Base64.encodeToString(pdfInBytes, Base64.DEFAULT);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    fromCamera = false;
                    /*alternar visibilidad*/
                    VASaber.setVisibility(View.INVISIBLE);
                    IMGSaber.setVisibility(View.INVISIBLE);
                    SABERtXT.setVisibility(View.VISIBLE);
                    break;
                case IMAGE_CAMERA_REQUEST:
                    Cursor curso = getContentResolver().query(UriSaber, null, null, null, null);
                    nameindex = curso.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    curso.moveToFirst();
                    nombreSaber = curso.getString(nameindex);
                    /*alternar visibilidad*/
                    VASaber.setVisibility(View.INVISIBLE);
                    IMGSaber.setVisibility(View.VISIBLE);
                    SABERtXT.setVisibility(View.INVISIBLE);
                    /*Asigna la imagen a la imageview*/
                    IMGSaber.setImageURI(UriSaber);
                    fromCamera = true;
                    break;
                case VIDEO_CAMERA_REQUEST:
                    Cursor cursor = getContentResolver().query(UriSaber, null, null, null, null);
                    nameindex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    cursor.moveToFirst();
                    nombreSaber = cursor.getString(nameindex);
                    /*alternar visibilidad*/
                    VASaber.setVisibility(View.VISIBLE);
                    IMGSaber.setVisibility(View.INVISIBLE);
                    SABERtXT.setVisibility(View.INVISIBLE);
                    /*añade controles al reproductor e inicia la reproduccion*/
                    VASaber.setMediaController(new MediaController(this));
                    VASaber.setVideoURI(UriSaber);
                    VASaber.start();
                    fromCamera = true;
                    break;
                    /*TODO: camera video and image are not implemented the fileupdated flag cause the activity is
                       not returning anything when it is added an uri in the implementation of the intent*/
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
        ArchivoCamara = Imagen;
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
        ArchivoCamara = Vid;
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

    /*Metodo usado para obtener el uri real del archivo que se ha seleccionado desde el activity*/
    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
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
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
}