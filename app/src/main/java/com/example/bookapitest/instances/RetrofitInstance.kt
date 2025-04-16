package com.example.bookapitest.instances

import com.example.bookapitest.interfaces.ApiInterface
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    //private const val BASE_URL = "http://ip:8000" Если сервер на другом устройстве (ip берем из ipconfig)
    private const val BASE_URL = "http://10.0.2.2:8000" //Дефолтный IP для эмулятора, если сервер стоит на том же ПК


    val retrofit: ApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build())
            .build()
            .create(ApiInterface::class.java)
    }
}