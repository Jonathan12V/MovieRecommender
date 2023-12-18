// AdapterPeliculas.kt
package com.example.movierecommender.views

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.movierecommender.R
import com.example.movierecommender.core.Constantes
import com.example.movierecommender.models.PeliculaModel

class AdapterPeliculas(
    val context: Context,
    var listaPeliculas: List<PeliculaModel>
): RecyclerView.Adapter<AdapterPeliculas.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val ivPoster = itemView.findViewById(R.id.iv_movie) as ImageView
        val tvMovieName = itemView.findViewById(R.id.tv_movie_name) as TextView
        val tvMovieRating = itemView.findViewById(R.id.tv_movie_rating) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_peliculas, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pelicula = listaPeliculas[position]

        Glide
            .with(context)
            .load("${Constantes.BASE_URL_IMAGEN}${pelicula.poster}")
            .apply(RequestOptions().override(Constantes.IMAGEN_ANCHO, Constantes.IMAGEN_ALTO))
            .into(holder.ivPoster)

        // Establece el nombre de la película en el TextView
        holder.tvMovieName.setText(pelicula.nombrePelicula)
        holder.tvMovieRating.setText(pelicula.votoPromedio)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetallesPeliculaActivity::class.java)
            intent.putExtra("id", pelicula.id.toInt())
            context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int {
        return listaPeliculas.size
    }
    fun actualizarLista(nuevaLista: List<PeliculaModel>) {
        listaPeliculas = nuevaLista
        notifyDataSetChanged()
    }
}
