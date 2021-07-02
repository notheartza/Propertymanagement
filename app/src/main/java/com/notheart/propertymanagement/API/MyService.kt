package com.notheart.propertymanagement.API


import com.google.gson.GsonBuilder
import com.liulishuo.filedownloader.FileDownloader
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class MyService {
    fun getService(): MyAPI {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY



        val httpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        httpClientBuilder.addInterceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
            requestBuilder.header("Accept", "application/json")
            //requestBuilder.addHeader("Connection", "close")
            requestBuilder.addHeader("Accept-Encoding", "identity")
            chain.proceed(requestBuilder.build())
        }
        //val client = httpClientBuilder.build()

        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()


        val gson = GsonBuilder().setLenient().create()

        val retrofit = Retrofit.Builder().baseUrl("http://landvist.xyz/").client(client)
                .addConverterFactory(GsonConverterFactory.create(gson)).build()

        return retrofit.create(MyAPI::class.java)

    }
}