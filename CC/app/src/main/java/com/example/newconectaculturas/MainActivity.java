package com.example.newconectaculturas;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    /*estableciend como seleccionada a una activity ypara camvbio de activiys*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*set selected item each time it changes and relation with xml file*/
        BottomNavigationView navegacion = (BottomNavigationView) findViewById(R.id.mainnavigation);
        navegacion.setSelectedItemId(R.id.navigation_main);
        /*ejecutar el cambio de activitys*/
        navegacion.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_main:
                        return true;
                    case R.id.navigation_add:
                        startActivity(new Intent(getApplicationContext()
                                , Listar_Saberes.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.navigation_search:
                        startActivity(new Intent(getApplicationContext()
                                , Buscar_Saber.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
        ValidarPermisos();

    }
    /*fin del onCreate*/
    /*verificacion de permisos para que la aplicacion funciones bien*/
    private boolean ValidarPermisos(){
        //if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M){return true;}
        if ((checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED) && (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                && (checkSelfPermission(RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
            return true;
        }
        if ((shouldShowRequestPermissionRationale(CAMERA)) || (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))
                || (shouldShowRequestPermissionRationale(RECORD_AUDIO))) {
            cargarRecomendacion();
        } else {
            requestPermissions(new String[]{CAMERA, RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, 100);
        }
        return false;
    }
    /*si no tiene permisos le recomienda hacerlo con el dialogo para otorgar permisos */
    private void cargarRecomendacion () {
        AlertDialog.Builder permisos = new AlertDialog.Builder(MainActivity.this);
        permisos.setTitle("Permita el acceso a estas configuraciones");
        permisos.setMessage("Debe permitir el acceso a camara, microfono y almacenamiento interno para que la aplicacion funcvione de forma optima");
        permisos.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestPermissions(new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, 100);

            }
        });
        permisos.show();
    }
    /*ejecuta la peticion de los permisos para tomar fotos y grabar*/
    @Override
    public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
                                             @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Hacer algo si quieres restringir hasta que no se han dado permisos", Toast.LENGTH_SHORT).show();
            } else {
                otorgarPermisosManual();
            }
        }
    }
    /*Envia a las configuraciones de la app para otorgar permisos manualmente*/
    private void otorgarPermisosManual(){
        final CharSequence[] opciones = {"si", "no"};
        final AlertDialog.Builder opcinesAlert = new AlertDialog.Builder(MainActivity.this);
        opcinesAlert.setTitle("Configurar permisos Manualmente");
        opcinesAlert.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (opciones[which].equals("si")) {
                    Intent permisoManual = new Intent();
                    permisoManual.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
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

}