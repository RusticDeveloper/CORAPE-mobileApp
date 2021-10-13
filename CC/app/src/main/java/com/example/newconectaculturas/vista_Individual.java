package com.example.newconectaculturas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class vista_Individual extends AppCompatActivity {
    /*Declaracion de variables y relacion con la vista*/
    List<singleResponse> userListResponseData;
    private String Ident;
    private ImageView ImagenSaber;
    private VideoView VideoSaber;
    private PDFView pdfView;
private TextView Titulo,Descripcion,Nacionalidad,Tematicas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_individual);
        pdfView=findViewById(R.id.textualKnow);
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
        VideoSaber=(VideoView)findViewById(R.id.VidSaber);
        ImagenSaber=(ImageView) findViewById(R.id.imagenSaber);


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
                CargarSaber(Type,NombreSaber);
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
        String Ruta;
        switch (Tipo){
            case "Audio":
                Ruta="http://192.168.1.51/wordpress/wp-content/uploads/Saberes/Audios/"+Nombre;
                CargarVidAud(Ruta);
                VideoSaber.setVisibility(View.VISIBLE);
                ImagenSaber.setVisibility(View.INVISIBLE);
                pdfView.setVisibility(View.INVISIBLE);
                break;
            case "Video":
                Ruta="http://192.168.1.51/wordpress/wp-content/uploads/Saberes/Videos/"+Nombre;
                CargarVidAud(Ruta);
                VideoSaber.setVisibility(View.VISIBLE);
                ImagenSaber.setVisibility(View.INVISIBLE);
                pdfView.setVisibility(View.INVISIBLE);
                break;
            case "Texto":
                Ruta="http://192.168.1.51/wordpress/wp-content/uploads/Saberes/Textos/"+Nombre;
                File fS=new File(Environment.getExternalStorageDirectory()+"/Saberes/Textos/"+Nombre);
                if(!fS.exists()){
                    Toast.makeText(vista_Individual.this, "Entro por que el archivo no exite", Toast.LENGTH_SHORT).show();
                    new DownloadPdf()
                            .execute(Ruta,Nombre);
                }
                Uri fileRoute=Uri.fromFile(fS);
                pdfView.fromUri(fileRoute)
                        .load();
                VideoSaber.setVisibility(View.INVISIBLE);
                ImagenSaber.setVisibility(View.INVISIBLE);
                pdfView.setVisibility(View.VISIBLE);
                break;
            case "Imagen":
                Ruta="http://192.168.1.51/wordpress/wp-content/uploads/Saberes/Imagenes/"+Nombre;
                Glide.with(vista_Individual.this)
                        .load(Ruta)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(ImagenSaber);
                VideoSaber.setVisibility(View.INVISIBLE);
                ImagenSaber.setVisibility(View.VISIBLE);
                pdfView.setVisibility(View.INVISIBLE);
                break;
        }
    }
    private void CargarVidAud(String ruta) {
        ProgressDialog pd = new ProgressDialog(vista_Individual.this);
        pd.setMessage("CargandoVideo");
        pd.show();
        Uri uri = Uri.parse(ruta);
        VideoSaber.setVideoURI(uri);
        VideoSaber.start();
        VideoSaber.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (pd != null) {
                    pd.dismiss();
                }
            }
        });
    }
}