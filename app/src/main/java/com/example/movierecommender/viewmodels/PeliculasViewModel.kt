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

class PeliculasViewModel : ViewModel() {

    private var _peliculasPorGenero = MutableLiveData<Map<Int, List<PeliculaModel>>>()
    val peliculasPorGenero: LiveData<Map<Int, List<PeliculaModel>>> = _peliculasPorGenero

    private val peliculasAgrupadas = mutableMapOf<Int, List<PeliculaModel>>()

    private val itemsPorPagina = 20  // Número de películas que procesar en cada iteración

    fun obtenerTodasLasPaginas() {
        if (_peliculasPorGenero.value != null) {
            // Ya se obtuvieron las páginas, no es necesario hacerlo de nuevo
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                var page = 1
                while (obtenerPagina(page) && page <= 20) {
                    page++
                }

                // Actualizar LiveData
                withContext(Dispatchers.Main) {
                    _peliculasPorGenero.value = peliculasAgrupadas
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
            // Agregar películas por género
            withContext(Dispatchers.Main) {
                agregarPeliculasPorGenero(movies.take(itemsPorPagina))  // Tomar solo las primeras "itemsPorPagina" películas
            }
            return true  // Hay más páginas
        } else {
            Log.d("PeliculasViewModel", "No more pages. Stopping recursion.")
            return false  // No hay más páginas
        }
    }

    private fun agregarPeliculasPorGenero(peliculas: List<PeliculaModel>?) {
        // Aquí deberías implementar la lógica para agrupar películas por género
        // Puedes usar el campo "genre_ids" de cada película para determinar su género

        // Ejemplo de agrupación por género (adaptarlo según tu lógica):
        peliculas?.let {
            val peliculasDeAccion = it.filter { pelicula -> pelicula.genreIds.contains(28) }
            val peliculasDeTerror = it.filter { pelicula -> pelicula.genreIds.contains(27) }
            val peliculasDeComedia = it.filter { pelicula -> pelicula.genreIds.contains(35) }
            val peliculasDeRomance = it.filter { pelicula -> pelicula.genreIds.contains(10749) }
            val peliculasDeAnimacion = it.filter { pelicula -> pelicula.genreIds.contains(16) }
            val peliculasDeMusica = it.filter { pelicula -> pelicula.genreIds.contains(10402) }
            val peliculasDeCienciaFiccion = it.filter { pelicula -> pelicula.genreIds.contains(878) }
            val peliculasDeMisterio = it.filter { pelicula -> pelicula.genreIds.contains(9648) }
            val peliculasDeFantasia = it.filter { pelicula -> pelicula.genreIds.contains(14) }

            peliculasAgrupadas[28] = (peliculasAgrupadas[28] ?: emptyList()) + peliculasDeAccion
            peliculasAgrupadas[27] = (peliculasAgrupadas[27] ?: emptyList()) + peliculasDeTerror
            peliculasAgrupadas[35] = (peliculasAgrupadas[35] ?: emptyList()) + peliculasDeComedia
            peliculasAgrupadas[10749] = (peliculasAgrupadas[10749] ?: emptyList()) + peliculasDeRomance
            peliculasAgrupadas[16] = (peliculasAgrupadas[16] ?: emptyList()) + peliculasDeAnimacion
            peliculasAgrupadas[10402] = (peliculasAgrupadas[10402] ?: emptyList()) + peliculasDeMusica
            peliculasAgrupadas[878] = (peliculasAgrupadas[878] ?: emptyList()) + peliculasDeCienciaFiccion
            peliculasAgrupadas[9648] = (peliculasAgrupadas[9648] ?: emptyList()) + peliculasDeMisterio
            peliculasAgrupadas[14] = (peliculasAgrupadas[14] ?: emptyList()) + peliculasDeFantasia
        }
    }
}
