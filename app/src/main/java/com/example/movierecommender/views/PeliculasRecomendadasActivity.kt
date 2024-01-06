// PeliculasRecomendadasActivity.kt
package com.example.movierecommender.views

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movierecommender.MenuActivity
import com.example.movierecommender.R
import com.example.movierecommender.databinding.ActivityPeliculasRecomendadasBinding
import com.example.movierecommender.models.PeliculaModel
import com.example.movierecommender.repository.MovieRepository
import com.example.movierecommender.viewmodels.PeliculasViewModel

class PeliculasRecomendadasActivity : MenuActivity(), AdapterPeliculas.OnCorazonClickListener {

    private lateinit var binding: ActivityPeliculasRecomendadasBinding
    private lateinit var viewModel: PeliculasViewModel
    private val adaptersMap: MutableMap<Int, AdapterPeliculas> = mutableMapOf()

    // Agrega una propiedad para el ProgressBar
    private lateinit var progressBar: ProgressBar
    private lateinit var movieRepository: MovieRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPeliculasRecomendadasBinding.inflate(layoutInflater)
        val contenidoPeliculas = binding.root

        // Crear una única instancia de MovieRepository en tu actividad
        movieRepository = MovieRepository(this)
        // Obtiene el contenedor principal del MenuActivity
        val contenedorMenu = findViewById<ConstraintLayout>(R.id.fragment_container)

        // Agrega el contenido de PeliculasRecomendadasActivity al contenedor
        contenedorMenu.addView(contenidoPeliculas)

        // Obtiene el ProgressBar del layout
        progressBar = findViewById(R.id.progressBar)

        val buttonTodo: RadioButton = findViewById(R.id.Todo)
        val buttonRecomendadas: RadioButton = findViewById(R.id.recomendados)

        buttonTodo.setOnClickListener {
            buttonTodo.isChecked = true
            buttonRecomendadas.isChecked = false

            progressBar.visibility = View.VISIBLE

            obtenerPeliculasTodasRecomendadas(true)
        }
        buttonRecomendadas.setOnClickListener {
            buttonRecomendadas.isChecked = true
            buttonTodo.isChecked = false

            obtenerPeliculasTodasRecomendadas(false)
        }
    }

    private fun obtenerPeliculasTodasRecomendadas(filtro: Boolean) {

        if (filtro) {
            obtenerPeliculasTodasRecyclerView()
        } else {
            obtenerPeliculasRecomendadasRecyclerView()
        }
    }

    private fun obtenerPeliculasTodasRecyclerView() {
        binding.searchEditText.addTextChangedListener { editable ->
            val query = editable.toString().trim()

            adaptersMap.forEach { (genreId, adapter) ->
                val recyclerView = findViewById<RecyclerView>(obtenerIdRecyclerViewPorGenero(genreId))
                recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

                val peliculasFiltradas = viewModel.peliculasPorGenero.value?.get(genreId)
                    ?.filter { pelicula ->
                        pelicula.nombrePelicula.startsWith(query, true)
                    } ?: emptyList()

                recyclerView.visibility = if (peliculasFiltradas.isNotEmpty()) View.VISIBLE else View.GONE

                adapter.actualizarLista(peliculasFiltradas)
                recyclerView.adapter = adapter

                val textViewGenero = obtenerTextViewPorGenero(genreId)
                textViewGenero.visibility = if (peliculasFiltradas.isNotEmpty()) View.VISIBLE else View.GONE
            }
        }

        adaptersMap.forEach { (genreId, _) ->
            val recyclerView = findViewById<RecyclerView>(obtenerIdRecyclerViewPorGenero(genreId))
            recyclerView.visibility = View.VISIBLE

            val textViewGenero = obtenerTextViewPorGenero(genreId)
            textViewGenero.visibility = View.VISIBLE
        }

        viewModel = ViewModelProvider(this)[PeliculasViewModel::class.java]

        viewModel.peliculasPorGenero.observe(this) { peliculasPorGenero ->
            peliculasPorGenero.forEach { (genreId, peliculas) ->
                val recyclerViewId = obtenerIdRecyclerViewPorGenero(genreId)
                val adapter = adaptersMap.getOrPut(genreId) { AdapterPeliculas(this, emptyList(), this, movieRepository) }

                val recyclerView = findViewById<RecyclerView>(recyclerViewId)
                recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                recyclerView.adapter = adapter

                adapter.listaPeliculas = peliculas
                adapter.notifyDataSetChanged()

                progressBar.visibility = View.GONE

                adaptersMap.keys.forEach { genreId ->
                    val textViewGenero = obtenerTextViewPorGenero(genreId)
                    textViewGenero.visibility = View.VISIBLE
                }
            }
        }

        if (adaptersMap.isEmpty()) {
            viewModel.obtenerTodasLasPaginas()
        }
    }

    private fun obtenerPeliculasRecomendadasRecyclerView() {
        binding.searchEditText.text = null // Borra el texto del EditText de búsqueda

        adaptersMap.forEach { (genreId, adapter) ->
            val recyclerView = findViewById<RecyclerView>(obtenerIdRecyclerViewPorGenero(genreId))

            recyclerView.visibility = View.GONE

            adapter.actualizarLista(emptyList())
        }

        adaptersMap.keys.forEach { genreId ->
            val textViewGenero = obtenerTextViewPorGenero(genreId)
            textViewGenero.visibility = View.GONE
        }

        Toast.makeText(this, "Mostrando todas las películas recomendadas", Toast.LENGTH_SHORT).show()
    }

    private fun obtenerIdRecyclerViewPorGenero(genreId: Int): Int {
        return when (genreId) {
            28 -> R.id.rvAccion
            27 -> R.id.rvTerror
            35 -> R.id.rvComedia
            10749 -> R.id.rvRomance
            16 -> R.id.rvAnimacion
            10402 -> R.id.rvMusica
            878 -> R.id.rvCienciaFiccion
            9648 -> R.id.rvMisterio
            14 -> R.id.rvFantasia
            else -> {
                println("Advertencia: No hay RecyclerView definido para el género con ID $genreId")
                0
            }
        }
    }

    private fun obtenerTextViewPorGenero(genreId: Int): TextView {
        return when (genreId) {
            28 -> binding.accion
            27 -> binding.terror
            35 -> binding.comedia
            10749 -> binding.romance
            16 -> binding.animacion
            10402 -> binding.musica
            878 -> binding.cienciaFiccion
            9648 -> binding.misterio
            14 -> binding.fantasia
            else -> {
                println("Advertencia: No hay TextView definido para el género con ID $genreId")
                TextView(this)
            }
        }
    }

    override fun onCorazonClick(pelicula: PeliculaModel) {
        val ivCorazon = findViewById<ImageView>(R.id.iv_corazon)

        // Verificar si la película ya está en la base de datos
        if (movieRepository.movieExists(pelicula.id.toInt())) {
            // Película ya existe en la base de datos, eliminarla
            movieRepository.deleteMovie(pelicula.id.toInt())
            Toast.makeText(this, "Película eliminada de favoritos", Toast.LENGTH_SHORT).show()
        } else {
            // Película no existe en la base de datos, añadirla
            val result = movieRepository.addMovie(pelicula.id, pelicula.nombrePelicula, pelicula.poster)

            if (result != -1L) {
                Toast.makeText(this, "Película añadida a la base de datos", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error al añadir la película", Toast.LENGTH_SHORT).show()
            }
        }
    }
}