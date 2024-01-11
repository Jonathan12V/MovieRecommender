package com.example.movierecommender.models

import com.google.gson.annotations.SerializedName

data class PeliculaModel(
    @SerializedName("id")
    var id: Int,
    @SerializedName("title")
    var nombrePelicula: String,
    @SerializedName("overview")
    var descripcion: String,
    @SerializedName("poster_path")
    var poster: String,
    @SerializedName("release_date")
    var fechaLanzamiento: String,
    @SerializedName("vote_average")
    var votoPromedio: String,
    @SerializedName("vote_count")
    var totalVotos: String,
    @SerializedName("genre_ids")
    val genreIds: List<Int>
)
