package com.mobileia.file.rest;

import com.mobileia.core.Mobileia;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by matiascamiletti on 28/7/17.
 */

public class RestGenerator {
    /**
     * URL de la API
     */
    private static final String API_BASE_URL = "https://files.mobileia.com/";
    /**
     * Almacena la instancia de Retrofit
     */
    private static Retrofit sRetrofit = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static void uploadImage(File file, OnFileUpload callback){
        upload(file, "image/*", callback);
    }

    public static void upload(File file, String mediaType, final OnFileUpload callback){
        // Creamos el servicio
        FileService service = createService(FileService.class);
        // Parseamos el archivo para la request
        RequestBody requestFile = RequestBody.create(MediaType.parse(mediaType), file);
        // Generamos el body
        MultipartBody.Part body = MultipartBody.Part.createFormData("file[0]", file.getName(), requestFile);
        // Ejecutamos request
        Call<com.mobileia.file.rest.Response> call = service.upload(Mobileia.getInstance().getAppId(), body);
        // Manejamos la respuesta
        call.enqueue(new Callback<com.mobileia.file.rest.Response>() {
            @Override
            public void onResponse(Call<com.mobileia.file.rest.Response> call, Response<com.mobileia.file.rest.Response> response) {
                // Verificamos si la respuesta fue correcta
                if(!response.isSuccessful()){
                    // Devolvemos error
                    callback.onError();
                    return;
                }
                // Llamamos al callback con el archivo subido
                callback.onSuccess(response.body().response.get(0));
            }

            @Override
            public void onFailure(Call<com.mobileia.file.rest.Response> call, Throwable t) {
                // Devolvemos error
                callback.onError();
            }
        });
    }
    /**
     * Crea el servicio de Retrofit
     * @param serviceClass
     * @param <S>
     * @return
     */
    public static <S> S createService(Class<S> serviceClass) {
        return sRetrofit.create(serviceClass);
    }

    public interface OnFileUpload{
        void onSuccess(com.mobileia.file.entity.File file);
        void onError();
    }
}
