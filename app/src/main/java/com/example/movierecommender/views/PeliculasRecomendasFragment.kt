// PeliculasRecomendadasFragment.kt
package com.example.movierecommender.views

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movierecommender.R
import com.example.movierecommender.databinding.ActivityPeliculasRecomendadasBinding
import com.example.movierecommender.models.PeliculaModel
import com.example.movierecommender.repository.MovieRepository
import com.example.movierecommender.viewmodels.PeliculasViewModel
import com.google.android.material.snackbar.Snackbar

class PeliculasRecomendadasFragment : Fragment(), AdapterPeliculas.OnCorazonClickListener {

    private lateinit var binding: ActivityPeliculasRecomendadasBinding
    private lateinit var viewModel: PeliculasViewModel
    private val adaptersMap: MutableMap<Int, AdapterPeliculas> = mutableMapOf()
    private lateinit var progressBar: ProgressBar
    private lateinit var movieRepository: MovieRepository
    // Shared Preferences para guardar el estado de los RadioButton
    private lateinit var sharedPreferences: SharedPreferences
    private val PREF_NAME = "PeliculasRecomendadasFragmentPrefs"
    private val KEY_SELECTED_RADIOBUTTON = "SelectedRadioButton"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_peliculas_recomendadas, container, false)
        binding = ActivityPeliculasRecomendadasBinding.bind(view)

        progressBar = view.findViewById(R.id.progressBar)

        movieRepository = MovieRepository(requireContext())
        sharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        progressBar.visibility = View.VISIBLE
        obtenerPeliculasTodasRecyclerView()
        return view
    }

    private fun obtenerPeliculasTodasRecyclerView() {
        binding.searchEditText.addTextChangedListener { editable ->
            val query = editable.toString().trim()

            adaptersMap.forEach { (genreId, adapter) ->
                val recyclerView = binding.root.findViewById<RecyclerView>(obtenerIdRecyclerViewPorGenero(genreId))
                recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

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
            val recyclerView = binding.root.findViewById<RecyclerView>(obtenerIdRecyclerViewPorGenero(genreId))
            recyclerView.visibility = View.VISIBLE

            val textViewGenero = obtenerTextViewPorGenero(genreId)
            textViewGenero.visibility = View.VISIBLE
        }

        viewModel = ViewModelProvider(requireActivity()).get(PeliculasViewModel::class.java)

        viewModel.peliculasPorGenero.observe(requireActivity()) { peliculasPorGenero ->
            peliculasPorGenero.forEach { (genreId, peliculas) ->
                val recyclerViewId = obtenerIdRecyclerViewPorGenero(genreId)
                val adapter = adaptersMap.getOrPut(genreId) { AdapterPeliculas(requireContext(), emptyList(), this, movieRepository) }

                val recyclerView = binding.root.findViewById<RecyclerView>(recyclerViewId)
                recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
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
                TextView(requireContext())
            }
        }
    }
    override fun onCorazonClick(pelicula: PeliculaModel) {
        // Verificar si la película ya está en la base de datos
        if (movieRepository.movieExists(UserInfo.id, pelicula.id.toInt())) {
            // Película ya existe en la base de datos, eliminarla
            movieRepository.deleteMovie(UserInfo.id, pelicula.id.toInt())
            Snackbar.make(requireView(), "Película eliminada de favoritos", Snackbar.LENGTH_SHORT).show()
        } else {
            // Película no existe en la base de datos, añadirla
            val result = movieRepository.addMovie(pelicula.id, pelicula.nombrePelicula, pelicula.poster, UserInfo.id)

            if (result != -1L) {
                Snackbar.make(requireView(), "Película añadida a la base de datos", Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(requireView(), "Error al añadir la película", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}
