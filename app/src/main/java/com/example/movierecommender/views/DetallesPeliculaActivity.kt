package com.example.movierecommender.views

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.movierecommender.core.Constantes.BASE_URL_IMAGEN
import coil.load
import coil.size.Scale
import com.example.movierecommender.MenuActivity
import com.example.movierecommender.databinding.ActivityPeliculasInformacionBinding
import com.example.movierecommender.models.DetallesPeliculasModel
import com.example.movierecommender.network.RetrofitClient
import com.example.movierecommender.R
import com.example.movierecommender.core.Constantes
import com.example.movierecommender.databinding.ActivityPeliculasRecomendadasBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetallesPeliculaActivity : MenuActivity() {

    private lateinit var binding: ActivityPeliculasInformacionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("DetallesPeliculaActivity", "DetallesPeliculaActivity iniciada con éxito")
        super.onCreate(savedInstanceState)

        binding = ActivityPeliculasInformacionBinding.inflate(layoutInflater)
        val contenidoMostrarPelicula = binding.root

        // Obtiene el contenedor principal del MenuActivity
        val contenedorMenu = findViewById<ConstraintLayout>(R.id.fragment_container)

        // Agrega el contenido de PeliculasRecomendadasActivity al contenedor
        contenedorMenu.addView(contenidoMostrarPelicula)

        // Obtener el ID de la película de los extras del intent
        val movieId = intent.getIntExtra("id", 1)
        // Llamada a la API utilizando Retrofit
        try {
            val call: Call<DetallesPeliculasModel> =
                RetrofitClient.webService.getMoviedetalles(movieId, Constantes.API_KEY)

            // Enqueue la llamada asíncrona
            call.enqueue(object : Callback<DetallesPeliculasModel> {
                override fun onResponse(
                    call: Call<DetallesPeliculasModel>,
                    response: Response<DetallesPeliculasModel>
                ) {
                    if (response.isSuccessful) {
                        val pelicula: DetallesPeliculasModel? = response.body()
                        if (pelicula != null) {
                            // Mostrar información en la interfaz de usuario
                            mostrarDetallesPelicula(pelicula)
                        } else {
                            Log.e("DetallesPeliculaActivity", "La respuesta del cuerpo es nula")
                        }
                    } else {
                        // Manejar el caso en que la respuesta no sea exitosa
                        val errorMessage = response.errorBody()?.string() ?: "Error desconocido"
                        Log.d("Response Code", "Error: ${response.code()}, Message: $errorMessage")
                    }
                }

                override fun onFailure(call: Call<DetallesPeliculasModel>, t: Throwable) {
                    // Manejar el caso de fallo en la llamada
                    Log.e("DetallesPeliculaActivity", "Error en la llamada a la API", t)
                }
            })

        } catch (e: Exception) {
            // Manejar el caso de error en la llamada
            Log.e("DetallesPeliculaActivity", "Error en la llamada a la API", e)
        }
    }

    private fun mostrarDetallesPelicula(pelicula: DetallesPeliculasModel) {
        // Mostrar información en la interfaz de usuario
        val imagePoster = BASE_URL_IMAGEN + pelicula.posterPath
        binding.ivMovieDetail.load(imagePoster) {
            crossfade(true)
            placeholder(R.mipmap.poster_placeholder)
            scale(Scale.FILL)
        }

        binding.tvNombrePelicula.text = pelicula.title
        binding.tvPeliculaAO.text = pelicula.releaseDate
        binding.tvPeliculaRating.text = pelicula.voteAverage.toString()
        binding.tvPeliculaDescripcion.text = pelicula.overview
        // Mostrar los nombres de los géneros
        val genres = pelicula.genres.map { it.name }
        binding.tvPeliculaGenero.text = genres.joinToString(", ")

    }
}

