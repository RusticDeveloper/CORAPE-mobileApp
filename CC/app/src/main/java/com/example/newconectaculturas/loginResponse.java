package com.example.newconectaculturas;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class loginResponse {

    @SerializedName("Respuesta")
    @Expose
    private String respuesta;
    /*getters*/
    public String getMensaje() {
        return respuesta;
    }
    /*setters*/
    public void setMensaje(String response) {
        respuesta = response;
    }
}
