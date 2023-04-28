package com.app.feenix.webservices

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    var BASE_URL_CLIENTPORTAL = "https://feenix.online/api/user/"
    var BASE_URL = "https://feenix.online/"

    private var sRetrofitclient: Retrofit? = null
    private lateinit var okHttpClient: OkHttpClient


    val clientportal: Retrofit
        get() {
            okHttpClient = OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()
            sRetrofitclient = Retrofit.Builder()
                .baseUrl(BASE_URL_CLIENTPORTAL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
            return sRetrofitclient!!

        }
}
