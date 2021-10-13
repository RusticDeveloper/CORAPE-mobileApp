package com.example.newconectaculturas;

public class singleResponse {

    private String Titulo;
    private String Descripcion;
    private String NacionalidadoPueblo;
    private String TipoArchivo;
    private String Publicado;
    private String TagsTematicas;
    private String NombreSaber;
    private String RutaSaber;
    private String ID;


    /*instanciacion de los getters*/

    public String getTipoArchivo() {
        return TipoArchivo;
    }

    public String getNacionalidadoPueblo() {
        return NacionalidadoPueblo;
    }

    public String getTitulo() {
        return Titulo;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public String getNombreSaber() {
        return NombreSaber;
    }

    public String getPublicado() {
        return Publicado;
    }

    public String getTagsTematicas() {
        return TagsTematicas;
    }

    public String getRutaSaber() {
        return RutaSaber;
    }

    public String getID() {
        return ID;
    }
    /*instanciacion de los setters*/

    public void setTipoArchivo(String tipoArchivo) {
        TipoArchivo = tipoArchivo;
    }

    public void setNacionalidadoPueblo(String nacionalidadoPueblo) {
        NacionalidadoPueblo = nacionalidadoPueblo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public void setNombreSaber(String nombre) {
        NombreSaber = nombre;
    }

    public void setPublicado(String publicado) {
        Publicado = publicado;
    }

    public void setTagsTematicas(String tagsTematicas) {
        TagsTematicas = tagsTematicas;
    }

    public void setRutaSaber(String rutaSaber) {
        RutaSaber = rutaSaber;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
