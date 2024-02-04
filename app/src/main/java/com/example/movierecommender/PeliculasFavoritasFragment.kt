package com.example.movierecommender

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movierecommender.models.PeliculaModel
import com.example.movierecommender.repository.MovieRepository
import com.example.movierecommender.views.AdapterPeliculasFavoritas
import com.google.android.material.snackbar.Snackbar

class PeliculasFavoritasFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterPeliculasFavoritas: AdapterPeliculasFavoritas
    private lateinit var movieRepository: MovieRepository
    private lateinit var noResultsTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_peliculas_favoritas, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewPeliculasFavoritas)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        // Inicializa el MovieRepository
        movieRepository = MovieRepository(requireContext())

        // Obtiene la lista de películas favoritas desde la base de datos
        val peliculasFavoritas = movieRepository.getMovies(UserInfo.id)

        // Inicializa el AdapterPeliculasFavoritas con la lista de películas favoritas
        adapterPeliculasFavoritas = AdapterPeliculasFavoritas(requireContext(), peliculasFavoritas, object : AdapterPeliculasFavoritas.OnCorazonClickListener {
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
                Snackbar.make(requireView(), "Película eliminada de favoritos", Snackbar.LENGTH_SHORT).show()
            }
        })

        // Configura el RecyclerView con el AdapterPeliculasFavoritas
        recyclerView.adapter = adapterPeliculasFavoritas

        // Obtener la referencia del TextView de noResults desde el layout
        noResultsTextView = view.findViewById(R.id.noResultsFavorites)

        // Mostrar el mensaje si no hay películas
        if (peliculasFavoritas.isEmpty()) {
            noResultsTextView.visibility = View.VISIBLE
        } else {
            noResultsTextView.visibility = View.GONE
        }

        return view
    }
}
