package com.example.newconectaculturas;

import com.google.android.gms.common.server.response.FastJsonResponse;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("/Android/buscartodo.php")
        // API's endpoints
    Call<List<StringResults>> getUsersList();
// UserListResponse is POJO class to get the data from API,
// we use List<UserListResponse> in callback because the data in our API is starting from JSONArray

    @GET("/Android/buscarID.php")
        // API's endpoints
    Call<List<singleResponse>> getSingleData(@Query("ID") String ident);

    @GET("/Android/buscarID.php")
        // API's endpoints
    Call<List<singleResponse>> GetFile();

    @FormUrlEncoded
    @POST("/Android/subirCampos.php")
        // API's endpoints
    Call<PostResponse> SendKnowData(
//            @Field("Titulo") String T,
//            @Field("Descripcion") String D,
//            @Field("NacionalidadoPueblo") String NP,
//            @Field("TipoArchivo") String TA,
//            @Field("Publicado") String Public,
//            @Field("TagsTematicas") String Temat
            @FieldMap Map<String, String> map
//            @Body Map<String, String> map
    );
@Multipart
@POST("/Android/subirArchivos.php")
        // API's endpoints
    Call<PostResponse> SendFile(@Part MultipartBody.Part file,@Part("nombreSaber") RequestBody name,@Part("TipoArchivo") RequestBody type);

@PUT("/Android/Update.php")
        // API's endpoints
    Call<List<PostResponse>> UpdateText();

@DELETE("/Android/Delete.php")
        // API's endpoints
    Call<List<PostResponse>> DeleteText();

}