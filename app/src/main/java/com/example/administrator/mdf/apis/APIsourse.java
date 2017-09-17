package com.example.administrator.mdf.apis;


import com.example.administrator.mdf.activitys.ACache;
import com.example.administrator.mdf.MYapplication;
import com.example.administrator.mdf.activitys.SharedPreferenceUtil;
import com.example.administrator.mdf.responsebody.Admin;
import com.example.administrator.mdf.responsebody.ErrorMessage;

import android.util.Log;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

import retrofit2.converter.gson.GsonConverterFactory;

public class APIsourse {
    public static String token;
    private static APIsourse apIsourse;
    private static Converter<ResponseBody, ErrorMessage> errorConverter;
    private Retrofit retrofit;//retrofit2对象
    private OkHttpClient httpClient;//okHttp对象
    private String toRefreshToken;//用于刷新token的旧token
    private ACache aCache = ACache.get(MYapplication.getAppContext());


    private APIsourse() {

        //构建httpclient
        httpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                final Request.Builder request = chain.request().newBuilder();
                //将ApiKey添加到请求头信息里
                request.addHeader("X-DreamFactory-Api-Key", MYapplication.apikey);
                toRefreshToken = SharedPreferenceUtil.getString(MYapplication.getAppContext(), "TO_REFRESH_SESSION_TOKEN");
                token = aCache.getAsString("SESSION_TOKEN");
                if (toRefreshToken != null && !toRefreshToken.isEmpty()) {//如果旧token存在，说明已经登录了
                    if (token != null && !token.isEmpty()) {//token不为空时说明token没有过期
                        //将token添加到请求头信息里
                        request.addHeader("X-DreamFactory-Session-Token", token);
                    } else {//否则说明token已经过期，需要刷新token

                        User authAPI = new Retrofit.Builder()
                                .baseUrl(MYapplication.instanceUrl)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build()
                                .create(User.class);
                        Call<Admin> call = authAPI.refreshToken();
                        call.enqueue(new Callback<Admin>() {
                            @Override
                            public void onResponse(Call<Admin> call, Response<Admin> response) {
                                if (response.isSuccessful()) {//如果成功
                                    Admin admin = response.body();
                                    //记录token,保存到缓存是为了检测token是否过期，保存到preference是为了刷新token时读取旧token
                                    SharedPreferenceUtil.putString(MYapplication.getAppContext(), "TO_REFRESH_SESSION_TOKEN", admin.getSession_token());
                                    aCache.put("SESSION_TOKEN", admin.getSession_token(), 1*ACache.TIME_MINIT);//token保存24小时
                                    System.out.println("刷新token成功");
                                    request.addHeader("X-DreamFactory-Session-Token", admin.getSession_token());
                                    System.out.println(admin.getSession_token());
                                } else {//刷新失败
                                   System.out.println("请求错误");
                                }
                            }

                            @Override
                            public void onFailure(Call<Admin> call, Throwable t) {
                                System.out.println("刷新token失败：" + t.getMessage());
                            }
                        });
                    }
                }

                return chain.proceed(request.build());
            }
        }).build();

        //构建retrofit对象
        retrofit = new Retrofit.Builder().baseUrl(MYapplication.instanceUrl)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        errorConverter = retrofit.responseBodyConverter(ErrorMessage.class, new Annotation[0]);
    }

    /**
     * 处理接口访问错误时返回的信息
     */
    public static ErrorMessage getErrorMessage(Response response) {
        ErrorMessage error = null;
        try {
            error = errorConverter.convert(response.errorBody());
        } catch (IOException e) {
            error = new ErrorMessage("未知错误");
            Log.i("error", e.toString());
        }
        return error;
    }

    /**
     * 获得唯一的APISource实例来调用API
     *
     * @return 唯一的APISource
     */
    public synchronized static APIsourse getInstance() {
        if (apIsourse == null) {
            apIsourse = new APIsourse();
        }
        return apIsourse;
    }

    /**
     * 根据传入的API类，动态返回所需的API对象
     */
    public <T> T getAPIObject(Class<T> apiClass) {
        return retrofit.create(apiClass);
    }
}

