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

class PeliculasRecomendadasModel : ViewModel() {

    private var _peliculasRecomendadas = MutableLiveData<Map<Int, List<PeliculaModel>>>()
    val peliculasRecomendadas: LiveData<Map<Int, List<PeliculaModel>>> = _peliculasRecomendadas

    private val peliculasAgrupadas = mutableMapOf<Int, List<PeliculaModel>>()

    private val itemsPorPagina = 20  // Número de películas que procesar en cada iteración

    fun obtenerRecomendadas() {
        if (_peliculasRecomendadas.value != null) {
            // Ya se obtuvieron las páginas, no es necesario hacerlo de nuevo
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                var page = 1
                while (obtenerRecomendados(page) && page <= 20) {
                    page++
                }

                // Actualizar LiveData
                withContext(Dispatchers.Main) {
                    _peliculasRecomendadas.value = peliculasAgrupadas
                }
            } catch (e: Exception) {
                Log.e("PeliculasRecomendadasModel", "Error al obtener películas recomendadas", e)
            }
        }
    }

    private suspend fun obtenerRecomendados(page: Int): Boolean {
        val responsePage =
            RetrofitClient.webService.obtenerRecomendados(Constantes.API_KEY, page)
        val movies = responsePage.body()?.resultados
        Log.d("Recomendadas", "Entrando en el metodo obtenerRecomendados")

        if (!movies.isNullOrEmpty()) {
            // Agregar películas por género
            withContext(Dispatchers.Main) {
                Log.d("Recomendadas", movies.toString())
            }
            return true  // Hay más páginas
        } else {
            Log.d("PeliculasRecomendadasModel", "No more pages. Stopping recursion.")
            return false  // No hay más páginas
        }
    }

}
