// AdapterPeliculas.kt
package com.example.movierecommender.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.movierecommender.R
import com.example.movierecommender.core.Constantes
import com.example.movierecommender.models.PeliculaModel

class AdapterPeliculasFavoritas(
    val context: Context,
    var listaPeliculas: List<PeliculaModel>,
    private var corazonClickListener: OnCorazonClickListener
): RecyclerView.Adapter<AdapterPeliculasFavoritas.ViewHolder>() {

    interface OnCorazonClickListener {
        fun onCorazonClick(pelicula: PeliculaModel)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val ivPoster = itemView.findViewById(R.id.iv_movie_favorite) as ImageView
        val ivCorazon = itemView.findViewById(R.id.iv_corazon_favorite) as ImageView
        val tvMovieName = itemView.findViewById(R.id.tv_movie_name_favorite) as TextView
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_peliculas_favoritas, parent, false)
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
        holder.tvMovieName.text = pelicula.nombrePelicula

        holder.ivCorazon.setOnClickListener {
            // Llama al método del listener cuando se hace clic en el corazón
            corazonClickListener.onCorazonClick(pelicula)
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
    // Agrega este método para eliminar una película de la lista
    // Método para eliminar una película de la lista
    fun removePelicula(pelicula: PeliculaModel) {
        val position = listaPeliculas.indexOf(pelicula)
        if (position != -1) {
            listaPeliculas = listaPeliculas.toMutableList().apply { removeAt(position) }
            notifyItemRemoved(position)
        }
    }
}
