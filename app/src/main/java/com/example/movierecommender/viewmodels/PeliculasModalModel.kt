package com.example.movierecommender.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movierecommender.core.Constantes
import com.example.movierecommender.models.PeliculaModel
import com.example.movierecommender.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PeliculasModalModel : ViewModel() {

    private var _peliculas = MutableLiveData<List<PeliculaModel>>()
    val peliculas: LiveData<List<PeliculaModel>> = _peliculas

    private val itemsPorPagina = 20  // Número de películas que procesar en cada iteración
    fun obtenerTodasLasPaginas() {
        if (_peliculas.value != null) {
            // Ya se obtuvieron las páginas, no es necesario hacerlo de nuevo
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                var page = 1
                while (obtenerPagina(page) && page <= 20) {
                    page++
                }
            } catch (e: Exception) {
                Log.e("PeliculasViewModel", "Error al obtener películas", e)
            }
        }
    }

    private suspend fun obtenerPagina(page: Int): Boolean {
        val responsePage = RetrofitClient.webService.obtenerCartelera(Constantes.API_KEY, page)
        val movies = responsePage.body()?.resultados

        if (!movies.isNullOrEmpty()) {
            // Agregar películas al LiveData
            withContext(Dispatchers.Main) {
                val currentMovies = _peliculas.value ?: emptyList()
                _peliculas.value = currentMovies + movies
            }
            return true  // Hay más páginas
        } else {
            Log.d("PeliculasViewModel", "No more pages. Stopping recursion.")
            return false  // No hay más páginas
        }
    }
}

