// AdapterPeliculas.kt
package com.example.movierecommender.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.movierecommender.R
import com.example.movierecommender.core.Constantes
import com.example.movierecommender.models.PeliculaModel
import com.example.movierecommender.repository.MovieRepository

class AdapterPeliculas(
    val context: Context,
    var listaPeliculas: List<PeliculaModel>,
    private var corazonClickListener: OnCorazonClickListener,
    private var movieRepository: MovieRepository
): RecyclerView.Adapter<AdapterPeliculas.ViewHolder>() {

    interface OnCorazonClickListener {
        fun onCorazonClick(pelicula: PeliculaModel)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val ivPoster = itemView.findViewById(R.id.iv_movie) as ImageView
        val ivCorazon = itemView.findViewById(R.id.iv_corazon) as ImageView
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

        // Verifica si la película está en la base de datos y si es favorita
        val esFavorito = movieRepository.movieExists(UserInfo.id, pelicula.id.toInt())
        if (esFavorito) {
            // La película está en la base de datos, establece el corazón en rojo
            holder.ivCorazon.setImageResource(R.mipmap.corazon_rojo)
        }else {
            // La película no está en la base de datos, establece el corazón en blanco
            holder.ivCorazon.setImageResource(R.mipmap.corazon)
        }

        // Establece el nombre de la película en el TextView
        holder.tvMovieName.text = pelicula.nombrePelicula
        holder.tvMovieRating.text = pelicula.votoPromedio


        holder.ivCorazon.setOnClickListener {
            // Llama al método del listener cuando se hace clic en el corazón
            corazonClickListener.onCorazonClick(pelicula)

            // Verificar si la película está en la base de datos y si es favorita
            val esFavoritoActualizado  = movieRepository.movieExists(UserInfo.id, pelicula.id.toInt())
            if (!esFavoritoActualizado ) {
                // Cambiar el recurso de origen del corazón a blanco (no marcado)
                holder.ivCorazon.setImageResource(R.mipmap.corazon)
            } else {
                // Cambiar el recurso de origen del corazón a rojo (no marcado)
                holder.ivCorazon.setImageResource(R.mipmap.corazon_rojo)
            }

            // Notificar al RecyclerView que el ítem en la posición ha cambiado
            notifyItemChanged(position)
        }

        holder.itemView.setOnClickListener {
            val fragment = DetallesPeliculaFragment()
            val bundle = Bundle()
            bundle.putInt("id", pelicula.id.toInt())
            fragment.arguments = bundle

            // Reemplaza el fragmento actual en tu actividad por el DetallesPeliculaFragment
            val transaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)  // Opcional: añade el fragmento al back stack
            transaction.commit()
        }
    }

    override fun getItemCount(): Int {
        return listaPeliculas.size
    }

    fun actualizarLista(nuevaLista: List<PeliculaModel>) {
        listaPeliculas = nuevaLista
        notifyDataSetChanged()
    }

    fun setOnCorazonClickListener(listener: OnCorazonClickListener) {
        corazonClickListener = listener
    }
}
