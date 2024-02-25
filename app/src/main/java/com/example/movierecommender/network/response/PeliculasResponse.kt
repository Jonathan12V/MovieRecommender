package com.example.movierecommender.network.response

import com.example.movierecommender.models.PeliculaModel
import com.google.gson.annotations.SerializedName

data class PeliculasResponse(

    @SerializedName("results")
    var resultados: List<PeliculaModel>
)