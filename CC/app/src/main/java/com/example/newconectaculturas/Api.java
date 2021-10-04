package com.example.newconectaculturas;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    private static Retrofit retrofit = null;
    public static ApiInterface getClient() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        // change your base URL
        if (retrofit==null) {
            OkHttpClient okHttpClient=new OkHttpClient();
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.1.51/")
//                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        //Creating object for our interface
        ApiInterface api = retrofit.create(ApiInterface.class);
        return api; // return the APIInterface object
    }
}
