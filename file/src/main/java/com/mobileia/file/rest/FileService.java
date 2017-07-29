package com.mobileia.file.rest;

import com.mobileia.file.entity.File;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by matiascamiletti on 28/7/17.
 */

public interface FileService {
    @Multipart
    @POST("api/upload")
    Call<Response> upload(@Query("app_id") int appId, @Part MultipartBody.Part file);
}
