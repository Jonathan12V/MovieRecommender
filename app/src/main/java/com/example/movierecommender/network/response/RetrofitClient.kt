package com.example.movierecommender.network

import com.example.movierecommender.core.Constantes
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constantes.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val webService: WebService by lazy {
        retrofit.create(WebService::class.java)
    }
}
