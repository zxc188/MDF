package com.example.administrator.mdf.apis;


import com.example.administrator.mdf.responsebody.Admin;
import com.example.administrator.mdf.responsebody.LoginResponsebody;
import com.example.administrator.mdf.requesbody.Loginbody;
import com.example.administrator.mdf.responsebody.RegisterResponsebody;
import com.example.administrator.mdf.requesbody.Registerbody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface User {

    //注册接口
    @POST("user/register")
    Call<RegisterResponsebody> register(@Body Registerbody registerbody);

    //登录接口
    @POST("user/session?remember_m=true")
    Call<LoginResponsebody> login( @Body Loginbody loginbody);

    //刷新token
    @PUT("user/session")
    Call<Admin> refreshToken();

    //注销接口
    @DELETE("user/session")
    Call<RegisterResponsebody> del();
}
