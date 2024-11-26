package com.vk.directop.myfastapiclient

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
//    private const val BASE_URL = "http://172.17.0.1:80" // URL вашего FastAPI сервера
//    private const val BASE_URL = "http://127.0.0.1:80" // URL вашего FastAPI сервера
    private const val BASE_URL = "http://10.0.2.2:80/" // URL вашего FastAPI сервера

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Можно также выбрать LEVEL.BASIC или LEVEL.HEADERS для разных уровней логирования
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor) // Добавляем интерцептор для логирования
        .build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)  // Устанавливаем клиент с логированием
            .build()
            .create(ApiService::class.java)
    }
}