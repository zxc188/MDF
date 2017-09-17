package com.example.administrator.mdf.apis;


import com.example.administrator.mdf.responsebody.MusicResponsebody;
import com.example.administrator.mdf.responsebody.Resource;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Music {

    @GET("music/_table/music")
    Call<Resource<MusicResponsebody>> getall();

}
