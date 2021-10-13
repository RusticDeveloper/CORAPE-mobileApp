package com.example.newconectaculturas;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class StringResults {
    private String Titulo;
    private String NacionalidadoPueblo;
    private String TipoArchivo;
    private String ID;
    private String mensaje;

    /*getters*/
    public String getID() {
        return ID;
    }

    public String getTitulo() {
        return Titulo;
    }

    public String getNacionalidadoPueblo() {
        return NacionalidadoPueblo;
    }

    public String getTipoArchivo() {
        return TipoArchivo;
    }

    public String getMensaje() {
        return mensaje;
    }

    /*setters*/
    public void setID(String ID) {
        this.ID = ID;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public void setNacionalidadoPueblo(String nacionalidadoPueblo) {
        NacionalidadoPueblo = nacionalidadoPueblo;
    }

    public void setTipoArchivo(String tipoArchivo) {
        TipoArchivo = tipoArchivo;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
