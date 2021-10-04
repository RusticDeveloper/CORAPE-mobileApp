package com.example.newconectaculturas;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostResponse {
    @SerializedName("mensaje")
    @Expose
    private String mensaje;
    /*getters*/

    public String getMensaje() {
        return mensaje;
    }
    /*setters*/
    public void setMensaje(String response) {
        mensaje = response;
    }
}
