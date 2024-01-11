package com.example.movierecommender

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movierecommender.models.PeliculaModel
import com.example.movierecommender.repository.MovieRepository
import com.example.movierecommender.views.AdapterPeliculasFavoritas
import androidx.recyclerview.widget.LinearLayoutManager

class PeliculasFavoritasActivity : MenuActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterPeliculasFavoritas: AdapterPeliculasFavoritas
    private lateinit var movieRepository: MovieRepository
    private lateinit var noResultsTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val settingsContent = layoutInflater.inflate(R.layout.activity_peliculas_favoritas, null)
        findViewById<ConstraintLayout>(R.id.fragment_container).addView(settingsContent)

        recyclerView = findViewById(R.id.recyclerViewPeliculasFavoritas)
        recyclerView.layoutManager = GridLayoutManager(applicationContext, 2)


        // Inicializa el MovieRepository
        movieRepository = MovieRepository(this)

        // Obtiene la lista de películas favoritas desde la base de datos
        val peliculasFavoritas = movieRepository.getMovies(UserInfo.id)

        // Inicializa el AdapterPeliculasFavoritas con la lista de películas favoritas
        adapterPeliculasFavoritas = AdapterPeliculasFavoritas(this, peliculasFavoritas, object : AdapterPeliculasFavoritas.OnCorazonClickListener {
            override fun onCorazonClick(pelicula: PeliculaModel) {
                // Eliminar la película de la base de datos
                movieRepository.deleteMovie(UserInfo.id, pelicula.id.toInt())
                // Eliminar la película de la lista del adaptador
                adapterPeliculasFavoritas.removePelicula(pelicula)

                // Verificar si no hay más películas en la lista
                if (adapterPeliculasFavoritas.itemCount == 0) {
                    noResultsTextView.visibility = View.VISIBLE
                } else {
                    noResultsTextView.visibility = View.GONE
                }
                Toast.makeText(this@PeliculasFavoritasActivity, "Película eliminada de favoritos", Toast.LENGTH_SHORT).show()
            }
        })

        // Configura el RecyclerView con el AdapterPeliculasFavoritas
        recyclerView.adapter = adapterPeliculasFavoritas

        // Obtener la referencia del TextView de noResults desde el layout
        noResultsTextView = findViewById(R.id.noResultsFavorites)

        // Mostrar el mensaje si no hay películas
        if (peliculasFavoritas.isEmpty()) {
            noResultsTextView.visibility = View.VISIBLE
        } else {
            noResultsTextView.visibility = View.GONE
        }

    }
}