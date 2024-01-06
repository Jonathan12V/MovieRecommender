package com.example.movierecommender.network


import com.example.movierecommender.models.DetallesPeliculasModel
import com.example.movierecommender.network.response.PeliculasResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WebService {

    @GET("discover/movie")
    suspend fun obtenerCartelera(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int,
        @Query("language") language: String = "es-ES"
    ): Response<PeliculasResponse>

    @GET("movie/{movie_id}")
    fun getMoviedetalles(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "es-ES"
    ): Call<DetallesPeliculasModel>
}
